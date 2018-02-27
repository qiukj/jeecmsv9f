package com.jeecms.cms.api.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsGroupApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsGroupApiAct.class);
	
	@RequestMapping("/group/list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		List<CmsGroup> list = manager.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/group/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsGroup bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsGroup();
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
	@RequestMapping("/group/save")
	public void save(CmsGroup bean,String viewChannelIds,String contriChannelIds,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),bean.getPriority(),
				bean.getAllowMaxFile(),bean.getAllowPerDay());
		if (!errors.hasErrors()) {
			Integer[] viewChannel = StrUtils.getInts(viewChannelIds);
			Integer[] contriChannel = StrUtils.getInts(contriChannelIds);
			errors = validateArrayLength(errors, viewChannel, contriChannel);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				bean.init();
				bean = manager.save(bean, viewChannel, contriChannel);
				log.info("save CmsGroup id={}", bean.getId());
				cmsLogMng.operating(request, "cmsGroup.log.save", "id=" + bean.getId()
						+ ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/group/update")
	public void update(CmsGroup bean,String viewChannelIds,String contriChannelIds
			,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName(),bean.getPriority(),
				bean.getAllowMaxFile(),bean.getAllowPerDay());
		if (!errors.hasErrors()) {
			Integer[] viewChannel = StrUtils.getInts(viewChannelIds);
			Integer[] contriChannel = StrUtils.getInts(contriChannelIds);
			errors = validateArrayLength(errors, viewChannel, contriChannel);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				bean = manager.update(bean, viewChannel, contriChannel);
				log.info("update CmsGroup id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsGroup.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/group/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsGroup[] beans = manager.deleteByIds(idArray);
				for (CmsGroup bean : beans) {
					log.info("delete CmsGroup id={}", bean.getId());
					cmsLogMng.operating(request, "cmsGroup.log.delete", "id="
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
	@RequestMapping("/group/priority")
	public void priority(String ids,String priorities,Integer regDefId,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,priorities);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] priority = StrUtils.getInts(priorities);
			errors = validateArrayLength(errors, idArray, priority);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.updatePriority(idArray, priority);
				if (regDefId!=null) {
					manager.updateRegDef(regDefId);
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
			
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private WebErrors validateArrayLength(WebErrors errors,Integer[] arr1 , Integer[] arr2){
		if (arr1!=null&&arr2!=null) {
			if (arr1.length!=arr2.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsGroupMng manager;
}
