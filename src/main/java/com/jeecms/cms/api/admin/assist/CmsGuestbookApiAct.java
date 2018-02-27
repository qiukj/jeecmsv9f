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
import com.jeecms.cms.entity.assist.CmsGuestbook;
import com.jeecms.cms.entity.assist.CmsGuestbookCtg;
import com.jeecms.cms.entity.assist.CmsGuestbookExt;
import com.jeecms.cms.manager.assist.CmsGuestbookCtgMng;
import com.jeecms.cms.manager.assist.CmsGuestbookMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsGuestbookApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsGuestbookApiAct.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/guestbook/list")
	public void list(Integer queryCtgId, Boolean queryRecommend,
			Short queryChecked, Integer pageNo,Integer pageSize,
			HttpServletResponse response,HttpServletRequest request){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		CmsSite site = CmsUtils.getSite(request);
		Integer[] ctgIds;
		List<CmsGuestbookCtg> ctgs=cmsGuestbookCtgMng.getList(site.getId());
		ctgIds=CmsGuestbookCtg.fetchIds(ctgs);
		Pagination page = manager.getPage(site.getId(),queryCtgId,ctgIds,null,
				queryRecommend, queryChecked, true, false, pageNo,pageSize);
		int totalCount = page.getTotalCount();
		List<CmsGuestbook> list = (List<CmsGuestbook>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
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
	
	@RequestMapping("/guestbook/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsGuestbook bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsGuestbook();
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
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/save")
	public void save(CmsGuestbook bean, CmsGuestbookExt ext, Integer ctgId,
			HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ctgId);
		if (!errors.hasErrors()) {
			CmsGuestbookCtg ctg = cmsGuestbookCtgMng.findById(ctgId);
			if (ctg==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				if (bean.getSite()==null) {
					bean.setSite(CmsUtils.getSite(request));
				}
				bean.init();
				String ip = RequestUtils.getIpAddr(request);
				bean = manager.save(bean, ext, ctgId, ip);
				log.info("save CmsGuestbook id={}", bean.getId());
				cmsLogMng.operating(request, "cmsGuestbook.log.save", "id="
						+ bean.getId() + ";title=" + bean.getTitle());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/update")
	public void update(CmsGuestbook bean, CmsGuestbookExt ext, Integer ctgId,String oldreply,
			HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ctgId,bean.getId());
		if (!errors.hasErrors()) {
			CmsGuestbookCtg ctg = cmsGuestbookCtgMng.findById(ctgId);
			CmsGuestbook guestbook = manager.findById(bean.getId());
			if (ctg==null||guestbook==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				Date now=new Date();
				if(StringUtils.isNotBlank(ext.getReply())&&!ext.getReply().equals(oldreply)){
					bean.setReplayTime(now);
					if(bean.getAdmin()!=null){
						if(!bean.getAdmin().equals(CmsUtils.getUser(request))){
							bean.setAdmin(CmsUtils.getUser(request));
						}
					}else{
						bean.setAdmin(CmsUtils.getUser(request));
					}
				}
				bean = manager.update(bean, ext, ctgId);
				log.info("update CmsGuestbook id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsGuestbook.log.update", "id="
						+ bean.getId() + ";title=" + bean.getTitle());
				body = "{\"id\":"+bean.getId()+"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateArr(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				CmsGuestbook[] beans = manager.deleteByIds(idArr);
				for (CmsGuestbook bean : beans) {
					log.info("delete CmsGuestbook id={}", bean.getId());
					cmsLogMng.operating(request, "cmsGuestbook.log.delete", "id="
							+ bean.getId() + ";title=" + bean.getTitle());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/check")
	public void check(String ids,Short isCheck,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids,isCheck);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateArr(idArr, request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}else{
				CmsGuestbook[] beans = manager.checkByIds(idArr,CmsUtils.getUser(request),isCheck);
				for (CmsGuestbook bean : beans) {
					log.info("check CmsGuestbook id={}", bean.getId());
					log.info("cancelCheck CmsGuestbook id={}", bean.getId());
					cmsLogMng.operating(request, "cmsGuestbook.log.check", "id="
							+ bean.getId() + ";title=" + bean.getTitle());
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/recommend")
	public void recommend(Integer id,Boolean isRecommend,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id,isRecommend);
		if (!errors.hasErrors()) {
			CmsGuestbook bean = manager.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.setRecommend(isRecommend);
				manager.update(bean, bean.getExt(), bean.getCtg().getId());
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/guestbook/reply")
	public void replay(Integer id,String reply,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, id,reply);
		if (!errors.hasErrors()) {
			CmsGuestbook bean = manager.findById(id);
			if (bean==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				bean.getExt().setReply(reply);
				Date now = new Date();
				bean.setReplayTime(now);
				bean.setAdmin(CmsUtils.getUser(request));
				manager.update(bean, bean.getExt(), bean.getCtg().getId());
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateArr(Integer[] ids, HttpServletRequest request) {
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
		CmsGuestbook entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsGuestbook.class, id, false)) {
			return true;
		}
		if (!entity.getSite().getId().equals(siteId)) {
			errors.addErrorString("error.notInSite");
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsGuestbookCtgMng cmsGuestbookCtgMng;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsGuestbookMng manager;
}
