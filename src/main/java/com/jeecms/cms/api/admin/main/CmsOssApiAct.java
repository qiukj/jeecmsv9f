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
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsOss;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.manager.CmsOssMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsOssApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsOssApiAct.class);
	
	/**
	 * OSS列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/oss/list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		List<CmsOss> list = manager.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size();i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * OSS详情
	 * @param id OSS编号
	 * @param response
	 * @param request
	 */
	@RequestMapping("/oss/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		CmsOss bean;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsOss();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				JSONObject jsonObject = bean.convertToJson();
				body = jsonObject.toString();
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
	
	/**
	 * OSS新增
	 * @param bean OSS对象
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@SignValidate
	@RequestMapping("/oss/save")
	public void add(CmsOss bean,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,
				bean.getSecretId(),bean.getAppKey(),bean.getName(),bean.getOssType(),bean.getBucketName());
		if (!errors.hasErrors()) {
			ApiAccount account=apiManager.getApiAccount(request);
			//appkey secretId解密保存
			bean.setAppKey(AES128Util.decrypt(bean.getAppKey(), account.getAesKey(), account.getIvKey()));
			bean.setSecretId(AES128Util.decrypt(bean.getSecretId(), account.getAesKey(), account.getIvKey()));
			bean = manager.save(bean);
			log.info("save OSS id={}",bean.getId());
			cmsLogMng.operating(request, "oss.log.save", "id=" + bean.getId()
			+ ";name=" + bean.getBucketName());
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * OSS修改
	 * @param bean OSS对象
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@SignValidate
	@RequestMapping("/oss/update")
	public void Update(CmsOss bean,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,
				bean.getId(),bean.getName(),bean.getOssType(),bean.getBucketName());
		if (!errors.hasErrors()) {
			ApiAccount account=apiManager.getApiAccount(request);
			if (StringUtils.isNotBlank(bean.getAppKey())) {
				//appkey解密
				bean.setAppKey(AES128Util.decrypt(bean.getAppKey(), account.getAesKey(), account.getIvKey()));
				
			}
			if (StringUtils.isNotBlank(bean.getSecretId())) {
				//secretId解密
				bean.setSecretId(AES128Util.decrypt(bean.getSecretId(), account.getAesKey(), account.getIvKey()));
			}
			bean = manager.update(bean);
			log.info("update oss id={}",bean.getId());
			cmsLogMng.operating(request, "oss.log.update", "id=" + bean.getId()
			+ ";name=" + bean.getBucketName());
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * OSS删除
	 * @param ids OSS编号组
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/oss/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsOss[] osses = manager.deleteByIds(idArray);
				for(int i =0;i<osses.length;i++){
					log.info("delete oss id={}",osses[i].getId());
					cmsLogMng.operating(request, "oss.log.delete", "id=" + osses[i].getId()
					+ ";name=" + osses[i].getBucketName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsOssMng manager;
	@Autowired
	private ApiAccountMng apiManager;
}
