package com.jeecms.cms.api.admin.main;

import java.util.ArrayList;
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
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.cms.manager.assist.CmsWebserviceMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsWebserviceApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsWebserviceApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/webservice/list")
	public void list(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsWebservice> list = (List<CmsWebservice>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(request));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/webservice/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsWebservice bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsWebservice();
			}else{
				bean = manager.findById(id);
			}
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				body = bean.convertToJson(request).toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/webservice/save")
	public void save(CmsWebservice bean,String params,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getAddress());
		if (!errors.hasErrors()) {
			/*String paramNames,String defaultValues
			,
			String[] paramNameArr = null;
			if (StringUtils.isNotBlank(paramNames)) {
				paramNameArr = paramNames.split(",");
			}
			String[] defaultValueArr = null;
			if (StringUtils.isNotBlank(defaultValues)) {
				defaultValueArr = defaultValues.split(",");
			}
			*/
			List<String>nameList=new ArrayList<String>();
			List<String>valueList=new ArrayList<String>();
			if (StringUtils.isNotBlank(params)) {
				try {
					JSONArray pArray=new JSONArray(params);
					for(int i=0;i<pArray.length();i++){
						JSONObject pJsonObject=pArray.getJSONObject(i);
						String name=(String) pJsonObject.get("paramName");
						String value=(String) pJsonObject.get("defaultValue");
						nameList.add(name);
						valueList.add(value);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			String[] nameArray=new String[]{};
			nameArray=nameList.toArray(nameArray);
			String[] valueArray=new String[]{};
			valueArray=valueList.toArray(valueArray);
			errors = validateArr(errors, nameArray, valueArray);
			if (!errors.hasErrors()) {
				bean = manager.save(bean,nameArray, valueArray);
				log.info("save CmsWebservice id={}", bean.getId());
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
	@RequestMapping("/webservice/update")
	public void update(CmsWebservice bean,String params,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),
				bean.getAddress());
		if (!errors.hasErrors()) {
			CmsWebservice webservice = manager.findById(bean.getId());
			if (webservice==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				//String paramNames,String defaultValues
				//String[] paramNameArr = null;
				//String[] defaultValueArr = null;
				//String[] paramArray=null;
				List<String>nameList=new ArrayList<String>();
				List<String>valueList=new ArrayList<String>();
				if (StringUtils.isNotBlank(params)) {
					try {
						JSONArray pArray=new JSONArray(params);
						for(int i=0;i<pArray.length();i++){
							JSONObject pJsonObject=pArray.getJSONObject(i);
							String name=(String) pJsonObject.get("paramName");
							String value=(String) pJsonObject.get("defaultValue");
							nameList.add(name);
							valueList.add(value);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				/*
				if (StringUtils.isNotBlank(paramNames)) {
					try {
						JSONObject jsonObject=new JSONObject(paramNames);
						JSONArray names=(JSONArray) jsonObject.get("paramNames");
						paramNameArr=(String[]) names.toList().toArray();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				if (StringUtils.isNotBlank(defaultValues)) {
					try {
						JSONObject jsonObject=new JSONObject(defaultValues);
						JSONArray values=(JSONArray) jsonObject.get("defaultValues");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				if (StringUtils.isNotBlank(paramNames)) {
					paramNameArr = paramNames.split(",");
				}
				if (StringUtils.isNotBlank(defaultValues)) {
					defaultValueArr = defaultValues.split(",");
				}
				*/
				String[] nameArray=new String[]{};
				nameArray=nameList.toArray(nameArray);
				String[] valueArray=new String[]{};
				valueArray=valueList.toArray(valueArray);
				errors = validateArr(errors, nameArray, valueArray);
				if (errors.hasErrors()) {
					message = errors.getErrors().get(0);
					code = ResponseCode.API_CODE_PARAM_ERROR;
				}else{
					bean = manager.update(bean,nameArray, valueArray);
					log.info("update CmsWebservice id={}.", bean.getId());
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
	@RequestMapping("/webservice/delete")
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
					CmsWebservice[] beans = manager.deleteByIds(idArr);
					for (int i = 0; i < beans.length; i++) {
						log.info("delete CmsWebservice id={}.", beans[i].getId());
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
	
	private WebErrors validateDelete(WebErrors errors,Integer[] idArr){
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				CmsWebservice webservice = manager.findById(idArr[i]);
				if (webservice==null) {
					errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
					return errors;
				}
			}
		}
		return errors;
	}
	
	private WebErrors validateArr(WebErrors errors,Object[] paramNameArr, Object[] defaultValueArr) {
		if ((paramNameArr==null && defaultValueArr!=null) 
				|| (paramNameArr!=null && defaultValueArr==null)) {
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			return errors;
		}
		if (paramNameArr!=null && defaultValueArr!=null) {
			if (paramNameArr.length!=defaultValueArr.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}

	@Autowired
	private CmsWebserviceMng manager;
}
