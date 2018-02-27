package com.jeecms.cms.api.front;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.ApiRecord;
import com.jeecms.cms.entity.main.ApiUserLogin;
import com.jeecms.cms.entity.main.CmsThirdAccount;
import com.jeecms.cms.manager.assist.CmsWebserviceMng;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiRecordMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.cms.manager.main.CmsThirdAccountMng;
import com.jeecms.cms.service.ImageSvc;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.HttpClientUtil;
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
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class UserApiAct {
	
	private final String WEIXIN_JSCODE_2_SESSION_URL="weixin.jscode2sessionUrl";
	
	/**
	 * 用户登录API
	 * @param username 用户名 必选
	 * @param aesPassword 加密密码 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/login")
	public void userLogin(
			String username,String aesPassword,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, appId,
				nonce_str,sign,username,aesPassword);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				user=cmsUserMng.findByUsername(username);
				if(user!=null){
					String aesKey=apiAccount.getAesKey();
					//解密用户输入的密码
					String encryptPass="";
					try {
						encryptPass = AES128Util.decrypt(aesPassword, aesKey,apiAccount.getIvKey());
					} catch (Exception e) {
						//e.printStackTrace();
					}
					//验证用户密码
					if(cmsUserMng.isPasswordValid(user.getId(), encryptPass)){
						//解决会话固定漏洞
						LoginUtils.logout();
						//sessionID加密后返回 ,该值作为用户数据交互识别的关键值
						//调用接口端将该值保存，调用用户数据相关接口传递加密sessionID后的值，服务器端解密后查找用户
						String sessionKey=session.getSessionId(request, response);
						apiUserLoginMng.userLogin(username, appId, sessionKey,request,response);
						//前后台统一登录 api和web
						LoginUtils.loginShiro(request, response, username);
						CmsUtils.setUser(request, user);
						try {
							//加密返回
							body="\""+AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey())+"\"";
						} catch (Exception e) {
							e.printStackTrace();
						}
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/user/login",sign);
						message=Constants.API_MESSAGE_SUCCESS;
					}else{
						//密码错误
						message=Constants.API_MESSAGE_PASSWORD_ERROR;
						code=ResponseCode.API_CODE_PASSWORD_ERROR;
					}
				}else{
					//用户不存在
					message=Constants.API_MESSAGE_USER_NOT_FOUND;
					code=ResponseCode.API_CODE_USER_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	/**
	 * 获取用户状态API
	 * @param username 用户名 必选
	 * @param sessionKey 会话标识 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/getStatus")
	public void getUserStatus(
			String username,String sessionKey,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, appId,
				nonce_str,sign,username,sessionKey);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message="\""+errors.getErrors().get(0)+"\"";
		}else{
			user=cmsUserMng.findByUsername(username);
			if(user!=null){
				String aesKey=apiAccount.getAesKey();
				String decryptSessionKey = null;
				try {
					decryptSessionKey = AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
				} catch (Exception e) {
					//e.printStackTrace();
				}
				if(StringUtils.isNotBlank(decryptSessionKey)){
					ApiUserLogin userLogin=apiUserLoginMng.findUserLogin(username, decryptSessionKey);
					if(userLogin!=null){
						message=Constants.API_MESSAGE_USER_STATUS_OVER_TIME;
						code=ResponseCode.API_CODE_USER_STATUS_OVER_TIME;
						if(userLogin.getActiveTime()!=null){
							Date now=Calendar.getInstance().getTime();
							Double timeOver=DateUtils.getDiffMinuteTwoDate(userLogin.getActiveTime(), now);
							if(timeOver<=Constants.USER_OVER_TIME){
								message=Constants.API_MESSAGE_USER_STATUS_LOGIN;
								code=ResponseCode.API_CODE_USER_STATUS_LOGIN;
								LoginUtils.loginShiro(request, response, username);
							}else{
								CmsUser currUser=CmsUtils.getUser(request);
								if(currUser!=null){
									apiUserLoginMng.userActive(request,response);
								}else{
									//如果记住登录的
									Subject subject = SecurityUtils.getSubject();
									if(subject.isRemembered()){
										String rememberUser =  (String) subject.getPrincipal();
										LoginUtils.loginShiro(request, response, rememberUser);
									}else{
										LoginUtils.logout();
									}
								}
							}
						}
					}else{
						message=Constants.API_MESSAGE_USER_STATUS_NOT_LOGIN;
						code=ResponseCode.API_CODE_USER_STATUS_LOGOUT;
						LoginUtils.logout();
					}
				}else{
					message=Constants.API_MESSAGE_PARAM_ERROR;
					code=ResponseCode.API_CODE_PARAM_ERROR;
				}
			}else{
				//用户不存在
				message=Constants.API_MESSAGE_USER_NOT_FOUND;
				code=ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 微信小程序-微信用户登录获取sessionKey和openid API
	 * @param js_code 微信小程序登录code 必选
	 * @param grant_type 非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/weixinLogin")
	public void weixinAppLogin(
			String js_code,String grant_type,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					{
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		if(StringUtils.isNotBlank(grant_type)){
			grant_type="authorization_code";
		}
		ApiAccount apiAccount = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, appId,
				nonce_str,sign,js_code);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				initWeiXinJsCode2SessionUrl();
				Map<String,String>params=new HashMap<String, String>();
				CmsConfig config=configMng.get();
				params.put("appid", config.getWeixinAppId());
				params.put("secret", config.getWeixinAppSecret());
				params.put("js_code",js_code);
				params.put("grant_type",grant_type);
				String result=HttpClientUtil.postParams(getWeiXinJsCode2SessionUrl(),
						params);
				JSONObject json;
				Object openId = null;
				try {
					json = new JSONObject(result);
					openId=json.get("openid");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String openid=null;
				if(openId!=null){
					openid=(String)openId;
				}
				if(StringUtils.isNotBlank(openid)){
					body=thirdLoginGetSessionKey(apiAccount, openid,null, 
							Constants.THIRD_SOURCE_WEIXIN_APP, request, response);
				}
				message=Constants.API_MESSAGE_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 第三方登录API
	 * @param thirdKey 第三方key 必选
	 * @param source 第三方来源 非必选 默认微信小程序
	 * @param username 为第三方用户指定创建的用户名
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/thirdLogin")
	public void thirdLoginApi(
			String thirdKey,String source,String username,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(StringUtils.isNotBlank(source)){
			source=Constants.THIRD_SOURCE_WEIXIN_APP;
		}
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, appId,
				nonce_str,sign,thirdKey);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				body=thirdLoginGetSessionKey(apiAccount, thirdKey,
						username, source, request, response);
				apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
						appId, "/user/thirdLogin",sign);
				message=Constants.API_MESSAGE_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private String thirdLoginGetSessionKey(ApiAccount apiAccount,
			String thirdKey,String username,String source,
			HttpServletRequest request,HttpServletResponse response){
		String aesKey=apiAccount.getAesKey();
		thirdKey=pwdEncoder.encodePassword(thirdKey);
		CmsThirdAccount thirdAccount=thirdAccountMng.findByKey(thirdKey);
		if(thirdAccount!=null){
			username=thirdAccount.getUsername();
		}else{
			//用户不存在,则新建用户
			//若是没有传递用户名则随机用户
			if(StringUtils.isBlank(username)){
				username=getRandomUsername();
			}else{
				//若是传递的用户名存在则随机
				if(userExist(username)){
					username=getRandomUsername();
				}
			}
			CmsUserExt userExt=new CmsUserExt();
			//第三方授权来自微信小程序
			if(source.equals(Constants.THIRD_SOURCE_WEIXIN_APP)){
				String nickName =request.getParameter("nickName");
				String avatarUrl =request.getParameter("avatarUrl");
				String gender =request.getParameter("gender");
				String province =request.getParameter("province");
				String city =request.getParameter("city");
				String country =request.getParameter("country");
				if(StringUtils.isNotBlank(gender)){
					if(gender.equals(2)){
						userExt.setGender(false);
					}else if(gender.equals(1)){
						userExt.setGender(true);
					}else{
						userExt.setGender(null);
					}
				}
				if(StringUtils.isNotBlank(nickName)){
					userExt.setRealname(nickName);
				}
				String comefrom="";
				if(StringUtils.isNotBlank(country)){
					comefrom+=country;
				}
				if(StringUtils.isNotBlank(province)){
					comefrom+=province;
				}
				if(StringUtils.isNotBlank(city)){
					comefrom+=city;
				}
				userExt.setComefrom(comefrom);
				String imageUrl="";
				if(StringUtils.isNotBlank(avatarUrl)){
					CmsConfig config=configMng.get();
					CmsSite site=CmsUtils.getSite(request);
					Ftp ftp=site.getUploadFtp();
					imageUrl=imgSvc.crawlImg(avatarUrl, config.getContextPath(), 
							config.getUploadToDb(), config.getDbFileUri(), 
							ftp,site.getUploadOss(), site.getUploadPath());
				}
				userExt.setUserImg(imageUrl);
			}
			String ip = RequestUtils.getIpAddr(request);
			boolean disabled=false;
			CmsSite site = CmsUtils.getSite(request);
			CmsConfig config = site.getConfig();
			if(config.getMemberConfig().isCheckOn()){
				disabled=true;
			}
			CmsUser user=null;
			user=cmsUserMng.registerMember(username, null, thirdKey, ip, null,null,disabled,userExt,null);
			if(user!=null){
				//解决会话固定漏洞
				LoginUtils.logout();
				cmsWebserviceMng.callWebService("false",username, thirdKey, null, userExt,CmsWebservice.SERVICE_TYPE_ADD_USER);
				//绑定新建的用户
				thirdAccount=new CmsThirdAccount();
				thirdAccount.setUsername(username);
				thirdAccount.setAccountKey(thirdKey);
				thirdAccount.setSource(source);
				thirdAccount.setUser(user);
				thirdAccountMng.save(thirdAccount);
				LoginUtils.loginShiro(request, response, username);
				CmsUtils.setUser(request, user);
			}
			
		}
		String sessionKey=session.getSessionId(request, response);
		apiUserLoginMng.userLogin(username, apiAccount.getAppId(), sessionKey,request,response);
		JSONObject json=new JSONObject();
		try {
			//加密返回
			json.put("sessionKey", AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey()));
			json.put("username",username);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return json.toString();
	}
	
	private  String getRandomUsername(){
		SimpleDateFormat fomat=new SimpleDateFormat("yyyyMMddHHmmss");
		String username=fomat.format(Calendar.getInstance().getTime())+RandomStringUtils.random(5,Num62.N10_CHARS);;
		if (userExist(username)) {
			return getRandomUsername();
		}else{
			return username;
		}
	}
	
	private  boolean userExist(String username){
		if (unifiedUserMng.usernameExist(username)) {
			return true;
		}else{
			return false;
		}
	}
	
	private void initWeiXinJsCode2SessionUrl(){
		if(getWeiXinJsCode2SessionUrl()==null){
			setWeiXinJsCode2SessionUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_JSCODE_2_SESSION_URL));
		}
	}
	
	private String weiXinJsCode2SessionUrl;
	
	public String getWeiXinJsCode2SessionUrl() {
		return weiXinJsCode2SessionUrl;
	}

	public void setWeiXinJsCode2SessionUrl(String weiXinJsCode2SessionUrl) {
		this.weiXinJsCode2SessionUrl = weiXinJsCode2SessionUrl;
	}

	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private CmsWebserviceMng cmsWebserviceMng;
	@Autowired
	private CmsThirdAccountMng thirdAccountMng;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private ImageSvc imgSvc;
	@Autowired
	private CmsConfigMng configMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private RealPathResolver realPathResolver;
	
}

