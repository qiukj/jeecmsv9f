package com.jeecms.cms.api.admin.assist;

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
import com.jeecms.cms.entity.assist.CmsFriendlinkCtg;
import com.jeecms.cms.manager.assist.CmsFriendlinkCtgMng;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsFriendlinkCtgApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsFriendlinkCtgApiAct.class);
	
	@RequestMapping("/friendlink/ctg_list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		List<CmsFriendlinkCtg> list = manager.getList(site.getId());
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i =0;i<list.size();i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/friendlink/ctg_get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsFriendlinkCtg bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsFriendlinkCtg();
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
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/friendlink/ctg_save")
	public void save(CmsFriendlinkCtg bean,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getName());
		if (!errors.hasErrors()) {
			bean.init();
			if (bean.getSite()==null) {
				bean.setSite(CmsUtils.getSite(request));
			}
			bean = manager.save(bean);
			log.info("save CmsFriendlinkCtg id={}", bean.getId());
			cmsLogMng.operating(request, "cmsFriendlinkCtg.log.save", "id="
					+ bean.getId() + ";name=" + bean.getName());
			body = "{\"id\":"+bean.getId()+"}";
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/friendlink/ctg_priority")
	public void update(String ids,String priorities,String names,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,ids,priorities,names);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			Integer[] priorityArr = StrUtils.getInts(priorities);
			String[] nameArr = null;
			if (StringUtils.isNotBlank(names)) {
				nameArr = names.split(",");
			}
			errors = validatePriority(errors, idArr, priorityArr, nameArr,request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				manager.updateFriendlinkCtgs(idArr, nameArr, priorityArr);
				log.info("update CmsFriendlinkCtg.");
				cmsLogMng.operating(request, "cmsFriendlinkCtg.log.update", null);
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/friendlink/ctg_delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors, idArr,request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				try {
					CmsFriendlinkCtg[] beans = manager.deleteByIds(idArr);
					for (CmsFriendlinkCtg bean : beans) {
						log.info("delete CmsFriendlinkCtg id={}", bean.getId());
						cmsLogMng.operating(request, "cmsFriendlinkCtg.log.delete", "id="
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
		ApiResponse apiResponse=new ApiResponse(request,body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors , Integer[] idArr,HttpServletRequest request){
		CmsSite site = CmsUtils.getSite(request);
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i],site.getId(), errors);
			}
		}
		return errors;
	}
	
	private WebErrors validatePriority(WebErrors errors , Integer[] idArr,Integer[] priorityArr,String[] nameArr,HttpServletRequest request){
		CmsSite site = CmsUtils.getSite(request);
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				vldExist(idArr[i],site.getId(), errors);
			}
		}
		if (idArr!=null && priorityArr!=null && nameArr!=null) {
			if (idArr.length!=priorityArr.length||idArr.length!=nameArr.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsFriendlinkCtg entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsFriendlinkCtg.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsFriendlinkCtgMng manager;
}
