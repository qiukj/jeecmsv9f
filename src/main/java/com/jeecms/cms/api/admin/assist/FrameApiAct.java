package com.jeecms.cms.api.admin.assist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class FrameApiAct {
	/**
	 * 获取数据库类型
	 * @param request
	 * @param response
	 */
	@RequestMapping("/frame/getDB")
	public void getDb(HttpServletRequest request,HttpServletResponse response){
		String body = "{\"db\":\""+db+"\"}";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private String db;

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}
	
}
