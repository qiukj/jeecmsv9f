package com.jeecms.cms.api.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.CmsTopic;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsTopicMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class TopicApiAct {
	/**
	 * 专题保存接口
	 * @param channelId 栏目ID 非必选
	 * @param name  名称  必选
	 * @param shortName  路径 非必选
	 * @param keywords  关键词 非必选
	 * @param desc  关键词 非必选
	 * @param titleImg  标题图 非必选
	 * @param contentImg  内容图 非必选
	 * @param priority  排序 非必选 默认10 
	 * @param recommend  推荐 非必选 默认false
	 * @param appId appid 必选
	 * @param nonce_str 随机数 必选
	 * @param sign 签名 必选
	 */
	@SignValidate
	@RequestMapping(value = "/topic/save")
	public void topicSave(
			Integer channelId,
			String name,String shortName,String keywords,String desc,
			String titleImg,String contentImg,Integer priority,
			Boolean recommend,
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		if(priority==null){
			priority=10;
		}
		if(recommend==null){
			recommend=false;
		}
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,name);
		if(!errors.hasErrors()){
			CmsTopic topic=new CmsTopic();
			if(channelId!=null){
				topic.setChannel(channelMng.findById(channelId));
			}
			topic.setName(name);
			topic.setContentImg(contentImg);
			topic.setDescription(desc);
			topic.setKeywords(keywords);
			topic.setPriority(priority);
			topic.setRecommend(recommend);
			topic.setShortName(shortName);
			topic.setTitleImg(titleImg);
			topic = topicMng.save(topic, channelId,null);
			body="{\"id\":"+"\""+topic.getId()+"\"}";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 专题修改接口
	 * @param id ID 必选
	 * @param channelId 栏目ID 非必选
	 * @param name  名称  非必选
	 * @param shortName  路径 非必选
	 * @param keywords  关键词 非必选
	 * @param desc  关键词 非必选
	 * @param titleImg  标题图 非必选
	 * @param contentImg  内容图 非必选
	 * @param priority  排序 非必选
	 * @param recommend  推荐 非必选
	 * @param appId appid 必选
	 * @param nonce_str 随机数 必选
	 * @param sign 签名 必选
	 */
	@SignValidate
	@RequestMapping(value = "/topic/update")
	public void topicUpdate(
			Integer id,Integer channelId,
			String name,String shortName,String keywords,String desc,
			String titleImg,String contentImg,Integer priority,
			Boolean recommend,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,id);
		if(!errors.hasErrors()){
			CmsTopic topic=topicMng.findById(id);
			if(topic!=null){
				if(channelId!=null){
					topic.setChannel(channelMng.findById(channelId));
				}
				if(StringUtils.isNotBlank(name)){
					topic.setName(name);
				}
				if(StringUtils.isNotBlank(contentImg)){
					topic.setContentImg(contentImg);
				}
				if(StringUtils.isNotBlank(desc)){
					topic.setDescription(desc);
				}
				if(StringUtils.isNotBlank(keywords)){
					topic.setKeywords(keywords);
				}
				if(priority!=null){
					topic.setPriority(priority);
				}
				if(recommend!=null){
					topic.setRecommend(recommend);
				}
				if(StringUtils.isNotBlank(shortName)){
					topic.setShortName(shortName);
				}
				if(StringUtils.isNotBlank(titleImg)){
					topic.setTitleImg(titleImg);
				}
				topic = topicMng.update(topic, channelId,null);
				body="{\"id\":"+"\""+topic.getId()+"\"}";
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message=Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsTopicMng topicMng;
}
