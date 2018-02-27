package com.jeecms.cms.api.admin.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.ChannelExt;
import com.jeecms.cms.entity.main.ChannelTxt;
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ChannelApiAct {
	
	private static final Logger log = LoggerFactory.getLogger(ChannelApiAct.class);

	
	@RequestMapping("/channel/select")
	public void channelSelect(Boolean hasContentOnly,Integer excludeId,
			HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		if (hasContentOnly==null) {
			hasContentOnly=true;
		}
		List<Channel> topList = channelMng.getTopList(site.getId(), hasContentOnly);
		JSONArray jsonArray = new JSONArray();
		if (topList!=null&&topList.size()>0) {
			int j=0;
			for(int i = 0 ; i<topList.size(); i++){
				JSONObject json=createSelectJson(topList.get(i),excludeId);
				if(json!=null){
					jsonArray.put(j,json);
				}else{
					j--;
				}
				j++;
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 栏目列表API
	 * @param parentId    父栏目ID
	 * @param all
	 */
	@RequestMapping(value = "/channel/list")
	public void channelList(Integer https,Integer parentId,Boolean all,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if(all==null){
			all=false;
		}
		Integer siteId = CmsUtils.getSiteId(request);
		List<Channel> list;
		if (parentId == null) {
			list = channelMng.getTopList(siteId, false);
		} else {
			list = channelMng.getChildList(parentId, false);
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https, all, false, null));
			}
		}
		body=jsonArray.toString();
		message=Constants.API_MESSAGE_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	
	@RequestMapping(value = "/channel/tree")
	public void tree(Integer https,Integer parentId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		boolean isRoot;
		if (parentId==null|| parentId==0) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		List<Channel> list;
		if (isRoot) {
			CmsSite site = CmsUtils.getSite(request);
			list = channelMng.getTopList(site.getId(), false);
		} else {
			list = channelMng.getChildList(parentId, false);
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https, false, false, null));
			}
		}
		body=jsonArray.toString();
		message=Constants.API_MESSAGE_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
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
			Integer id,String path,Integer siteId,
			HttpServletRequest request,
			HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		Channel channel;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id != null) {
			if (id.equals(0)) {
				channel = new Channel();
			}else{
				channel = channelMng.findById(id);
			}
		} else {
			if(siteId==null){
				siteId=CmsUtils.getSiteId(request);
			}
			channel = channelMng.findByPathForTag(path, siteId);
		}
		if (channel != null) {
			channel.init();
			List<CmsModel>modelList=modelMng.getList(false, true, siteId);
			JSONObject json=channel.convertToJson(https, false, true, modelList);
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		} else {
			code = ResponseCode.API_CODE_NOT_FOUND;
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
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
	 * @param appId appid 必选
	 * @param nonce_str 随机数 必选
	 * @param sign 签名 必选
	 */
	@SignValidate
	@RequestMapping(value = "/channel/save")
	public void save(Integer parentId, Channel bean, ChannelExt ext,
			ChannelTxt txt, String viewGroupIds, String contriGroupIds,
			Integer modelId,Integer workflowId,
			String modelIds,String tpls, String mtpls,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors = WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				ext.getName(),bean.getPath(),modelId);
		Integer[] viewGroupIdArray=null;
		if(StringUtils.isNotBlank(viewGroupIds)){
			viewGroupIdArray=StrUtils.getInts(viewGroupIds);
		}
		Integer[] contriGroupIdArray=null;
		if(StringUtils.isNotBlank(contriGroupIds)){
			contriGroupIdArray=StrUtils.getInts(contriGroupIds);
		}
		Integer[] modelIdArray=null;
		if(StringUtils.isNotBlank(modelIds)){
			modelIdArray=StrUtils.getInts(modelIds);
		}
		String[] tplArray=null;
		if(StringUtils.isNotBlank(tpls)){
			tplArray=StrUtils.getStrArrayComplete(tpls);
		}else{
			if (modelIdArray!=null && modelIdArray.length==1) {
				tplArray = new String[1];
				tplArray[0] = "";
			}
		}
		String[] mtplArray=null;
		if(StringUtils.isNotBlank(mtpls)){
			mtplArray=StrUtils.getStrArrayComplete(mtpls);
		}else{
			if (modelIdArray!=null && modelIdArray.length==1) {
				mtplArray = new String[1];
				mtplArray[0] = "";
			}
		}
		if(bean.getPriority()==null){
			bean.setPriority(10);
		}
		if(bean.getDisplay()==null){
			bean.setDisplay(true);
		}
		if(!errors.hasErrors()){
			CmsSite site = CmsUtils.getSite(request);
			// 加上模板前缀
			String tplPath = site.getTplPath();
			if (!StringUtils.isBlank(ext.getTplChannel())) {
				ext.setTplChannel(tplPath + ext.getTplChannel());
			}
			if (!StringUtils.isBlank(ext.getTplContent())) {
				ext.setTplContent(tplPath + ext.getTplContent());
			}
			if (!StringUtils.isBlank(ext.getTplMobileChannel())) {
				ext.setTplMobileChannel(tplPath + ext.getTplMobileChannel());
			}
			if(tplArray!=null&&tplArray.length>0){
				for(int t=0;t<tplArray.length;t++){
					if (!StringUtils.isBlank(tplArray[t])&&!tplArray[t].startsWith(tplPath)) {
						tplArray[t]=tplPath+tplArray[t];
					}
				}
			}
			if(mtplArray!=null&&mtplArray.length>0){
				for(int t=0;t<mtplArray.length;t++){
					if (!StringUtils.isBlank(mtplArray[t])&&!mtplArray[t].startsWith(tplPath)) {
						mtplArray[t]=tplPath+mtplArray[t];
					}
				}
			}
			bean.setAttr(RequestUtils.getRequestMap(request, "attr_"));
			bean = channelMng.save(bean, ext, txt, viewGroupIdArray, contriGroupIdArray,
					null, CmsUtils.getSiteId(request), parentId, modelId,
					modelIdArray,tplArray,mtplArray,false);
			log.info("save Channel id={}, name={}", bean.getId(), bean.getName());
			cmsLogMng.operating(request, "channel.log.save", "id=" + bean.getId()
					+ ";title=" + bean.getTitle());
			body="{\"id\":"+"\""+bean.getId()+"\"}";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_REQUIRED;
			code = ResponseCode.API_CODE_PARAM_ERROR;
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
	 * @param appId appid 必选
	 * @param nonce_str 随机数 必选
	 * @param sign 签名 必选
	 * @throws JSONException
	 */
	@SignValidate
	@RequestMapping(value = "/channel/update")
	public void update(Integer parentId, Channel bean, ChannelExt ext,
			ChannelTxt txt, String viewGroupIds,String contriGroupIds,
			String modelIds,String tpls,String  mtpls,
			Integer workflowId,Integer modelId,HttpServletRequest request,
			HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors = validateUpdate(bean.getId(), request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				ext.getName(),bean.getPath(),modelId);
		Integer[] viewGroupIdArray=null;
		if(StringUtils.isNotBlank(viewGroupIds)){
			viewGroupIdArray=StrUtils.getInts(viewGroupIds);
		}
		Integer[] contriGroupIdArray=null;
		if(StringUtils.isNotBlank(contriGroupIds)){
			contriGroupIdArray=StrUtils.getInts(contriGroupIds);
		}
		Integer[] modelIdArray=null;
		if(StringUtils.isNotBlank(modelIds)){
			modelIdArray=StrUtils.getInts(modelIds);
		}
		String[] tplArray=null;
		if(StringUtils.isNotBlank(tpls)){
			tplArray=StrUtils.getStrArrayComplete(tpls);
		}else{
			if (modelIdArray!=null && modelIdArray.length==1) {
				tplArray = new String[1];
				tplArray[0] = "";
			}
		}
		String[] mtplArray=null;
		if(StringUtils.isNotBlank(mtpls)){
			mtplArray=StrUtils.getStrArrayComplete(mtpls);
		}else{
			if (modelIdArray!=null && modelIdArray.length==1) {
				mtplArray = new String[1];
				mtplArray[0] = "";
			}
		}
		if(bean.getPriority()==null){
			bean.setPriority(10);
		}
		if(bean.getDisplay()==null){
			bean.setDisplay(true);
		}
		if(!errors.hasErrors()){
			CmsSite site = CmsUtils.getSite(request);
			// 加上模板前缀
			String tplPath = site.getTplPath();
			if (!StringUtils.isBlank(ext.getTplChannel())) {
				ext.setTplChannel(tplPath + ext.getTplChannel());
			}
			if (!StringUtils.isBlank(ext.getTplContent())) {
				ext.setTplContent(tplPath + ext.getTplContent());
			}
			if (!StringUtils.isBlank(ext.getTplMobileChannel())) {
				ext.setTplMobileChannel(tplPath + ext.getTplMobileChannel());
			}
			if(tplArray!=null&&tplArray.length>0){
				for(int t=0;t<tplArray.length;t++){
					if (!StringUtils.isBlank(tplArray[t])&&!tplArray[t].startsWith(tplPath)) {
						tplArray[t]=tplPath+tplArray[t];
					}
				}
			}
			if(mtplArray!=null&&mtplArray.length>0){
				for(int t=0;t<mtplArray.length;t++){
					if (!StringUtils.isBlank(mtplArray[t])&&!mtplArray[t].startsWith(tplPath)) {
						mtplArray[t]=tplPath+mtplArray[t];
					}
				}
			}
			Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
			bean = channelMng.update(bean, ext, txt, viewGroupIdArray, contriGroupIdArray,
					null, parentId, attr,modelId,modelIdArray,tplArray,mtplArray);
			log.info("update Channel id={}.", bean.getId());
			cmsLogMng.operating(request, "channel.log.update", "id=" + bean.getId()
					+ ";name=" + bean.getName());
			body="{\"id\":"+"\""+bean.getId()+"\"}";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/channel/delete")
	public void delete(String ids,
			HttpServletRequest request, HttpServletResponse response) {
		Integer[]idArray=null;
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,ids);
		if(StringUtils.isNotBlank(ids)){
			idArray=StrUtils.getInts(ids);
		}
		if(idArray==null||idArray.length<=0){
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
			message=Constants.API_MESSAGE_PARAM_REQUIRED;
			code = ResponseCode.API_CODE_PARAM_ERROR;
		}else{
			errors = validateDelete(idArray, request);
			message=Constants.API_MESSAGE_DELETE_ERROR;
			code = ResponseCode.API_CODE_DELETE_ERROR;
		}
		if(!errors.hasErrors()){
			try {
				Channel[] beans = channelMng.deleteByIds(idArray);
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				for (Channel bean : beans) {
					log.info("delete Channel id={}", bean.getId());
					cmsLogMng.operating(request, "channel.log.delete", "id="
							+ bean.getId() + ";title=" + bean.getTitle());
				}
			} catch (Exception e) {
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/channel/copy")
	public void channelCopy(
			String ids,String solution,String mobileSolution,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,ids);
		Integer[]channelIds = null;
		if(StringUtils.isNotBlank(ids)){
			channelIds=StrUtils.getInts(ids);
		}
		if(channelIds==null||channelIds.length<=0){
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
		}
		if(!errors.hasErrors()){
			CmsSite site=CmsUtils.getSite(request);
			Map<String,String>pathMap=new HashMap<String,String>();
			for(Integer id:channelIds){
				channelMng.copy(id, solution, mobileSolution, site.getId(), pathMap);
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
			//临时存放新旧栏目路径对应关系
			pathMap.clear();
			pathMap=null;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/channel/priority")
	public void priority(String ids, String prioritys,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,ids,prioritys);
		Integer[] idInts=null,priorityInts=null;
		if(StringUtils.isNotBlank(ids)){
			idInts=StrUtils.getInts(ids);
			priorityInts=StrUtils.getInts(prioritys);
		}
		if(idInts==null||idInts.length<=0||
				priorityInts==null||priorityInts.length<=0){
			errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
		}
		if(!errors.hasErrors()){
			errors= validatePriority(idInts, priorityInts, request);
		}
		if(!errors.hasErrors()){
			channelMng.updatePriority(idInts, priorityInts);
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/channel/create_path")
	public void createPath(String name,HttpServletRequest request,
			HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,name);
		if(!errors.hasErrors()){
			String path;
			if (StringUtils.isBlank(name)) {
				path = "";
			} else {
				path=ChineseCharToEn.getAllFirstLetter(name);
			}
			body="\""+path+"\"";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/channel/v_check_path")
	public void checkPath(Integer id,String path,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_CALL_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,path);
		if(!errors.hasErrors()){
			String pass;
			if (StringUtils.isBlank(path)) {
				pass = "false";
			} else {
				Channel c = channelMng.findByPath(path, CmsUtils.getSiteId(request));
				if(c==null){
					pass="true" ;
				}else{
					if(id!=null&&c.getId().equals(id)){
						pass= "true";
					}else{
						pass="false";
					}
				}
			}
			body="\""+pass+"\"";
			message=Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject createSelectJson(Channel channel,Integer excludeId) {
		JSONObject json = new JSONObject();
		if(channel!=null){
			if(excludeId==null||(excludeId!=null&&!channel.getId().equals(excludeId))){
				if (channel.getId()!=null) {
					json.put("id", channel.getId());
				}else{
					json.put("id", "");
				}
				if (StringUtils.isNotBlank(channel.getName())) {
					json.put("name", channel.getName());
				}else{
					json.put("name", "");
				}
				if (channel.getChild()!=null&&channel.getChild().size()>0) {
					json.put("hasChild", true);
					Set<Channel> child = channel.getChild();
					JSONArray jsonArray = new JSONArray();
					int index = 0 ;
					for (Channel c : child) {
						JSONObject jsonObj=createSelectJson(c,excludeId);
						if(jsonObj!=null){
							jsonArray.put(index,jsonObj);
						}else{
							index--;
						}
						index++;
					}
					json.put("child", jsonArray);
				}else{
					json.put("hasChild", false);
				}
			}else{
				//清空对象，否则前端还是多个空对象
				json=null;
			}
		}else{
			json=null;
		}
		return json;
	}
	
	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		errors.ifEmpty(ids, "ids", false);
		for (Integer id : ids) {
			if (vldExist(id, site.getId(), errors)) {
				return errors;
			}
			// 检查是否可以删除
			String code = channelMng.checkDelete(id);
			if (code != null) {
				errors.addErrorString(code);
				return errors;
			}
		}
		return errors;
	}
	
	
	private WebErrors validatePriority(Integer[] wids, Integer[] priority,
			HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(wids, "wids", false)) {
			return errors;
		}
		if (errors.ifEmpty(priority, "priority", false)) {
			return errors;
		}
		if (wids.length != priority.length) {
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			return errors;
		}
		for (int i = 0, len = wids.length; i < len; i++) {
			if (vldExist(wids[i], site.getId(), errors)) {
				return errors;
			}
			if (priority[i] == null) {
				priority[i] = 0;
			}
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		Channel entity = channelMng.findById(id);
		if (errors.ifNotExist(entity, Channel.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsModelMng modelMng;
}

