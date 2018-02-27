package com.jeecms.cms.api.admin.assist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.Content.ContentStatus;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ContentReuseApiAct {
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/content/reuse_list")
	public void list(String queryStatus, Integer queryTypeId,
			Boolean queryTopLevel, Boolean queryRecommend,String queryUsername,String queryTitle,
			Integer queryOrderBy, Integer querySiteId, Integer pageNo,Integer pageSize,
			Integer format,Boolean hasCollect,Integer https,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (queryTopLevel == null) {
			queryTopLevel = false;
		}
		if (queryRecommend == null) {
			queryRecommend = false;
		}
		if (queryOrderBy == null) {
			queryOrderBy = 0;
		}
		if (format==null) {
			format=0;
		}
		if (hasCollect==null) {
			hasCollect=false;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		ContentStatus status;
		if (!StringUtils.isBlank(queryStatus)) {
			status = ContentStatus.valueOf(queryStatus);
		} else {
			status = ContentStatus.all;
		}
		Integer queryInputUserId = null;
		if (!StringUtils.isBlank(queryUsername)) {
			CmsUser u = cmsUserMng.findByUsername(queryUsername);
			if (u != null) {
				queryInputUserId = u.getId();
			} else {
				// 用户名不存在，清空。
				queryUsername = null;
			}
		}else{
			queryInputUserId=0;
		}
		if (querySiteId == null) {
			querySiteId = CmsUtils.getSiteId(request);
		}
		Pagination p = contentMng.getPageBySite(queryTitle, queryTypeId,
				CmsUtils.getUserId(request),queryInputUserId, queryTopLevel, queryRecommend, status,
				querySiteId, queryOrderBy,pageNo,pageSize);
		List<Content> list = (List<Content>) p.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(format, https, hasCollect,true, false));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/content/reuse_page")
	public void getPage(String queryStatus, Integer queryTypeId,
			Boolean queryTopLevel, Boolean queryRecommend,String queryUsername,String queryTitle,
			Integer queryOrderBy, Integer querySiteId, Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (queryTopLevel == null) {
			queryTopLevel = false;
		}
		if (queryRecommend == null) {
			queryRecommend = false;
		}
		if (queryOrderBy == null) {
			queryOrderBy = 0;
		}
		ContentStatus status;
		if (!StringUtils.isBlank(queryStatus)) {
			status = ContentStatus.valueOf(queryStatus);
		} else {
			status = ContentStatus.all;
		}
		Integer queryInputUserId = null;
		if (!StringUtils.isBlank(queryUsername)) {
			CmsUser u = cmsUserMng.findByUsername(queryUsername);
			if (u != null) {
				queryInputUserId = u.getId();
			} else {
				// 用户名不存在，清空。
				queryUsername = null;
			}
		}else{
			queryInputUserId=0;
		}
		if (querySiteId == null) {
			querySiteId = CmsUtils.getSiteId(request);
		}
		Pagination p = contentMng.getPageCountBySite(queryTitle, queryTypeId,
				CmsUtils.getUserId(request),queryInputUserId, queryTopLevel, queryRecommend, status,
				querySiteId, queryOrderBy, pageNo,pageSize);
		JSONObject json = getPage(p);
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = json.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject getPage(Pagination p){
		JSONObject json = new JSONObject();
		json.put("pageNo", p.getPageNo());
		json.put("pageSize", p.getPageSize());
		json.put("totalCount", p.getTotalCount());
		json.put("totalPage", p.getTotalPage());
		json.put("firstPage", p.isFirstPage());
		json.put("lastPage", p.isLastPage());
		json.put("prePage", p.getPrePage());
		json.put("nextPage", p.getNextPage());
		return json;
	}
	
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private CmsUserMng cmsUserMng;
}
