package com.jeecms.core.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.Channel;
import com.jeecms.common.hibernate4.PriorityComparator;
import com.jeecms.common.hibernate4.PriorityInterface;
import com.jeecms.core.entity.base.BaseCmsGroup;

public class CmsGroup extends BaseCmsGroup implements PriorityInterface {
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
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (getAllowPerDay()!=null) {
			json.put("allowPerDay", getAllowPerDay());
		}else{
			json.put("allowPerDay", "");
		}
		if (getAllowMaxFile()!=null) {
			json.put("allowMaxFile", getAllowMaxFile());
		}else{
			json.put("allowMaxFile", "");
		}
		if (StringUtils.isNotBlank(getAllowSuffix())) {
			json.put("allowSuffix", getAllowSuffix());
		}else{
			json.put("allowSuffix", "");
		}
		json.put("allowFileSize", "");
		json.put("allowFileTotal", "");
		if (getNeedCaptcha()!=null) {
			json.put("needCaptcha", getNeedCaptcha());
		}else{
			json.put("needCaptcha", "");
		}
		if (getNeedCheck()!=null) {
			json.put("needCheck", getNeedCheck());
		}else{
			json.put("needCheck", "");
		}
		if (getRegDef()!=null) {
			json.put("regDef", getRegDef());
		}else{
			json.put("regDef", "");
		}
		if (getViewChannels()!=null&&getViewChannels().size()>0) {
			JSONArray jsonArray = new JSONArray();
			Set<Channel> set = getViewChannels();
			int index = 0 ;
			for (Channel channel : set) {
				jsonArray.put(index,channel.getId());
				index++;
			}
			json.put("viewChannelIds", jsonArray);
		}else{
			json.put("viewChannelIds", new JSONArray());
		}
		if (getContriChannels()!=null&&getContriChannels().size()>0) {
			JSONArray jsonArray = new JSONArray();
			Set<Channel> set = getContriChannels();
			int index = 0 ;
			for (Channel channel : set) {
				jsonArray.put(index,channel.getId());
				index++;
			}
			json.put("contriChannelIds", jsonArray);
		}else{
			json.put("contriChannelIds", new JSONArray());
		}
		return json;
	}
	
	/**
	 * 从集合中提取ID数组
	 * 
	 * @param groups
	 * @return
	 */
	public static Integer[] fetchIds(Collection<CmsGroup> groups) {
		Integer[] ids = new Integer[groups.size()];
		int i = 0;
		for (CmsGroup g : groups) {
			ids[i++] = g.getId();
		}
		return ids;
	}

	/**
	 * 根据列表排序
	 * 
	 * @param source
	 *            元素集合
	 * @param target
	 *            有顺序列表
	 * @return 一个新的、按目标排序的列表
	 */
	public static List<CmsGroup> sortByList(Set<CmsGroup> source,
			List<CmsGroup> target) {
		List<CmsGroup> list = new ArrayList<CmsGroup>(source.size());
		for (CmsGroup g : target) {
			if (source.contains(g)) {
				list.add(g);
			}
		}
		return list;
	}

	/**
	 * 是否允许上传后缀
	 * 
	 * @param ext
	 * @return
	 */
	public boolean isAllowSuffix(String ext) {
		String suffix = getAllowSuffix();
		if (StringUtils.isBlank(suffix)) {
			return true;
		}
		String[] attr = StringUtils.split(suffix, ",");
		for (int i = 0, len = attr.length; i < len; i++) {
			if (attr[i].equals(ext)) {
				return true;
			}
		}
		return false;
	}

	public void init() {
		if (getRegDef() == null) {
			setRegDef(false);
		}
		if (getAllowPerDay()==null) {
			setAllowPerDay(0);
		}
		if (getAllowMaxFile()==null) {
			setAllowMaxFile(0);
		}
		if (getNeedCaptcha()==null) {
			setNeedCaptcha(true);
		}
		if (getNeedCheck()==null) {
			setNeedCheck(true);
		}
	}
	public Set<Integer> getViewChannelIds(Integer siteId) {
		Set<Channel> channels = getViewChannels();
		Set<Integer> ids = new HashSet<Integer>();
		for (Channel c : channels) {
			if (c.getSite().getId().equals(siteId)) {
				ids.add(c.getId());
			}
		}
		return ids;
	}
	public Set<Integer> getContriChannelIds(Integer siteId) {
		Set<Channel> channels = getContriChannels();
		Set<Integer> ids = new HashSet<Integer>();
		for (Channel c : channels) {
			if (c.getSite().getId().equals(siteId)) {
				ids.add(c.getId());
			}
		}
		return ids;
	}
	public void addToViewChannels(Channel channel) {
		Set<Channel> channels = getViewChannels();
		if (channels == null) {
			channels = new TreeSet<Channel>(new PriorityComparator());
			setViewChannels(channels);
		}
		channels.add(channel);
		channel.getViewGroups().add(this);
	}

	public void addToContriChannels(Channel channel) {
		Set<Channel> channels = getContriChannels();
		if (channels == null) {
			channels = new TreeSet<Channel>(new PriorityComparator());
			setContriChannels(channels);
		}
		channels.add(channel);
		channel.getContriGroups().add(this);
	}
	
	public boolean allowUploadFileSuffix(String ext){
		String allowSuffix=getAllowSuffix();
		if(StringUtils.isBlank(allowSuffix)){
			return true;
		}
		String[]suffixs=allowSuffix.split(",");
		if(Arrays.asList(suffixs).contains(ext)){
			return true;
		}
		return false;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsGroup (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsGroup (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer priority,
		java.lang.Integer allowPerDay,
		java.lang.Integer allowMaxFile,
		java.lang.Boolean needCaptcha,
		java.lang.Boolean needCheck,
		java.lang.Boolean regDef) {

		super (
			id,
			name,
			priority,
			allowPerDay,
			allowMaxFile,
			needCaptcha,
			needCheck,
			regDef);
	}

	/* [CONSTRUCTOR MARKER END] */

}