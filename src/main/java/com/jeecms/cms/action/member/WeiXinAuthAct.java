package com.jeecms.cms.action.member;


import static com.jeecms.cms.Constants.TPLDIR_MEMBER;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.MemberConfig;
import com.jeecms.core.manager.CmsUserAccountMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;


/**
 * 微信授权登陆
 */
@Controller
public class WeiXinAuthAct {
	
	public static final String MEMBER_WEIXIN_AUTH = "tpl.weixinAuth";
	public static final String MEMBER_WEIXIN_AUTH_ENTER = "tpl.weixinAuthEnter";
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getCodeUrl";
	public static final String WEIXIN_AUTH_TOKEN_URL ="weixin.auth.getAccessTokenUrl";
	private static final Logger log = LoggerFactory.getLogger(WeiXinAuthAct.class);
	
	//进入微信授权登录二维码页面(需要先登陆在进入扫码)
	@RequestMapping(value = "/member/weixin_auth_enter.jspx", method = RequestMethod.GET)
	public String weixinAuthEnter(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String url="/member/weixin_auth.jspx";
		if(StringUtils.isNotBlank(site.getContextPath())){
			url=site.getUrlPrefixWithNoDefaultPort()+site.getContextPath()+url;
		}else{
			url=site.getUrlPrefixWithNoDefaultPort()+url;
		}
		model.addAttribute("url", url);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_WEIXIN_AUTH_ENTER);
	}
	
	//进入微信授权登录二维码页面(需要先登陆在进入扫码)
	@RequestMapping(value = "/member/weixin_auth.jspx", method = RequestMethod.GET)
	public String weixinAuth(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		String codeUrl="";
		if(getWeixinAuthCodeUrl()==null){
			codeUrl=PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_CODE_URL);
			setWeixinAuthCodeUrl(codeUrl);
		}
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		String redirect_uri="/member/weixin_auth_call.jspx";
		if(StringUtils.isNotBlank(site.getContextPath())){
			redirect_uri=site.getUrlPrefixWithNoDefaultPort()+site.getContextPath()+redirect_uri;
		}else{
			redirect_uri=site.getUrlPrefixWithNoDefaultPort()+redirect_uri;
		}
		codeUrl=getWeixinAuthCodeUrl()+"?appid="+config.getWeixinAppId()+"&redirect_uri="+redirect_uri
				+"&response_type=code&scope=snsapi_userinfo&state=jeecms#wechat_redirect";
		model.addAttribute("codeUrl", codeUrl);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_WEIXIN_AUTH);
	}
	
	
	/**
	 * 微信授权网页登陆回调地址
	 * 设置用户账户的微信openid,用于提现，企业打款接口
	 * @param code
	 */
	@RequestMapping(value = "/member/weixin_auth_call.jspx", method = RequestMethod.GET)
	public String weixinAuthCall(String code,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		MemberConfig mcfg = site.getConfig().getMemberConfig();
		// 没有开启会员功能
		if (!mcfg.isMemberOn()) {
			return FrontUtils.showMessage(request, model, "member.memberClose");
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(getWeixinAuthTokenUrl()==null){
			setWeixinAuthTokenUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_TOKEN_URL));
		}
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		String tokenUrl=getWeixinAuthTokenUrl()+"&appid="+config.getWeixinAppId()+"&secret="+config.getWeixinSecret()+"&code="+code;
		JSONObject json=null;
		try {
			json = new JSONObject(HttpClientUtil.getInstance().get(tokenUrl));
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		FrontUtils.frontData(request, model, site);
		if(json!=null){
			try {
				String openid = json.getString("openid");
				if(StringUtils.isNotBlank(openid)){
					userAccountMng.updateWeiXinOpenId(user.getId(), openid);
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
	
	
	//获取微信用户openid
	@RequestMapping(value = "/common/getOpenId.jspx", method = RequestMethod.GET)
	public void getWeixinOpenId(String rediretUrl,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		String codeUrl="";
		JSONObject json=new JSONObject();
		if(getWeixinAuthCodeUrl()==null){
			codeUrl=PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_CODE_URL);
			setWeixinAuthCodeUrl(codeUrl);
		}
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		String redirect_uri="/common/setOpenId.jspx";
		if(StringUtils.isNotBlank(site.getContextPath())){
			redirect_uri=site.getUrlPrefixWithNoDefaultPort()+site.getContextPath()+redirect_uri;
		}else{
			redirect_uri=site.getUrlPrefixWithNoDefaultPort()+redirect_uri;
		}
		codeUrl=getWeixinAuthCodeUrl()+"?appid="+config.getWeixinAppId()+"&redirect_uri="+redirect_uri
				+"&response_type=code&scope=snsapi_base&state=jeecms#wechat_redirect";
		try {
			json.put("url", codeUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		session.setAttribute(request, response, "weChatAuthRediretUrl", rediretUrl);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	/**
	 * 静默模式获取用户openid
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/common/setOpenId.jspx", method = RequestMethod.GET)
	public void setOpenId(String code,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if(getWeixinAuthTokenUrl()==null){
			setWeixinAuthTokenUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_AUTH_TOKEN_URL));
		}
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		String tokenUrl=getWeixinAuthTokenUrl()+"&appid="+config.getWeixinAppId()+"&secret="+config.getWeixinSecret()+"&code="+code;
		JSONObject json=null;
		try {
			json = new JSONObject(HttpClientUtil.getInstance().get(tokenUrl));
		} catch (JSONException e2) {
			//e2.printStackTrace();
			log.error("get token ->",e2.getMessage());
		}
		FrontUtils.frontData(request, model, site);
		if(json!=null){
			try {
				String openid = json.getString("openid");
				if(StringUtils.isNotBlank(openid)){
					session.setAttribute(request, response, "wxopenid", openid);
				}
			} catch (JSONException e) {
				String errcode = null;
				try {
					errcode = json.getString("errcode");
					log.error("get open id ->",errcode);
				} catch (JSONException e1) {
					//e1.printStackTrace();
				}
			}
		}
		String weChatAuthRediretUrl=(String) session.getAttribute(request, "weChatAuthRediretUrl");
		if(StringUtils.isNotBlank(weChatAuthRediretUrl)){
			try {
				response.sendRedirect(weChatAuthRediretUrl);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	

	private String weixinAuthCodeUrl;
	private String weixinAuthTokenUrl;
	

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

	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
	@Autowired
	private CmsUserAccountMng userAccountMng;
	@Autowired
	private SessionProvider session;
}
