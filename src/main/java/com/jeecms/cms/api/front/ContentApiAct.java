package com.jeecms.cms.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ContentApiAct {
	
	/**
	 * 内容列表API
	 * ids tagIds、topicId、channelIds、siteIds 必须有一个参数有值，否则结果为空 
	 * ids tagIds、topicId、channelIds、siteIds 的参数值之间以,号分隔,比如channelIds "73,74"
	 * channelOption参数仅在使用 channelIds参数时有效，默认值为1
	 * @param format 非必选 默认1 内容信息简化模式  0全部信息模式
	 * @param ids  ids优先级最高
	 * @param tagIds  tagID优先级第二
	 * @param topicId 专题ID 优先级第三
	 * @param channelIds 栏目ID 优先级第四
	 * @param siteIds 站点ID 优先级第五
	 * @param typeIds 类型ID数组  非必选
	 * @param titleImg  是否有标题图 非必选
	 * @param recommend  推荐 非必选 
	 * @param orderBy  排序 非必选 默认4固顶降序
	 * @param title   标题检索  非必选
	 * @param channelOption  channelOption 1子栏目 2副栏目  0或者channelIds多个值
	 * @param first   查询开始下标  非必选  默认0
	 * @param count	  查询数量  非必选  默认10
	 */
	@RequestMapping(value = "/content/list")
	public void contentList(
			Integer format,String ids,String tagIds,Integer topicId,
			String channelIds,String siteIds,Integer https,
			String typeIds,Boolean titleImg,Boolean recommend,
			Integer orderBy,String title,
			Integer channelOption,Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(orderBy==null){
			orderBy=4;
		}
		if(channelOption==null){
			channelOption=1;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		if(format==null){
			format=Content.CONTENT_INFO_SIMPLE;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		Integer[]intIds= null,intTagIds = null
				,intChannelIds= null,intSiteIds = null,intTypeIds= null;
		if(StringUtils.isNotBlank(ids)){
			intIds=ArrayUtils.parseStringToArray(ids);
		}
		if(StringUtils.isNotBlank(tagIds)){
			intTagIds=ArrayUtils.parseStringToArray(tagIds);
		}
		if(StringUtils.isNotBlank(channelIds)){
			intChannelIds=ArrayUtils.parseStringToArray(channelIds);
		}
		if(StringUtils.isNotBlank(siteIds)){
			intSiteIds=ArrayUtils.parseStringToArray(siteIds);
		}
		if(StringUtils.isNotBlank(typeIds)){
			intTypeIds=ArrayUtils.parseStringToArray(typeIds);
		}
		List<Content>contents=null;
		if(intIds!=null){
			contents=contentMng.getListByIdsForTag(intIds, orderBy);
		}else if(intTagIds!=null){
			contents=contentMng.getListByTagIdsForTag(intTagIds, intSiteIds,
					intChannelIds, intTypeIds, null, titleImg, recommend,
					title,null,orderBy, first, count);
		}else if(topicId!=null){
			contents=contentMng.getListByTopicIdForTag(topicId, intSiteIds,
					intChannelIds, intTypeIds, titleImg, recommend, title,
					null,orderBy,first, count);
		}else if(intChannelIds!=null){
			contents=contentMng.getListByChannelIdsForTag(intChannelIds,
					intTypeIds, titleImg, recommend, title,null,
					orderBy,channelOption, first,count);
		}else if(intSiteIds!=null){
			contents=contentMng.getListBySiteIdsForTag(intSiteIds, intTypeIds,
					titleImg, recommend, title,null,
					orderBy,first, count);
		}
		JSONArray jsonArray=new JSONArray();
		if(contents!=null&&contents.size()>0){
			for(int i=0;i<contents.size();i++){
				jsonArray.put(i, contents.get(i).convertToJson(format,https,false,true, false));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 我的内容API
	 * @param appId   appid 必选
	 * @param sessionKey 会话标识 必选
	 * @param format 内容显示模式  1简化模式    0全   默认1
	 * @param channelId 栏目id 非必选
	 * @param siteId 站点id 非必选默认当前站
	 * @param modelId 模型id 非必选  
	 * @param orderBy 排序 非必选 默认4 固顶降序
	 * @param title 标题 非必选
	 * @param first 开始 非必选 默认0
	 * @param count 数量 非必选 默认10 
	 */
	@RequestMapping(value = "/content/mycontents")
	public void contentListForUser(
			String appId,String sessionKey,Integer https,
			Integer format,Integer channelId,Integer siteId,
			Integer modelId,Integer orderBy,String title,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(orderBy==null){
			orderBy=4;
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		if(format==null){
			format=Content.CONTENT_INFO_SIMPLE;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if(siteId!=null){
			siteId=CmsUtils.getSiteId(request);
		}
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		CmsUser user;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,appId,
				sessionKey);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			//验证签名
			errors=ApiValidate.validateApiAccount(request, errors,apiAccount);
		}
		if(errors.hasErrors()){
			message="\""+errors.getErrors().get(0)+"\"";
		}else{
			String aesKey=apiAccount.getAesKey();
			user=apiUserLoginMng.findUser(sessionKey, aesKey,apiAccount.getIvKey());
			JSONArray jsonArray=new JSONArray();
			if(user!=null){
				List<Content>contents=contentMng.getListForMember(title, channelId, 
						siteId, modelId, user.getId(), first, count);
				if(contents!=null&&contents.size()>0){
					for(int i=0;i<contents.size();i++){
						jsonArray.put(i, contents.get(i).convertToJson(format,https,false,true, false));
					}
				}
			}
			body=jsonArray.toString();
			message=Constants.API_MESSAGE_SUCCESS;
		}		
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 内容信息获取API
	 * @param format 1简化模式  0全 非必选 默认1
	 * @param id id 必选  
	 * @param next 非必选   1下一篇 0 上一篇  为空则是当前id内容
	 * @param channelId  栏目id  非必选
	 * @param siteId 站点id  非必选 默认当前站
	 * @param sessionKey 会话标识 非必选
	 * @param appId appId 非必选 如果存在以及sessionKey存在则判断用户是否收藏了该内容
	 */ 
	@RequestMapping(value = "/content/get")
	public void contentGet(Integer format,Integer https,Integer id,Integer next,
			Integer channelId,Integer siteId,
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		String code = ResponseCode.API_CODE_NOT_FOUND;
		if (id == null) {
			ResponseUtils.renderJson(response, "[]");
			return;
		}
		if(format==null){
			format=Content.CONTENT_INFO_SIMPLE;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if(siteId==null){
			siteId=CmsUtils.getSiteId(request);
		}
		Content content;
		if(next==null){
			content=contentMng.findById(id);
		}else{
			boolean nextBool=false;
			if(next.equals(1)){
				nextBool=true;
			}
			content = contentMng.getSide(id, siteId, channelId, nextBool);
		}
		if (content != null) {
			boolean hasCollect=false;
			body = content.convertToJson(format,https,hasCollect,false, false).toString();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			code = ResponseCode.API_CODE_NOT_FOUND;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
}

