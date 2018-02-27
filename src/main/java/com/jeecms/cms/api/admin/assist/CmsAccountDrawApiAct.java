package com.jeecms.cms.api.admin.assist;

import java.util.Date;
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
import com.jeecms.cms.entity.assist.CmsAccountDraw;
import com.jeecms.cms.manager.assist.CmsAccountDrawMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;

import net.sf.json.JSONObject;

@Controller
public class CmsAccountDrawApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsAccountDrawApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/accountPay/draw_apply_list")
	public void list(String queryUsername,Short status,
			Date timeBegin,Date timeEnd,Integer pageNo,Integer pageSize,
			HttpServletResponse response,HttpServletRequest request){
		Integer userId=null;
		if(StringUtils.isNotBlank(queryUsername)){
			CmsUser user=cmsUserMng.findByUsername(queryUsername);
			if(user!=null){
				userId=user.getId();
			}else{
				userId=0;
			}
		}
		Pagination page = manager.getPage(userId, status, timeBegin, timeEnd, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsAccountDraw> list = (List<CmsAccountDraw>) page.getList();
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
	
	@RequestMapping("/accountPay/draw_apply_get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			CmsAccountDraw bean = manager.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				JSONObject json = createJson(bean);
				body = json.toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject createJson(CmsAccountDraw bean) {
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(bean.getApplyAccount())) {
			json.put("applyAccount", bean.getApplyAccount());
		}else{
			json.put("applyAccount", "");
		}
		if (bean.getApplyAmount()!=null) {
			json.put("applyAmount", bean.getApplyAmount());
		}else{
			json.put("applyAmount", "");
		}
		if (bean.getAccountPay()!=null && bean.getAccountPay().getId()!=null) {
			json.put("drawId", bean.getAccountPay().getId());
		}else{
			json.put("drawId", "");
		}
		return json;
	}

	@SignValidate
	@RequestMapping("/accountPay/draw_apply_check")
	public void checkApply(String ids,Boolean checks,HttpServletRequest request,
			HttpServletResponse response){
		String body ="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,checks);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateExist(errors,idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				for (int i = 0; i < idArr.length; i++) {
					CmsAccountDraw bean = manager.findById(idArr[i]);
					if (checks) {//true表示审核通过
						bean.setApplyStatus(CmsAccountDraw.CHECKED_SUCC);
					}else{//否则退回
						bean.setApplyStatus(CmsAccountDraw.CHECKED_FAIL);
					}
					manager.update(bean);
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/accountPay/draw_apply_delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body ="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateExist(errors, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				CmsAccountDraw[] beans = manager.deleteByIds(idArr);
				for (CmsAccountDraw bean : beans) {
					log.info("delete CmsAccountDraw id={}", bean.getId());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateExist(WebErrors errors, Integer[] idArr) {
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				CmsAccountDraw draw = manager.findById(idArr[i]);
				if (draw==null) {
					errors.addErrorString(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
					return errors;
				}
			}
		}
		return errors;
	}

	@Autowired
	private CmsAccountDrawMng manager;
	@Autowired
	private CmsUserMng cmsUserMng;
}
