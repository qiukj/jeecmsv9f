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
import com.jeecms.cms.entity.assist.CmsFriendlink;
import com.jeecms.cms.entity.assist.CmsFriendlinkCtg;
import com.jeecms.cms.manager.assist.CmsFileMng;
import com.jeecms.cms.manager.assist.CmsFriendlinkCtgMng;
import com.jeecms.cms.manager.assist.CmsFriendlinkMng;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsFriendlinkApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsFriendlinkApiAct.class);
	
	@RequestMapping("/friendlink/list")
	public void list(Integer queryCtgId,Boolean enabled,
			HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		List<CmsFriendlink> list = manager.getList(site.getId(), queryCtgId, enabled);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/friendlink/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsFriendlink bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsFriendlink();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code =ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/friendlink/save")
	public void save(CmsFriendlink bean, Integer categoryId,HttpServletResponse response,
			HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),bean.getDomain(),
				bean.getPriority(),bean.getViews(),bean.getEnabled(),categoryId);
		if (!errors.hasErrors()) {
			CmsFriendlinkCtg ctg = cmsFriendlinkCtgMng.findById(categoryId);
			if (ctg==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.init();
				if (bean.getSite()==null) {
					bean.setSite(CmsUtils.getSite(request));
				}
				bean = manager.save(bean, categoryId);
				fileMng.updateFileByPath(bean.getLogo(), true, null);
				log.info("save CmsFriendlink id={}", bean.getId());
				cmsLogMng.operating(request, "cmsFriendlink.log.save", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/friendlink/update")
	public void update(CmsFriendlink bean, Integer categoryId,String oldLog,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName(),bean.getDomain(),
				bean.getPriority(),bean.getViews(),bean.getEnabled(),categoryId);
		if (!errors.hasErrors()) {
			CmsFriendlinkCtg ctg = cmsFriendlinkCtgMng.findById(categoryId);
			CmsFriendlink friendlink = manager.findById(bean.getId());
			if (ctg==null||friendlink==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean = manager.update(bean, categoryId);
				fileMng.updateFileByPath(oldLog, false, null);
				fileMng.updateFileByPath(bean.getLogo(), true, null);
				log.info("update CmsFriendlink id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsFriendlink.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/friendlink/priority")
	public void priority(String ids,String priorities
			,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,priorities);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			Integer[] priorityArr = StrUtils.getInts(priorities);
			errors = validateExist(errors, idArr,request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				errors = validatePriority(errors,idArr,priorityArr);
				if (errors.hasErrors()) {
					message = errors.getErrors().get(0);
					code = ResponseCode.API_CODE_PARAM_ERROR;
				}else{
					manager.updatePriority(idArr, priorityArr);
					log.info("update CmsFriendlink priority.");
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validatePriority(WebErrors errors, Integer[] idArr, Integer[] priorityArr) {
		if (idArr!=null&&priorityArr!=null) {
			if (idArr.length!=priorityArr.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors ;
	}

	@SignValidate
	@RequestMapping("/friendlink/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateExist(errors, idArr,request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				try {
					CmsFriendlink[] beans = manager.deleteByIds(idArr);
					for (CmsFriendlink bean : beans) {
						fileMng.updateFileByPath(bean.getLogo(), false, null);
						log.info("delete CmsFriendlink id={}", bean.getId());
						cmsLogMng.operating(request, "cmsFriendlink.log.delete", "id="
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
	
	private WebErrors validateExist(WebErrors errors,Integer[] idArr,HttpServletRequest request){
		CmsSite site = CmsUtils.getSite(request);
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i],site.getId(), errors);
			}
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsFriendlink entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsFriendlink.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsFriendlinkCtgMng cmsFriendlinkCtgMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsFriendlinkMng manager;
	@Autowired
	private CmsFileMng fileMng;
}
