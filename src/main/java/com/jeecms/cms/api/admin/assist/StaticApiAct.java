package com.jeecms.cms.api.admin.assist;

import java.io.IOException;
import java.util.Date;

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

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.staticpage.StaticPageSvc;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.CmsUtils;

import freemarker.template.TemplateException;

@Controller
public class StaticApiAct {
	
	private static final Logger log = LoggerFactory.getLogger(StaticApiAct.class);
	
	@SignValidate
	@RequestMapping(value = "/static/index")
	public void indexSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		JSONObject json = new JSONObject();
		try {
			boolean staticRequired=true;
			CmsSite site = CmsUtils.getSite(request);
			if(!site.getStaticIndex()){
				staticRequired=false;
				message="error.static.noopen";
			}
			if(staticRequired){
				json.put("success", true);
				staticPageSvc.index(site);
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
			}
		} catch (IOException e) {
			log.error("static index error!", e);
			json.put("success", false);
		} catch (TemplateException e) {
			log.error("static index error!", e);
			json.put("success", false);
		}
		body=json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/static/index_remove")
	public void indexRemove(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		CmsSite site = CmsUtils.getSite(request);
		JSONObject json = new JSONObject();
		if (staticPageSvc.deleteIndex(site)) {
			json.put("success", true);
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
		} else {
			json.put("success", false);
		}
		body=json.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/static/channel")
	public void channelSubmit(Integer channelId, Boolean containChild,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		if(containChild==null){
			containChild=true;
		}
		CmsSite site=CmsUtils.getSite(request);
		try {
			boolean staticRequired=true;
			if(channelId!=null){
				Channel c=channelMng.findById(channelId);
				if(c!=null&&(!StringUtils.isBlank(c.getLink()) || !c.getStaticChannel())){
					staticRequired=false;
					message="error.static.noopen";
				}
			}
			if(staticRequired){
				int count = staticPageSvc.channel(request, response,
						site.getId(), channelId, containChild);
				body="\""+count+"\"";
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
			}
		} catch (IOException e) {
			log.error("static channel error!", e);
			e.printStackTrace();
		} catch (TemplateException e) {
			log.error("static channel error!", e);
			e.printStackTrace();
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/static/content")
	public void contentSubmit(Integer channelId, Date startDate,
			HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		try {
			Integer siteId = null;
			boolean staticRequired=true;
			if (channelId == null) {
				// 没有指定栏目，则需指定站点
				siteId = CmsUtils.getSiteId(request);
			}else{
				Channel c=channelMng.findById(channelId);
				if(c!=null&&(!StringUtils.isBlank(c.getLink()) || !c.getStaticContent())){
					staticRequired=false;
					message="error.static.noopen";
				}
			}
			if(staticRequired){
				int count = staticPageSvc.content(request, response, siteId, channelId, startDate);
				body="\""+count+"\"";
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
			}
		} catch (IOException e) {
			log.error("static channel error!", e);
		} catch (TemplateException e) {
			log.error("static channel error!", e);
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/static/progress")
	public void getProgress(
			HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		Integer totalCount=null,currCount=null;
		Integer progress=0;
		Object total=session.getAttribute(request, Constants.STATIC_TOTAL_COUNT_KEY);
		Object curr=session.getAttribute(request, Constants.STATIC_CURR_COUNT_KEY);
		if(total!=null){
			totalCount=(Integer) total;
		}
		if(curr!=null){
			currCount=(Integer) curr;
		}
		if(total!=null&&curr!=null){
			progress=(int) (StrUtils.retainTwoDecimal(Double.valueOf(currCount)/totalCount)*100);
		}
		if(progress>100){
			progress=100;
		}
		body="\""+progress+"\"";
		code = ResponseCode.API_CODE_CALL_SUCCESS;
		message=Constants.API_MESSAGE_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private StaticPageSvc staticPageSvc;
	@Autowired
	private SessionProvider session;
}

