package com.jeecms.cms.api.admin.assist;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.plug.weixin.entity.Weixin;
import com.jeecms.plug.weixin.manager.WeixinMng;

@Controller
public class WeixinApiAct {
	
	/**
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/weixin/config")
	public void config(Weixin bean,String wxAppkey,String wxAppSecret,
			String wxToken,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		Weixin entity = manager.find(CmsUtils.getSiteId(request));
		
		Map<String,String>wxMap=new HashMap<String,String>();
		if(!StringUtils.isBlank(wxAppkey)){
			wxMap.put(com.jeecms.core.Constants.WEIXIN_APPKEY, wxAppkey);
		}
		if(!StringUtils.isBlank(wxAppSecret)){
			wxMap.put(com.jeecms.core.Constants.WEIXIN_APPSECRET, wxAppSecret);
		}
		if(!StringUtils.isBlank(wxToken)){
			wxMap.put(com.jeecms.core.Constants.WEIXIN_TOKEN, wxToken);
		}
		siteMng.updateAttr(site.getId(), wxMap);
		if(entity!=null){
			bean.setId(entity.getId());
			manager.update(bean);
		}else{
			manager.save(bean);
		}
		message = Constants.API_MESSAGE_SUCCESS;
		code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/weixin/get")
	public void configGet(HttpServletRequest request,
			HttpServletResponse response){
		Weixin entity = manager.find(CmsUtils.getSiteId(request));
		CmsSite site = CmsUtils.getSite(request);
		JSONObject json = entity.convertToJson();
		json = createEasyJson(json,site);
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body= json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject createEasyJson(JSONObject json, CmsSite site) {
		if (site.getAttr()!=null&& site.getAttr().size()>0) {
			Map<String, String> map = site.getAttr();
			if (StringUtils.isNotBlank(map.get("wxAppkey"))) {
				json.put("wxAppkey", map.get("wxAppkey"));
			}else{
				json.put("wxAppkey", "");
			}
		}
		return json;
	}

	@Autowired
	private WeixinMng manager;
	@Autowired
	private CmsSiteMng siteMng;
}
