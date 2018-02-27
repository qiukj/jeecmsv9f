package com.jeecms.cms.action.front;


import static com.jeecms.cms.Constants.TPLDIR_INDEX;
import static com.jeecms.cms.Constants.TPLDIR_MEMBER;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.main.CmsThirdAccount;
import com.jeecms.cms.manager.main.CmsThirdAccountMng;
import com.jeecms.cms.service.ImageSvc;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.HttpRequestUtil;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;


/**
 * 第三方登录Action
 * 腾讯qq、新浪微博登陆、微信登陆
 */
@Controller
public class ThirdLoginAct {
	public static final String TPL_BIND = "tpl.member.bind";
	public static final String TPL_AUTH = "tpl.member.auth";
	public static final String TPL_INDEX = "tpl.index";
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getQrCodeUrl";
	public static final String WEIXIN_AUTH_TOKEN_URL ="weixin.auth.getAccessTokenUrl";
	public static final String WEIXIN_AUTH_USER_URL ="weixin.auth.getUserInfoUrl";
	
	public static final String USER_LOG_OUT_FLAG = "logout";
	
	
	@RequestMapping(value = "/public_auth.jspx")
	public String auth(String openId,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, TPL_AUTH);
	}
	
	@RequestMapping(value = "/public_auth_login.jspx")
	public void authLogin(String key,String source,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JSONException {
		if(StringUtils.isNotBlank(source)){
			if(source.equals(CmsThirdAccount.QQ_PLAT)){
				session.setAttribute(request,response,CmsThirdAccount.QQ_KEY, key);
			}else if(source.equals(CmsThirdAccount.QQ_WEBO_PLAT)){
				session.setAttribute(request,response,CmsThirdAccount.QQ_WEBO_KEY, key);
			}else if(source.equals(CmsThirdAccount.SINA_PLAT)){
				session.setAttribute(request,response,CmsThirdAccount.SINA_KEY, key);
			}
		}
		JSONObject json=new JSONObject();
		//库中存放的是加密后的key
		if(StringUtils.isNotBlank(key)){
			key=pwdEncoder.encodePassword(key);
		}
		CmsThirdAccount account=accountMng.findByKey(key);
		if(account!=null){
			json.put("succ", true);
			//已绑定直接登陆
			loginByKey(key, request, response, model);
		}else{
			json.put("succ", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping(value = "/public_bind.jspx",method = RequestMethod.GET)
	public String bind_get(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, TPL_BIND);
	}
	
	@RequestMapping(value = "/public_bind.jspx",method = RequestMethod.POST)
	public String bind_post(String username,String password,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		boolean usernameExist=unifiedUserMng.usernameExist(username);
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String source="";
		if(!usernameExist){
			//用户名不存在
			errors.addErrorCode("error.usernameNotExist");
		}else{
			UnifiedUser u=unifiedUserMng.getByUsername(username);
			boolean passwordValid=unifiedUserMng.isPasswordValid(u.getId(), password);
			if(!passwordValid){
				errors.addErrorCode("error.passwordInvalid");
			}else{
				//获取用户来源
				String openId=(String) session.getAttribute(request, CmsThirdAccount.QQ_KEY);
				String uid=(String) session.getAttribute(request, CmsThirdAccount.SINA_KEY);
				String weboOpenId=(String) session.getAttribute(request, CmsThirdAccount.QQ_WEBO_KEY);
				String weixinOpenId=(String) session.getAttribute(request, CmsThirdAccount.WEIXIN_KEY);
				if(StringUtils.isNotBlank(openId)){
					source=CmsThirdAccount.QQ_PLAT;
				}else if(StringUtils.isNotBlank(uid)){
					source=CmsThirdAccount.SINA_PLAT;
				}else if(StringUtils.isNotBlank(weboOpenId)){
					source=CmsThirdAccount.QQ_WEBO_PLAT;
				}else if(StringUtils.isNotBlank(weixinOpenId)){
					source=CmsThirdAccount.WEIXIN_PLAT;
				}
				//提交登录并绑定账号
				loginByUsername(username, request, response, model);
			}
		}
		if(errors.hasErrors()){
			errors.toModel(model);
			model.addAttribute("success",false);
		}else{
			model.addAttribute("success",true);
		}
		model.addAttribute("source", source);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, TPL_BIND);
	}
	
	@RequestMapping(value = "/public_bind_username.jspx")
	public String bind_username_post(String username,
			String nickname,Integer sex,String province,
			String city,String headimgurl,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String source="";
		if(StringUtils.isBlank(username)){
			//用户名为空
			errors.addErrorCode("error.usernameRequired");
		}else{
			boolean usernameExist=unifiedUserMng.usernameExist(username);
			if(usernameExist){
				//用户名存在
				errors.addErrorCode("error.usernameExist");
			}else{
				//获取用户来源
				String openId=(String) session.getAttribute(request, CmsThirdAccount.QQ_KEY);
				String uid=(String) session.getAttribute(request, CmsThirdAccount.SINA_KEY);
				String weboOpenId=(String) session.getAttribute(request, CmsThirdAccount.QQ_WEBO_KEY);
				String weixinOpenId=(String) session.getAttribute(request, CmsThirdAccount.WEIXIN_KEY);
				//(获取到登录授权key后可以注册用户)
				CmsUserExt ext=new CmsUserExt();
				if(StringUtils.isNotBlank(weixinOpenId)){
					String comefrom="";
					if(StringUtils.isNotBlank(province)){
						comefrom+=province;
					}
					if(StringUtils.isNotBlank(city)){
						comefrom+=city;
					}
					ext.setComefrom(comefrom);
					if(StringUtils.isNotBlank(nickname)){
						ext.setRealname(nickname);
					}
					if(sex!=null){
						if(sex.equals(1)){
							ext.setGender(true);
						}else if(sex.equals(2)){
							ext.setGender(false);
						}
					}
					if(StringUtils.isNotBlank(headimgurl)){
						CmsConfig config=cmsConfigMng.get();
						Ftp ftp=site.getUploadFtp();
						String imageUrl=imgSvc.crawlImg(headimgurl, 
								config.getContextPath(), config.getUploadToDb(), 
								config.getDbFileUri(), ftp,site.getUploadOss(),
								site.getUploadPath());
						ext.setUserImg(imageUrl);
					}
				}
				if(StringUtils.isNotBlank(openId)||
						StringUtils.isNotBlank(uid)||
						StringUtils.isNotBlank(weboOpenId)||
						StringUtils.isNotBlank(weixinOpenId)){
					//初始设置密码同用户名
					cmsUserMng.registerMember(username, null, username, RequestUtils.getIpAddr(request), null, null, false,ext , null);
				}
				if(StringUtils.isNotBlank(openId)){
					source=CmsThirdAccount.QQ_PLAT;
				}else if(StringUtils.isNotBlank(uid)){
					source=CmsThirdAccount.SINA_PLAT;
				}else if(StringUtils.isNotBlank(weboOpenId)){
					source=CmsThirdAccount.QQ_WEBO_PLAT;
				}else if(StringUtils.isNotBlank(weixinOpenId)){
					source=CmsThirdAccount.WEIXIN_PLAT;
				}
				//提交登录并绑定账号
				loginByUsername(username, request, response, model);
			}
		}
		if(errors.hasErrors()){
			errors.toModel(model);
			model.addAttribute("success",false);
		}else{
			model.addAttribute("success",true);
		}
		model.addAttribute("source", source);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_MEMBER, TPL_BIND);
	}
	

	@RequestMapping(value = "/weixin_login.jspx")
	public String weixinLogin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String codeUrl="";
		if(getWeixinAuthCodeUrl()==null){
			codeUrl=PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_CODE_URL);
			setWeixinAuthCodeUrl(codeUrl);
		}
		CmsConfig config=cmsConfigMng.get();
		String auth_url="/weixin_auth.jspx";
		String redirect_uri=site.getUrlPrefixWithNoDefaultPort();
		if(StringUtils.isNotBlank(site.getContextPath())){
			redirect_uri+=site.getContextPath();
		}
		redirect_uri+=auth_url;
		codeUrl=getWeixinAuthCodeUrl()+"&appid="+config.getWeixinLoginId()+"&redirect_uri="+redirect_uri
				+"&state="+RandomStringUtils.random(10,Num62.N36_CHARS)+"#wechat_redirect";
		return "redirect:"+codeUrl;
	}
	
	@RequestMapping(value = "/weixin_auth.jspx")
	public String weixinAuth(String code,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if(getWeixinAuthTokenUrl()==null){
			setWeixinAuthTokenUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_TOKEN_URL));
		}
		if(getWeixinAuthUserUrl()==null){
			setWeixinAuthUserUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_USER_URL));
		}
		CmsConfig config=cmsConfigMng.get();
		String tokenUrl=getWeixinAuthTokenUrl()+"&appid="+config.getWeixinLoginId()+"&secret="+config.getWeixinLoginSecret()+"&code="+code;
		JSONObject json=null;
		try {
			//获取openid和access_token
			json = new JSONObject(HttpClientUtil.getInstance().get(tokenUrl));
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		if(json!=null){
			try {
				String openid = json.getString("openid");
				String access_token = json.getString("access_token");
				if(StringUtils.isNotBlank(openid)&&StringUtils.isNotBlank(access_token)){
					//库中存储的是加密后的
					String md5OpenId=pwdEncoder.encodePassword(openid);
					CmsThirdAccount account=accountMng.findByKey(md5OpenId);
					if(account!=null){
						//已绑定直接登陆
						loginByKey(md5OpenId, request, response, model);
						return FrontUtils.getTplPath(request, site.getSolutionPath(),TPLDIR_INDEX, TPL_INDEX);
					}else{
						String userUrl=getWeixinAuthUserUrl()+"&access_token="+access_token+"&openid="+openid;
						try {
							//获取用户信息
							json = new JSONObject(HttpClientUtil.getInstance().get(userUrl));
							String nickname=(String) json.get("nickname");
							Integer sex=(Integer) json.get("sex");
							String province=(String)json.get("province");
							String city=(String)json.get("city");
							String headimgurl=(String)json.get("headimgurl");
							model.addAttribute("nickname", nickname);
							model.addAttribute("sex", sex);
							model.addAttribute("province", province);
							model.addAttribute("city", city);
							model.addAttribute("headimgurl", headimgurl);
							session.setAttribute(request, response, CmsThirdAccount.WEIXIN_KEY, openid);
							return FrontUtils.getTplPath(request, site.getSolutionPath(),
									TPLDIR_MEMBER, TPL_BIND);
						} catch (JSONException e3) {
							e3.printStackTrace();
						}
					}
				}
			} catch (JSONException e) {
				WebErrors errors=WebErrors.create(request);
				String errcode = null;
				try {
					errcode = json.getString("errcode");
				} catch (JSONException e1) {
					//e1.printStackTrace();
				}
				if(StringUtils.isNotBlank(errcode)){
					errors.addErrorCode("weixin.auth.fail");
				}
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		return FrontUtils.showMessage(request, model,"weixin.auth.succ");
	}
	
	//判断用户是否登录
	@RequestMapping(value = "/sso/authenticate.jspx")
	public void authenticate(String username,String sessionId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsUser user= cmsUserMng.findByUsername(username);
		if(user!=null&&sessionId!=null){
			String userSessionId=user.getSessionId();
			if(StringUtils.isNotBlank(userSessionId)){
				if(userSessionId.equals(sessionId)){
					ResponseUtils.renderJson(response, "true");
				}
			}else{
				ResponseUtils.renderJson(response, "false");
			}
		}
	}
	
	@RequestMapping(value = "/sso/login.jspx")
	public void loginSso(String username,String sessionId,String ssoLogout,HttpServletRequest request,HttpServletResponse response) {
		CmsUser user =CmsUtils.getUser(request);
		if(StringUtils.isNotBlank(username)){
			JSONObject object =new JSONObject();
			try {
				if(user==null){
					//未登录，其他地方已经登录，则登录自身
					CmsConfig config=cmsConfigMng.get();
					List<String>authenticateUrls=config.getSsoAuthenticateUrls();
					String success=authenticate(username, sessionId, authenticateUrls);
					if(success.equals("true")){
						LoginUtils.loginShiro(request, response, username);
						user = cmsUserMng.findByUsername(username);
						if(user!=null){
							cmsUserMng.updateLoginInfo(user.getId(), null,null,sessionId);
						}
						object.put("result", "login");
					}
				}else if(StringUtils.isNotBlank(ssoLogout)&&ssoLogout.equals("true")){
					LoginUtils.logout();
					object.put("result", "logout");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseUtils.renderJson(response, object.toString());
		}
	}
	
	private String authenticate(String username,String sessionId,List<String>authenticateUrls){
		String result="false";
		for(String url:authenticateUrls){
			result=authenticate(username, sessionId, url);
			if(result.equals("true")){
				break;
			}
		}
		return result;
	}
	
	private String authenticate(String username,String sessionId,String authenticateUrl){
		Map<String,String>params=new HashMap<String, String>();
		params.put("username", username);
		params.put("sessionId", sessionId);
		String success="false";
		try {
			success=HttpRequestUtil.request(authenticateUrl, params, "post", "utf-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * 用户名登陆,绑定用户名和第三方账户key
	 * @param username
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByUsername(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String openId=(String) session.getAttribute(request, CmsThirdAccount.QQ_KEY);
		String uid=(String) session.getAttribute(request, CmsThirdAccount.SINA_KEY);
		String weboOpenId=(String) session.getAttribute(request, CmsThirdAccount.QQ_WEBO_KEY);
		String weixinOpenId=(String) session.getAttribute(request, CmsThirdAccount.WEIXIN_KEY);
		if(StringUtils.isNotBlank(openId)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username, openId,  CmsThirdAccount.QQ_PLAT);
		}
		if(StringUtils.isNotBlank(uid)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username, uid,  CmsThirdAccount.SINA_PLAT);
		}
		if(StringUtils.isNotBlank(weboOpenId)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username, weboOpenId,  CmsThirdAccount.QQ_WEBO_PLAT);
		}
		if(StringUtils.isNotBlank(weixinOpenId)){
			loginShiro(request, response, username);
			//绑定账户
			bind(username, weixinOpenId,  CmsThirdAccount.WEIXIN_PLAT);
		}
	}
	
	/**
	 * 已绑定用户key登录
	 * @param key
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByKey(String key,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsThirdAccount account=accountMng.findByKey(key);
		if(StringUtils.isNotBlank(key)&&account!=null){
			String username=account.getUsername();
			loginShiro(request, response, username);
		}
	}
	
	
	private void loginShiro(HttpServletRequest request,HttpServletResponse response,String username){
		PrincipalCollection principals = new SimplePrincipalCollection(username, username);  
		Builder builder = new WebSubject.Builder( request,response);  
		builder.principals(principals);  
		builder.authenticated(true);  
		WebSubject subject = builder.buildWebSubject();  
		ThreadContext.bind(subject); 
	}
	
	private void bind(String username,String openId,String source){
		CmsThirdAccount account=accountMng.findByKey(openId);
		if(account==null){
			account=new CmsThirdAccount();
			account.setUsername(username);
			//第三方账号唯一码加密存储 防冒名登录
			openId=pwdEncoder.encodePassword(openId);
			account.setAccountKey(openId);
			account.setSource(source);
			account.setUser(cmsUserMng.findByUsername(username));
			accountMng.save(account);
		}
	}
	
	private String weixinAuthCodeUrl;
	private String weixinAuthTokenUrl;
	private String weixinAuthUserUrl;
	

	public String getWeixinAuthCodeUrl() {
		return weixinAuthCodeUrl;
	}

	public void setWeixinAuthCodeUrl(String weixinAuthCodeUrl) {
		this.weixinAuthCodeUrl = weixinAuthCodeUrl;
	}

	public String getWeixinAuthTokenUrl() {
		return weixinAuthTokenUrl;
	}

	public void setWeixinAuthTokenUrl(String weixinAuthTokenUrl) {
		this.weixinAuthTokenUrl = weixinAuthTokenUrl;
	}
	
	public String getWeixinAuthUserUrl() {
		return weixinAuthUserUrl;
	}

	public void setWeixinAuthUserUrl(String weixinAuthUserUrl) {
		this.weixinAuthUserUrl = weixinAuthUserUrl;
	}

	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsThirdAccountMng accountMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private ImageSvc imgSvc;
}
