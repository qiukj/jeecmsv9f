package com.jeecms.cms.entity.assist;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsGuestbookCtg;



public class CmsGuestbookCtg extends BaseCmsGuestbookCtg {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority","");
		}
		return json;
	}
	
	public void init(){
		if (getPriority()==null) {
			setPriority(10);
		}
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsGuestbookCtg () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsGuestbookCtg (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsGuestbookCtg (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.Integer priority) {

		super (
			id,
			site,
			name,
			priority);
	}
	public static Integer[] fetchIds(Collection<CmsGuestbookCtg> guestBookCtgs) {
		if (guestBookCtgs == null) {
			return null;
		}
		Integer[] ids = new Integer[guestBookCtgs.size()];
		int i = 0;
		for (CmsGuestbookCtg c : guestBookCtgs) {
			ids[i++] = c.getId();
		}
		return ids;
	}

/*[CONSTRUCTOR MARKER END]*/


}