package com.jeecms.cms.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.ApiUserLogin;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiRecordMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.security.CmsAuthorizingRealm;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

/**
 * 后台API拦截器
 * 
 * 判断用户权限信息
 * 
 * @author tom HandlerInterceptorAdapter
 */
public class AdminApiInterceptor extends HandlerInterceptorAdapter implements InitializingBean, DisposableBean {
	private static final Logger log = Logger.getLogger(AdminApiInterceptor.class);
	public static final String SITE_PARAM = "_site_id_param";
	public static final String SITE_COOKIE = "_site_id_cookie";
	public static final String PERMISSION_MODEL = "_permission_key";
	public static final String SITE_PATH_PARAM = "path";
	private static String property_firewall_open = "firewall.open";
	private static String property_firewall_domain = "firewall.domain";
	private static String property_firewall_hour = "firewall.hour";
	private static String property_firewall_week = "firewall.week";
	private static String property_firewall_ips = "firewall.ips";
	public static final String FIREWALL_CONFIG_LASTMODIFIED = "firewall_config_lastmodified";

	private InputStream in;
	private Properties p = new Properties();
	private File fireWallConfigFile;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 正常状态
		String uri = getURI(request);
		String body = "\"\"";
		String message = Constants.API_STATUS_FAIL;
		WebErrors errors = WebErrors.create(request);
		// 验证appId是否有效
		String code = ResponseCode.API_CODE_USER_NOT_LOGIN;
		ApiAccount apiAccount = apiAccountMng.getApiAccount(request);
		if (apiAccount != null && !apiAccount.getDisabled()) {
			// 不允许访问
			if (!isFireWallAllow(request, response)) {
				code = ResponseCode.API_CODE_FIREWALL_FORBID;
				errors.addErrorString(Constants.API_MESSAGE_FIREWALL_FORBID);
			} else {
				// 获取用户并且刷新用户活跃时间
				CmsUser user = apiUserLoginMng.getUser(apiAccount, request);
				// 获取用户状态
				Short userStatus = apiUserLoginMng.getStatus(apiAccount, request, response);
				if (user == null) {
					errors.addErrorString(Constants.API_MESSAGE_USER_NOT_LOGIN);
				} else {
					// 获得站点
					CmsSite site = getSite(user, request, response);
					CmsUtils.setSite(request, site);
					boolean hasNoSiteRight = false;
					// 没有该站管理权限(则切换站点？)
					if (site != null && user != null && user.getUserSite(site.getId()) == null) {
						Set<CmsUserSite> userSites = user.getUserSites();
						if (userSites != null && userSites.size() > 0) {
							CmsSite s = userSites.iterator().next().getSite();
							authorizingRealm.removeUserAuthorizationInfoCache(user.getUsername().toString());
							CmsUtils.setSite(request, s);
							CmsThreadVariable.setSite(s);
							hasNoSiteRight = true;
						}
					}
					// Site加入线程变量
					CmsThreadVariable.setSite(CmsUtils.getSite(request));
					// 不在验证的范围内
					if (exclude(uri)) {
						return true;
					}
					// 判断用户活跃状态
					if (userStatus.equals(ApiUserLogin.USER_STATUS_LOGOVERTIME)) {
						errors.addErrorString(Constants.API_MESSAGE_USER_OVER_TIME);
						code = ResponseCode.API_CODE_USER_STATUS_OVER_TIME;
					} else {
						// 用户不是管理员，提示无权限。用户是否拥有本API调用的权限
						if (!user.getAdmin() || !hasUrlPerm(site, user, uri) || hasNoSiteRight) {
							errors.addErrorString(Constants.API_MESSAGE_USER_NOT_HAS_PERM);
							code = ResponseCode.API_CODE_USER_NOT_HAS_PERM;
						} else {
							boolean needValidateSign = false;

							HandlerMethod handlerMethod = (HandlerMethod) handler;
							Method method = handlerMethod.getMethod();
							SignValidate annotation = method.getAnnotation(SignValidate.class);
							if (annotation != null) {
								needValidateSign = annotation.need();
							}
							if (needValidateSign) {
								if (user.getViewonlyAdmin()) {
									errors.addErrorString(Constants.API_MESSAGE_USER_NOT_HAS_PERM);
									code = ResponseCode.API_CODE_USER_NOT_HAS_PERM;
								} else {
									Object[] result = validateSign(request, errors);
									Boolean succ = (Boolean) result[0];
									if (!succ) {
										code = (String) result[1];
									} else {
										// 需要验证签名是否重复请求数据
										String sign = RequestUtils.getQueryParam(request, Constants.COMMON_PARAM_SIGN);
										apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
												apiAccount.getAppId(), request.getRequestURI(), sign);
									}
								}
							}
							CmsUtils.setUser(request, user);
							// User加入线程变量
							CmsThreadVariable.setUser(user);
						}
						// 刷新用户活跃时间
						apiUserLoginMng.userActive(request, response);
					}
				}
			}
		} else {
			code = ResponseCode.API_CODE_APP_PARAM_ERROR;
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
		}
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
			ApiResponse apiResponse = new ApiResponse(request, body, message, code);
			ResponseUtils.renderApiJson(response, request, apiResponse);
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public void afterPropertiesSet() {
		fireWallConfigFile = new File(realPathResolver.get(com.jeecms.cms.Constants.FIREWALL_CONFIGPATH));
		try {
			in = new FileInputStream(realPathResolver.get(com.jeecms.cms.Constants.FIREWALL_CONFIGPATH));
			p.load(in);
			in.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 按参数、cookie、域名、默认。
	 * 
	 * @param request
	 * @return 不会返回null，如果站点不存在，则抛出异常。
	 */
	private CmsSite getSite(CmsUser user, HttpServletRequest request, HttpServletResponse response) {
		CmsSite site = getByParams(request, response);
		if (site == null) {
			site = getByCookie(request);
		}
		if (site == null) {
			if (!hasRepeatDomainSite(request)) {
				site = getByDomain(request);
			}
		}
		if (site == null) {
			site = getByUserSites(user, request, response);
		}
		if (site == null) {
			site = getByDefault();
		}
		if (site == null) {
			throw new RuntimeException("cannot get site!");
		} else {
			return site;
		}
	}

	private CmsSite getByParams(HttpServletRequest request, HttpServletResponse response) {
		String p = request.getParameter(SITE_PARAM);
		if (!StringUtils.isBlank(p)) {
			try {
				Integer siteId = Integer.parseInt(p);
				CmsSite site = cmsSiteMng.findById(siteId);
				if (site != null) {
					// 若使用参数选择站点，则应该把站点保存至cookie中才好。
					CookieUtils.addCookie(request, response, SITE_COOKIE, site.getId().toString(), null, null);
					return site;
				}
			} catch (NumberFormatException e) {
				log.warn("param site id format exception", e);
			}
		}
		return null;
	}

	private CmsSite getByCookie(HttpServletRequest request) {
		Cookie cookie = CookieUtils.getCookie(request, SITE_COOKIE);
		if (cookie != null) {
			String v = cookie.getValue();
			if (!StringUtils.isBlank(v)) {
				try {
					Integer siteId = Integer.parseInt(v);
					return cmsSiteMng.findById(siteId);
				} catch (NumberFormatException e) {
					log.warn("cookie site id format exception", e);
				}
			}
		}
		return null;
	}

	private CmsSite getByDomain(HttpServletRequest request) {
		String domain = request.getServerName();
		if (!StringUtils.isBlank(domain)) {
			return cmsSiteMng.findByDomain(domain);
		}
		return null;
	}

	private CmsSite getByDefault() {
		List<CmsSite> list = cmsSiteMng.getListFromCache();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	private CmsSite getByUserSites(CmsUser user, HttpServletRequest request, HttpServletResponse response) {
		if (user != null) {
			Set<CmsSite> sites = user.getSites();
			if (sites != null && sites.size() > 0) {
				CmsSite site = sites.iterator().next();
				return site;
			}
		}
		return null;
	}

	private boolean hasRepeatDomainSite(HttpServletRequest request) {
		String domain = request.getServerName();
		if (!StringUtils.isBlank(domain)) {
			return cmsSiteMng.hasRepeatByProperty("domain");
		}
		return false;
	}

	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(uri)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request) throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 2;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException("admin access path not like '/jeeadmin/jeecms/...' pattern: " + uri);
		}
		return uri.substring(start);
	}

	private Object[] validateSign(HttpServletRequest request, WebErrors errors) {
		boolean vali = true;
		String sign = request.getParameter(Constants.COMMON_PARAM_SIGN);
		String appId = request.getParameter(Constants.COMMON_PARAM_APPID);
		ApiAccount apiAccount = apiAccountMng.findByAppId(appId);
		errors = ApiValidate.validateApiAccount(request, errors, apiAccount);
		String code = "";
		Object[] result = new Object[2];
		if (errors.hasErrors()) {
			code = ResponseCode.API_CODE_APP_PARAM_ERROR;
			vali = false;
		} else {
			// 验证签名
			errors = ApiValidate.validateSign(request, errors, apiAccount, sign);
			// Account可能获取不到，需要再次判断
			if (errors.hasErrors()) {
				code = ResponseCode.API_CODE_SIGN_ERROR;
				vali = false;
			}
		}
		result[0] = vali;
		result[1] = code;
		return result;
	}

	private boolean hasUrlPerm(CmsSite site, CmsUser user, String url) {
		Set<String> perms = getUserPermission(site, user);
		if (perms == null) {
			return true;
		} else {
			Iterator<String> it = perms.iterator();
			while (it.hasNext()) {
				String perm = it.next();
				if (perm.equals("*") || url.startsWith(perm)) {
					return true;
				}
			}
		}
		return false;
	}

	private Set<String> getUserPermission(CmsSite site, CmsUser user) {
		Set<String> perms = user.getPerms();
		Set<String> userPermission = new HashSet<String>();
		if (perms != null) {
			for (String perm : perms) {
				userPermission.add(perm);
			}
		}
		return userPermission;
	}

	private boolean isFireWallAllow(HttpServletRequest request, HttpServletResponse response) {
		Boolean configFileModified = false;
		Long configLastModifiedTime = getFireWallConfigFileLastModifiedTime(request, response);
		if (configLastModifiedTime == null || fireWallConfigFile.lastModified() > configLastModifiedTime) {
			configFileModified = true;
			changeConfigModifiedTime(request, response);
		}
		String open;
		String domain;
		String ips;
		String week;
		String hour;
		if (configFileModified) {
			try {
				in = new FileInputStream(realPathResolver.get(com.jeecms.cms.Constants.FIREWALL_CONFIGPATH));
				p.load(in);
				in.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		open = p.getProperty(property_firewall_open);
		domain = p.getProperty(property_firewall_domain);
		ips = p.getProperty(property_firewall_ips);
		week = p.getProperty(property_firewall_week);
		hour = p.getProperty(property_firewall_hour);
		String[] ipArrays = StringUtils.split(ips, ",");
		String[] weekArrays = StringUtils.split(week, ",");
		String[] hourArrays = StringUtils.split(hour, ",");

		String requestIp = RequestUtils.getIpAddr(request);
		if (open.equals("1")) {
			if (!isAuthDomain(domain, request.getServerName())) {
				return false;
			} else {
				if (!isAuthIp(ipArrays, requestIp)) {
					return false;
				} else {
					if (!isAuthWeek(weekArrays)) {
						return false;
					} else {
						if (!isAuthHour(hourArrays)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private Boolean isAuthDomain(String domain, String requestDomain) {
		if (StringUtils.isNotBlank(domain)) {
			if (domain.equals(requestDomain)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private Boolean isAuthIp(String[] ips, String requestIp) {
		if (ips != null && ips.length > 0) {
			for (String ip : ips) {
				if (ip.equals(requestIp)) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	private Boolean isAuthWeek(String[] weeks) {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK);
		if (weeks != null && weeks.length > 0) {
			for (String week : weeks) {
				if (week.equals(day_of_week + "")) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	private Boolean isAuthHour(String[] hours) {
		Calendar c = Calendar.getInstance();
		int hour_of_day = c.get(Calendar.HOUR_OF_DAY);
		if (hours != null && hours.length > 0) {
			for (String hour : hours) {
				if (hour.equals(hour_of_day + "")) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	private Long getFireWallConfigFileLastModifiedTime(HttpServletRequest request, HttpServletResponse response) {
		return (Long) session.getAttribute(request, FIREWALL_CONFIG_LASTMODIFIED);
	}

	private void changeConfigModifiedTime(HttpServletRequest request, HttpServletResponse response) {
		session.setAttribute(request, response, FIREWALL_CONFIG_LASTMODIFIED,
				Calendar.getInstance().getTime().getTime());
	}

	private CmsSiteMng cmsSiteMng;

	private String[] excludeUrls;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private CmsAuthorizingRealm authorizingRealm;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private SessionProvider session;

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

	public void setExcludeUrls(String[] excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}