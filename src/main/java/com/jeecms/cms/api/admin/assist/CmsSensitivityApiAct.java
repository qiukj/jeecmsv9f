package com.jeecms.cms.api.admin.assist;

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
import com.jeecms.cms.entity.assist.CmsSensitivity;
import com.jeecms.cms.manager.assist.CmsSensitivityMng;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsSensitivityApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSensitivityApiAct.class);
	
	@RequestMapping("/sensitivity/list")
	public void list(Boolean cacheable,
			HttpServletRequest request,HttpServletResponse response){
		if (cacheable==null) {
			cacheable=false;
		}
		List<CmsSensitivity> list = manager.getList(cacheable);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/sensitivity/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSensitivity bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsSensitivity();
			}else{
				bean = manager.findById(id);
			}
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/sensitivity/save")
	public void save(CmsSensitivity bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getSearch(),bean.getReplacement());
		if (!errors.hasErrors()) {
			bean = manager.save(bean);
			log.info("save CmsSensitivity id={}", bean.getId());
			cmsLogMng.operating(request, "cmsSensitivity.log.save", "id="
					+ bean.getId() + ";name=" + bean.getSearch());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/sensitivity/update")
	public void update(String ids,String searchs,String replacements,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,searchs,replacements);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			String[] searchArr = searchs.split(",");
			String[] replacementArr = replacements.split(",");
			errors = validateUpdate(errors,idArr,searchArr,replacementArr);
			if (!errors.hasErrors()) {
				manager.updateEnsitivity(idArr, searchArr, replacementArr);
				log.info("update CmsSensitivity.");
				cmsLogMng.operating(request, "cmsSensitivity.log.save", null);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/sensitivity/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				try {
					CmsSensitivity[] beans = manager.deleteByIds(idArr);
					for (CmsSensitivity bean : beans) {
						log.info("delete CmsSensitivity id={}", bean.getId());
						cmsLogMng.operating(request, "cmsSensitivity.log.delete", "id="
								+ bean.getId() + ";name=" + bean.getSearch());
					}
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors , Integer[] idArr){
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i], errors);
			}
		}
		return errors;
	}
	
	private WebErrors validateUpdate(WebErrors errors, Integer[] idArr, String[] searchArr, String[] replacementArr) {
		for (int i = 0; i < idArr.length; i++) {
			vldExist(idArr[i], errors);
		}
		if (searchArr!=null) {
			for (int i = 0; i < searchArr.length; i++) {
				if (searchArr[i]==null) {
					errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
					return errors;
				}
			}
		}
		if (replacementArr!=null) {
			for (int i = 0; i < replacementArr.length; i++) {
				if (replacementArr[i]==null) {
					errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
					return errors;
				}
			}
		}
		if (idArr.length != searchArr.length || idArr.length != replacementArr.length) {
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			return errors;
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, WebErrors errors) {
		CmsSensitivity entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsSensitivity.class, id, false)) {
			return true;
		}
		return false;
	}

	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsSensitivityMng manager;
}
