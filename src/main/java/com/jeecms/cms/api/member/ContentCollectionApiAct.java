package com.jeecms.cms.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ContentCollectionApiAct {
	/**
	 * 我的收藏
	 * @param siteId 站点id 非必选 默认当前站
	 * @param format 模式 非必选 默认1 内容信息简化模式  0全部信息
	 * @param appId   appid 必选
	 * @param sessionKey 会话标识 必选
	 * @param first 开始 非必选 默认0
	 * @param count 数量 非必选 默认10 
	 */
	@SignValidate
	@RequestMapping(value = "/content/mycollect")
	public void mycollectList(
			Integer siteId,Integer format,Integer https,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser user = CmsUtils.getUser(request);
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		if(siteId==null){
			siteId=CmsUtils.getSiteId(request);
		}
		if(format==null){
			format=Content.CONTENT_INFO_SIMPLE;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<Content>contents=null;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors);
		if(!errors.hasErrors()){
			contents=contentMng.getListForCollection(
					siteId, user.getId(), first, count);
			JSONArray jsonArray=new JSONArray();
			if(contents!=null&&contents.size()>0){
				for(int i=0;i<contents.size();i++){
					jsonArray.put(i, contents.get(i).convertToJson(format,https,true,true, false));
				}
			}
			body=jsonArray.toString();
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 收藏API
	 * @param id   内容ID 必选
	 * @param operate 操作 非必选  1收藏 0 取消收藏   默认1 
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识 必选
	 */
	@SignValidate
	@RequestMapping(value = "/content/collect")
	public void contentCollect(
			Integer id,Integer operate,
			HttpServletRequest request,HttpServletResponse response)throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		if(operate==null){
			operate=1;
		}
		WebErrors errors=WebErrors.create(request);
		CmsUser user = CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,id);
		if(!errors.hasErrors()){
			Content content=contentMng.findById(id);
			if(content!=null){
				userMng.updateUserConllection(user,id,operate);
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message=Constants.API_MESSAGE_CONTENT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private CmsUserMng userMng;
}
