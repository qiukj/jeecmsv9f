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
import com.jeecms.cms.entity.main.ContentTag;
import com.jeecms.cms.manager.main.ContentTagMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class ContentTagApiAct {
	
	/**
	 * Tag关键词列表API
	 * @param first 开始 非必选 默认0
	 * @param count 数量 非必选 默认10
	 */
	@RequestMapping(value = "/contenttag/list")
	public void contenttagList(Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		List<ContentTag> list = contentTagMng.getListForTag(first,count);
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
	@Autowired
	private ContentTagMng contentTagMng;
}

