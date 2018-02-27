package com.jeecms.cms.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class ChannelApiAct {
	
	/**
	 * 栏目列表API
	 * @param parentId  父栏目ID
	 * @param siteId    站点ID
	 * @param hasContentOnly  是否有内容
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@RequestMapping(value = "/channel/list")
	public void channelList(Integer https,Integer parentId,Integer siteId,
			Boolean hasContentOnly,Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(hasContentOnly==null){
			hasContentOnly=false;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		List<Channel> list;
		if (parentId != null) {
			list = channelMng.getChildListForTag(parentId, hasContentOnly);
		} else {
			if (siteId == null) {
				siteId = 1;
			}
			list = channelMng.getTopListForTag(siteId, hasContentOnly);
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https, false, false, null));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取栏目信息
	 * id或者path
	 * path和siteId必须一起使用
	 * @param id 栏目id
	 * @param path  栏目路径
	 * @param siteId  站点id
	 */
	@RequestMapping(value = "/channel/get")
	public void channelGet(Integer https,
			Integer id,String path,Integer siteId,Boolean showTxt,
			HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		String code = ResponseCode.API_CODE_NOT_FOUND;
		Channel channel;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if(showTxt!=null){
			showTxt=true;
		}
		if (id != null) {
			if (id.equals(0)) {
				channel = new Channel();
			}else{
				channel = channelMng.findById(id);
			}
		} else {
			if(siteId==null){
				siteId=1;
			}
			channel = channelMng.findByPathForTag(path, siteId);
		}
		if (channel!=null) {
			body = channel.convertToJson(https, false, showTxt, null).toString();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ChannelMng channelMng;
}

