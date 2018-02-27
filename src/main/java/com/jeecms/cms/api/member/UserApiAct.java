package com.jeecms.cms.api.member;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.ApiUserLogin;
import com.jeecms.cms.manager.assist.CmsWebserviceMng;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.manager.CmsUserExtMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class UserApiAct {
	
	/**
	 * 获取用户状态API
	 * @param username 用户名 必选
	 * @param sessionKey 会话标识 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/getStatus")
	public void getUserStatus(
			String username,String sessionKey,String appId,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,sessionKey,appId,username);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
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
	 * 添加会员用户
	 * @param username 用户名   必选
	 * @param email 邮箱 非必选
	 * @param loginPassword 密码  必选
	 * @param realname 真实姓名 非必选
	 * @param gender 性别 非必选
	 * @param birthdayStr 生日 格式"yyyy-MM-dd" 例如"1980-01-01" 非必选
	 * @param phone  电话 非必选
	 * @param mobile 手机 非必选
	 * @param qq qq号  非必选
	 * @param userImg 用户头像  非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/add")
	public void userAdd(
			String username, String email, String loginPassword,
			String realname,Boolean gender,String birthdayStr,
			String phone,String mobile,String qq,String userImg,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,username,loginPassword);
		if(!errors.hasErrors()){
			user=cmsUserMng.findByUsername(username);
			if(user==null){
				String ip = RequestUtils.getIpAddr(request);
				Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
				boolean disabled=false;
				CmsSite site = CmsUtils.getSite(request);
				CmsConfig config = site.getConfig();
				if(config.getMemberConfig().isCheckOn()){
					disabled=true;
				}
				CmsUserExt userExt=new CmsUserExt();
				if(StringUtils.isNotBlank(birthdayStr)){
					userExt.setBirthday(DateUtils.parseDayStrToDate(birthdayStr));
				}
				userExt.setGender(gender);
				userExt.setMobile(mobile);
				userExt.setPhone(phone);
				userExt.setQq(qq);
				userExt.setRealname(realname);
				userExt.setUserImg(userImg);
				user=cmsUserMng.registerMember(username, email, loginPassword, ip, null,null,disabled,userExt,attrs);
				cmsWebserviceMng.callWebService("false",username, loginPassword, email, userExt,CmsWebservice.SERVICE_TYPE_ADD_USER);
				body="{\"id\":"+"\""+user.getId()+"\"}";
				message=Constants.API_MESSAGE_SUCCESS;
				code =ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				//用户名已存在
				message=Constants.API_MESSAGE_USERNAME_EXIST;
				code=ResponseCode.API_CODE_USERNAME_EXIST;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改会员资料
	 * @param username 用户名   必选
	 * @param realname 真实姓名 非必选
	 * @param gender 性别 非必选
	 * @param birthdayStr 生日 格式"yyyy-MM-dd" 例如"1980-01-01" 非必选
	 * @param phone  电话 非必选
	 * @param mobile 手机 非必选
	 * @param qq qq号  非必选
	 * @param userImg 用户头像 非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/edit")
	public void userEdit(
			String username, String realname,Boolean gender,
			String birthdayStr,String phone,String mobile,String qq,
			String userImg,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, username);
		if(!errors.hasErrors()){
			user=cmsUserMng.findByUsername(username);
			if(user!=null){
				CmsUserExt userExt=user.getUserExt();
				if(StringUtils.isNotBlank(birthdayStr)){
					userExt.setBirthday(DateUtils.parseDayStrToDate(birthdayStr));
				}
				userExt.setGender(gender);
				if(StringUtils.isNotBlank(mobile)){
					userExt.setMobile(mobile);
				}
				if(StringUtils.isNotBlank(phone)){
					userExt.setPhone(phone);
				}
				if(StringUtils.isNotBlank(qq)){
					userExt.setQq(qq);
				}
				if(StringUtils.isNotBlank(realname)){
					userExt.setRealname(realname);
				}
				if(StringUtils.isNotBlank(userImg)){
					userExt.setUserImg(userImg);
				}
				cmsUserExtMng.update(userExt, user);
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				//用户不存在
				message=Constants.API_MESSAGE_USER_NOT_FOUND;
				code = ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}
		
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改会员密码
	 * @param username 用户名   必选
	 * @param email 邮箱 非必选
	 * @param origPwd 原密码  必选
	 * @param newPwd 新密码 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/pwd")
	public void pwdEdit(
			String username, String origPwd,String newPwd,String email,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,username);
		if(!errors.hasErrors()){
			user=cmsUserMng.findByUsername(username);
			if(user!=null){
				//原密码错误
				if (!cmsUserMng.isPasswordValid(user.getId(), origPwd)) {
					message=Constants.API_MESSAGE_ORIGIN_PWD_ERROR;
					code=ResponseCode.API_CODE_ORIGIN_PWD_ERROR;
				}else{
					cmsUserMng.updatePwdEmail(user.getId(), newPwd, email);
					message=Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
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
	 * 获取用户信息
	 * @param username 用户名必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/get")
	public void getUserInfo(Integer https,	String username,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		WebErrors errors=WebErrors.create(request);
		CmsUser user = null;
		CmsSite site=CmsUtils.getSite(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,username);
		if(!errors.hasErrors()){
			user=cmsUserMng.findByUsername(username);
			if(user!=null){
				try {
					body=user.convertToJson(site,https,null).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				message=Constants.API_MESSAGE_SUCCESS;
			}else{
				//用户不存在
				message=Constants.API_MESSAGE_USER_NOT_FOUND;
			}
		}else{
			message=Constants.API_MESSAGE_PARAM_REQUIRED;
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 用户退出API
	 * @param username 用户名 必选
	 * @param sessionKey 会话标识 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/logout")
	public void userLogout(String appId,String sessionKey,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		CmsUser user = CmsUtils.getUser(request);
		if (user!=null) {
			//已登录则退出
			String decryptSessionKey="";
			if(StringUtils.isNotBlank(appId)){
				ApiAccount apiAccount=apiAccountMng.findByAppId(appId);
				if(apiAccount!=null){
					String aesKey=apiAccount.getAesKey();
					try {
						decryptSessionKey = AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				apiUserLoginMng.userLogout(user.getUsername(),appId, decryptSessionKey);
				LoginUtils.logout();
			}
			message = Constants.API_MESSAGE_SUCCESS;
		}else{
			//用户不存在
			message=Constants.API_MESSAGE_USER_NOT_FOUND;
			code=ResponseCode.API_CODE_USER_NOT_FOUND;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	private String weiXinJsCode2SessionUrl;
	
	public String getWeiXinJsCode2SessionUrl() {
		return weiXinJsCode2SessionUrl;
	}

	public void setWeiXinJsCode2SessionUrl(String weiXinJsCode2SessionUrl) {
		this.weiXinJsCode2SessionUrl = weiXinJsCode2SessionUrl;
	}

	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsUserExtMng cmsUserExtMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private CmsWebserviceMng cmsWebserviceMng;
	
}

