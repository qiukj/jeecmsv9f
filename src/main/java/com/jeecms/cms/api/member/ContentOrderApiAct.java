package com.jeecms.cms.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.ContentBuy;
import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.cms.manager.main.ContentBuyMng;
import com.jeecms.cms.manager.main.ContentChargeMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ContentOrderApiAct {
	private static final int OPERATOR_BUY=1;
	private static final int OPERATOR_ORDER=2;
	private static final int OPERATOR_CHARGELIST=3;
	
	/**
	 * 我消费的记录和我的内容被打赏记录
	 * @param orderNum 订单号 非必选
	 * @param orderType 类型  1我消费的记录 2我的内容被打赏记录 默认1
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 * @param first 非必选 默认0
	 * @param count 非必选 默认10
	 */
	@RequestMapping(value = "/order/myorders")
	public void myOrderList(String orderNum,Integer orderType,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					 {
		getMyInfoList(orderType, orderNum, 
				first, count, request, response);
	}
	
	/**
	 * 我的内容收费统计
	 * @param orderNum 订单号 非必选
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 * @param first 非必选 默认0
	 * @param count 非必选 默认10
	 */
	@RequestMapping(value = "/order/chargelist")
	public void chargeList(String orderNum,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					 {
		getMyInfoList(OPERATOR_CHARGELIST, orderNum,
				first, count, request, response);
	}
	
	private void getMyInfoList(Integer operate,String orderNum,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(operate==null){
			operate=OPERATOR_BUY;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		WebErrors errors=WebErrors.create(request);
		CmsUser user = CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors);
		if(!errors.hasErrors()){
			JSONArray jsonArray=new JSONArray();
			List<ContentBuy>list = null;
			List<ContentCharge>chargeList=null;
			if(OPERATOR_BUY==operate){
				list=contentBuyMng.getList(orderNum,
						user.getId(), null, null, first, count);
			}else if(OPERATOR_ORDER==operate){
				list=contentBuyMng.getList(orderNum,
						null, user.getId(), null, first, count);
			}else if(OPERATOR_CHARGELIST==operate){
				chargeList=contentChargeMng.getList(null, user.getId(),
						null, null, 1, first, count);
			}
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			if(chargeList!=null&&chargeList.size()>0){
				for(int i=0;i<chargeList.size();i++){
					jsonArray.put(i, chargeList.get(i).convertToJson());
				}
			}
			body=jsonArray.toString();
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ContentBuyMng contentBuyMng;
	@Autowired
	private ContentChargeMng contentChargeMng;
}
