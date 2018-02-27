package com.jeecms.cms.entity.assist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsWebservice;

public class CmsWebservice extends BaseCmsWebservice {
	private static final long serialVersionUID = 1L;
	
	public static final String SERVICE_TYPE_ADD_USER = "addUser";
	public static final String SERVICE_TYPE_UPDATE_USER = "updateUser";
	public static final String SERVICE_TYPE_DELETE_USER = "deleteUser";
	
	public void addToParams(String name, String defaultValue) {
		List<CmsWebserviceParam> list = getParams();
		if (list == null) {
			list = new ArrayList<CmsWebserviceParam>();
			setParams(list);
		}
		CmsWebserviceParam param = new CmsWebserviceParam();
		param.setParamName(name);
		param.setDefaultValue(defaultValue);
		list.add(param);
	}
	
	public JSONObject convertToJson(HttpServletRequest request){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAddress())) {
			json.put("address", getAddress());
		}else{
			json.put("address", "");
		}
		if (StringUtils.isNotBlank(getTargetNamespace())) {
			json.put("targetNamespace", getTargetNamespace());
		}else{
			json.put("targetNamespace", "");
		}
		if (StringUtils.isNotBlank(getSuccessResult())) {
			json.put("successResult", getSuccessResult());
		}else{
			json.put("successResult", "");
		}
		if (StringUtils.isNotBlank(getType())) {
			json.put("type", getType());
		}else{
			json.put("type", "");
		}
		if (StringUtils.isNotBlank(getOperate())) {
			json.put("operate", getOperate());
		}else{
			json.put("operate", "");
		}
		JSONArray jsonArray = new JSONArray();
		if (getParams()!=null&&getParams().size()>0) {
			List<CmsWebserviceParam> list = getParams();
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		json.put("params", jsonArray);
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsWebservice () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsWebservice (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsWebservice (
		java.lang.Integer id,
		java.lang.String address) {

		super (
			id,
			address);
	}

/*[CONSTRUCTOR MARKER END]*/


}