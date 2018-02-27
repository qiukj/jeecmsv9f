package com.jeecms.cms.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.ApiValidate;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.cms.entity.assist.CmsGuestbook;
import com.jeecms.cms.entity.assist.CmsGuestbookCtg;
import com.jeecms.cms.manager.assist.CmsGuestbookCtgMng;
import com.jeecms.cms.manager.assist.CmsGuestbookMng;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;

@Controller
public class CmsGuestbookApiAct {
	/**
	 * 留言发布API
	 * @param siteId 站点ID 非必选 默认当前站
	 * @param ctgId  分类ID 必选
	 * @param title 标题 必选
	 * @param content 内容 必选
	 * @param email 邮箱 非必选
	 * @param phone 电话 非必选
	 * @param qq qq号 非必选
	 */
	@RequestMapping(value = "/guestbook/save")
	public void guestbookSave(
			Integer siteId, Integer ctgId, String title,
			String content, String email, String phone, String qq,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		CmsUser user = CmsUtils.getUser(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(request,errors,ctgId,title,content);
		if(!errors.hasErrors()){
			CmsGuestbookCtg ctg=cmsGuestbookCtgMng.findById(ctgId);
			if(ctg!=null){
				String ip = RequestUtils.getIpAddr(request);
				if(siteId==null){
					siteId=CmsUtils.getSiteId(request);
				}
				CmsGuestbook guestbook=cmsGuestbookMng.save(user, siteId, 
						ctgId, ip, title, content, email,phone, qq);
				body="{\"id\":"+"\""+guestbook.getId()+"\"}";
				message=Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message=Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 我的留言API
	 * @param siteId 站点ID   非必选
	 * @param ctgId 分类ID  非必选
	 * @param appId appId 必选
	 * @param sessionKey 会话标识 必选
	 * @param first 开始 非必选 默认0
	 * @param count 数量  非必选 默认10
	 */
	@RequestMapping(value = "/guestbook/mylist")
	public void myGuestbookList(Integer siteId,Integer ctgId,
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if (siteId == null) {
			siteId = CmsUtils.getSiteId(request);
		}
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		CmsUser user = CmsUtils.getUser(request);
		List<CmsGuestbook> list = null ;
		if(user!=null){
			list= cmsGuestbookMng.getList(siteId, ctgId, user.getId(), null,
					null, true, true, first, count);
		}
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		String body= jsonArray.toString();
		String message=Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(request, body, message,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	protected CmsGuestbookMng cmsGuestbookMng;
	@Autowired
	private CmsGuestbookCtgMng cmsGuestbookCtgMng;
}
