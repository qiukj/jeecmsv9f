package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsAccountDraw;
import com.jeecms.common.util.DateUtils;



public class CmsAccountDraw extends BaseCmsAccountDraw {
	private static final long serialVersionUID = 1L;
	
	public static final Short CHECKING = 0;
	public static final Short CHECKED_SUCC = 1;
	public static final Short CHECKED_FAIL = 2;
	public static final Short DRAW_SUCC = 3;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getApplyAmount()!=null) {
			json.put("applyAmount", getApplyAmount());
		}else{
			json.put("applyAmount", "");
		}
		if (getApplyStatus()!=null) {
			json.put("applyStatus", getApplyStatus());
		}else{
			json.put("applyStatus", "");
		}
		if(getApplyTime()!=null){
			json.put("applyTime", DateUtils.parseDateToTimeStr(getApplyTime()));
		}else{
			json.put("applyTime", "");
		}
		if (getDrawUser()!=null&&StringUtils.isNotBlank(getDrawUser().getUsername())) {
			json.put("drawUserName", getDrawUser().getUsername());
		}else{
			json.put("drawUserName", "");
		}
		if (getDrawUser()!=null&&getDrawUser().getId()!=null) {
			json.put("drawUserId", getDrawUser().getId());
		}else{
			json.put("drawUserId", "");
		}
		if (StringUtils.isNotBlank(getApplyAccount())) {
			json.put("applyAccount", getApplyAccount());
		}else{
			json.put("applyAccount", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsAccountDraw () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsAccountDraw (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsAccountDraw (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser drawUser,
		java.lang.String applyAccount,
		java.lang.Double applyAmount,
		java.lang.Short applyStatus,
		java.util.Date applyTime) {

		super (
			id,
			drawUser,
			applyAccount,
			applyAmount,
			applyStatus,
			applyTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}