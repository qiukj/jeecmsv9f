package com.jeecms.cms.staticpage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.CmsUtils;

import freemarker.template.TemplateException;

@Controller
public class StaticAct {
	private static final Logger log = LoggerFactory.getLogger(StaticAct.class);

	@RequiresPermissions("static:v_index")
	@RequestMapping(value = "/static/v_index.do")
	public String indexInput(HttpServletRequest request, ModelMap model) {
		return "static/index";
	}

	@RequiresPermissions("static:o_index")
	@RequestMapping(value = "/static/o_index.do")
	public void indexSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			boolean staticRequired=true;
			CmsSite site = CmsUtils.getSite(request);
			if(!site.getStaticIndex()){
				staticRequired=false;
				ResponseUtils.renderJson(response, "{\"success\":false,\"no\":true}");
			}
			if(staticRequired){
				staticPageSvc.index(site);
				ResponseUtils.renderJson(response, "{\"success\":true}");
			}
		} catch (IOException e) {
			log.error("static index error!", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
			ResponseUtils.renderJson(response, json.toString());
		} catch (TemplateException e) {
			log.error("static index error!", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
			ResponseUtils.renderJson(response, json.toString());
		}
	}

	@RequiresPermissions("static:o_index_remove")
	@RequestMapping(value = "/static/o_index_remove.do")
	public void indexRemove(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		JSONObject json = new JSONObject();
		if (staticPageSvc.deleteIndex(site)) {
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequiresPermissions("static:v_channel")
	@RequestMapping(value = "/static/v_channel.do")
	public String channelInput(HttpServletRequest request, ModelMap model) {
		// 栏目列表
		CmsSite site = CmsUtils.getSite(request);
		List<Channel> topList = channelMng.getTopList(site.getId(), false);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				null, false);
		model.addAttribute("channelList", channelList);
		return "static/channel";
	}

	@RequiresPermissions("static:o_channel")
	@RequestMapping(value = "/static/o_channel.do")
	public void channelSubmit(Integer channelId, Boolean containChild,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		JSONObject json = new JSONObject();
		if (containChild == null) {
			containChild = true;
		}
		try {
			boolean staticRequired=true;
			if(channelId!=null){
				Channel c=channelMng.findById(channelId);
				if(c!=null&&(!StringUtils.isBlank(c.getLink()) || !c.getStaticChannel())){
					staticRequired=false;
					json.put("success", false);
					json.put("no", true);
				}
			}
			if(staticRequired){
				int count = staticPageSvc.channel(request, response,
						site.getId(), channelId, containChild);
				json.put("success", true);
				json.put("count", count);
			}
		} catch (IOException e) {
			log.error("static channel error!", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
		} catch (TemplateException e) {
			log.error("static channel error!", e);
			json.put("success", false);
			json.put("msg", e.getMessage());
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequiresPermissions("static:v_content")
	@RequestMapping(value = "/static/v_content.do")
	public String contentInput(HttpServletRequest request, ModelMap model) {
		// 栏目列表
		CmsSite site = CmsUtils.getSite(request);
		List<Channel> topList = channelMng.getTopList(site.getId(), true);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				null, true);
		model.addAttribute("channelList", channelList);
		return "static/content";
	}

	@RequiresPermissions("static:o_content")
	@RequestMapping(value = "/static/o_content.do")
	public void contentSubmit(Integer channelId, Date startDate,
			HttpServletRequest request, HttpServletResponse response) {
		String msg="";
		try {
			Integer siteId = null;
			boolean staticRequired=true;
			if (channelId == null) {
				// 没有指定栏目，则需指定站点
				siteId = CmsUtils.getSiteId(request);
			}else{
				Channel c=channelMng.findById(channelId);
				if(c!=null&&(!StringUtils.isBlank(c.getLink()) || !c.getStaticContent())){
					staticRequired=false;
					msg = "{\"success\":false,\"no\":true}";
				}
			}
			if(staticRequired){
				int count = staticPageSvc.content(request, response, siteId, channelId, startDate);
				msg = "{\"success\":true,\"count\":" + count + "}";
			}
		} catch (IOException e) {
			log.error("static channel error!", e);
			e.printStackTrace();
			msg = "{\"success\":false,\"msg\":'" + e.getMessage() + "'}";
		} catch (TemplateException e) {
			log.error("static channel error!", e);
			e.printStackTrace();
			msg = "{\"success\":false,\"msg\":'" + e.getMessage() + "'}";
		}
		ResponseUtils.renderJson(response, msg);
	}

	@RequiresPermissions("static:v_reset_generate")
	@RequestMapping(value = "/static/v_reset_generate.do")
	public String resetgenerateInput(HttpServletRequest request, ModelMap model) {
		// 栏目列表
		CmsSite site = CmsUtils.getSite(request);
		List<Channel> topList = channelMng.getTopList(site.getId(), false);
		List<Channel> channelList = Channel.getListForSelect(topList, null,
				null, false);
		model.addAttribute("channelList", channelList);
		return "static/resetgenerate";
	}

	@RequiresPermissions("static:o_reset_generate")
	@RequestMapping("/static/o_reset_generate.do")
	public void resetGenerate(Integer channelId, HttpServletRequest request,
			HttpServletResponse response){
		String msg;
		List<Content> contents;
		if (channelId == null) {
			Integer siteId = CmsUtils.getSiteId(request);
			Integer[] siteIds = new Integer[] { siteId };
			contents = contentMng.getListBySiteIdsForTag(siteIds, null, null,
					null, null, null,1, 0, Integer.MAX_VALUE);
		} else {
			Channel c = channelMng.findById(channelId);
			if (c == null) {
				msg = "{\"success\":false,\"msg\":'" + "channel is not find "
						+ "'}";
			}
			Integer[] cIds = new Integer[] { channelId };
			contents=contentMng.getListByChannelIdsForTag(cIds, null, null, null, null, null, 1, 1, 0, null);
		}
		if (contents != null && contents.size() > 0) {
			for (Content content : contents) {
				content.getContentExt().setNeedRegenerate(true);
				contentMng.update(content);
			}
		}
		msg = "{\"success\":true,\"count\":" + contents.size() + "}";
		ResponseUtils.renderJson(response, msg);
	}

	@Autowired
	private StaticPageSvc staticPageSvc;
	@Autowired
	private ChannelMng channelMng;
	@Autowired
	private ContentMng contentMng;

}
