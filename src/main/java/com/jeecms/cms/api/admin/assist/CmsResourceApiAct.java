package com.jeecms.cms.api.admin.assist;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.manager.assist.CmsResourceMng;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsResourceApiAct {
	
	/**
	 * 资源树API
	 */
	@RequestMapping(value = "/resource/tree")
	public void tree(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String root = site.getResPath();
		JSONObject result=new JSONObject();
		List<FileWrap>list=(List<FileWrap>) resourceMng.listFile(root, true);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToTreeJson(list.get(i)));
			}
		}
		message=Constants.API_MESSAGE_SUCCESS;
		result.put("resources", jsonArray);
		result.put("rootPath", site.getResPath());
		body=result.toString();
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 资源列表API
	 */
	@RequestMapping(value = "/resource/list")
	public void resourceList(String root,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (StringUtils.isBlank(root)) {
			root = site.getResPath();
		}
		WebErrors errors = validateList(root, site.getResPath(), request);
		if (errors.hasErrors()) {
			code=ResponseCode.API_CODE_PARAM_ERROR;
			message=Constants.API_MESSAGE_PARAM_ERROR;
		}else{
			List<FileWrap>list=(List<FileWrap>) resourceMng.listFile(root, false);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body=jsonArray.toString();
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/resource/dir_save")
	public void createDir(String root, String dirName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		if (StringUtils.isBlank(root)) {
			root = site.getResPath();
		}
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,root,dirName);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getResPath(), request);
		}
		if(!errors.hasErrors()){
			resourceMng.createDir(root, dirName);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/resource/get")
	public void get(String name,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="";
		String message=Constants.G_API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.G_API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,name);
		if(!errors.hasErrors()){
			errors=validateList(name, site.getResPath(), request);
		}
		if(!errors.hasErrors()){
			String source="";
			try {
				source = resourceMng.readFile(name);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			message=Constants.G_API_MESSAGE_SUCCESS;
			code=ResponseCode.G_API_CODE_CALL_SUCCESS;
			body=source;
		}else{
			message=Constants.G_API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.G_API_CODE_PARAM_ERROR;
		}
		//css等资源含特殊字符
//		JsonObject json=new JsonObject();
		JSONObject json = new JSONObject();
		json.put("body", body);
		json.put("message",message);
		json.put("code", code);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@SignValidate
	@RequestMapping("/resource/save")
	public void save(String root, String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		if (StringUtils.isBlank(root)) {
			root = site.getResPath();
		}
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				 root,filename,source);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getResPath(), request);
		}
		if(!errors.hasErrors()){
			try {
				resourceMng.createFile(request,root, filename, source);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/resource/update")
	public void update(String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				filename,source);
		if(!errors.hasErrors()){
			errors=validateList(filename, site.getResPath(), request);
		}
		if(!errors.hasErrors()){
			try {
				resourceMng.updateFile(filename, source);
			} catch (IOException e) {
				e.printStackTrace();
			}
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/resource/delete")
	public void delete(String names,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, names);
		String[]nameArray=null;
		if(!errors.hasErrors()){
			nameArray=names.split(Constants.API_ARRAY_SPLIT_STR);
			for(String n:nameArray){
				errors=validatePath(n, site.getResPath(), errors);
				if(errors.hasErrors()){
					break;
				}
			}
		}
		if(!errors.hasErrors()){
			try {
				resourceMng.delete(nameArray);
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}  catch (Exception e) {
				message=Constants.API_MESSAGE_DELETE_ERROR;
				code=ResponseCode.API_CODE_DELETE_ERROR;
			}
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/resource/rename")
	public void rename(String origName, String distName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, origName,distName);
		if(!errors.hasErrors()){
			errors=validateRename(origName,distName, site.getResPath(), request);
		}
		if(!errors.hasErrors()){
			resourceMng.rename(origName,distName);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/resource/upload")
	public void upload(String root,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors, root,file);
		if(!errors.hasErrors()){
			 errors = validateUpload(root,site.getResPath(), file, request);
		}
		if(!errors.hasErrors()){
			try {
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				resourceMng.saveFile(request,root, file);
				JSONObject json=new JSONObject();
				json.put("ext", ext.toUpperCase());
				json.put("size", file.getSize());
				json.put("url", root+"/"+origName);
				json.put("name", file.getOriginalFilename());
				body=json.toString();
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}  catch (Exception e) {
				e.printStackTrace();
				message=Constants.API_MESSAGE_UPLOAD_ERROR;
				code=ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	private WebErrors validateUpload(String root,String tplPath,
			MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("error.noFileToUpload");
			return errors;
		}
		if(isUnValidName(root, root, tplPath, errors)){
			errors.addErrorString("template.invalidParams");
		}
		String filename=file.getOriginalFilename();
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
			return errors;
		}
		return errors;
	}
	
	private WebErrors validateList(String name, String tplPath,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorString("template.invalidParams");
		}
		return errors;
	}
	
	private WebErrors validatePath(String name, String tplPath,
			WebErrors errors) {
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorString("template.invalidParams");
		}
		return errors;
	}
	
	private WebErrors validateRename(String name, String newName,
			String tplPath,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorString("template.invalidParams");
		}
		if(isUnValidName(newName, newName, tplPath, errors)){
			errors.addErrorString("template.invalidParams");
		}
		return errors;
	}
	
	private boolean isUnValidName(String path,String name,String tplPath, WebErrors errors) {
		if (!path.startsWith(tplPath)||path.contains("../")||path.contains("..\\")||name.contains("..\\")||name.contains("../")) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean vldExist(String name, WebErrors errors) {
		if (errors.ifNull(name, "name", false)) {
			return true;
		}
		Tpl entity = tplManager.get(name);
		if (errors.ifNotExist(entity, Tpl.class, name, false)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private TplManager tplManager;
	@Autowired
	private CmsResourceMng resourceMng;
}
