package com.jeecms.cms.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.action.front.AbstractVote;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsVoteApiAct extends AbstractVote{
	
	/**
	 * 调查列表API
	 * @param siteId 站点ID 非必选 默认当前站
	 * @param def 是否默认  非必选 默认全部  true默认  false 非默认 
	 * @param first 开始 非必选 默认0
	 * @param count 数量 非必选 默认10
	 */
	@RequestMapping(value = "/vote/list")
	public void cmsVoteList(Integer siteId,
			Boolean def,Integer first,Integer count,
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
		List<CmsVoteTopic>list=cmsVoteTopicMng.getList(
				def,siteId,first,count);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(false));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 投票信息获取
	 * @param id 投票ID
	 */
	@RequestMapping(value = "/vote/get")
	public void cmsVoteGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsVoteTopic voteTopic = cmsVoteTopicMng.findById(id);
			if (voteTopic!=null) {
				body = voteTopic.convertToJson(true).toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code =ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}

