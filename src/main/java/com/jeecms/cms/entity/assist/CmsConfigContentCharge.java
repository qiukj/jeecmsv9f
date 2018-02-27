package com.jeecms.cms.entity.assist;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.jeecms.cms.entity.assist.base.BaseCmsConfigContentCharge;
import com.jeecms.common.util.DateUtils;

public class CmsConfigContentCharge extends BaseCmsConfigContentCharge {
	private static final long serialVersionUID = 1L;

	public JSONObject convertToJson(){
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getWeixinAppId())) {
			json.put("weixinAppId", getWeixinAppId());
		}else{
			json.put("weixinAppId", "");
		}
		if (StringUtils.isNotBlank(getWeixinSecret())) {
			json.put("weixinSecret", getWeixinSecret());
		}else{
			json.put("weixinSecret", "");
		}
		if (StringUtils.isNotBlank(getWeixinAccount())) {
			json.put("weixinAccount", getWeixinAccount());
		}else{
			json.put("weixinAccount", "");
		}
		if (StringUtils.isNotBlank(getWeixinPassword())) {
			json.put("weixinPassword", getWeixinPassword());
		}else{
			json.put("weixinPassword", "");
		}
		if (StringUtils.isNotBlank(getAlipayPartnerId())) {
			json.put("alipayPartnerId", getAlipayPartnerId());
		}else{
			json.put("alipayPartnerId", "");
		}
		if (StringUtils.isNotBlank(getAlipayAccount())) {
			json.put("alipayAccount", getAlipayAccount());
		}else{
			json.put("alipayAccount", "");
		}
		if (StringUtils.isNotBlank(getAlipayKey())) {
			json.put("alipayKey", getAlipayKey());
		}else{
			json.put("alipayKey", "");
		}
		if (StringUtils.isNotBlank(getAlipayAppId())) {
			json.put("alipayAppId", getAlipayAppId());
		}else{
			json.put("alipayAppId", "");
		}
		if (StringUtils.isNotBlank(getAlipayPublicKey())) {
			json.put("alipayPublicKey", getAlipayPublicKey());
		}else{
			json.put("alipayPublicKey", "");
		}
		if (StringUtils.isNotBlank(getAlipayPrivateKey())) {
			json.put("alipayPrivateKey", getAlipayPrivateKey());
		}else{
			json.put("alipayPrivateKey", "");
		}
		if (getChargeRatio()!=null) {
			json.put("chargeRatio", getChargeRatio());
		}else{
			json.put("chargeRatio", "");
		}
		if (getMinDrawAmount()!=null) {
			json.put("minDrawAmount", getMinDrawAmount());
		}else{
			json.put("minDrawAmount", "");
		}
		if (getCommissionTotal()!=null) {
			json.put("commissionTotal", getCommissionTotal());
		}else{
			json.put("commissionTotal", "");
		}
		if (getCommissionYear()!=null) {
			json.put("commissionYear", getCommissionYear());
		}else{
			json.put("commissionYear", "");
		}
		if (getCommissionMonth()!=null) {
			json.put("commissionMonth", getCommissionMonth());
		}else{
			json.put("commissionMonth", "");
		}
		if (getCommissionDay()!=null) {
			json.put("commissionDay", getCommissionDay());
		}else{
			json.put("commissionDay", "");
		}
		if (getLastBuyTime()!=null) {
			json.put("lastBuyTime", DateUtils.parseDateToDateStr(getLastBuyTime()));
		}else{
			json.put("lastBuyTime", "");
		}
		if (StringUtils.isNotBlank(getPayTransferPassword())) {
			json.put("payTransferPassword", getPayTransferPassword());
		}else{
			json.put("payTransferPassword", "");
		}
		if (StringUtils.isNotBlank(getTransferApiPassword())) {
			json.put("transferApiPassword", getTransferApiPassword());
		}else{
			json.put("transferApiPassword", "");
		}
		if (getRewardMin()!=null) {
			json.put("rewardMin", getRewardMin());
		}else{
			json.put("rewardMin", "");
		}
		if (getRewardMax()!=null) {
			json.put("rewardMax", getRewardMax());
		}else{
			json.put("rewardMax", "");
		}
		if (getRewardPattern()!=null) {
			json.put("rewardPattern", getRewardPattern());
		}else{
			json.put("rewardPattern", "");
		}
		return json;
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsConfigContentCharge() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsConfigContentCharge(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */

	public CmsConfigContentCharge(Integer id,String weixinAppId, String weixinAccount,
			String weixinPassword, String alipayAppid, String alipayAccount, String alipayKey, Double chargeRatio,
			Double minDrawAmount) {
		super(id, weixinAppId, weixinAccount, weixinPassword, alipayAppid, alipayAccount, alipayKey, chargeRatio,
				minDrawAmount);
	}

	/* [CONSTRUCTOR MARKER END] */

}