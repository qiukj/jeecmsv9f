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
import com.jeecms.cms.entity.assist.CmsSearchWords;
import com.jeecms.cms.manager.assist.CmsSearchWordsMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsSearchWordsApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSearchWordsApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/searchwords/list")
	public void list(String qname,Integer qrecommend,Integer orderBy,
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(CmsUtils.getSiteId(request), qname, qrecommend, orderBy, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsSearchWords> list = (List<CmsSearchWords>) page.getList();
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
	
	@RequestMapping("/searchwords/get")
	public void get(Integer id,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSearchWords bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsSearchWords();
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
	@RequestMapping("/searchwords/save")
	public void save(CmsSearchWords bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName());
		if (!errors.hasErrors()) {
			bean.init();
			bean.setHitCount(0);
			bean.setSite(CmsUtils.getSite(request));
			bean.setNameInitial(ChineseCharToEn.getAllFirstLetter(bean.getName()));
			bean = manager.save(bean);
			log.info("save CmsSearchWords id={}", bean.getId());
			cmsLogMng.operating(request, "CmsSearchWords.log.save", "id="
					+ bean.getId() + ";name=" + bean.getName());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/searchwords/update")
	public void update(CmsSearchWords bean,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),bean.getName());
		if (!errors.hasErrors()) {
			CmsSearchWords words = manager.findById(bean.getId());
			if (words==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.setNameInitial(ChineseCharToEn.getAllFirstLetter(bean.getName()));
				bean = manager.update(bean);
				log.info("update CmsSearchWords id={}.", bean.getId());
				cmsLogMng.operating(request, "CmsSearchWords.log.update", "id="
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
	@RequestMapping("/searchwords/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors, idArr);
			if (!errors.hasErrors()) {
				CmsSearchWords[] beans = manager.deleteByIds(idArr);
				for (CmsSearchWords bean : beans) {
					log.info("delete CmsSearchWords id={}", bean.getId());
					cmsLogMng.operating(request, "CmsSearchWords.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors,Integer[] idArr){
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				CmsSearchWords words = manager.findById(idArr[i]);
				if (words==null) {
					errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
					return errors;
				}
			}
		}
		return errors;
	}
	
	@Autowired
	private CmsSearchWordsMng manager;
	@Autowired
	private CmsLogMng cmsLogMng;
}
