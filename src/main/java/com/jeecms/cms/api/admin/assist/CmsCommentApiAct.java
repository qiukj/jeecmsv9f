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
import com.jeecms.cms.entity.assist.CmsComment;
import com.jeecms.cms.entity.assist.CmsCommentExt;
import com.jeecms.cms.manager.assist.CmsCommentMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

import net.sf.json.JSONObject;

@Controller
public class CmsCommentApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsCommentApiAct.class);

	@SuppressWarnings("unchecked")
	@RequestMapping("/comment/list_by_content")
	public void list(Integer queryContentId, Short queryChecked, Boolean queryRecommend, Integer pageNo,
			Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (queryContentId != null) {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			CmsSite site = CmsUtils.getSite(request);
			Pagination page = manager.getPage(site.getId(), queryContentId, null, queryChecked, queryRecommend, true,
					pageNo, pageSize);
			int totalCount = page.getTotalCount();
			List<CmsComment> list = (List<CmsComment>) page.getList();
			JSONArray jsonArray = new JSONArray();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = jsonArray.toString() + ",\"totalCount\":" + totalCount;
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/comment/list")
	public void newList(Integer queryContentId, Short queryChecked, Boolean queryRecommend, Integer pageNo,
			Integer pageSize, HttpServletRequest request, HttpServletResponse response) {
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Pagination page = manager.getNewPage(CmsUtils.getSiteId(request), 
				queryContentId, queryChecked, queryRecommend,
				pageNo, pageSize);
		List<CmsComment> list = (List<CmsComment>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i, createCommentJson(list.get(i)));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString() + ",\"totalCount\":" + totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@RequestMapping("/comment/get")
	public void get(Integer id, HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsComment bean = null;
		if (id != null) {
			if (id.equals(0)) {
				bean = new CmsComment();
			} else {
				bean = manager.findById(id);
			}
			if (bean != null) {
				bean.init();
				body = bean.convertToJson().toString();
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} else {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping("/comment/update")
	public void update(CmsComment bean, CmsCommentExt ext, HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId());
		if (!errors.hasErrors()) {
			errors = validateUpdate(errors, bean.getId(), request);
			if (!errors.hasErrors()) {
				// 若回复内容不为空而且回复更新，则设置回复时间，已最新回复时间为准
				if (StringUtils.isNotBlank(ext.getReply())) {
					bean.setReplayTime(new Date());
					bean.setReplayUser(CmsUtils.getUser(request));
				}
				bean = manager.update(bean, ext);
				log.info("update CmsComment id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsComment.log.update", "id=" + bean.getId());
				body = "{\"id\":\"" + bean.getId() + "\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			} else {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping("/comment/delete")
	public void delete(String ids, HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateExist(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			} else {
				try {
					CmsComment[] beans = manager.deleteByIds(idArr);
					for (CmsComment bean : beans) {
						log.info("delete CmsComment id={}", bean.getId());
						cmsLogMng.operating(request, "cmsComment.log.delete", "id=" + bean.getId());
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

	/**
	 * 审核
	 * @param ids
	 * @param isCheck 0未审核 1审核通过 2审核不通过
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/comment/check")
	public void check(String ids,Short isCheck, HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,isCheck);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateExist(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			} else {
				CmsComment[] beans = manager.checkByIds(idArr, CmsUtils.getUser(request), isCheck);
				for (CmsComment bean : beans) {
					log.info("check CmsGuestbook id={}", bean.getId());
					cmsLogMng.operating(request, "cmsComment.log.check",
							"id=" + bean.getId() + ";title=" + bean.getReplayHtml());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping("/comment/recommend")
	public void recommend(Integer id,Boolean isRecommend,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id,isRecommend);
		if (!errors.hasErrors()) {
			CmsComment bean = manager.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.setRecommend(isRecommend);
				manager.update(bean, bean.getCommentExt());
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/comment/reply")
	public void reply(Integer id,String reply,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id,reply);
		if (!errors.hasErrors()) {
			CmsComment bean = manager.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.getCommentExt().setReply(reply);
				manager.update(bean, bean.getCommentExt());
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	

	private JSONObject createCommentJson(CmsComment bean) {
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(bean.getText())) {
			json.put("text", bean.getText());
		}else{
			json.put("text", "");
		}
		if (bean.getChecked()!=null) {
			json.put("checked", bean.getChecked());
		}else{
			json.put("checked", "");
		}
		if (bean.getCommentUser()!=null&&StringUtils.isNotBlank(bean.getCommentUser().getUsername())) {
			json.put("commenteUsername", bean.getCommentUser().getUsername());
		}else{
			json.put("commenteUsername", "");
		}
		if (bean.getCreateTime()!=null) {
			json.put("createTime",DateUtils.parseDateToTimeStr(bean.getCreateTime()));
		}else{
			json.put("createTime", "");
		}
		if (StringUtils.isNotBlank(bean.getIp())) {
			json.put("ip", bean.getIp());
		}else{
			json.put("ip", "");
		}
		if (bean.getContent()!=null && bean.getContent().getId()!=null) {
			json.put("contentId", bean.getContent().getId());
		}else{
			json.put("contentId", "");
		}
		if (bean.getContent()!=null && StringUtils.isNotBlank(bean.getContent().getTitle())) {
			json.put("contentTitle", bean.getContent().getTitle());
		}else{
			json.put("contentTitle", "");
		}
		if (bean.getContent()!=null && bean.getContent().getChannel()!=null && StringUtils.isNotBlank(bean.getContent().getChannel().getName())) {
			json.put("channelName", bean.getContent().getChannel().getName());
		}else{
			json.put("channelName", "");
		}
		if (bean.getContent()!=null && StringUtils.isNotBlank(bean.getContent().getUrl())) {
			json.put("contentURL", bean.getContent().getUrl());
		}else{
			json.put("contentURL", "");
		}
		if (bean.getCommentExt()!=null&&bean.getCommentExt().getId()!=null) {
			json.put("replyId", bean.getCommentExt().getId());
		}else{
			json.put("replyId", "");
		}
		if (bean.getCommentExt()!=null&&StringUtils.isNotBlank(bean.getCommentExt().getReply())) {
			json.put("replyContent", bean.getCommentExt().getReply());
		}else{
			json.put("replyContent", "");
		}
		if (bean.getRecommend()!=null) {
			json.put("recommend", bean.getRecommend());
		}else{
			json.put("recommend", "");
		}
		return json;
	}


	private WebErrors validateUpdate(WebErrors errors, Integer id, HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateExist(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids", false)) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsComment entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsComment.class, id, false)) {
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
	private CmsCommentMng manager;
}
