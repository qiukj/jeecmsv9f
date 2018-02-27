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
import com.jeecms.cms.entity.assist.CmsScoreGroup;
import com.jeecms.cms.entity.assist.CmsScoreItem;
import com.jeecms.cms.manager.assist.CmsScoreGroupMng;
import com.jeecms.cms.manager.assist.CmsScoreItemMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsScoreItemApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsScoreItemApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/scoreitem/list")
	public void list(Integer groupId,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		String body = "\"\"";
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors = WebErrors.create(request);
		errors = validateGroup(errors, groupId);
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
			code = errors.getErrors().get(1);
		}else{
			Pagination page = manager.getPage(groupId, pageNo, pageSize);
			int totalCount = page.getTotalCount();
			List<CmsScoreItem> list = (List<CmsScoreItem>) page.getList();
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/scoreitem/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsScoreItem bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsScoreItem();
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
	@RequestMapping("/scoreitem/save")
	public void save(CmsScoreItem bean,Integer groupId, HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName(),bean.getScore());
		if (!errors.hasErrors()) {
			errors = validateGroup(errors, groupId);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = errors.getErrors().get(1);
			}else{
				CmsScoreGroup group = scoreGroupMng.findById(groupId);
				bean.init();
				bean.setGroup(group);
				bean = manager.save(bean);
				log.info("save CmsScoreItem id={}", bean.getId());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/scoreitem/update")
	public void update(CmsScoreItem bean , HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getName(),bean.getScore());
		if (!errors.hasErrors()) {
			errors = validateItem(errors, bean.getId());
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = errors.getErrors().get(1);
			}else{
				bean = manager.update(bean);
				log.info("update CmsScoreItem id={}.", bean.getId());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/scoreitem/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelet(errors, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = errors.getErrors().get(1);
			}else{
				try {
					CmsScoreItem[] beans = manager.deleteByIds(idArr);
					for (CmsScoreItem bean : beans) {
						log.info("delete CmsScoreItem id={}", bean.getId());
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
	
	private WebErrors validateDelet(WebErrors errors,Integer[] idArr){
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				errors = validateItem(errors, idArr[i]);
				if (errors.hasErrors()) {
					return errors;
				}
			}
		}
		return errors;
	}
	
	private WebErrors validateItem(WebErrors errors,Integer id){
		if (id!=null) {
			CmsScoreItem item = manager.findById(id);
			if (item==null) {
				errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
				errors.addErrorString(ResponseCode.API_CODE_NOT_FOUND);
				return errors;
			}
		}
		return errors;
	}
	
	private WebErrors validateGroup(WebErrors errors,Integer groupId){
		if (groupId==null) {
			errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
			errors.addErrorString(ResponseCode.API_CODE_PARAM_REQUIRED);
			return errors;
		}
		CmsScoreGroup group = scoreGroupMng.findById(groupId);
		if (group==null) {
			errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
			errors.addErrorString(ResponseCode.API_CODE_NOT_FOUND);
			return errors;
		}
		return errors;
	}
	
	@Autowired
	private CmsScoreItemMng manager;
	@Autowired
	private CmsScoreGroupMng scoreGroupMng;
}
