package com.jeecms.cms.action.front;

import static com.jeecms.cms.Constants.TPLDIR_CSI;
import static com.jeecms.cms.Constants.TPLDIR_SPECIAL;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.assist.CmsComment;
import com.jeecms.cms.entity.main.ChannelExt;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.assist.CmsCommentMng;
import com.jeecms.cms.manager.assist.CmsSensitivityMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class CommentAct {
	private static final Logger log = LoggerFactory.getLogger(CommentAct.class);

	public static final String COMMENT_PAGE = "tpl.commentPage";
	public static final String COMMENT_LIST = "tpl.commentList";
	public static final String COMMENT_INPUT = "tpl.commentInput";

	@RequestMapping(value = "/comment*.jspx", method = RequestMethod.GET)
	public String page(Integer contentId, Integer pageNo,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		if(contentId==null){
			return FrontUtils.showMessage(request, model,
			"comment.contentNotFound");
		}
		Content content = contentMng.findById(contentId);
		if (content == null) {
			return FrontUtils.showMessage(request, model,
					"comment.contentNotFound");
		}
		if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_OFF) {
			return FrontUtils.showMessage(request, model, "comment.closed");
		}
		// 将request中所有参数保存至model中。
		model.putAll(RequestUtils.getQueryParams(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		model.addAttribute("content", content);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_SPECIAL, COMMENT_PAGE);
	}
	
	@RequestMapping(value = "/comment_input_csi.jspx")
	public String custom(String tpl,Integer contentId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		log.debug("visit csi custom template: {}", tpl);
		CmsSite site = CmsUtils.getSite(request);
		if(contentId==null){
			return FrontUtils.showMessage(request, model,
			"comment.contentNotFound");
		}
		Content content = contentMng.findById(contentId);
		if (content == null) {
			return FrontUtils.showMessage(request, model,
					"comment.contentNotFound");
		}
		if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_OFF) {
			return FrontUtils.showMessage(request, model, "comment.closed");
		}
		// 将request中所有参数保存至model中。
		model.putAll(RequestUtils.getQueryParams(request));
		model.addAttribute("content", content);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_SPECIAL, COMMENT_INPUT);
	}

	@RequestMapping(value = "/comment_list.jspx")
	public String list(Integer siteId, Integer contentId, Integer parentId,
			Integer greatTo,Integer recommend, Short checked, 
			Integer orderBy, Integer count,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (count == null || count <= 0 || count > 200) {
			count = 200;
		}
		boolean desc;
		if (orderBy == null || orderBy == 0) {
			desc = true;
		} else {
			desc = false;
		}
		Boolean rec;
		if (recommend != null) {
			rec = recommend != 0;
		} else {
			rec = null;
		}
		List<CmsComment> list = cmsCommentMng.getListForTag(siteId, contentId,
				parentId,greatTo, checked, rec, desc, 0,count);
		// 将request中所有参数
		model.putAll(RequestUtils.getQueryParams(request));
		model.addAttribute("list", list);
		model.addAttribute("contentId", contentId);
		model.addAttribute("content", contentMng.findById(contentId));
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_CSI, COMMENT_LIST);
	}

	@RequestMapping(value = "/comment.jspx", method = RequestMethod.POST)
	public void submit(Integer contentId, Integer parentId,Integer score,
			String text, String captcha,String sessionId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException, IOException {
		CmsSite site = CmsUtils.getSite(request);
		CmsUser user = CmsUtils.getUser(request);
		CmsConfig config=site.getConfig();
		JSONObject json = new JSONObject();
		if (contentId == null) {
			json.put("success", false);
			json.put("status", 100);
			ResponseUtils.renderJson(response, json.toString());
			return;
		}
		if (StringUtils.isBlank(text)) {
			json.put("success", false);
			json.put("status", 101);
			ResponseUtils.renderJson(response, json.toString());
			return;
		}
		if (user == null || user.getGroup().getNeedCaptcha()) {
			// 验证码错误
			try {
				if (!imageCaptchaService.validateResponseForID(session
						.getSessionId(request, response), captcha)) {
					json.put("success", false);
					json.put("status", 1);
					ResponseUtils.renderJson(response, json.toString());
					return;
				}
			} catch (CaptchaServiceException e) {
				json.put("success", false);
				json.put("status", 1);
				log.warn("", e);
				ResponseUtils.renderJson(response, json.toString());
				return;
			}
		}
		Content content = contentMng.findById(contentId);
		if (content == null) {
			// 内容不存在
			json.put("success", false);
			json.put("status", 2);
		} else if (content.getChannel().getCommentControl() == ChannelExt.COMMENT_OFF||!config.getCommentOpen()) {
			// 评论关闭
			json.put("success", false);
			json.put("status", 3);
		} else if ((content.getChannel().getCommentControl() == ChannelExt.COMMENT_LOGIN|content.getChannel().getCommentControl() == ChannelExt.COMMENT_LOGIN_MANY)
				&& user == null) {
			// 需要登录才能评论
			json.put("success", false);
			json.put("status", 4);
		}else if(content.getChannel().getCommentControl() == ChannelExt.COMMENT_LOGIN&&user!=null){
			if (hasCommented(user, content)) {
				// 已经评论过，不能重复评论
				json.put("success", false);
				json.put("status", 5);
			}
		}else {
			short checked = 0;
			Integer userId = null;
			boolean limit=false;
			if (user != null) {
				if (!user.getGroup().getNeedCheck()) {
					checked = 1;
				}
				userId = user.getId();
				if(user.getUserExt().getTodayCommentTotal()>=config.getCommentDayLimit()){
					limit=true;
				}
			}
			if(!limit){
				boolean haveSensitive=sensitivityMng.haveSensitivity(text);
				if(haveSensitive){
					json.put("success", false);
					json.put("status", 10);
					ResponseUtils.renderJson(response, json.toString());
					return;
				}
				cmsCommentMng.comment(score,text, RequestUtils.getIpAddr(request),
						contentId, site.getId(), userId, checked, false,parentId);
				json.put("success", true);
				json.put("status", 0);
			}else{
				json.put("success", false);
				json.put("status", 6);
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/comment_up.jspx")
	public void up(Integer commentId, HttpServletRequest request,
			HttpServletResponse response) {
		if (exist(commentId)) {
			cmsCommentMng.ups(commentId);
			ResponseUtils.renderJson(response, "true");
		} else {
			ResponseUtils.renderJson(response, "false");
		}
	}

	@RequestMapping(value = "/comment_down.jspx")
	public void down(Integer commentId, HttpServletRequest request,
			HttpServletResponse response) {
		if (exist(commentId)) {
			cmsCommentMng.downs(commentId);
			ResponseUtils.renderJson(response, "true");
		} else {
			ResponseUtils.renderJson(response, "false");
		}
	}

	private boolean hasCommented(CmsUser user, Content content) {
		if (content.hasCommentUser(user)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean exist(Integer id) {
		if (id == null) {
			return false;
		}
		CmsComment comment = cmsCommentMng.findById(id);
		return comment != null;
	}

	@Autowired
	private CmsCommentMng cmsCommentMng;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private CmsSensitivityMng sensitivityMng;
}
