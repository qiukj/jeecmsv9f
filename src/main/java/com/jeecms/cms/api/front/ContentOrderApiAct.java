package com.jeecms.cms.api.front;

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
import com.jeecms.cms.entity.main.ContentBuy;
import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.cms.manager.main.ContentBuyMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class ContentOrderApiAct {
	/**
	 * 内容打赏购买记录
	 * @param contentId  内容ID 必选 
	 * @param type 1购买记录  2打赏记录 非必选 默认2
	 * @param first 非必选 默认0
	 * @param count 非必选 默认10
	 */
	@RequestMapping(value = "/order/getByContent")
	public void getOrderListByContent(
			Integer contentId,Short type,Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					 {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(type==null){
			type=ContentCharge.MODEL_REWARD;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		List<ContentBuy>list;
		if(contentId!=null){
			list=contentBuyMng.getListByContent(contentId,
					type, first, count);
			JSONArray jsonArray=new JSONArray();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			body=jsonArray.toString();
			message=Constants.API_MESSAGE_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	@Autowired
	private ContentBuyMng contentBuyMng;
}

