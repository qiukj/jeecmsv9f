package com.jeecms.cms.api.admin.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.jeecms.cms.manager.main.ApiUserLoginMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsRole;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsLogMng;
import com.jeecms.core.manager.CmsRoleMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.security.CmsAuthorizingRealm;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsRoleApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsRoleApiAct.class);
	
	@RequestMapping(value = "/user/getPerms")
	public void getUserPerms(Integer https,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		ApiAccount apiAccount = apiAccountMng.getApiAccount(request);
		if (apiAccount!=null) {
			if (apiAccount.getDisabled()) {
				message=Constants.API_MESSAGE_ACCOUNT_DISABLED;
				code = ResponseCode.API_CODE_ACCOUNT_DISABLED;
			}else{
				CmsUser user = apiUserLoginMng.getUser(apiAccount, request);
				if(user!=null){
					//JSONObject json = user.convertToJson(site, https);
					JSONObject json = new JSONObject();
					if (user.getAdmin()) {
						json.put("perms", user.getPermStr());
					}else{
						json.put("perms", "");
					}
					json.put("siteId", site.getId());
					json.put("isMasterSite", false);
					JSONArray jsonArray = new JSONArray();
					int index = 0;
					for (CmsSite cmsSite : user.getSites()) {
						JSONObject siteJson = new JSONObject();
						if (cmsSite.getId()!=null) {
							siteJson.put("id", cmsSite.getId());
						}else{
							siteJson.put("id", "");
						}
						if (StringUtils.isNotBlank(cmsSite.getName())) {
							siteJson.put("name", cmsSite.getName());
						}else{
							siteJson.put("name", "");
						}
						jsonArray.put(index,siteJson);
						index++;
					}
					json.put("sites", jsonArray);
					body=json.toString();
					message=Constants.API_MESSAGE_SUCCESS;
				}else{
					//用户不存在
					message=Constants.API_MESSAGE_USER_NOT_LOGIN;
					code=ResponseCode.API_CODE_USER_NOT_LOGIN;
				}
			}
		}else{
			//API账户错误
			message=Constants.API_MESSAGE_APP_PARAM_ERROR;
			code=ResponseCode.API_CODE_APP_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/role/list")
	public void list(Integer level,HttpServletRequest request,HttpServletResponse response){
		List<CmsRole> list = manager.getList(level);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i <list.size();i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String body = jsonArray.toString();
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/role/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsRole bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsRole();
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
	@RequestMapping("/role/save")
	public void save(CmsRole bean,String perms,
			HttpServletResponse response,HttpServletRequest request){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsUser user=CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(request, errors, 
				bean.getName(),bean.getPriority(),bean.getLevel(),
				bean.getAll());
		CmsRole currUserTopRole=user.getTopRole();
		Integer currUserTopRoleLevel=10;
		if(currUserTopRole!=null){
			currUserTopRoleLevel= currUserTopRole.getLevel();
		}
		if(bean.getLevel()>currUserTopRoleLevel){
			message = Constants.API_MESSAGE_ROLE_LEVEL_ERROR;
			code = ResponseCode.API_CODE_ROLE_LEVEL_ERROR;
		}else{
			if (!errors.hasErrors()) {
				bean.init();
				bean = manager.save(bean, splitPerms(perms));
				log.info("save CmsRole id={}", bean.getId());
				cmsLogMng.operating(request, "cmsRole.log.save", "id=" + bean.getId()
						+ ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/role/update")
	public void update(CmsRole bean,String perms,boolean all,
			HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, bean.getId(),
				bean.getName(),bean.getPriority(),bean.getLevel(),
				bean.getAll());
		CmsUser user=CmsUtils.getUser(request);
		CmsRole currUserTopRole=user.getTopRole();
		Integer currUserTopRoleLevel=10;
		if(currUserTopRole!=null){
			currUserTopRoleLevel= currUserTopRole.getLevel();
		}
		if(bean.getLevel()>currUserTopRoleLevel){
			message = Constants.API_MESSAGE_ROLE_LEVEL_ERROR;
			code = ResponseCode.API_CODE_ROLE_LEVEL_ERROR;
		}else{
			if (!errors.hasErrors()) {
				bean = manager.update(bean, splitPerms(perms));
				String[] split = null;
				if (perms!=null) {
					split = perms.split(",");
				}
				if (hasChangePermission(all, split, bean)) {
					Set<CmsUser> admins = bean.getUsers();
					for (CmsUser admin : admins) {
						authorizingRealm.removeUserAuthorizationInfoCache(admin.getUsername());
					}
				}
				log.info("update CmsRole id={}.", bean.getId());
				cmsLogMng.operating(request, "cmsRole.log.update", "id=" + bean.getId()
						+ ";name=" + bean.getName());
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/role/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsRole[] beans = manager.deleteByIds(idArray);
				for (CmsRole bean : beans) {
					log.info("delete CmsRole id={}", bean.getId());
					cmsLogMng.operating(request, "cmsRole.log.delete", "id="
							+ bean.getId() + ";name=" + bean.getName());
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/role/member_list")
	public void memberList(Integer https,Integer roleId,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		Pagination pagination = userMng.getAdminsByRoleId(roleId, pageNo, pageSize);
		int totalCount = pagination.getTotalCount();
		List<CmsUser> list = (List<CmsUser>) pagination.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i =0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson(site, https,null));
			}
		}
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(request, body, message, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/role/member_delete")
	public void memberDelete(Integer roleId,String userIds,HttpServletRequest request,
			HttpServletResponse response){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(request, errors, roleId, userIds);
		if (!errors.hasErrors()) {
			CmsRole role = manager.findById(roleId);
			if (role!=null) {
				Integer[] idArray = StrUtils.getInts(userIds);
				manager.deleteMembers(role, idArray);
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
	
	private boolean hasChangePermission(boolean all,String[] perms,CmsRole bean){
		CmsRole role = manager.findById(bean.getId());
		if (!role.getAll().equals(all)) {
			return true;
		}
		if (!bean.getPerms().toArray().equals(perms)) {
			return true;
		}
		return false;
	}
	
	private Set<String> splitPerms(String perms) {
		Set<String> set = new HashSet<String>();
		if (perms != null) {
			for (String p : StringUtils.split(perms, ',')) {
				if (!StringUtils.isBlank(p)) {
					if (p.startsWith("/api/admin")) {
						p=p.substring(10);
					}
					set.add(p);
				}
			}
		}
		return set;
	}
	
	@Autowired
	private CmsUserMng userMng;
	@Autowired
	private CmsAuthorizingRealm authorizingRealm;
	@Autowired
	private CmsLogMng cmsLogMng;
	@Autowired
	private CmsRoleMng manager;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
}
