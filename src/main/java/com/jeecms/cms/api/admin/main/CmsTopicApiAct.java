package com.jeecms.cms.api.admin.main;

import static com.jeecms.cms.Constants.TPLDIR_TOPIC;
import static com.jeecms.cms.action.front.TopicAct.TOPIC_INDEX;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.CmsTopic;
import com.jeecms.cms.manager.assist.CmsFileMng;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.CmsTopicMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.CoreUtils;

import net.sf.json.JSONObject;

@Controller
public class CmsTopicApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsTopicApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/topic/list")
	public void list(String initials,Integer pageNo,Integer pageSize,HttpServletRequest request,
			HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(initials,pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsTopic> list = (List<CmsTopic>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/topic/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsTopic bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsTopic();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/topic/save")
	public void save(CmsTopic bean,Integer channelId,String channelIds,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),
				bean.getPriority(),bean.getRecommend());
		if (!errors.hasErrors()) {
			Integer[] channelIdArray = StrUtils.getInts(channelIds);
			bean.init();
			//添加首字母拼音字段数据
			bean.setInitials(ChineseCharToEn.getAllFirstLetter(bean.getName()));
			bean = manager.save(bean, channelId, channelIdArray);
			fileMng.updateFileByPath(bean.getContentImg(), true, null);
			fileMng.updateFileByPath(bean.getTitleImg(), true, null);
			log.info("save CmsTopic id={}", bean.getId());
			cmsLogMng.operating(request, "cmsTopic.log.save", "id=" + bean.getId()
					+ ";name=" + bean.getName());
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/topic/update")
	public void update(CmsTopic bean,Integer channelId,String channelIds,
			String oldTitleImg,String oldContentImg,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName(),
				bean.getPriority(),bean.getRecommend());
		if (!errors.hasErrors()) {
			Integer[] channelIdArray = StrUtils.getInts(channelIds);
			//更新首字母拼音字段数据
			bean.setInitials(ChineseCharToEn.getAllFirstLetter(bean.getName()));
			bean = manager.update(bean, channelId, channelIdArray);
			//旧标题图
			fileMng.updateFileByPath(oldTitleImg, false, null);
			//旧内容图
			fileMng.updateFileByPath(oldContentImg, false, null);
			fileMng.updateFileByPath(bean.getContentImg(), true, null);
			fileMng.updateFileByPath(bean.getTitleImg(), true, null);
			log.info("update CmsTopic id={}.", bean.getId());
			cmsLogMng.operating(request, "cmsTopic.log.update", "id="
					+ bean.getId() + ";name=" + bean.getName());
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/topic/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsTopic[] beans = manager.deleteByIds(idArray);
				for (CmsTopic bean : beans) {
					fileMng.updateFileByPath(bean.getContentImg(), false, null);
					fileMng.updateFileByPath(bean.getTitleImg(), false, null);
					log.info("delete CmsTopic id={}", bean.getId());
					cmsLogMng.operating(request, "cmsTopic.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/topic/priority")
	public void priority(String ids,String priorities,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,priorities);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] priority = StrUtils.getInts(priorities);
			errors = validatePriority(errors, idArray, priority);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.updatePriority(idArray, priority);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/topic/by_channel")
	public void channel(Integer channelId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		List<CmsTopic> list = new ArrayList<>();
		if (channelId!=null&&!channelId.equals(0)) {
			Channel channel = channelMng.findById(channelId);
			if (channel==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				list= manager.getListByChannel(channelId);
			}
		}else{
		    list = manager.getListForTag(null, false,0, Integer.MAX_VALUE);
		}
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i=0;i<list.size();i++){
				jsonArray.put(i,byChannelToJson(list.get(i)));
			}
		}
		body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/topic/tpl_list")
	public void tplList(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		// 模板
		List<String> list = getTplList(request, site, null);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private List<String> getTplList(HttpServletRequest request, CmsSite site,
			String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(site
				.getSolutionPath()
				+ "/" + TPLDIR_TOPIC + "/");
		String tplIndex = MessageResolver.getMessage(request, TOPIC_INDEX);
		tplList = CoreUtils.tplTrim(tplList, site.getTplPath(), tpl,tplIndex);
		return tplList;
	}
	
	private JSONObject byChannelToJson(CmsTopic bean){
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(bean.getName())) {
			json.put("name", bean.getName());
		}else{
			json.put("name", "");
		}
		return json;
	}
	
	private WebErrors validatePriority(WebErrors errors,Integer[] arr1,Integer[] arr2){
		if (arr1!=null&&arr2!=null) {
			if (arr1.length!=arr2.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}
	
	@Autowired
	private TplManager tplManager;
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsFileMng fileMng;
	@Autowired
	private CmsTopicMng manager;
}
