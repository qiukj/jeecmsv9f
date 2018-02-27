package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseContentCharge;
import com.jeecms.common.util.DateUtils;



public class ContentCharge extends BaseContentCharge {
	private static final long serialVersionUID = 1L;
	public static final Short MODEL_FREE=0;
	public static final Short MODEL_CHARGE=1;
	public static final Short MODEL_REWARD=2;
	
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json = baseJson(json);
		if (getContent()!=null&&getContent().getId()!=null) {
			json.put("contentId", getContent().getId());
		}else{
			json.put("contentId", "");
		}
		if (getContent()!=null&&StringUtils.isNotBlank(getContent().getTitle())) {
			json.put("contentTitle", getContent().getTitle());
		}else{
			json.put("contentTitle", "");
		}
		return json;
	}
	
	public JSONObject baseJson(JSONObject json){
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getChargeAmount()!=null) {
			json.put("chargeAmount", getChargeAmount());
		}else{
			json.put("chargeAmount", "");
		}
		if (getTotalAmount()!=null) {
			json.put("totalAmount", getTotalAmount());
		}else{
			json.put("totalAmount", "");
		}
		if (getYearAmount()!=null) {
			json.put("yearAmount", getYearAmount());
		}else{
			json.put("yearAmount", "");
		}
		if (getMonthAmount()!=null) {
			json.put("monthAmount", getMonthAmount());
		}else{
			json.put("monthAmount", "");
		}
		if (getDayAmount()!=null) {
			json.put("dayAmount", getDayAmount());
		}else{
			json.put("dayAmount", "");
		}
		if (getChargeReward()!=null) {
			json.put("chargeReward", getChargeReward());
		}else{
			json.put("chargeReward", "");
		}
		if(getLastBuyTime()!=null){
			json.put("lastBuyTime", DateUtils.parseDateToTimeStr(getLastBuyTime()));
		}else{
			json.put("lastBuyTime","");
		}
		return json;
	}
	
	public void init(){
		if(getChargeAmount()==null){
			setChargeAmount(0d);
		}
		if(getDayAmount()==null){
			setDayAmount(0d);
		}
		if(getMonthAmount()==null){
			setMonthAmount(0d);
		}
		if(getYearAmount()==null){
			setYearAmount(0d);
		}
		if(getTotalAmount()==null){
			setTotalAmount(0d);
		}
		if(getRewardRandomMax()==null){
			setRewardRandomMax(0d);
		}
		if(getRewardRandomMin()==null){
			setRewardRandomMin(0d);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ContentCharge () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContentCharge (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ContentCharge (
		java.lang.Integer id,
		java.lang.Double chargeAmount,
		java.lang.Double totalAmount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		super (
			id,
			chargeAmount,
			totalAmount,
			yearAmount,
			monthAmount,
			dayAmount);
	}

/*[CONSTRUCTOR MARKER END]*/


}