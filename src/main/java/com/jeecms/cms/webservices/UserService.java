/**
*	TOM
*/
package com.jeecms.cms.webservices;



import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.jeecms.cms.manager.assist.CmsWebserviceAuthMng;
import com.jeecms.cms.manager.assist.CmsWebserviceCallRecordMng;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserExt;
import com.jeecms.core.manager.CmsGroupMng;
import com.jeecms.core.manager.CmsUserMng;


public class UserService  extends SpringBeanAutowiringSupport{
	private static final String SERVICE_CODE_USER_DELETE="user_delete";
	private static final String SERVICE_CODE_USER_ADD="user_add";
	private static final String SERVICE_CODE_USER_UPDATE="user_update";
	private static final String RESPONSE_CODE_SUCCESS="100";
	private static final String RESPONSE_CODE_AUTH_ERROR="101";
	private static final String RESPONSE_CODE_PARAM_REQUIRED="102";
	private static final String RESPONSE_CODE_USER_NOT_FOUND="103";
	private static final String RESPONSE_CODE_USER_ADD_ERROR="104";
	private static final String RESPONSE_CODE_USER_UPDATE_ERROR="105";
	private static final String RESPONSE_CODE_USER_DELETE_ERROR="106";
	private static final String LOCAL_IP="127.0.0.1";
	
	public String addUser(String auth_username,String auth_password,String admin,String username
			,String password,String email,String realname,String sex,String tel,String groupId,
			String rank,String departmentId,String role,String site,String allChannel,String allControlChannel) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				if(StringUtils.isBlank(admin)){
					admin="false";
				}
				try {
					CmsUserExt userExt=new CmsUserExt();
					userExt.setRealname(realname);
					if(StringUtils.isNotBlank(sex)){
						if(sex.equals("true")){
							userExt.setGender(true);
						}else if(sex.equals("false")){
							userExt.setGender(false);
						}
					}
					userExt.setMobile(tel);
					CmsGroup group=null;
					if(StringUtils.isNotBlank(groupId)){
						Integer gid=Integer.parseInt(groupId);
						group= cmsGroupMng.findById(gid);
					}
					if(group==null){
						group=cmsGroupMng.getRegDef();
					}
					if(admin.equals("false")){
						cmsUserMng.registerMember(username, email, password, LOCAL_IP, group.getId(), null, false, userExt, null);
					}else if(admin.equals("true")){
						Integer rankInt=1;
						if(StringUtils.isNotBlank(rank)){
							rankInt=Integer.parseInt(rank);
						}
						Integer[]roleIds = null;
						if(StringUtils.isNotBlank(role)){
							String roles[]=role.split(",");
							roleIds=new Integer[roles.length];
							for(int i=0;i<roles.length;i++){
								roleIds[i]=Integer.parseInt(roles[i]);
							}
						}
						Integer siteIds[]=null;
						Byte steps[]=null;
						Boolean allChannels[]=null;
						Boolean allControlChannels[]=null;
						if(StringUtils.isNotBlank(site)){
							String sites[]=site.split(",");
							String allChannelArray[]=allChannel.split(",");
							String allControlChannelArray[]=allControlChannel.split(",");
							siteIds=new Integer[sites.length];
							steps=new Byte[sites.length];
							allChannels=new Boolean[sites.length];
							allControlChannels=new Boolean[sites.length];
							for(int i=0;i<sites.length;i++){
								siteIds[i]=Integer.parseInt(sites[i]);
								steps[i]=1;
								allChannels[i]=Boolean.parseBoolean(allChannelArray[i]);
								allControlChannels[i]=Boolean.parseBoolean(allControlChannelArray[i]);
							}
						}
						cmsUserMng.saveAdmin(username, email, password, LOCAL_IP, false, false, rankInt, group.getId(), roleIds, null, siteIds, steps, allChannels, userExt);
					}
					responseCode=RESPONSE_CODE_SUCCESS;
					webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_ADD);
				} catch (Exception e) {
					responseCode=RESPONSE_CODE_USER_ADD_ERROR;
				}
			}
		}
		return responseCode;
	}
	
	public String updateUser(String auth_username,String auth_password,
			String username,String password,String email,String realname,String sex,String tel,
			String groupId) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				CmsUser user=cmsUserMng.findByUsername(username);
				if(user!=null){
					try {
						Boolean gender=null;
						if(StringUtils.isNotBlank(sex)){
							if(sex.equals("true")){
								gender=true;
							}else if(sex.equals("false")){
								gender=false;
							}
						}
						CmsGroup group=null;
						Integer groupInt=null;
						if(StringUtils.isNotBlank(groupId)){
							Integer gid=Integer.parseInt(groupId);
							group= cmsGroupMng.findById(gid);
							if(group!=null){
								groupInt=gid;
							}
						}
						cmsUserMng.updateMember(user.getId(), email, password, groupInt, realname, tel, gender);
						responseCode=RESPONSE_CODE_SUCCESS;
						webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_UPDATE);
					} catch (Exception e) {
						e.printStackTrace();
						responseCode=RESPONSE_CODE_USER_UPDATE_ERROR;
					}
				}else{
					responseCode=RESPONSE_CODE_USER_NOT_FOUND;
				}
			}
		}
		return responseCode;
	}
	
	public String delUser(String auth_username,String auth_password,String username) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isNotBlank(username)){
				CmsUser user=cmsUserMng.findByUsername(username);
				if(user!=null){
					try{
						cmsUserMng.deleteById(user.getId());
						responseCode=RESPONSE_CODE_SUCCESS;
						webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_DELETE);
					} catch (Exception e) {
						responseCode=RESPONSE_CODE_USER_DELETE_ERROR;
					}
				}else{
					responseCode=RESPONSE_CODE_USER_NOT_FOUND;
				}
			}else{
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}
		}
		return responseCode;
	}
	
	private boolean validate(String username,String password){
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return false;
		}else{
			return cmsWebserviceAuthMng.isPasswordValid(username, password);
		}
	}
	
	@Autowired
	private CmsUserMng cmsUserMng;
	@Autowired
	private CmsGroupMng cmsGroupMng;
	@Autowired
	private CmsWebserviceAuthMng cmsWebserviceAuthMng;
	@Autowired
	private CmsWebserviceCallRecordMng webserviceCallRecordMng;
}

