package com.jeecms.cms.manager.main.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.dao.main.ApiUserLoginDao;
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.ApiUserLogin;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;

@Service
@Transactional
public class ApiUserLoginMngImpl implements ApiUserLoginMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	public void clearByDate(Date end){
		dao.clearByDate(end);
	}
	
	@Transactional(readOnly = true)
	public List<ApiUserLogin> getList(Date end,int first, int count){
		return dao.getList(end,first,count);
	}

	@Transactional(readOnly = true)
	public ApiUserLogin findById(Long id) {
		ApiUserLogin entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ApiUserLogin findUserLogin(String username,String sessionKey){
		return dao.findUserLogin(username, sessionKey);
	}
	
	public CmsUser getUser(HttpServletRequest request){
		CmsUser user=null;
		ApiAccount apiAccount=apiAccountMng.getApiAccount(request);
		if(apiAccount!=null&&!apiAccount.getDisabled()){
			user=getUser(apiAccount, request);
		}
		return user;
	}
	
	@Transactional(readOnly = true)
	public CmsUser getUser(ApiAccount apiAccount,HttpServletRequest request){
		CmsUser user=null;
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		String aesKey=apiAccount.getAesKey();
		user=findUser(sessionKey, aesKey, apiAccount.getIvKey());
		return user;
	}
	
	@Transactional(readOnly = true)
	public CmsUser findUser(String sessionKey,String aesKey,String ivKey){
		String decryptSessionKey="";
		CmsUser user=null;
		if(StringUtils.isNotBlank(sessionKey)){
			try {
				//sessionKey用户会话标志加密串
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,ivKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ApiUserLogin apiUserLogin=findUserLogin(null, decryptSessionKey);
			if(apiUserLogin!=null&&StringUtils.isNotBlank(decryptSessionKey)){
				String username=apiUserLogin.getUsername();
				if(StringUtils.isNotBlank(username)){
					user=userMng.findByUsername(username);
				}
			}
		}
		return user;
	}
	
	public ApiUserLogin userLogin(String username,String appId, String sessionKey,
			HttpServletRequest request,HttpServletResponse response){
		ApiAccount apiAccount;
		if(StringUtils.isNotBlank(appId)){
			apiAccount=apiAccountMng.findByAppId(appId);
		}else{
			apiAccount=apiAccountMng.findByDefault();
		}
		// 一个账户只允许一个设备登陆则修改
		ApiUserLogin login=null;
		boolean isLimitSingleDev=false;
		if(apiAccount!=null&&!apiAccount.getDisabled()&&apiAccount.getLimitSingleDevice()){
			isLimitSingleDev=true;
		}
		if(isLimitSingleDev){
			login=findUserLogin(username,null);
		}else{
			login=findUserLogin(username,sessionKey);
		}
		if(login==null){
			login=new ApiUserLogin();
			login.setLoginTime(Calendar.getInstance().getTime());
			login.setActiveTime(Calendar.getInstance().getTime());
			login.setLoginCount(1);
			login.setSessionKey(sessionKey);
			login.setUsername(username);
			login=save(login);
		}else{
			login.setLoginTime(Calendar.getInstance().getTime());
			login.setActiveTime(Calendar.getInstance().getTime());
			login.setLoginCount(1+login.getLoginCount());
			login.setSessionKey(sessionKey);
			update(login);
		}
		onLoginSuccess(username, request, response);
		return login;
	}
	
	public ApiUserLogin userLogout(String username,String appId, String sessionKey){
		ApiUserLogin login=findUserLogin(username,sessionKey);
		if(login!=null){
			boolean isLimitSingleDev=false;
			ApiAccount apiAccount;
			if(StringUtils.isNotBlank(appId)){
				apiAccount=apiAccountMng.findByAppId(appId);
			}else{
				apiAccount=apiAccountMng.findByDefault();
			}
			// 一个账户只允许一个设备登陆则修改
			if(apiAccount!=null&&!apiAccount.getDisabled()&&apiAccount.getLimitSingleDevice()){
				isLimitSingleDev=true;
			}
			if(isLimitSingleDev){
				login.setSessionKey("");
				login.setActiveTime(null);
				update(login);
			}else{
				deleteById(login.getId());
			}
		}
		return login;
	}
	
	public void userActive(HttpServletRequest request,HttpServletResponse response){
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		ApiAccount apiAccount = apiAccountMng.getApiAccount(request);
		Short status=getStatus(apiAccount,request,response);
		if(apiAccount!=null){
			String decryptSessionKey="";
			String aesKey=apiAccount.getAesKey();
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				userActive(decryptSessionKey);
			}
		}
		if(apiAccount!=null&&status.equals(ApiUserLogin.USER_STATUS_LOGIN)){
			userActive(sessionKey);
		}
	}
	
	@Transactional(readOnly = true)
	public Short getStatus(ApiAccount apiAccount,
			HttpServletRequest request,HttpServletResponse response){
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		Short loginStatus=ApiUserLogin.USER_STATUS_LOGOUT;
		if(apiAccount!=null){
			String decryptSessionKey="";
			String aesKey=apiAccount.getAesKey();
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				loginStatus=getUserStatus(decryptSessionKey);
			}
		}
		return loginStatus;
	}
	
	
	@Transactional(readOnly = true)
	public Short getUserStatus(String sessionKey){
		ApiUserLogin login=findUserLogin(null, sessionKey);
		if(login!=null&&login.getActiveTime()!=null&&login.getSessionKey().equals(sessionKey)){
			Date activeTime=login.getActiveTime();
			Date now=Calendar.getInstance().getTime();
			if(DateUtils.getDiffMinuteTwoDate(activeTime, now)<=Constants.USER_OVER_TIME){
				return ApiUserLogin.USER_STATUS_LOGIN;
			}else{
				return ApiUserLogin.USER_STATUS_LOGOVERTIME;
			}
		}else{
			return ApiUserLogin.USER_STATUS_LOGOUT;
		}
	}
	

	public ApiUserLogin save(ApiUserLogin bean) {
		dao.save(bean);
		return bean;
	}

	public ApiUserLogin update(ApiUserLogin bean) {
		Updater<ApiUserLogin> updater = new Updater<ApiUserLogin>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ApiUserLogin deleteById(Long id) {
		ApiUserLogin bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiUserLogin[] deleteByIds(Long[] ids) {
		ApiUserLogin[] beans = new ApiUserLogin[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	private ApiUserLogin userActive(String sessionKey){
		ApiUserLogin login=findUserLogin(null, sessionKey);
		if(login!=null){
			login.setActiveTime(Calendar.getInstance().getTime());
		}
		return login;
	}
	
	private void onLoginSuccess(String username,
			HttpServletRequest request,HttpServletResponse response){
		CmsUser user = userMng.findByUsername(username);
		String ip = RequestUtils.getIpAddr(request);
		Date now = new Timestamp(System.currentTimeMillis());
		String userSessionId=session.getSessionId(request,response);
		userMng.updateLoginInfo(user.getId(), ip,now,userSessionId);
		unifiedUserMng.updateLoginSuccess(user.getId(), ip);
	}

	private ApiUserLoginDao dao;
	@Autowired
	private CmsUserMng userMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private UnifiedUserMng unifiedUserMng;

	@Autowired
	public void setDao(ApiUserLoginDao dao) {
		this.dao = dao;
	}
}