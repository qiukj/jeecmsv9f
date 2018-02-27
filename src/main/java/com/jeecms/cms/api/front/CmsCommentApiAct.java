package com.jeecms.cms.api.front;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsComment;
import com.jeecms.cms.manager.assist.CmsCommentMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsCommentApiAct {
	
	/**
	 * 评论列表
	 * @param siteId 站点ID 非必选 
	 * @param contentId 内容ID 非必选 
	 * @param parentId 父评论ID 非必选 
	 * @param greaterThen 支持数大于 非必选 
	 * @param checked 是否审核 非必选 
	 * @param recommend 是否推荐 非必选 
	 * @param orderBy 是否推荐 0升序  1降序 非必选 默认1
	 * @param first 开始
	 * @param count 数量
	 */
	@RequestMapping(value = "/comment/list")
	public void commentList(Integer siteId,Integer contentId,Integer parentId,
			Integer greaterThen,Short checked,Boolean recommend,Integer orderBy,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if (siteId == null) {
			siteId = CmsUtils.getSiteId(request);
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		boolean orderDesc=true;
		if(orderBy!=null&&orderBy.equals(0)){
			orderDesc=false;
		}
		List<CmsComment> list = cmsCommentMng.getListForTag(siteId,
				contentId,parentId, greaterThen,
				checked, recommend, orderDesc,first,count);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取单个评论
	 * @param id 评论ID
	 */
	@RequestMapping(value = "/comment/get")
	public void commentGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsComment comment = cmsCommentMng.findById(id);
			if (comment!=null) {
				body = comment.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	protected CmsCommentMng cmsCommentMng;
}

