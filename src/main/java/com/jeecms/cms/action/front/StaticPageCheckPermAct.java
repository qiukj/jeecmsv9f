package com.jeecms.cms.action.front;


import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ContentBuyMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsGroup;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

/**
 * 静态页检查浏览权限
 */
@Controller
public class StaticPageCheckPermAct {
	
	public static final String GROUP_FORBIDDEN = "login.groupAccessForbidden";
	
	private static final Logger log = LoggerFactory
			.getLogger(StaticPageCheckPermAct.class);
	
	@RequestMapping(value = "/page_checkperm.jspx")
	public void checkPerm(Integer contentId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		Content content = contentMng.findById(contentId);
		String result="1";
		if (content == null) {
			log.debug("Content id not found: {}", contentId);
			result="2";
		}
		CmsUser user = CmsUtils.getUser(request);
		Set<CmsGroup> groups = content.getViewGroupsExt();
		int len = groups.size();
		// 需要浏览权限
		if (len != 0) {
			// 没有登录
			if (user == null) {
				result="3";
			}else{
				// 已经登录但没有权限
				Integer gid = user.getGroup().getId();
				boolean right = false;
				for (CmsGroup group : groups) {
					if (group.getId().equals(gid)) {
						right = true;
						break;
					}
				}
				//无权限
				if (!right) {
					result="4";
				}
			}
		}
		//收费模式，检查是否已购买
		if(content.getCharge()){
			if (user == null) {
				result="3";
			}else{
				if(!content.getUser().equals(user)){
					boolean hasBuy=contentBuyMng.hasBuyContent(user.getId(), contentId);
					if(!hasBuy){
						result="5";
					}
				}
			}
		}
		ResponseUtils.renderJson(response, result);
	}
	
	@RequestMapping(value = "/user_no_login.jspx")
	public String userNoLogin(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		return FrontUtils.showLogin(request, model, CmsUtils.getSite(request));
	}
	
	@RequestMapping(value = "/group_forbidden.jspx")
	public String groupForbidden(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		CmsUser user=CmsUtils.getUser(request);
		if(user!=null){
			return FrontUtils.showMessage(request, model, GROUP_FORBIDDEN,user.getGroup().getName());	
		}else{
			return userNoLogin(request, response, model);
		}
	}
	
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private ContentBuyMng contentBuyMng;
}
