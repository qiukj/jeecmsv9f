package com.jeecms.cms.api.admin.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.json.JSONArray;
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

import net.sf.json.JSONObject;

@Controller
public class CmsAdminLocalApiAct extends CmsAdminAbstractApi{
	private static final Logger log = LoggerFactory
			.getLogger(CmsAdminLocalApiAct.class);
	/**
	 * 获取本站所有管理员id和username
	 * @param request
	 * @param response
	 */
	@RequestMapping("/admin/local_all")
	public void allUser(HttpServletRequest request,HttpServletResponse response){
		List<CmsUser> list = manager.getAdminList(CmsUtils.getSiteId(request), null, null, null);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,createEasyJson(list.get(i)));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 管理员(本站)列表信息
	 * @param queryUsername
	 * @param queryEmail
	 * @param queryGroupId
	 * @param queryStatu
	 * @param queryRealName
	 * @param queryRoleId
	 * @param queryAllChannel
	 * @param queryAllControlChannel
	 * @param https
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/admin/local_list")
	public void list(String queryUsername, String queryEmail,
			Integer queryGroupId, Integer queryStatu, 
			String queryRealName,Integer queryRoleId,
			Boolean queryAllChannel,Boolean queryAllControlChannel,Integer https,
			Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		if (pageNo==null) {
			pageNo = 1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (https==null) {
			https=Constants.URL_HTTP;
		}
		Pagination pagination = manager.getPage(queryUsername, queryEmail, site
				.getId(), queryGroupId, queryStatu, true,
				user.getRank(), queryRealName,queryRoleId,queryAllChannel,
				queryAllControlChannel,pageNo,pageSize);
		int totalCount = pagination.getTotalCount();
		List<CmsUser> list = (List<CmsUser>) pagination.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i <list.size(); i++){
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
	 * 管理员(本站)详情
	 * @param id
	 * @param https
	 * @param request
	 * @param response
	 */
	@RequestMapping("/admin/local_get")
	public void get(Integer id,Integer https,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsUser bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsUser();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				bean.init();
				body = bean.convertToJson(CmsUtils.getSite(request), https,true).toString();
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
	 * 添加管理员(本站)
	 * @param bean
	 * @param ext
	 * @param username
	 * @param email
	 * @param password
	 * @param selfAdmin
	 * @param rank
	 * @param groupId
	 * @param departmentId
	 * @param roleIds
	 * @param channelIds
	 * @param steps
	 * @param allChannels
	 * @param allControlChannels
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/admin/local_save")
	public void save(CmsUser bean,CmsUserExt ext,String username,String email,String password,
			Boolean selfAdmin,Integer rank,Integer groupId,String roleIds,
			String channelIds,String steps,String allChannels,String allControlChannels,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, username,password,
				groupId,bean.getPriority(),
				selfAdmin);
		if (!errors.hasErrors()) {
			CmsUser user = manager.findByUsername(username);
			if (user!=null) {
				message = Constants.API_MESSAGE_USERNAME_EXIST;
				code = ResponseCode.API_CODE_USERNAME_EXIST;
			}else{
				CmsSite site = CmsUtils.getSite(request);
				Integer[] siteIds = new Integer[] { site.getId() };
				String ip = RequestUtils.getIpAddr(request);
				bean.init();
				Integer[] roleIdArr = StrUtils.getInts(roleIds);
				Integer[] channelIdArr = StrUtils.getInts(channelIds);
				Byte[] stepArr = strToByte(steps);
				Boolean[] allChannelArr = strToBoolean(allChannels);
				Boolean[] allControlChannelArr = strToBoolean(allControlChannels);
				if (siteIds!=null&&siteIds.length>0) {
					if (stepArr==null) {
						stepArr = new Byte[siteIds.length];
						for (int i = 0; i < stepArr.length; i++) {
							stepArr[i]=0;
						}
					}
					if (allChannelArr==null) {
						allChannelArr = new Boolean[siteIds.length];
						for (int i = 0; i < allChannelArr.length; i++) {
							allChannelArr[i] = false;
						}
					}
					if (allControlChannelArr==null) {
						allControlChannelArr = new Boolean[siteIds.length];
						for (int i = 0; i < allControlChannelArr.length; i++) {
							allControlChannelArr[i] = false;
						}
					}
				}
				bean = manager.saveAdmin(username, email, password, ip, false,
						selfAdmin, rank, groupId, roleIdArr,channelIdArr, siteIds,
						stepArr, allChannelArr, ext);
				cmsWebserviceMng.callWebService("true",username, password, email, ext,CmsWebservice.SERVICE_TYPE_ADD_USER);
				log.info("save CmsAdmin id={}", bean.getId());
				cmsLogMng.operating(request, "cmsUser.log.save", "id=" + bean.getId()
						+ ";username=" + bean.getUsername());
				body = "{\"id\":\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/admin/local_update")
	public void update(CmsUser bean,CmsUserExt ext,String password,Integer groupId,
			String roleIds,String channelIds,Byte steps,Boolean allChannels,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		errors = ApiValidate.validateRequiredParams(request, errors,bean.getId(),
				groupId,bean.getRank(),bean.getSelfAdmin(),
				steps,allChannels);
		if (!errors.hasErrors()) {
			CmsUser user = manager.findById(bean.getId());
			if (user==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else{
				Integer[] roleIdArr = StrUtils.getInts(roleIds);
				Integer[] channelIdArr = StrUtils.getInts(channelIds);
				bean = manager.updateAdmin(bean, ext, password, groupId,
						roleIdArr,channelIdArr, site.getId(), steps, allChannels);
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
	
	@SignValidate
	@RequestMapping("/admin/local_delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArr = StrUtils.getInts(ids);
			errors = validateDelete(errors, idArr);
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_NOT_FOUND;
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
	
	@RequestMapping("/admin/userSite")
	public void userSite(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			Integer siteId = CmsUtils.getSiteId(request);
			CmsUser user = manager.findById(id);
			if (user!=null) {
				CmsUserSite userSite = user.getUserSite(siteId);
				if (userSite!=null) {
					body = userSite.convertToJson().toString();
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_PARAM_ERROR;
					code = ResponseCode.API_CODE_PARAM_ERROR;
				}
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateDelete(WebErrors errors , Integer [] ids){
		if (ids!=null) {
			for (int i = 0; i < ids.length; i++) {
				vldExist(ids[i], errors);
			}
		}
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
	
	
	private Boolean[] strToBoolean(String str){
		if (StringUtils.isNotBlank(str)) {
			String[] split = str.split(",");
			Boolean[] arr = new Boolean[split.length];
			for(int i =0; i<split.length;i++){
				arr[i] = Boolean.parseBoolean(split[i]);
			}
			return arr;
		}else{
			return null;
		}
	}
	
	private JSONObject createEasyJson(CmsUser bean){
		JSONObject json = new JSONObject();
		if (bean.getId()!=null) {
			json.put("id", bean.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(bean.getUsername())) {
			json.put("username", bean.getUsername());
		}else{
			json.put("username", "");
		}
		return json;
	}
	
	private Byte[] strToByte(String str){
		if (StringUtils.isNotBlank(str)) {
			String[] split = str.split(",");
			Byte[] arr = new Byte[split.length];
			for(int i =0; i<split.length;i++){
				arr[i] = Byte.parseByte(split[i]);
			}
			return arr;
		}else{
			return null;
		}
	}
}
