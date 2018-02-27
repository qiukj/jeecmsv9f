package com.jeecms.cms.action.member;

import static com.jeecms.cms.Constants.TPLDIR_MEMBER;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.ApiUserLogin;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.cms.web.Token;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

@Controller
public class CasLoginAct {
	public static final String COOKIE_ERROR_REMAINING = "_error_remaining";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final String LOGIN_STATUS = "tpl.loginStatus";
	public static final String TPL_INDEX = "tpl.index";


	@Token(save=true)
	@RequestMapping(value = "/login.jspx", method = RequestMethod.GET)
	public String input(String returnUrl,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String sol = site.getSolutionPath();
		Integer errorTimes=configMng.getConfigLogin().getErrorTimes();
		model.addAttribute("errorTimes", errorTimes);
		model.addAttribute("site",site);
		if(StringUtils.isBlank(returnUrl)){
			session.setAttribute(request, response, "loginSource", null);
		}
		Object source=session.getAttribute(request, "loginSource");
		if(source!=null){
			String loginSource=(String) source;
			model.addAttribute("loginSource",loginSource);
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, sol, TPLDIR_MEMBER, LOGIN_INPUT);
	}

	@Token(remove=true)
	@RequestMapping(value = "/login.jspx", method = RequestMethod.POST)
	public String submit(String username, HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String sol = site.getSolutionPath();
		Object error = request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		FrontUtils.frontData(request, model, site);
		if (error != null) {
			model.addAttribute("error",error);
			model.addAttribute("errorRemaining", unifiedUserMng.errorRemaining(username));
		}
		session.setAttribute(request, response, "loginSource", null);
	
		return FrontUtils.getTplPath(request, sol, TPLDIR_MEMBER, LOGIN_INPUT);
	}
	
	@RequestMapping(value = "/adminLogin.jspx", method = RequestMethod.POST)
	public void adminLogin(HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		CmsUser user=CmsUtils.getUser(request);
		ApiAccount apiAccount=apiAccountMng.findByDefault();
		JSONObject json=new JSONObject();
		String encryptSessionKey="";
		//登陆API后台
		if(user!=null&&apiAccount!=null){
			String aesKey=apiAccount.getAesKey();
			String sessionKey=session.getSessionId(request, response);
			apiUserLoginMng.userLogin(user.getUsername(), apiAccount.getAppId(), sessionKey,request,response);
			try {
				encryptSessionKey=
						AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey());
				json.put("sessionKey", encryptSessionKey);
				json.put("userName", user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping(value = "/adminLogout.jspx", method = RequestMethod.POST)
	public void adminLogout(String userName,
			String sessionKey,HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		ApiAccount apiAccount=apiAccountMng.findByDefault();
		JSONObject json=new JSONObject();
		//登出API后台
		if(StringUtils.isNotBlank(userName)&&
				StringUtils.isNotBlank(sessionKey)&&apiAccount!=null){
			String decryptSessionKey="";
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, 
						apiAccount.getAesKey(), apiAccount.getIvKey());
			} catch (Exception e) {
			}
			//检查是否登陆
			if(StringUtils.isNotBlank(decryptSessionKey)){
				ApiUserLogin userLogin=apiUserLoginMng.findUserLogin(userName, decryptSessionKey);
				if(userLogin!=null){
					apiUserLoginMng.userLogout(userName,null, decryptSessionKey);
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
}
