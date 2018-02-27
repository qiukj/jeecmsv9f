package com.jeecms.cms.api.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.ChannelExt;
import com.jeecms.cms.entity.main.ChannelTxt;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ChannelApiAct {
	/**
	 * 栏目保存接口
	 * @param siteId 站点ID 非必选
	 * @param parentId 父栏目ID 非必选
	 * @param name  名称  必选
	 * @param path  路径 必选
	 * @param title 标题 非必选
	 * @param keywords meta关键词  非必选
	 * @param desc 描述 非必选
	 * @param txt  栏目文本内容 非必选
	 * @param priority 排序  非必选
	 * @param display 是否展示 非必选
	 * @param modelId 模型ID 必选  新闻 1
	 * @param workflowId 应用工作流ID 非必选
	 * @param titleImg 标题图 非必选
	 * @param contentImg 内容图 非必选
	 * @param finalStep 终审步骤 非必选
	 * @param afterCheck 审核后 非必选
	 * @param tplChannel PC端模板 非必选
	 * @param tplMobileChannel 移动端模板 非必选
	 */
	@RequestMapping(value = "/channel/save")
	public void channelSave(
			Integer siteId,Integer parentId,
			String name,String path,String title,String keywords,
			String desc,String txt,Integer priority,
			Boolean display,Integer modelId,Integer workflowId,
			String titleImg,String contentImg,Byte finalStep,
			Byte afterCheck,String tplChannel,String tplMobileChannel,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				name,path,modelId);
		if(!errors.hasErrors()){
			CmsSite site=siteMng.findById(siteId);
			if(site==null){
				site=CmsUtils.getSite(request);
			}
			Channel channel=new Channel();
			ChannelExt ext=new ChannelExt();
			ChannelTxt channelTxt=new ChannelTxt();
			channel.setChannelExt(ext);
			channel.setPath(path);
			ext.setName(name);
			ext.setTitle(title);
			ext.setKeywords(keywords);
			ext.setDescription(desc);
			ext.setTitleImg(titleImg);
			ext.setContentImg(contentImg);
			ext.setFinalStep(finalStep);
			ext.setAfterCheck(afterCheck);
			ext.setChannel(channel);
			channelTxt.setChannel(channel);
			channelTxt.setTxt(txt);
			if (priority == null) {
				channel.setPriority(10);
			}
			if (display == null) {
				channel.setDisplay(true);
			}
			// 加上模板前缀
			String tplPath = site.getTplPath();
			if (!StringUtils.isBlank(tplChannel)) {
				ext.setTplChannel(tplPath + tplChannel);
			}
			if (!StringUtils.isBlank(tplMobileChannel)) {
				ext.setTplMobileChannel(tplPath + tplMobileChannel);
			}
			channel.setAttr(RequestUtils.getRequestMap(request, "attr_"));
			channel=channelMng.save(channel, ext, channelTxt, null, null,
					null, siteId, parentId, modelId,null,null,null,false);
			body="{\"id\":"+"\""+channel.getId()+"\"}";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 栏目修改接口
	 * @param channelId 栏目ID 必选
	 * @param siteId 站点ID 非必选
	 * @param parentId 父栏目ID 非必选
	 * @param name  名称  非必选
	 * @param path  路径 非必选
	 * @param title 标题 非必选
	 * @param keywords meta关键词  非必选
	 * @param desc 描述 非必选
	 * @param txt  栏目文本内容 非必选
	 * @param priority 排序  非必选
	 * @param display 是否展示 非必选
	 * @param modelId 模型ID 非必选  新闻 1
	 * @param workflowId 应用工作流ID 非必选
	 * @param titleImg 标题图 非必选
	 * @param contentImg 内容图 非必选
	 * @param finalStep 终审步骤 非必选
	 * @param afterCheck 审核后 非必选
	 * @param tplChannel PC端模板 非必选
	 * @param tplMobileChannel 移动端模板 非必选
	 * @throws JSONException
	 */
	@RequestMapping(value = "/channel/update")
	public void channelUpdate(
			Integer channelId,Integer siteId,Integer parentId,
			String name,String path,String title,String keywords,
			String desc,String txt,Integer priority,
			Boolean display,Integer modelId,Integer workflowId,
			String titleImg,String contentImg,Byte finalStep,
			Byte afterCheck,String tplChannel,String tplMobileChannel,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,channelId);
		if(!errors.hasErrors()){
			CmsSite site=siteMng.findById(siteId);
			Channel channel=channelMng.findById(channelId);
			if(site==null){
				site=CmsUtils.getSite(request);
			}
			if(channel==null){
				message=Constants.API_MESSAGE_PARAM_ERROR;
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				site=channel.getSite();
				ChannelExt ext=channel.getChannelExt();
				ChannelTxt channelTxt=channel.getChannelTxt();
				if(channelTxt==null){
					channelTxt=new ChannelTxt();
				}
				if(StringUtils.isNotBlank(path)){
					channel.setPath(path);
				}
				if(StringUtils.isNotBlank(name)){
					ext.setName(name);
				}
				if(StringUtils.isNotBlank(title)){
					ext.setTitle(title);
				}
				if(StringUtils.isNotBlank(keywords)){
					ext.setKeywords(keywords);
				}
				if(StringUtils.isNotBlank(desc)){
					ext.setDescription(desc);
				}
				if(StringUtils.isNotBlank(titleImg)){
					ext.setTitleImg(titleImg);
				}
				if(StringUtils.isNotBlank(contentImg)){
					ext.setContentImg(contentImg);
				}
				if(finalStep!=null){
					ext.setFinalStep(finalStep);
				}
				if(afterCheck!=null){
					ext.setAfterCheck(afterCheck);
				}
				if(StringUtils.isNotBlank(txt)){
					channelTxt.setTxt(txt);
				}
				if (priority != null) {
					channel.setPriority(priority);
				}
				if (display != null) {
					channel.setDisplay(display);
				}
				// 加上模板前缀
				String tplPath = site.getTplPath();
				if (!StringUtils.isBlank(tplChannel)) {
					ext.setTplChannel(tplPath + tplChannel);
				}
				if (!StringUtils.isBlank(tplMobileChannel)) {
					ext.setTplMobileChannel(tplPath + tplMobileChannel);
				}
				channelMng.update(channel, ext, channelTxt, null, null,
						null, parentId, attr,modelId,null,null,null);
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsSiteMng siteMng;
}
