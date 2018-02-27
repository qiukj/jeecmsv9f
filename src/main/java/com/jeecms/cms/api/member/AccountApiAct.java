package com.jeecms.cms.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.assist.CmsAccountDraw;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.manager.assist.CmsAccountDrawMng;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;

@Controller
public class AccountApiAct {
	
	/**
	 * 提现申请
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param first
	 * @param count
	 */
	@RequestMapping(value = "/draw/list")
	public void myDrawApplyList(
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		getMyInfoList(first, count, request, response);
	}
	
	/**
	 * 删除提现申请
	 * @param ids   申请的id ,间隔 比如 1,2   必选
	 * @param appId      appid   必选
	 */
	@SignValidate
	@RequestMapping(value = "/draw/delete")
	public void deleteApply(String ids,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		applyDelete(ids,request, response);
	}
	
	/**
	 * 申请提现
	 * @param drawAmout  申请金额  必选
	 */
	@SignValidate
	@RequestMapping(value = "/draw/apply")
	public void drawApply(Double drawAmout,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser user = apiUserLoginMng.getUser(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,drawAmout);
		if(!errors.hasErrors()){
			//验证用户业务
			if(user.getUserAccount()==null){
				errors.addErrorString("user_account_not_found");
			}else{
				CmsConfigContentCharge config=configContentChargeMng.getDefault();
				if(drawAmout>user.getUserAccount().getContentNoPayAmount()){
					errors.addErrorString("balance_not_Enough");
					code=ResponseCode.API_CODE_USER_BALANCE_NOT_ENOUGH;
				}
				if(drawAmout<config.getMinDrawAmount()){
					errors.addErrorString("draw_less_min_amount");
					code=ResponseCode.API_CODE_DRAW_LESS;
				}
			}
			if(errors.hasErrors()){
				message="\""+errors.getErrors().get(0)+"\"";
			}else{
				//微信openid作为默认提现账户
				accountDrawMng.draw(user, drawAmout, user.getUserAccount().getAccountWeixinOpenId());
				message=Constants.API_STATUS_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 获取用户账户信息
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/account/get")
	public void getAccountInfo(
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		CmsUser user = apiUserLoginMng.getUser(request);
		if(user!=null&&user.getUserAccount()!=null){
			body=user.getUserAccount().convertToJson().toString();
			message=Constants.API_STATUS_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message="\"user account not found\"";
			code=ResponseCode.API_CODE_USER_ACCOUNT_NOT_FOUND;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void applyDelete(
			String  ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		CmsUser user = apiUserLoginMng.getUser(request);
		WebErrors errors=WebErrors.create(request);
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, ids);
		if(!errors.hasErrors()){
			//会话用户
			if(user!=null){
				Integer[] intIds=ArrayUtils.parseStringToArray(ids);
				accountDrawMng.deleteByIds(intIds);
				message=Constants.API_STATUS_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message=Constants.API_MESSAGE_SESSION_ERROR;
				code = ResponseCode.API_CODE_SESSION_ERROR;
			}
		}		
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void getMyInfoList(Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException{
		String body="\"\"";
		String message=Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		CmsUser user=apiUserLoginMng.getUser(request);
		JSONArray jsonArray=new JSONArray();
		List<CmsAccountDraw>list = null;
		list=accountDrawMng.getList(user.getId(),null,null,null,
				first,count);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		body=jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private CmsAccountDrawMng accountDrawMng;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
}

