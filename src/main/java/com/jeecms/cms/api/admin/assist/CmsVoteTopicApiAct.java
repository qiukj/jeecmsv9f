package com.jeecms.cms.api.admin.assist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
import com.jeecms.cms.entity.assist.CmsVoteItem;
import com.jeecms.cms.entity.assist.CmsVoteSubTopic;
import com.jeecms.cms.entity.assist.CmsVoteTopic;
import com.jeecms.cms.manager.assist.CmsVoteTopicMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;


@Controller
public class CmsVoteTopicApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsVoteTopicApiAct.class);

	static final String  sub_split_str="_sub_";
	static final String  item_split_str="_item_";
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/vote_topic/list")
	public void list(Integer pageNo,Short voteStatus,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		Pagination page = manager.getPage(site.getId(),voteStatus, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsVoteTopic> list = (List<CmsVoteTopic>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(false));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/vote_topic/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsVoteTopic bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsVoteTopic();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson(true).toString();
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
	
	/*
	@SignValidate
	@RequestMapping("/vote_topic/save")
	public void save(CmsVoteTopic bean,String subtitles,String subPrioritys,
			String itemTitles,String itemVoteCounts, String itemPrioritys,
			String pictures,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getTitle());
		if (!errors.hasErrors()) {
			String[] subtitleArr =null;
			if (StringUtils.isNotBlank(subtitles)) {
				subtitleArr =subtitles.split(",");
			}
			Integer[] subPriorityArr = StrUtils.getInts(subPrioritys);
			String[] itemTitleArr =null;
			if (StringUtils.isNotBlank(itemTitles)) {
				itemTitleArr =itemTitles.split(",");
			}
			Integer[] itemVoteCountArr = StrUtils.getInts(itemVoteCounts);
			Integer[] itemPriorityArr = StrUtils.getInts(itemPrioritys);
			String[] pictureArr =null;
			if (StringUtils.isNotBlank(pictures)) {
				pictureArr =pictures.split(",");
			}
			if (bean.getSite()==null) {
				bean.setSite(CmsUtils.getSite(request));
			}
			bean.init();
			List<Integer>subTypeIds=getSubTypeIdsParam(request);
			Set<CmsVoteSubTopic>subTopics=getSubTopics(null, subtitleArr,subPriorityArr, subTypeIds);
			bean = manager.save(bean, subTopics);
			List<List<CmsVoteItem>>voteItems=getSubtopicItems(itemTitleArr, itemVoteCountArr, itemPriorityArr, pictureArr);
			List<CmsVoteSubTopic>subTopicSet=subTopicMng.findByVoteTopic(bean.getId());
			for(int i=0;i<voteItems.size();i++){
				if(voteItems.get(i).size()<=0){
					voteItems.remove(i);
				}
			}
			if(voteItems.size()>0){
				for(int i=0;i<subTopicSet.size();i++){
					voteItemMng.save(voteItems.get(i), subTopicSet.get(i));
				}
			}
			log.info("save CmsVoteTopic id={}", bean.getId());
			cmsLogMng.operating(request, "cmsVoteTopic.log.save", "id="
					+ bean.getId() + ";title=" + bean.getTitle());
			body = "{\"id\":\""+bean.getId()+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	*/
	
	@SuppressWarnings("unchecked")
	@SignValidate
	@RequestMapping("/vote_topic/save")
	public void save(CmsVoteTopic bean,String source,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getTitle());
		if (!errors.hasErrors()) {
			if (bean.getSite()==null) {
				bean.setSite(CmsUtils.getSite(request));
			}
			bean.init();
			try {
				Object[]subObject=getSubTopicItem(source);
				Map<Integer, Set<CmsVoteItem>>itemMap=(Map<Integer, Set<CmsVoteItem>>) subObject[1];
				Set<CmsVoteSubTopic>subTopics=(Set<CmsVoteSubTopic>) subObject[0];
				manager.save(bean, subTopics, itemMap);
				log.info("save CmsVoteTopic id={}", bean.getId());
				cmsLogMng.operating(request, "cmsVoteTopic.log.save", "id="
						+ bean.getId() + ";title=" + bean.getTitle());
				body = "{\"id\":\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_PARAM_ERROR;
				code = ResponseCode.API_CODE_PARAM_ERROR;
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/*
	@SignValidate
	@RequestMapping("/vote_topic/update")
	public void update(CmsVoteTopic bean,String subtitles,String subPrioritys,String subTypeIds,
			String itemTitles,String itemVoteCounts, String itemPrioritys,
			String itemPictures,String subTopicIds,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getTitle());
		if (!errors.hasErrors()) {
			CmsVoteTopic topic = manager.findById(bean.getId());
			if (topic==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				//分割调查题目
				Integer[] subTopicIdArr = StrUtils.getInts(subTopicIds);
				String[] subTitleArr = spliteByToStr(subtitles,sub_split_str);
				Integer[] subPriorityArr = strToInt(spliteByToStr(subPrioritys,sub_split_str));
				Integer[] subTypeIdArr = strToInt(spliteByToStr(subTypeIds,sub_split_str));
				//分割投票项
				//先通过 sub_split_str 进行分割，将同一题目下的项分割出来
				String[] itemTitleArr = spliteByToStr(itemTitles,sub_split_str);
				String[] itemVoteCountArr = spliteByToStr(itemVoteCounts,sub_split_str);
				String[] itemPriorityArr = spliteByToStr(itemPrioritys,sub_split_str);
				String[] itemPictureArr = spliteByToStr(itemPictures,sub_split_str);
				Set<CmsVoteSubTopic>subTopics=getSubTopics(subTopicIdArr, subTitleArr,subPriorityArr, subTypeIdArr);
				bean = manager.update(bean);
				subTopicMng.update(subTopics,bean);
				List<List<CmsVoteItem>>voteItems=getSubtopicItems(itemTitleArr, itemVoteCountArr, itemPriorityArr,itemPictureArr);
				List<CmsVoteSubTopic>subTopicSet=subTopicMng.findByVoteTopic(bean.getId());
				for(int i=0;i<voteItems.size();i++){
					if(voteItems.get(i).size()<=0){
						voteItems.remove(i);
					}
				}
				for(int i=0;i<subTopicSet.size();i++){
					CmsVoteSubTopic voteSubTopic= subTopicSet.get(i);
					if(voteSubTopic.getType()!=3&&voteItems.size()>=subTopicSet.size()){
						voteItemMng.update(voteItems.get(i),voteSubTopic);
					}
				}
				log.info("update CmsVoteTopic id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsVoteTopic.log.update", "id="
						+ bean.getId() + ";title=" + bean.getTitle());
				body = "{\"id\":\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	*/
	
	@SuppressWarnings("unchecked")
	@SignValidate
	@RequestMapping("/vote_topic/update")
	public void update(CmsVoteTopic bean,String source,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getTitle());
		if (!errors.hasErrors()) {
			CmsVoteTopic topic = manager.findById(bean.getId());
			if (topic==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				Object[]subObject=getSubTopicItem(source);
				Map<Integer, Set<CmsVoteItem>>itemMap=(Map<Integer, Set<CmsVoteItem>>) subObject[1];
				Set<CmsVoteSubTopic>subTopics=(Set<CmsVoteSubTopic>) subObject[0];
				manager.update(bean, subTopics, itemMap);
				log.info("update CmsVoteTopic id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsVoteTopic.log.update", "id="
						+ bean.getId() + ";title=" + bean.getTitle());
				body = "{\"id\":\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/vote_topic/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				try {
					CmsVoteTopic[] beans = manager.deleteByIds(idArr);
					for (CmsVoteTopic bean : beans) {
						log.info("delete CmsVoteTopic id={}", bean.getId());
						cmsLogMng.operating(request, "cmsVoteTopic.log.delete", "id="
								+ bean.getId() + ";title=" + bean.getTitle());
					}
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/vote_topic/priority")
	public void priority(String ids,Integer defId,String disableds,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			Boolean[] disabledArr = strToBoolean(disableds,idArr);
			errors = validatePriority(errors, idArr,defId, disabledArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = Constants.API_MESSAGE_PARAM_ERROR;
			}else{
				Integer siteId = CmsUtils.getSiteId(request);
				manager.updatePriority(idArr, defId, disabledArr,siteId);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Object[]getSubTopicItem(String source){
		JSONArray jsonArray = new JSONArray(source);
		Map<Integer, Set<CmsVoteItem>>itemMap=new HashMap<Integer, Set<CmsVoteItem>>();
		Set<CmsVoteSubTopic>subTopics=new HashSet<CmsVoteSubTopic>();
		if (jsonArray!=null&&jsonArray.length()>0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject subJson = (JSONObject) jsonArray.get(i);
				Integer subId = jsonToInt(subJson, "id");
				String subTitle = (String) subJson.get("title");
				Integer priority = (Integer) subJson.get( "priority");
				Integer type = (Integer) subJson.get( "type");
				CmsVoteSubTopic subTopic = new CmsVoteSubTopic();
				subTopic.setTitle(subTitle);
				subTopic.setId(subId);
				subTopic.setPriority(priority);
				subTopic.setType(type);
				subTopic.setVoteIndex(i);
				Object items=null;
				try {
					items=subJson.get("voteItems");
				} catch (Exception e) {
				}
				if(items!=null && !(items instanceof String)){
					JSONArray itemArray = (JSONArray) subJson.get("voteItems");
					if (itemArray!=null&&itemArray.length()>0) {
						Set<CmsVoteItem> set = new HashSet<CmsVoteItem>();
						for (int j = 0; j < itemArray.length(); j++) {
							JSONObject itemJson = (JSONObject) itemArray.get(j);
							Integer id = jsonToInt(itemJson, "id");
							String itemTitle = (String) itemJson.get("title");
							Integer itemVoteCount =(Integer) itemJson.get("voteCount");
							Integer itemPriority =(Integer) itemJson.get("priority");
							if (itemPriority==null) {
								itemPriority=0;
							}
							String itemPicture = (String) itemJson.get("picture");
							CmsVoteItem item = new CmsVoteItem();
							item.setId(id);
							item.setTitle(itemTitle);
							item.setVoteCount(itemVoteCount);
							item.setPriority(itemPriority);
							item.setPicture(itemPicture);
							set.add(item);
						}
						itemMap.put(i, set);
					}
				}
				subTopics.add(subTopic);
			}
		}
		Object[]result=new Object[2];
		result[0]=subTopics;
		result[1]=itemMap;
		return result;
	}
	
	private Integer jsonToInt(JSONObject json,String key){
		if (json.get(key)!=null) {
			String str  = String.valueOf( json.get(key)) ;
			if (!"".equals(str)) {
				return Integer.parseInt(str);
			}
		}
		return null;
	}
	
	private WebErrors validatePriority(WebErrors errors,Integer[] idArr,Integer defId,Boolean[] disabledArr,HttpServletRequest request){
		if (disabledArr==null) {
			return errors;
		}
		if (idArr==null || idArr.length!=disabledArr.length) {
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			return errors;
		}
		CmsSite site = CmsUtils.getSite(request);
		for (Integer id : idArr) {
			vldExist(id, site.getId(), errors);
		}
		if(defId!=null){
			CmsVoteTopic topic = manager.findById(defId);
			if (topic==null) {
				errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
				return errors;
			}
		}
		return errors;
	}
	
	private Boolean[] strToBoolean(String str,Integer[] idArr){
		Boolean[] booleans = null;
		if (StringUtils.isNotBlank(str)) {
			String[] strArr = str.split(",");
			booleans = new Boolean[strArr.length];
			for (int i = 0; i < strArr.length; i++) {
				if (StringUtils.isNotBlank(strArr[i])) {
					booleans[i] = Boolean.parseBoolean(strArr[i]);
				}else{
					booleans[i]= false;
				}
			}
		}else{
			if (idArr!=null) {
				booleans = new Boolean[idArr.length];
				for (int i = 0; i < booleans.length; i++) {
					booleans[i]=false;
				}
			}
		}
		return booleans;
	}
	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids", false)) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsVoteTopic entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsVoteTopic.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsVoteTopicMng manager;
}
