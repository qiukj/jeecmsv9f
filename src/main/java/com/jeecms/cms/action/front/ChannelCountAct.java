package com.jeecms.cms.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.cms.manager.main.ChannelMng;
import com.jeecms.cms.service.ChannelCountCache;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class ChannelCountAct {
	@RequestMapping(value = "/channel_view.jspx", method = RequestMethod.GET)
	public void contentView(Integer channelId, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		if (channelId == null) {
			ResponseUtils.renderJson(response, "[]");
			return;
		}
		//栏目访问量计数
		Channel channel=channelMng.findById(channelId);
		int[] counts =channelCountCache.viewAndGet(channel.getId());
		String json;
		if (counts != null) {
			json = new JSONArray(counts).toString();
			ResponseUtils.renderJson(response, json);
		} else {
			ResponseUtils.renderJson(response, "[]");
		}
	}
	

	@Autowired
	private ChannelCountCache channelCountCache;
	@Autowired
	private ChannelMng channelMng;
}
