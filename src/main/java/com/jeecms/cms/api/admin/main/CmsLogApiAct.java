package com.jeecms.cms.api.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsLog;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsLogApiAct {
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/log/operating_list")
	public void list(String queryUsername, String queryTitle,Integer category,
			String queryIp, Integer pageNo, Integer pageSize,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (category==null) {
			category=CmsLog.OPERATING;
		}
		Pagination page =null;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		if (category.equals(CmsLog.OPERATING)) {
			page = manager.getPage(CmsLog.OPERATING, site.getId(),queryUsername, queryTitle, queryIp, pageNo, pageSize);
		}else if (category.equals(CmsLog.LOGIN_SUCCESS)) {
			page = manager.getPage(CmsLog.LOGIN_SUCCESS, null,queryUsername, queryTitle, queryIp, pageNo, pageSize);
		}else if (category.equals(CmsLog.LOGIN_FAILURE)) {
			page = manager.getPage(CmsLog.LOGIN_FAILURE, null,null, queryTitle, queryIp, pageNo, pageSize);
		}else{
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
		}
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
			code = ResponseCode.API_CODE_PARAM_ERROR;
		}else{
			int totalCount = page.getTotalCount();
			List<CmsLog> list = (List<CmsLog>) page.getList();
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private CmsLogMng manager;
}
