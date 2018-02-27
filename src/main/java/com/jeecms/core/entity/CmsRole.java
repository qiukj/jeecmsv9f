package com.jeecms.core.entity;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsRole;

public class CmsRole extends BaseCmsRole {
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
		if (getLevel()!=null) {
			json.put("level", getLevel());
		}else{
			json.put("level", "");
		}
		if (getAll()!=null) {
			json.put("all", getAll());
		}else{
			json.put("all", "");
		}
		JSONArray jsonArray = new JSONArray();
		if (getPerms()!=null&&getPerms().size()>0) {
			Set<String> set = getPerms();
			int index = 0 ;
			for(String s : set){
				jsonArray.put(index,s);
				index++;
			}
		}
		json.put("perms", jsonArray);
		return json;
	}
	
	public void init(){
		if (getLevel()==null) {
			setLevel(10);
		}
		if (getPriority()==null) {
			setPriority(10);
		}
		if (getAll()==null) {
			setAll(false);
		}
	}

	public static Integer[] fetchIds(Collection<CmsRole> roles) {
		if (roles == null) {
			return null;
		}
		Integer[] ids = new Integer[roles.size()];
		int i = 0;
		for (CmsRole r : roles) {
			ids[i++] = r.getId();
		}
		return ids;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsRole () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsRole (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsRole (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer priority,
		java.lang.Boolean m_super) {

		super (
			id,
			name,
			priority,
			m_super);
	}
	public void delFromUsers(CmsUser user) {
		if (user == null) {
			return;
		}
		Set<CmsUser> set = getUsers();
		if (set == null) {
			return;
		}
		set.remove(user);
	}
	/* [CONSTRUCTOR MARKER END] */

}