package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsSearchWords;



public class CmsSearchWords extends BaseCmsSearchWords {
	private static final long serialVersionUID = 1L;
	/**
	 * 搜索次数降序
	 */
	public static final int HIT_DESC=1;
	/**
	 * 优先级降序
	 */
	public static final int PRIORITY_DESC=3;
	/**
	 *  
	 */
	public static final int HIT_ASC=2;
	/**
	 * 优先级升序
	 */
	public static final int PRIORITY_ASC=4;
	
	public static final int DEFAULT_PRIORITY=10;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getHitCount()!=null) {
			json.put("hitCount", getHitCount());
		}else{
			json.put("hitCount", "");
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
		if (getRecommend()!=null) {
			json.put("recommend", getRecommend());
		}else{
			json.put("recommend", "");
		}
		return json;
	}
	
	public void init(){
		if (getPriority()==null) {
			setPriority(10);
		}
		if (getRecommend()==null) {
			setRecommend(false);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsSearchWords () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSearchWords (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSearchWords (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer hitCount,
		java.lang.Integer priority,
		java.lang.String nameInitial) {

		super (
			id,
			name,
			hitCount,
			priority,
			nameInitial);
	}

/*[CONSTRUCTOR MARKER END]*/


}