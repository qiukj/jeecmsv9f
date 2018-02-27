package com.jeecms.cms.entity.main;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseCmsTopic;

public class CmsTopic extends BaseCmsTopic {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getShortName())) {
			json.put("shortName", getShortName());
		}else{
			json.put("shortName", "");
		}
		if (StringUtils.isNotBlank(getKeywords())) {
			json.put("keywords", getKeywords());
		}else{
			json.put("keywords", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getTitleImg())) {
			json.put("titleImg", getTitleImg());
		}else{
			json.put("titleImg", "");
		}
		if (StringUtils.isNotBlank(getContentImg())) {
			json.put("contentImg", getContentImg());
		}else{
			json.put("contentImg", "");
		}
		if (StringUtils.isNotBlank(getTplContent())) {
			json.put("tplContent", getTplContent());
		}else{
			json.put("tplContent", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (getRecommend()!=null) {
			json.put("recommend", getRecommend());
		}else{
			json.put("recommend", "");
		}
		if (StringUtils.isNotBlank(getInitials())) {
			json.put("initials", getInitials());
		}else{
			json.put("initials", "");
		}
		JSONArray channelArray = new JSONArray();
		if (getChannels()!=null) {
			Set<Channel> set = getChannels();
			for (Channel channel : set) {
				channelArray.put(channel.getId());
			}
		}
		json.put("channelIds", channelArray);
		return json;
	}
	
	public java.lang.String getTplContentShort (String tplBasePath) {
		String tplContent=super.getTplContent();
		// 当前模板，去除基本路径
		int tplPathLength = tplBasePath.length();
		if (!StringUtils.isBlank(tplContent)) {
			tplContent = tplContent.substring(tplPathLength);
		}
		return tplContent;
	}

	/**
	 * 获得简短名称，如果不存在则返回名称
	 * 
	 * @return
	 */
	public String getSname() {
		if (!StringUtils.isBlank(getShortName())) {
			return getShortName();
		} else {
			return getName();
		}
	}

	public void init() {
		blankToNull();
		if (getPriority()==null) {
			setPriority(10);
		}
		if (getRecommend()==null) {
			setRecommend(false);
		}
	}

	public void blankToNull() {
		if (StringUtils.isBlank(getTitleImg())) {
			setTitleImg(null);
		}
		if (StringUtils.isBlank(getContentImg())) {
			setContentImg(null);
		}
		if (StringUtils.isBlank(getShortName())) {
			setShortName(null);
		}
	}

	/**
	 * 从集合中获取ID数组
	 * 
	 * @param topics
	 * @return
	 */
	public static Integer[] fetchIds(Collection<CmsTopic> topics) {
		Integer[] ids = new Integer[topics.size()];
		int i = 0;
		for (CmsTopic g : topics) {
			ids[i++] = g.getId();
		}
		return ids;
	}

	public void addToChannels(Channel channel) {
		Set<Channel> channels = getChannels();
		if (channels == null) {
			channels = new HashSet<Channel>();
			setChannels(channels);
		}
		channels.add(channel);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsTopic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsTopic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsTopic (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer priority,
		java.lang.Boolean recommend) {

		super (
			id,
			name,
			priority,
			recommend);
	}

	/* [CONSTRUCTOR MARKER END] */

}