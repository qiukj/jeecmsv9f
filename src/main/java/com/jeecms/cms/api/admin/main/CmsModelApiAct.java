package com.jeecms.cms.api.admin.main;

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
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsModelApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsModelApiAct.class);
	
	/**
	 * 模型管理列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/model/list")
	public void list(Boolean containDisabled,Boolean hasContent,HttpServletRequest request,HttpServletResponse response){
		if (containDisabled==null) {
			containDisabled = true;
		}
		List<CmsModel> list = manager.getList(containDisabled, hasContent, CmsUtils.getSiteId(request));
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResult = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResult);;
	}
	
	/**
	 * 模型管理详情
	 * @param id 模型编号
	 * @param request 
	 * @param response
	 */
	@RequestMapping("/model/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsModel bean = null;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			if (id.equals(0)) {
				bean = new CmsModel();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				JSONObject json = bean.convertToJson();
				body = json.toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模型编号校验 可用编号返回true，已存在编号返回false
	 * @param id 模型编号
	 * @param request
	 * @param response
	 */
	@RequestMapping("/model/check_id")
	public void chekcID(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		JSONObject json = new JSONObject();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			if (id>0) {//id必须为正数
				CmsModel model = manager.findById(id);
				if (model==null) {//id不能重复
					result = true;
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_PARAM_ERROR;
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		json.put("result", result);
		body = json.toString();
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模型添加
	 * @param bean 模型对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/model/save")
	public void save(CmsModel bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName()
				,bean.getGlobal(),bean.getPath(),bean.getTplChannelPrefix(),bean.getPriority(),bean.getDisabled());
		if (!errors.hasErrors()) {
			CmsModel model = manager.findById(bean.getId());
			if (model!=null) {
				message = Constants.API_MESSAGE_MODEL_EXIST;
				code = ResponseCode.API_CODE_MODEL_EXIST;
			}else{
				bean.init();
				if(!bean.getGlobal()){
					bean.setSite(CmsUtils.getSite(request));
				}
				bean = manager.save(bean);
				log.info("save CmsModel id={}",bean.getId());
				cmsLogMng.operating(request, "cmsModel.log.save", "id=" + bean.getId()
				+ ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模型修改
	 * @param bean
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/model/update")
	public void Update(CmsModel bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(),bean.getName()
				,bean.getGlobal(),bean.getPath(),bean.getTplChannelPrefix(),bean.getPriority(),bean.getDisabled());
		if (!errors.hasErrors()) {
			CmsModel model = manager.findById(bean.getId());
			if (model==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				if(!bean.getGlobal()){
					bean.setSite(CmsUtils.getSite(request));
				}
				bean = manager.update(bean);
				log.info("update CmsModel id={}",bean.getId());
				cmsLogMng.operating(request, "cmsModel.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模型删除
	 * @param ids
	 * @param response
	 * @param request
	 */
	@SignValidate
	@RequestMapping("/model/delete")
	public void delete(String ids,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsModel[] beans = manager.deleteByIds(idArray);
				for(CmsModel bean: beans){
					log.info("delete CmsModel id={}", bean.getId());
					cmsLogMng.operating(request, "cmsModel.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模型批量保存
	 * @param ids
	 * @param priorities
	 * @param disableds
	 * @param defId
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/model/priority")
	public void priority(String ids,String priorities,String disableds,
			Integer defId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,priorities,disableds,defId);
		if (!errors.hasErrors()) {
			Integer[] wids = StrUtils.getInts(ids);
			Integer[] priority = StrUtils.getInts(priorities);
			Boolean[] disabled = strToBoolean(disableds);
			errors = validatePriority(wids, priority, disabled, defId, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				manager.updatePriority(wids, priority, disabled, defId);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Boolean[] strToBoolean(String str){
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] split = str.split(",");
		Boolean[] booleans = new Boolean[split.length];
		for (int i = 0; i < split.length; i++) {
			booleans[i] = Boolean.parseBoolean(split[i]);
		}
		return booleans;
	}
	
	private WebErrors validatePriority(Integer[] wids, Integer[] priority,
			Boolean[] disabled, Integer defId, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (wids.length != priority.length || wids.length != disabled.length) {
			String s = Constants.API_MESSAGE_PARAM_ERROR;
			errors.addErrorString(s);
			return errors;
		}
		for (int i = 0, len = wids.length; i < len; i++) {
			if (vldExist(wids[i], errors)) {
				errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
				return errors;
			}
			if (priority[i] == null) {
				priority[i] = 0;
			}
			if (disabled[i] == null) {
				disabled[i] = false;
			}
		}
		if (vldExist(defId, errors)) {
			errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
			return errors;
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsModel entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsModel.class, id, false)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsModelMng manager;
}
