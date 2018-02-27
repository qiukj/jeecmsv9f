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
import com.jeecms.cms.entity.main.ContentTag;
import com.jeecms.cms.manager.main.ContentTagMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class ContentTagApiAct {
	private static final Logger log = LoggerFactory.getLogger(ContentTagApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/tag/list")
	public void list(Integer pageNo,Integer pageSize,String queryName,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(queryName, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<ContentTag> list = (List<ContentTag>) page.getList();
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
	
	@RequestMapping("/tag/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		ContentTag bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new ContentTag();
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
	@RequestMapping("/tag/save")
	public void save(ContentTag bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName());
		if (!errors.hasErrors()) {
			ContentTag tag = manager.findByNameForTag(bean.getName());
			if (tag!=null) {
				message = Constants.API_MESSAGE_TAG_NAME_EXIST;
				code = ResponseCode.API_CODE_TAG_NAME_EXIST;
			}else{
				bean.init();
				bean = manager.save(bean);
				log.info("save ContentTag id={}", bean.getId());
				cmsLogMng.operating(request, "contentTag.log.save", "id="
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
	@RequestMapping("/tag/update")
	public void update(ContentTag bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), bean.getName());
		if (!errors.hasErrors()) {
			errors = validateUpdate(errors, bean.getId());
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				ContentTag tag = manager.findByNameForTag(bean.getName());
				if (tag!=null&&!tag.getId().equals(bean.getId())) {
					message = Constants.API_MESSAGE_TAG_NAME_EXIST;
					code = Constants.API_MESSAGE_TAG_NAME_EXIST;
				}else{
					bean = manager.update(bean);
					log.info("update ContentTag id={}.", bean.getId());
					cmsLogMng.operating(request, "contentTag.log.update", "id="
							+ bean.getId() + ";name=" + bean.getName());
					body = "{\"id\":"+bean.getId()+"}";
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/tag/delete")
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
				ContentTag[] beans = manager.deleteByIds(idArr);
				for (ContentTag bean : beans) {
					log.info("delete ContentTag id={}", bean.getId());
					cmsLogMng.operating(request, "contentTag.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/tag/check_name")
	public void checkName(String name,Integer id,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		boolean result = false;
		errors = ApiValidate.validateRequiredParams(request, errors, name);
		if (!errors.hasErrors()) {
			ContentTag tag = manager.findByNameForTag(name);
			if (tag==null) {
				result = true;
			}else{
				if (id!=null&&id.equals(tag.getId())) {
					result=true;
				}
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors,Integer[] idArr){
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i], errors);
			}
		}
		return errors;
	}
	
	private WebErrors validateUpdate(WebErrors errors,Integer id){
		if (id!=null) {
			vldExist(id, errors);
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, WebErrors errors) {
		ContentTag entity = manager.findById(id);
		if (errors.ifNotExist(entity, ContentTag.class, id, false)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private ContentTagMng manager;
}
