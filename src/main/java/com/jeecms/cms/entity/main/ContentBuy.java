package com.jeecms.cms.entity.main;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.cms.entity.main.base.BaseContentBuy;
import com.jeecms.common.util.DateUtils;



public class ContentBuy extends BaseContentBuy {
	private static final long serialVersionUID = 1L;
	
	public static final Integer PAY_METHOD_WECHAT=1;
	public static final Integer PAY_METHOD_ALIPAY=2;
	
	//订单号错误
	public static final Integer PRE_PAY_STATUS_ORDER_NUM_ERROR=2;
	//订单成功
	public static final Integer PRE_PAY_STATUS_SUCCESS=1;
	//订单金额不足以购买内容
	public static final Integer PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH=3;
	
	public boolean getUserHasPaid(){
		if(StringUtils.isNotBlank(getOrderNumWeiXin())
				||StringUtils.isNotBlank(getOrderNumAliPay())){
			return true;
		}else{
			return false;
		}
	}
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
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
		if (getAuthorAmount()!=null) {
			json.put("authorAmount", getAuthorAmount());
		}else{
			json.put("authorAmount", "");
		}
		if (getPlatAmount()!=null) {
			json.put("platAmount", getPlatAmount());
		}else{
			json.put("platAmount", "");
		}
		if (getBuyTime()!=null) {
			json.put("buyTime", DateUtils.parseDateToTimeStr(getBuyTime()));
		}else{
			json.put("buyTime", "");
		}
		if (StringUtils.isNotBlank(getOrderNumber())) {
			json.put("orderNumber", getOrderNumber());
		}else{
			json.put("orderNumber", "");
		}
		if (StringUtils.isNotBlank(getOrderNumWeiXin())) {
			json.put("orderNumWeiXin", getOrderNumWeiXin());
		}else{
			json.put("orderNumWeiXin", "");
		}
		if (StringUtils.isNotBlank(getOrderNumAliPay())) {
			json.put("orderNumAliPay", getOrderNumAliPay());
		}else{
			json.put("orderNumAliPay", "");
		}
		if (getChargeReward()!=null) {
			json.put("chargeReward", getChargeReward());
		}else{
			json.put("chargeReward", "");
		}
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
		if (getContent()!=null&&StringUtils.isNotBlank(getContent().getUrl())) {
			json.put("contentUrl", getContent().getUrl());
		}else{
			json.put("contentUrl", "");
		}
		if(getBuyUser()!=null&&StringUtils.isNotBlank(getBuyUser().getUsername())){
			json.put("buyUserName", getBuyUser().getUsername());
		}else{
			json.put("buyUserName", "");
		}
		if(getBuyUser()!=null&&StringUtils.isNotBlank(getBuyUser().getRealname())){
			json.put("buyRealname", getBuyUser().getRealname());
		}else{
			json.put("buyRealname", "");
		}
		if(getBuyUser()!=null&&getBuyUser().getId()!=null){
			json.put("buyUserId", getBuyUser().getId());
		}else{
			json.put("buyUserId", "");
		}
		if (getAuthorUser()!=null&&StringUtils.isNotBlank(getAuthorUser().getUsername())) {
			json.put("authorUserName", getAuthorUser().getUsername());
		}else{
			json.put("authorUserName", "");
		}
		if (getAuthorUser()!=null&&StringUtils.isNotBlank(getAuthorUser().getRealname())) {
			json.put("authorRealname", getAuthorUser().getRealname());
		}else{
			json.put("authorRealname", "");
		}
		if (getAuthorUser()!=null&&getAuthorUser().getId()!=null) {
			json.put("authorUserId", getAuthorUser().getId());
		}else{
			json.put("authorUserId", "");
		}
		return json;
	}
	
	public int getPrePayStatus() {
		return prePayStatus;
	}

	public void setPrePayStatus(int prePayStatus) {
		this.prePayStatus = prePayStatus;
	}

	private int prePayStatus;
	
	
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public ContentBuy () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ContentBuy (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ContentBuy (
		java.lang.Long id,
		com.jeecms.cms.entity.main.Content content,
		com.jeecms.core.entity.CmsUser buyUser,
		com.jeecms.core.entity.CmsUser authorUser,
		java.lang.Double chargeAmount,
		java.lang.Double authorAmount,
		java.lang.Double platAmount,
		java.util.Date buyTime,
		boolean hasPaidAuthor) {

		super (
			id,
			content,
			buyUser,
			authorUser,
			chargeAmount,
			authorAmount,
			platAmount,
			buyTime,
			hasPaidAuthor);
	}

/*[CONSTRUCTOR MARKER END]*/


}