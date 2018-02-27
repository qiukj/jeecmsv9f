package com.jeecms.cms.api.admin.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.annotation.SignValidate;
import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.entity.CmsUserSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsAdminGlobalApiAct extends CmsAdminAbstractApi{
	private static final Logger log = LoggerFactory
			.getLogger(CmsAdminGlobalApiAct.class);
	
	@RequestMapping("/admin/getSteps")
	public void steps(HttpServletRequest request,HttpServletResponse response){
		CmsUser user = CmsUtils.getUser(request);
		Integer currSiteId=CmsUtils.getSiteId(request);
		Set<CmsUserSite> set = user.getUserSites();
		JSONArray jsonArray = new JSONArray();
		Byte maxStep=0;
		if (set!=null && set.size()>0) {
			for (CmsUserSite userSite : set) {
				if(userSite.getSite().getId().equals(currSiteId)){
					maxStep=userSite.getCheckStep();
				}
			}
		}
		if (maxStep>0) {
			for(byte i=0;i<maxStep;i++){
				jsonArray.put(i,i);
			}
		}else{
			jsonArray.put(0);
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 管理员全站(列表)
	 * @param queryUsername
	 * @param queryEmail
	 * @param queryGroupId
	 * @param queryStatu
	 * @param https
	 * @param queryRealName
	 * @param queryRoleId
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admin/global_list")
	public void list(String queryUsername, String queryEmail,
			Integer queryGroupId, Integer queryStatu, Integer https,
			String queryRealName,Integer queryRoleId,Integer pageNo,
			Integer pageSize,HttpServletRequest request,HttpServletResponse response){
		CmsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = manager.getPage(queryUsername, queryEmail, null, queryGroupId, queryStatu, true, 
				user.getRank(), queryRealName, queryRoleId, null, null, pageNo, pageSize);
		int totalCount = page.getTotalCount();
		List<CmsUser> list = (List<CmsUser>) page.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(site, https,null));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 管理员全站详情
	 * @param id
	 * @param https
	 * @param response
	 * @param request
	 */
	@RequestMapping("/admin/global_get")
	public void get(Integer id,Integer https,HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsUser bean = null;
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsUser();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson(site, https,null).toString();
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
	 * 添加管理员(全站)信息
	 * @param bean
	 * @param ext
	 * @param username
	 * @param email
	 * @param password
	 * @param selfAdmin
	 * @param rank
	 * @param groupId
	 * @param roleIds
	 * @param channelIds
	 * @param siteIds
	 * @param steps
	 * @param allChannels
	 * @param allControlChannels
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/admin/global_save")
	public void save(CmsUser bean,CmsUserExt ext,String username,String email,
			String password,Boolean selfAdmin,Integer rank,Integer groupId,Integer departmentId,
			String roleIds,String channelIds, String source,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,username,password,groupId,rank,selfAdmin);
		if (!errors.hasErrors()) {
			CmsUser user = manager.findByUsername(username);
			if (user!=null) {
				message = Constants.API_MESSAGE_USERNAME_EXIST;
				code = ResponseCode.API_CODE_USERNAME_EXIST;
			}else{
				Integer[] roleIdArr = StrUtils.getInts(roleIds);
				Integer[] channelIdArr = StrUtils.getInts(channelIds);
				List<Map<String, Object>> list = getSiteChannel(source);
				Integer[] siteArr = getSiteArr(list);
				Byte[] stepArr = getStepArr(list);
				Boolean[] allChannelArr = getChannelArr(list, "allChannels");
				errors = validateSave(errors,siteArr,stepArr,allChannelArr);
				if (errors.hasErrors()) {
					message = errors.getErrors().get(0);
					code = errors.getErrors().get(1);
				}else{
					bean.init();
					String ip = RequestUtils.getIpAddr(request);
					bean = manager.saveAdmin(username, email, password, ip, false,
							selfAdmin, rank, groupId, roleIdArr, channelIdArr, siteArr, stepArr,
							allChannelArr,ext);
					cmsWebserviceMng.callWebService("true",username, password, email, ext,CmsWebservice.SERVICE_TYPE_ADD_USER);
					log.info("save CmsAdmin id={}", bean.getId());
					cmsLogMng.operating(request, "cmsUser.log.save", "id=" + bean.getId()
							+ ";username=" + bean.getUsername());
					body = "{\"id\":\""+bean.getId()+"\"}";
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改管理员(全站)信息
	 * @param bean
	 * @param ext
	 * @param password
	 * @param groupId
	 * @param roleIds
	 * @param channelIds
	 * @param siteIds
	 * @param steps
	 * @param allChannels
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/admin/global_update")
	public void update(CmsUser bean,CmsUserExt ext,String password,
			Integer groupId,
			String roleIds,String channelIds, String siteIds,
			String source,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(), groupId,bean.getRank(),bean.getSelfAdmin());
		if (!errors.hasErrors()) {
			Integer[] roleIdArr = StrUtils.getInts(roleIds);
			Integer[] channelIdArr = StrUtils.getInts(channelIds);
			List<Map<String, Object>> list = getSiteChannel(source);
			Integer[] siteArr = getSiteArr(list);
			Byte[] stepArr = getStepArr(list);
			Boolean[] allChannelArr = getChannelArr(list, "allChannels");
			errors= validateUpdate(errors,bean.getId(),bean.getRank(),request);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				bean = manager.updateAdmin(bean, ext, password, groupId,roleIdArr, channelIdArr,
						siteArr, stepArr, allChannelArr);
				cmsWebserviceMng.callWebService("true",bean.getUsername(), password, null, ext,CmsWebservice.SERVICE_TYPE_UPDATE_USER);
				log.info("update CmsAdmin id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsUser.log.update", "id=" + bean.getId()
						+ ";username=" + bean.getUsername());
				body = "{\"id\":\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 删除管理员(全站)信息
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/admin/global_delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors,idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_CALL_FAIL;
			}else{
				try {
					CmsUser[] beans = manager.deleteByIds(idArr);
					CmsUser user =CmsUtils.getUser(request);
					boolean deleteCurrentUser=false;
					for (CmsUser bean : beans) {
						Map<String,String>paramsValues=new HashMap<String, String>();
						paramsValues.put("username", bean.getUsername());
						paramsValues.put("admin", "true");
						cmsWebserviceMng.callWebService(CmsWebservice.SERVICE_TYPE_DELETE_USER, paramsValues);
						log.info("delete CmsAdmin id={}", bean.getId());
						if(user.getUsername().equals(bean.getUsername())){
							deleteCurrentUser=true;
						}else{
							cmsLogMng.operating(request, "cmsUser.log.delete", "id="
									+ bean.getId() + ";username=" + bean.getUsername());
						}
					}
					if(deleteCurrentUser){
						 Subject subject = SecurityUtils.getSubject();
						 subject.logout();
					}
					body = "{\"currentUser\":"+deleteCurrentUser+"}";
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					e.printStackTrace();
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/admin/val_rank")
	public void valdateRank(Integer id,Integer rank,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		boolean result = false;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors,rank);
		if (!errors.hasErrors()) {
			CmsUser user = CmsUtils.getUser(request);
			if (id==null) {//添加不需要id
				if (rank <= user.getRank()) {
					result = true;
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				CmsUser entity = manager.findById(id);
				if (entity==null) {
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}else{
					if (rank <= user.getRank() && entity.getRank() <= user.getRank()) {
						result = true;
					}
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}
		}
		body = "{\"result\":"+result+"}";
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Integer[] getSiteArr(List<Map<String, Object>> list){
		if (list!=null && list.size()>0) {
			Integer[] siteArr=new Integer[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> sub = list.get(i);
				siteArr[i] = (Integer)sub.get("siteIds");
			}
			return siteArr;
		}else{
			return null;
		}
	}
	
	private Byte[] getStepArr(List<Map<String, Object>> list){
		if (list!=null && list.size()>0) {
			Byte[] stepArr=new Byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> sub = list.get(i);
				stepArr[i] = Byte.valueOf(sub.get("steps").toString());
			}
			return stepArr;
		}else{
			return null;
		}
	}
	
	private Boolean[] getChannelArr(List<Map<String, Object>> list,String attr){
		if (list!=null && list.size()>0) {
			Boolean[] arr=new Boolean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> sub = list.get(i);
				arr[i] = (Boolean)sub.get(attr);
			}
			return arr;
		}else{
			return null;
		}
	}
	
	private List<Map<String, Object>> getSiteChannel(String source) {
		JSONArray jsonArray = new JSONArray(source);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (jsonArray!=null && jsonArray.length()>0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = (JSONObject) jsonArray.get(i);
				Map<String, Object> sub = new HashMap<String, Object>();
				sub.put("siteIds", json.get("siteIds"));
				sub.put("steps", json.get("steps"));
				sub.put("allChannels", json.get("allChannels"));
				list.add(sub);
			}
		}
		return list;
	}
	
	private WebErrors validateDelete(WebErrors errors, Integer[] idArr) {
		if (idArr!=null) {
			for (int i = 0; i < idArr.length; i++) {
				if (vldExist(idArr[i], errors)) {
					return errors;
				}
			}
		}
		return errors;
	}

	private WebErrors validateUpdate(WebErrors errors, Integer id, Integer rank,HttpServletRequest request) {
		if (vldExist(id, errors)) {
			return errors;
		}
		if (vldParams(id,rank, request, errors)) {
			return errors;
		}
		// TODO 检查管理员rank
		return errors;
	}
	
	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id", false)) {
			return true;
		}
		CmsUser entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsUser.class, id, false)) {
			return true;
		}
		return false;
	}
	
	private boolean vldParams(Integer id,Integer rank, HttpServletRequest request,
			WebErrors errors) {
		CmsUser user = CmsUtils.getUser(request);
		CmsUser entity = manager.findById(id);
		//提升等级大于当前登录用户
		if (rank > user.getRank()) {
			errors.addErrorString("error.noPermissionToRaiseRank");
			return true;
		}
		//修改的用户等级大于当前登录用户 无权限
		if (entity.getRank() > user.getRank()) {
			errors.addErrorString(Constants.API_MESSAGE_USER_NOT_HAS_PERM);
			return true;
		}
		return false;
	}

	private WebErrors validateSave(WebErrors errors,Integer[] siteArr, Byte[] stepArr,
			Boolean[] allChannelArr){
		if (siteArr!=null) {
			if (stepArr==null) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
				errors.addErrorString(ResponseCode.API_CODE_PARAM_REQUIRED);
				return errors;
			}
			if (allChannelArr == null) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
				errors.addErrorString(ResponseCode.API_CODE_PARAM_REQUIRED);
				return errors;
			}
			if (siteArr.length != stepArr.length
					|| stepArr.length != allChannelArr.length) {
				errors.addErrorString(Constants.API_MESSAGE_PARAM_ERROR);
				errors.addErrorString(ResponseCode.API_CODE_PARAM_ERROR);
				return errors;
			}
		}
		return errors;
	}
}
