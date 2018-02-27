package com.jeecms.cms.api.admin.main;

import java.util.List;
import java.util.Map;

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
import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.cms.manager.main.ApiAccountMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;
@Controller
public class ApiAccountApiAct {
	private static final Logger log = LoggerFactory.getLogger(ApiAccountApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/apiAccount/list")
	public void list(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(pageNo, pageSize);
		List<ApiAccount> list = (List<ApiAccount>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"toatalCount\":"+totalCount;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/apiAccount/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		ApiAccount bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new ApiAccount();
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
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/apiAccount/save")
	public void save(ApiAccount bean,String apiPwd,String setAppId,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,apiPwd, setAppId,bean.getAppKey(),
				bean.getAesKey(),bean.getIvKey());
		if (!errors.hasErrors()) {
			try {
				CmsConfig config = configMng.get();
				Map<String, String> map = config.getAttr();
				ApiAccount apiAccount=manager.getApiAccount(request);
				//独立密码解密
				String decrypt = AES128Util.decrypt(apiPwd, apiAccount.getAesKey(), apiAccount.getIvKey());
				//独立密码MD5加密
				String encodePassword = pwdEncoder.encodePassword(decrypt);
				String oldPWD = map.get("apiAccountMngPassword");
				//判断输入密码是否正确
				if (encodePassword.equals(oldPWD)) {
					bean.setAppId(setAppId);
					bean.init();
					bean = manager.save(bean);
					log.info("save ApiAccount id={}",bean.getId());
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					body = "{\"id\":"+"\""+bean.getId()+"\"}";
				}else{
					message = Constants.API_MESSAGE_PASSWORD_ERROR;
					code = ResponseCode.API_CODE_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				message = Constants.API_MESSAGE_AES128_ERROR;
				code = ResponseCode.API_CODE_AES128_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/apiAccount/val_pwd")
	public void valApiPWD(String password,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, password);
		boolean result = false;
		if (!errors.hasErrors()) {
			CmsConfig config = configMng.get();
			ApiAccount account = manager.getApiAccount(request);
			try {
				//密码解密
				String decryptOld = AES128Util.decrypt(password, account.getAesKey(), account.getIvKey());
				//旧密码加密
				String encodePassword = pwdEncoder.encodePassword(decryptOld);
				Map<String, String> map = config.getAttr();
				String apiPwd = map.get("apiAccountMngPassword");
				if (encodePassword.equals(apiPwd)) {
					result = true;
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				message = Constants.API_MESSAGE_AES128_ERROR;
				code = ResponseCode.API_CODE_AES128_ERROR;
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/apiAccount/pwd_update")
	public void updateApiPWD(String oldPWD,String newPWD,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, oldPWD,newPWD);
		if (!errors.hasErrors()) {
			CmsConfig config = configMng.get();
			ApiAccount account = manager.getApiAccount(request);
			//密码解密
			try {
				String decryptOld = AES128Util.decrypt(oldPWD, account.getAesKey(), account.getIvKey());
				String decryptNew = AES128Util.decrypt(newPWD, account.getAesKey(), account.getIvKey());
				//旧密码加密
				String encodePassword = pwdEncoder.encodePassword(decryptOld);
				Map<String, String> map = config.getAttr();
				String apiPwd = map.get("apiAccountMngPassword");
				if (encodePassword.equals(apiPwd)) {//判断旧密码与原密码是否相同
					//新密码加密
					map.put("apiAccountMngPassword", pwdEncoder.encodePassword(decryptNew));
					config.setAttr(map);
					CmsConfig update = configMng.update(config);
					log.info("update api account password id={}",update.getId());
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_PASSWORD_ERROR;
					code = ResponseCode.API_CODE_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				message = Constants.API_MESSAGE_AES128_ERROR;
				code = ResponseCode.API_CODE_AES128_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private CmsConfigMng configMng ;
	@Autowired
	private ApiAccountMng manager;
}
