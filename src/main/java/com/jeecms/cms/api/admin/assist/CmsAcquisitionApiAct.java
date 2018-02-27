package com.jeecms.cms.api.admin.assist;

import java.util.Arrays;
import java.util.List;

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
import com.jeecms.cms.entity.assist.CmsAcquisition;
import com.jeecms.cms.entity.assist.CmsAcquisitionHistory;
import com.jeecms.cms.entity.assist.CmsAcquisitionTemp;
import com.jeecms.cms.manager.assist.CmsAcquisitionHistoryMng;
import com.jeecms.cms.manager.assist.CmsAcquisitionMng;
import com.jeecms.cms.manager.assist.CmsAcquisitionTempMng;
import com.jeecms.cms.service.AcquisitionSvc;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsAcquisitionApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsAcquisitionApiAct.class);
	
	@RequestMapping("/acquisition/list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		List<CmsAcquisition> list = manager.getList(site.getId());
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/acquisition/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsAcquisition bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsAcquisition();
			}else{
				bean = manager.findById(id);
			}
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.init();
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/save")
	public void save(CmsAcquisition bean,Integer channelId,Integer typeId,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),channelId,typeId);
		if (!errors.hasErrors()) {
			bean.setSite(CmsUtils.getSite(request));
			bean.init();
			Integer siteId = CmsUtils.getSiteId(request);
			Integer userId = CmsUtils.getUserId(request);
			bean = manager.save(bean, channelId, typeId, userId, siteId);
			log.info("save CmsAcquisition id={}", bean.getId());
			cmsLogMng.operating(request, "cmsAcquisition.log.save", "id="
					+ bean.getId() + ";name=" + bean.getName());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/update")
	public void update(CmsAcquisition bean,Integer channelId,Integer typeId,
			HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getName(),channelId,typeId);
		if (!errors.hasErrors()) {
			errors = validateUpdate(errors,bean.getId(), request);
			if (!errors.hasErrors()) {
				bean = manager.update(bean, channelId, typeId);
				log.info("update CmsAcquisition id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsAcquisition.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateArr(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				try {
					CmsAcquisition[] beans = manager.deleteByIds(idArr);
					for (CmsAcquisition bean : beans) {
						log.info("delete CmsAcquisition id={}", bean.getId());
						cmsLogMng.operating(request, "cmsAcquisition.log.delete", "id="
								+ bean.getId() + ";name=" + bean.getName());
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
	@RequestMapping("/acquisition/start")
	public void start(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateArr(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				Integer queueNum = manager.hasStarted(siteId);
				if(queueNum==0){
					acquisitionSvc.start(idArr[0]);
				}
				manager.addToQueue(idArr, queueNum);
				log.info("start CmsAcquisition ids={}", Arrays.toString(idArr));
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/end")
	public void end(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			errors  = validateExist(errors, id, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.end(id);
				CmsAcquisition acqu = manager.popAcquFromQueue(siteId);
				if (acqu != null) {
					Integer acquId = acqu.getId();
					acquisitionSvc.start(acquId);
				}
				log.info("end CmsAcquisition id={}", id);
				body = "{\"id\":"+id+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/pause")
	public void pause(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			errors = validateExist(errors, id, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.pause(id);
				log.info("pause CmsAcquisition id={}", id);
				body = "{\"id\":"+id+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code =ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/cancel")
	public void cancel(Integer id,Integer sortId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			errors = validateExist(errors, id, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.cancel(siteId, id);
				log.info("cancel CmsAcquisition id={}", id);
				body = "{\"id\":"+id+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code =ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/acquisition/progress_data")
	public void progressData(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		Integer siteId = site.getId();
		CmsAcquisition bean = manager.getStarted(siteId);
		JSONObject json = new JSONObject();
		if (bean!=null) {
			List<CmsAcquisitionTemp> list = cmsAcquisitionTempMng.getList(siteId);
			JSONArray jsonArray = new JSONArray();
			if (list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,createEasyJson(list.get(i)));
				}
			}
			Integer percent = cmsAcquisitionTempMng.getPercent(siteId);
			json.put("list", jsonArray);
			json.put("acquisition", createEasyAcq(bean));
			json.put("percent", percent);
			json.put("havaAcquisition", true);
		}else{
			json.put("havaAcquisition", false);
			cmsAcquisitionTempMng.clear(siteId);
		}
		String body = json.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject createEasyAcq(CmsAcquisition bean) {
		JSONObject json = new JSONObject();
		if (bean!=null) {
			if (StringUtils.isNotBlank(bean.getName())) {
				json.put("name", bean.getName());
			}else{
				json.put("name", "");
			}
			json.put("totalNum", bean.getTotalNum());
			if (bean.getCurrNum()!=null) {
				json.put("currNum", bean.getCurrNum());
			}else{
				json.put("currNum", "");
			}
		}
		return json;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/acquisition/history")
	public void history(Integer acquId,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination page = cmsAcquisitionHistoryMng.getPage(siteId, acquId, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsAcquisitionHistory> list = (List<CmsAcquisitionHistory>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/acquisition/history_delete")
	public void historyDelete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateHistoryDelete(errors, idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				try {
					CmsAcquisitionHistory[] beans = cmsAcquisitionHistoryMng
							.deleteByIds(idArr);
					for (CmsAcquisitionHistory bean : beans) {
						log.info("delete CmsAcquisitionHistory id={}", bean.getId());
						cmsLogMng.operating(request, "cmsAcquisitionHistory.log.delete",
								"id=" + bean.getId() + ";name=" + bean.getTitle());
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
	
	private JSONObject createEasyJson(CmsAcquisitionTemp temp) {
		JSONObject json = new JSONObject();
		if (temp.getSeq()!=null) {
			json.put("seq", temp.getSeq());
		}else{
			json.put("seq", "");
		}
		if (StringUtils.isNotBlank(temp.getContentUrl())) {
			json.put("contentUrl", temp.getContentUrl());
		}else{
			json.put("contentUrl", "");
		}
		if (StringUtils.isNotBlank(temp.getTitle())) {
			json.put("title", temp.getTitle());
		}else{
			json.put("title", "");
		}
		if (StringUtils.isNotBlank(temp.getDescription())) {
			json.put("description", temp.getDescription());
		}else{
			json.put("description", "");
		}
		return json;
	}
	
	private WebErrors validateHistoryDelete(WebErrors errors,Integer[] ids,
			HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids", false)) {
			return errors;
		}
		for (Integer id : ids) {
			vldHistoryExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	private boolean vldHistoryExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsAcquisitionHistory entity = cmsAcquisitionHistoryMng.findById(id);
		if (errors.ifNotExist(entity, CmsAcquisitionHistory.class, id, false)) {
			return true;
		}
		if (!entity.getAcquisition().getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	private WebErrors validateExist(WebErrors errors,Integer id,HttpServletRequest request){
		if (id!=null) {
			vldExist(id, CmsUtils.getSiteId(request), errors);
		}
		return errors;
	}
	
	private WebErrors validateArr(WebErrors errors,Integer[] ids, HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids", false)) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}
	
	private WebErrors validateUpdate(WebErrors errors,Integer id, HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsAcquisition entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsAcquisition.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private AcquisitionSvc acquisitionSvc;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsAcquisitionMng manager;
	@Autowired
	private CmsAcquisitionHistoryMng cmsAcquisitionHistoryMng;
	@Autowired
	private CmsAcquisitionTempMng cmsAcquisitionTempMng;
}
