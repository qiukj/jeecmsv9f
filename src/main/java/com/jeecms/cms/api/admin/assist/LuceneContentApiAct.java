package com.jeecms.cms.api.admin.assist;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.lucene.LuceneContentSvc;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class LuceneContentApiAct {
	
	@SignValidate
	@RequestMapping("/lucene/create")
	public void create(Integer channelId, Date startDate,
			Date endDate, Integer startId, Integer max,
			HttpServletRequest request, HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		Integer siteId = CmsUtils.getSiteId(request);
		if (max==null) {
			max = 1000;
		}
		try {
			Integer lastId = luceneContentSvc.createIndex(siteId, channelId,
					startDate, endDate, startId, max);
			if (lastId!=null) {
				body = "{\"lastId\":"+lastId+"}";
			}else{
				body = "{\"lastId\":\"\"}";
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		} catch (Exception e) {
			message = Constants.API_MESSAGE_CREATE_ERROR;
			code = ResponseCode.API_CODE_CALL_FAIL;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	

	@Autowired
	private LuceneContentSvc luceneContentSvc;
}
