package com.jeecms.cms.api.admin.assist;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.jeecms.cms.entity.assist.CmsAdvertising;
import com.jeecms.cms.manager.assist.CmsAdvertisingMng;
import com.jeecms.cms.manager.assist.CmsAdvertisingSpaceMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsAdvertisingApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsAdvertisingApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/advertising/list")
	public void list(Integer queryAdspaceId, Boolean queryEnabled,
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		CmsSite site = CmsUtils.getSite(request);
		Pagination page = manager.getPage(site.getId(), queryAdspaceId, queryEnabled, pageNo, pageSize);
		List<CmsAdvertising> list = (List<CmsAdvertising>) page.getList();
		int totalCount = page.getTotalCount();
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
	
	@RequestMapping("/advertising/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsAdvertising bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsAdvertising();
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
	@RequestMapping("/advertising/save")
	public void save(CmsAdvertising bean, Integer adspaceId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),bean.getCategory(),adspaceId);
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		if (!errors.hasErrors()) {
			if (bean.getCategory().equals("image")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("image_link"),
						attr.get("image_url"));
			}else if (bean.getCategory().equals("flash")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("flash_url"));
			}else if (bean.getCategory().equals("text")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("text_link"),
						attr.get("text_title"));
			}else if (bean.getCategory().equals("code")) {
				errors = ApiValidate.validateRequiredParams(request, errors, bean.getCode());
			}else{
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		if (!errors.hasErrors()) {
			if (cmsAdvertisingSpaceMng.findById(adspaceId)!=null) {
				bean.init();
				if (bean.getSite()==null) {
					bean.setSite(CmsUtils.getSite(request));
				}
				// 去除为空串的属性
				Set<String> toRemove = new HashSet<String>();
				for (Entry<String, String> entry : attr.entrySet()) {
					if (StringUtils.isBlank(entry.getValue())) {
						toRemove.add(entry.getKey());
					}
				}
				for (String key : toRemove) {
					attr.remove(key);
				}
				bean = manager.save(bean, adspaceId, attr);
				log.info("save CmsAdvertising id={}", bean.getId());
				cmsLogMng.operating(request, "cmsAdvertising.log.save", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+bean.getId()+"}";
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
	@RequestMapping("/advertising/update")
	public void update(CmsAdvertising bean, Integer adspaceId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getName(),bean.getCategory());
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		if (!errors.hasErrors()) {
			if (bean.getCategory().equals("image")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("image_link"),
						attr.get("image_url"));
			}else if (bean.getCategory().equals("flash")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("flash_url"));
			}else if (bean.getCategory().equals("text")) {
				errors = ApiValidate.validateRequiredParams(request, errors, attr.get("text_link"),
						attr.get("text_title"));
			}else if (bean.getCategory().equals("code")) {
				errors = ApiValidate.validateRequiredParams(request, errors, bean.getCode());
			}else{
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		if (!errors.hasErrors()) {
			CmsAdvertising advertising = manager.findById(bean.getId());
			if (advertising==null&&cmsAdvertisingSpaceMng.findById(adspaceId)!=null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				// 去除为空串的属性
				Set<String> toRemove = new HashSet<String>();
				for (Entry<String, String> entry : attr.entrySet()) {
					if (StringUtils.isBlank(entry.getValue())) {
						toRemove.add(entry.getKey());
					}
				}
				for (String key : toRemove) {
					attr.remove(key);
				}
				bean = manager.update(bean, adspaceId, attr);
				log.info("update CmsAdvertising id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsAdvertising.log.update", "id="
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
	@RequestMapping("/advertising/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors, idArr,request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				CmsAdvertising[] beans = manager.deleteByIds(idArr);
				for (CmsAdvertising bean : beans) {
					log.info("delete CmsAdvertising id={}", bean.getId());
					cmsLogMng.operating(request, "cmsAdvertising.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors,Integer[] idArr,HttpServletRequest request){
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
		CmsAdvertising entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsAdvertising.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsAdvertisingSpaceMng cmsAdvertisingSpaceMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsAdvertisingMng manager;
}
