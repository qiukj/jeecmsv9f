package com.jeecms.cms.api.admin.assist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.jeecms.cms.entity.assist.CmsFile;
import com.jeecms.cms.manager.assist.CmsFileMng;
import com.jeecms.cms.manager.assist.CmsResourceMng;
import com.jeecms.common.file.FileWrap;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsFileApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsFileApiAct.class);
	private static final String INVALID_PARAM = "template.invalidParams";
	
	@RequestMapping("/file/list")
	public void list(String root,Boolean valid,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = "\"\"";
		String code = ResponseCode.API_CODE_CALL_FAIL;
		CmsSite site = CmsUtils.getSite(request);
		log.debug("list Resource root: {}", root);
		if (StringUtils.isBlank(root)) {
			root = site.getUploadPath();
		}
		String uploadPath = root.substring(site.getUploadPath().length());
		if (uploadPath.length() == 0) {
			uploadPath = "/";
		}
		WebErrors errors = validateList(root,site.getUploadPath(),request);
		if (!errors.hasErrors()) {
			List<FileWrap> list = resourceMng.queryFiles(root, valid);
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			body = jsonArray.toString();
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message = errors.getErrors().get(0);
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/file/freefiles_delete")
	public void delFreeFiles(String root,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = "\"\"";
		String code = ResponseCode.API_CODE_CALL_FAIL;
		List<CmsFile>fileList=fileMng.getList(false);
		CmsSite site=CmsUtils.getSite(request);
		String contextPath=site.getContextPath();
		String[]names=new String[fileList.size()];
		String filePath;
		for(int i=0;i<names.length;i++){
			filePath=fileList.get(i).getFilePath();
			//只有文件才删除
			if(filePath.indexOf(".")!=-1){
				if(StringUtils.isNotBlank(contextPath)){
					if(filePath.contains(contextPath)){
						names[i]=filePath.substring(filePath.indexOf(contextPath)+contextPath.length());
					}
				}else{
					names[i]=filePath;
				}
			}
		}
		//去掉空
		List<String>nameList=new ArrayList<String>();
		for(String name:names){
			if(StringUtils.isNotBlank(name)){
				nameList.add(name);
			}
		}
		names=(String[]) nameList.toArray(new String[nameList.size()]);
		WebErrors errors = validateDeleteFreeFile(root, site.getUploadPath(),  names, request);
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
		}else{
			if(names!=null&&names.length>0){
				try {
					int count = resourceMng.delete(names);
					log.info("delete Resource count: {}", count);
					for (String name : names) {
						fileMng.deleteByPath(name);
						log.info("delete Resource name={}", name);
						cmsLogMng.operating(request, "resource.log.delete", "filename="
								+ name);
					}
					body = "{\"count\":"+count+"}";
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
	
	@SignValidate
	@RequestMapping("/file/delete")
	public void delete(String root,String names,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, names);
		if (!errors.hasErrors()) {
			String[] nameArr = names.split(",");
			CmsSite site=CmsUtils.getSite(request);
			errors = validateDelete(root, site.getUploadPath(), nameArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				try {
					int count = resourceMng.delete(nameArr);
					log.info("delete Resource count: {}", count);
					for (String name : nameArr) {
						fileMng.deleteByPath(name);
						log.info("delete Resource name={}", name);
						cmsLogMng.operating(request, "resource.log.delete", "filename="
								+ name);
					}
					body = "{\"count\":"+count+"}";
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
	
	@SignValidate
	@RequestMapping("/file/flag")
	public void flagOldFilesValid(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		String root = site.getUploadPath();
		String body = "\"\"";
		String message = "\"\"";
		String code = ResponseCode.API_CODE_CALL_FAIL;
		if (StringUtils.isNotBlank(root)) {
			saveFileFlags(realPathResolver.get(root), root);
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private  void saveFileFlags(String realpath,String path){   
        File file = new File(realpath);   
        File[] array = file.listFiles();   
        String filePath;
        for(int i=0;i<array.length;i++){   
        	filePath=path+"/"+array[i].getName();
            if(array[i].isFile()){ 
            	if(fileMng.findByPath(filePath)==null){
            		fileMng.saveFileByPath(filePath,array[i].getName(), true);
            	}
            }else if(array[i].isDirectory()){ 
            	if(fileMng.findByPath(filePath)==null){
            		fileMng.saveFileByPath(filePath,array[i].getName(), true);
            	}
            	saveFileFlags(array[i].getPath(),path);   
            }   
        }   
    }
	
	private WebErrors validateDelete(String root, String path,String[] names,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(names, "names", true);
		for (String id : names) {
			vldExist(id, errors);
		}
		for(String name:names){
			if(isUnValidName(root, name, path, errors)){
				errors.addErrorString(INVALID_PARAM);
				return errors;
			}
		}
		return errors;
	}
	
	private WebErrors validateDeleteFreeFile(String root, String path,String[] names,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if(names==null||names.length<=0){
			errors.addErrorString("error.findnofreefile");
		}
		for(String name:names){
			if(isUnValidName(root, name, path, errors)){
				errors.addErrorString(INVALID_PARAM);
				return errors;
			}
		}
		return errors;
	}
	
	private WebErrors validateList(String name, String path, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, path, errors)){
			errors.addErrorString(INVALID_PARAM);
		}
		return errors ;
	}
	
	private boolean isUnValidName(String path,String name,String resPath, WebErrors errors) {
		if (!path.startsWith(resPath)||path.contains("../")||path.contains("..\\")||name.contains("..\\")||name.contains("../")) {
			return true;
		}else{
			return false;
		}
	}

	private boolean vldExist(String name, WebErrors errors) {
		if (errors.ifNull(name, "name", false)) {
			return true;
		}
		return false;
	}


	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsFileMng fileMng;
	private CmsResourceMng resourceMng;
	@Autowired
	private RealPathResolver realPathResolver;

	@Autowired
	public void setResourceMng(CmsResourceMng resourceMng) {
		this.resourceMng = resourceMng;
	}
}
