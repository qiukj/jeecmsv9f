package com.jeecms.cms.api.admin.assist;

import static com.jeecms.cms.Constants.TPLDIR_INDEX;
import static com.jeecms.cms.Constants.TPL_BASE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jeecms.cms.entity.main.CmsModel;
import com.jeecms.cms.manager.assist.CmsResourceMng;
import com.jeecms.cms.manager.main.CmsModelMng;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.tpl.FileTpl;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.CoreUtils;

@Controller
public class CmsTemplateApiAct {
	
	private static final Logger log = LoggerFactory
			.getLogger(CmsTemplateApiAct.class);
	
	@RequestMapping("/tpl/select_content_model")
	public void selectContentModel(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		JSONArray jsonArray = new JSONArray();
		//查询所有模型
		List<CmsModel> models = modelMng.getList(true, true, site.getId());
		int index =0 ;
		for (CmsModel model : models) {
			JSONObject json = new JSONObject();
			if (model.getId()!=null) {
				json.put("id", model.getId());
			}else{
				json.put("id", "");
			}
			if (StringUtils.isNotBlank(model.getName())) {
				json.put("name", model.getName());
			}else{
				json.put("name", "");
			}
			JSONArray contentArray = new JSONArray();
			List<String> content = getTplContent(site, model, null);
			for (int i = 0; i < content.size(); i++) {
				contentArray.put(i,content.get(i));
			}
			json.put("contentTpl", contentArray);
			JSONArray mobileContentArray = new JSONArray();
			List<String> mobileTplContent = getMobileTplContent(site, model, null);
			for (int i = 0; i < mobileTplContent.size(); i++) {
				mobileContentArray.put(i,mobileTplContent.get(i));
			}
			json.put("mobileContentTpl", mobileContentArray);
			jsonArray.put(index,json);
			index++;
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	/**
	 * 首页模板列表
	 * @param response
	 * @param request
	 */
	@RequestMapping("/tpl/list")
	public void getTpl(HttpServletResponse response,HttpServletRequest request){
		CmsSite site = CmsUtils.getSite(request);
		List<String> indexTplList = getTplIndex(site, null);
		JSONArray jsonArray = new JSONArray();
		if (indexTplList!=null&&indexTplList.size()>0) {
			for(int i = 0 ; i < indexTplList.size() ; i++){
				jsonArray.put(i,indexTplList.get(i));
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/tpl/model_list")
	public void modelList(Integer modelId,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		if (modelId!=null) {
			CmsModel model = modelMng.findById(modelId);
			if (model==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				JSONObject json = new JSONObject();
				//栏目模板列表
				List<String> channelTplList = getTplChannel(site, model, null);
				json.put("channelTpl", strToJson(channelTplList));
				//栏目移动版模板列表
				List<String> channelMobileTplList = getMobileTplChannel(site, model, null);
				json.put("channelMobileTpl", strToJson(channelMobileTplList));
				//内容模板列表
				List<String> contentTplList = getTplContent(site, model, null);
				json.put("contentTpl", strToJson(contentTplList));
				//内容移动版模板列表
				List<String> contentMobileTplList = getMobileTplContent(site, model, null);
				json.put("contentMobileTpl", strToJson(contentMobileTplList));
				body = json.toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONArray strToJson(List<String> list){
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i));
			}
		}
		return jsonArray;
	}
	
	@RequestMapping("/tpl/channel_list")
	public void channelTplList(Integer modelId,Boolean isMobile,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		if (isMobile==null) {
			isMobile=false;
		}
		if (modelId!=null) {
			CmsModel model = modelMng.findById(modelId);
			if (model==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				List<String> channelTplList = new ArrayList<>();
				if (!isMobile) {//栏目模板列表
					channelTplList = getTplChannel(site, model, null);
				}else{//栏目移动版模板列表
					channelTplList = getMobileTplChannel(site, model, null);
				}
				JSONArray jsonArray = new JSONArray();
				if (channelTplList!=null && channelTplList.size()>0) {
					for (int i = 0; i < channelTplList.size(); i++) {
						jsonArray.put(i,channelTplList.get(i));
					}
				}
				body = jsonArray.toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/tpl/content_list")
	public void contentTplList(Integer modelId,Boolean isMobile,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		if (isMobile==null) {
			isMobile=false;
		}
		if (modelId!=null) {
			CmsModel model = modelMng.findById(modelId);
			if (model==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				List<String> contentTplList = new ArrayList<>();
				if (!isMobile) {//内容模板列表
					contentTplList = getTplContent(site, model, null);
				}else{//内容移动版模板列表
					contentTplList = getMobileTplContent(site, model, null);
				}
				JSONArray jsonArray = new JSONArray();
				if (contentTplList!=null && contentTplList.size()>0) {
					for (int i = 0; i < contentTplList.size(); i++) {
						jsonArray.put(i,contentTplList.get(i));
					}
				}
				body = jsonArray.toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模板树API
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/template/tree")
	public void tree(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String root = site.getTplPath();
		JSONObject result=new JSONObject();
		List<FileTpl>list=(List<FileTpl>) tplManager.getChild(root);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToTreeJson(list.get(i)));
			}
		}
		result.put("resources", jsonArray);
		result.put("rootPath", root);
		message=Constants.API_MESSAGE_SUCCESS;
		body=result.toString();
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模板列表API
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/template/list")
	public void templateList(String root,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		WebErrors errors = validateList(root, site.getTplPath(), request);
		if (errors.hasErrors()) {
			code=ResponseCode.API_CODE_PARAM_ERROR;
			message=Constants.API_MESSAGE_PARAM_ERROR;
		}else{
			List<FileTpl>list=(List<FileTpl>) tplManager.getChild(root);
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
	@RequestMapping("/template/dir_save")
	public void createDir(String root, String dirName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,
				root,dirName);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.save(root + "/" + dirName, null, true);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/get")
	public void get(String name,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="";
		String message=Constants.G_API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.G_API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors,name);
		if(!errors.hasErrors()){
			errors=validateList(name, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			FileTpl tpl=(FileTpl) tplManager.get(name);
			message=Constants.G_API_MESSAGE_SUCCESS;
			code=ResponseCode.G_API_CODE_CALL_SUCCESS;
			body=tpl.getSource();
		}else{
			message=Constants.G_API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.G_API_CODE_PARAM_ERROR;
		}
//		JsonObject json=new JsonObject();
		JSONObject json = new JSONObject();
		json.put("body", body);
		json.put("message",message);
		json.put("code", code);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@SignValidate
	@RequestMapping("/template/save")
	public void save(String root, String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors,
				 root,filename,source);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			String name = root + "/" + filename + com.jeecms.cms.Constants.TPL_SUFFIX;
			tplManager.save(name, source, false);
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
	@RequestMapping("/template/update")
	public void update( String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String root= site.getTplPath();
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors,
				 filename,source);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.update(filename, source);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
			log.error(errors.getErrors().get(0));
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/delete")
	public void delete(String names,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors, names);
		String[]nameArray = null;
		if(!errors.hasErrors()){
			nameArray=names.split(Constants.API_ARRAY_SPLIT_STR);
			for(String n:nameArray){
				errors=validatePath(n, site.getTplPath(), errors);
				if(errors.hasErrors()){
					break;
				}
			}
		}
		if(!errors.hasErrors()){
			try {
				if(nameArray!=null){
					tplManager.delete(nameArray);
				}
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
	
	@SignValidate
	@RequestMapping("/template/rename")
	public void rename(String origName, String distName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors, origName,distName);
		if(!errors.hasErrors()){
			errors=validateRename(origName,distName, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.rename(origName,distName);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/template/getSolutions")
	public void getSolutions(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String[] solutions = resourceMng.getSolutions(site.getTplPath());
		if(solutions!=null){
			for(int i=0;i<solutions.length;i++){
				jsonArray.put(i, solutions[i]);
			}
		}
		message=Constants.API_MESSAGE_SUCCESS;
		code=ResponseCode.API_CODE_CALL_SUCCESS;
		body=jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/solutionupdate")
	public void setTempate(String solution,String mobile, 
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors, solution);
//		if(mobile==null){
//			mobile=false;
//		}
		if(!errors.hasErrors()){
			cmsSiteMng.updateTplSolution(site.getId(), solution,mobile);
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/exportTpl")
	public void tplExport(String solution,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request ,errors, solution);
		if(!errors.hasErrors()){
			List<FileEntry> fileEntrys = resourceMng.export(site, solution);
			response.setContentType("application/x-download;charset=UTF-8");
			response.addHeader("Content-disposition", "filename=template-"
					+ solution + ".zip");
			try {
				// 模板一般都在windows下编辑，所以默认编码为GBK
				Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
			} catch (IOException e) {
				log.error("export template error!", e);
			}
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/importTpl")
	public void tplImport(
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		CmsSite site=CmsUtils.getSite(request);
		WebErrors errors = validate(file, request);
		if(errors.hasErrors()){
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}else{
			//验证公共非空参数
			errors=ApiValidate.validateRequiredParams(request ,errors,file);
			if(!errors.hasErrors()){
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				String filepath = "";
				try {
					File tempFile = File.createTempFile("tplZip", "temp");
					file.transferTo(tempFile);
					resourceMng.imoport(tempFile, site);
					tempFile.delete();
					JSONObject json=new JSONObject();
					json.put("ext", ext.toUpperCase());
					json.put("size", file.getSize());
					json.put("url", filepath);
					json.put("name", file.getOriginalFilename());
					body=json.toString();
					message=Constants.API_MESSAGE_SUCCESS;
				} catch (Exception e) {
					//e.printStackTrace();
					code=ResponseCode.API_CODE_UPLOAD_ERROR;
				} 
			}else{
				code=ResponseCode.API_CODE_PARAM_REQUIRED;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/upload")
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
		errors=ApiValidate.validateRequiredParams(request ,errors, file);
		if(!errors.hasErrors()){
			 errors = validateUpload(root,site.getTplPath(),file, request);
		}
		if(!errors.hasErrors()){
			try {
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				tplManager.save(root, file);
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
	
	private WebErrors validate(MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("error.noFileToUpload");
			return errors;
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
	
	private List<String> getTplIndex(CmsSite site,String tpl) {
		String path=site.getPath();
		List<String> tplList = tplManager.getNameListByPrefix(site.getTplIndexPrefix(TPLDIR_INDEX));
		return CoreUtils.tplTrim(tplList,getTplPath(path), tpl);
	}
	
	private String getTplPath(String path){
		return TPL_BASE + "/" + path;
	}
	
	private List<String> getTplContent(CmsSite site, CmsModel model, String tpl) {
		String sol = site.getSolutionPath();
		List<String> tplList = tplManager.getNameListByPrefix(model
				.getTplContent(sol, false));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	private List<String> getMobileTplContent(CmsSite site, CmsModel model, String tpl) {
		String sol = site.getMobileSolutionPath();
		List<String> tplList = tplManager.getNameListByPrefix(model
				.getTplContent(sol, false));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private List<String> getTplChannel(CmsSite site, CmsModel model, String tpl) {
		String sol = site.getSolutionPath();
		List<String> tplList = tplManager.getNameListByPrefix(model.getTplChannel(sol, false));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	private List<String> getMobileTplChannel(CmsSite site, CmsModel model, String tpl) {
		String sol = site.getMobileSolutionPath();
		List<String> tplList = tplManager.getNameListByPrefix(model.getTplChannel(sol, false));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	@Autowired
	private CmsModelMng modelMng;
	@Autowired
	private TplManager tplManager;
	@Autowired
	private CmsResourceMng resourceMng;
	@Autowired
	private CmsSiteMng cmsSiteMng;
}
