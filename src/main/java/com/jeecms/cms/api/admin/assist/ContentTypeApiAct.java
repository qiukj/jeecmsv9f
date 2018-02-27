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
import com.jeecms.cms.entity.main.ContentType;
import com.jeecms.cms.manager.main.ContentTypeMng;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class ContentTypeApiAct {
	private static final Logger log = LoggerFactory.getLogger(ContentTypeApiAct.class);
	
	@RequestMapping("/type/list")
	public void list(Boolean containDisabled,HttpServletRequest request,HttpServletResponse response){
		List<ContentType> list = manager.getList(containDisabled);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i <list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/type/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		ContentType bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new ContentType();
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
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/type/save")
	public void save(ContentType bean,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName(),
				bean.getHasImage(),bean.getDisabled(),bean.getImgHeight(),bean.getImgWidth());
		if (!errors.hasErrors()) {
			ContentType type = manager.findById(bean.getId());
			if (type!=null) {
				message = Constants.API_MESSAGE_CONTENTTYPE_ID_EXIST;
				code = ResponseCode.API_CODE_CONTENTTYPE_ID_EXIST;
			}else{
				bean = manager.save(bean);
				log.info("save ContentType id={}", bean.getId());
				cmsLogMng.operating(request, "contentType.log.save", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/type/update")
	public void update(ContentType bean,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName(),
				bean.getHasImage(),bean.getDisabled(),bean.getImgHeight(),bean.getImgWidth());
		if (!errors.hasErrors()) {
			ContentType type = manager.findById(bean.getId());
			if (type==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean = manager.update(bean);
				log.info("update ContentType id={}.", bean.getId());
				cmsLogMng.operating(request, "contentType.log.update", "id="
						+ bean.getId() + ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/type/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		Integer siteId = CmsUtils.getSiteId(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			for(int i =0 ; i <idArr.length;i++){
				vldExist(idArr[i], siteId, errors);
			}
			if (errors.hasErrors()) {
				message = Constants.API_MESSAGE_PARAM_ERROR;
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				try {
					ContentType[] beans = manager.deleteByIds(idArr);
					for (ContentType bean : beans) {
						log.info("delete ContentType id={}", bean.getId());
						cmsLogMng.operating(request, "contentType.log.delete", "id="
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
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/type/check_id")
	public void CheckId(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id);
		if (!errors.hasErrors()) {
			ContentType type = manager.findById(id);
			if (type!=null) {
				result = true;
			}
		}
		body = "{\"result\":"+result+"}";
		message = Constants.API_MESSAGE_SUCCESS;
		code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		ContentType entity = manager.findById(id);
		if (errors.ifNotExist(entity, ContentType.class, id, false)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private ContentTypeMng manager;
}
