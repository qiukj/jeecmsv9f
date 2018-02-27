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
import com.jeecms.cms.entity.assist.CmsGuestbook;
import com.jeecms.cms.entity.assist.CmsGuestbookCtg;
import com.jeecms.cms.manager.assist.CmsGuestbookCtgMng;
import com.jeecms.cms.manager.assist.CmsGuestbookMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsGuestbookApiAct {
	
	/**
	 * 留言列表API
	 * @param siteId 站点ID  非必选
	 * @param ctgId 分类ID  非必选
	 * @param checked 是否审核  非必选
	 * @param recommend 是否推荐  非必选
	 * @param orderBy 排序 0升序  1降序   默认降序
	 * @param first 开始
	 * @param count 数量
	 */
	@RequestMapping(value = "/guestbook/list")
	public void guestbookList(Integer siteId,Integer ctgId,
			Short checked,Boolean recommend,Integer orderBy,
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
		List<CmsGuestbook> list = cmsGuestbookMng.getList(siteId,
				ctgId, null,recommend, checked,
				orderDesc, orderDesc, first, count);
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
	 * 留言类别API
	 * @param siteId 站点id
	 */
	@RequestMapping(value = "/guestbookctg/list")
	public void guestbookCtgList(Integer siteId,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if (siteId == null) {
			siteId = CmsUtils.getSiteId(request);
		}
		List<CmsGuestbookCtg> list = cmsGuestbookCtgMng.getList(siteId);
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
	 * 留言详情获取API
	 * @param id 留言ID 必选
	 */
	@RequestMapping(value = "/guestbook/get")
	public void guestbookGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsGuestbook guestbook = cmsGuestbookMng.findById(id);
			if (guestbook!=null) {
				body = guestbook.convertToJson().toString();
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
	protected CmsGuestbookMng cmsGuestbookMng;
	@Autowired
	private CmsGuestbookCtgMng cmsGuestbookCtgMng;
}

