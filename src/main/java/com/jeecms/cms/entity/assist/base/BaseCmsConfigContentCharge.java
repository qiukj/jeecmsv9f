package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_config_content_charge table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_config_content_charge"
 */

public abstract class BaseCmsConfigContentCharge  implements Serializable {

	public static String REF = "CmsConfigContentCharge";
	public static String PROP_WEIXIN_PASSWORD = "weixinPassword";
	public static String PROP_SETTLEMENT_DATE = "settlementDate";
	public static String PROP_WEIXIN_APP_ID = "weixinAppId";
	public static String PROP_CHARGE_RATIO = "chargeRatio";
	public static String PROP_COMMISSION_MONTH = "commissionMonth";
	public static String PROP_ENABLE = "enable";
	public static String PROP_COMMISSION_DAY = "commissionDay";
	public static String PROP_COMMISSION_YEAR = "commissionYear";
	public static String PROP_ID = "id";
	public static String PROP_COMMISSION_TOTAL = "commissionTotal";
	public static String PROP_WEIXIN_ACCOUNT = "weixinAccount";


	// constructors
	public BaseCmsConfigContentCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsConfigContentCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsConfigContentCharge(Integer id, String weixinAppId, String weixinAccount,
			String weixinPassword, String alipayAppid, String alipayAccount, String alipayKey, Double chargeRatio,
			Double minDrawAmount) {
		super();
		this.id = id;
		this.weixinAppId = weixinAppId;
		this.weixinAccount = weixinAccount;
		this.weixinPassword = weixinPassword;
		this.alipayAccount = alipayAccount;
		this.alipayKey = alipayKey;
		this.chargeRatio = chargeRatio;
		this.minDrawAmount = minDrawAmount;
	}

	protected void initialize () {}






	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String weixinAppId;
	private java.lang.String weixinSecret;
	private java.lang.String weixinAccount;
	private java.lang.String weixinPassword;
	private java.lang.String alipayPartnerId;
	private java.lang.String alipayAccount;
	private java.lang.String alipayKey;

	private java.lang.String alipayAppId;
	private java.lang.String alipayPublicKey;
	private java.lang.String alipayPrivateKey;
	
	private java.lang.Double chargeRatio;
	private java.lang.Double minDrawAmount;
	private java.lang.Double commissionTotal;
	private java.lang.Double commissionYear;
	private java.lang.Double commissionMonth;
	private java.lang.Double commissionDay;
	private java.util.Date lastBuyTime;
	private java.lang.String payTransferPassword;
	private java.lang.String transferApiPassword;
	private java.lang.Double rewardMin;
	private java.lang.Double rewardMax;
	private java.lang.Boolean rewardPattern;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="config_content_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: account_weixin_appid
	 */
	public java.lang.String getWeixinAppId () {
		return weixinAppId;
	}

	/**
	 * Set the value related to the column: account_weixin_appid
	 * @param weixinAppId the account_weixin_appid value
	 */
	public void setWeixinAppId (java.lang.String weixinAppId) {
		this.weixinAppId = weixinAppId;
	}

	public java.lang.String getWeixinSecret() {
		return weixinSecret;
	}

	public void setWeixinSecret(java.lang.String weixinSecret) {
		this.weixinSecret = weixinSecret;
	}

	/**
	 * Return the value associated with the column: weixin_account
	 */
	public java.lang.String getWeixinAccount () {
		return weixinAccount;
	}

	/**
	 * Set the value related to the column: weixin_account
	 * @param weixinAccount the weixin_account value
	 */
	public void setWeixinAccount (java.lang.String weixinAccount) {
		this.weixinAccount = weixinAccount;
	}


	/**
	 * Return the value associated with the column: weixin_password
	 */
	public java.lang.String getWeixinPassword () {
		return weixinPassword;
	}

	/**
	 * Set the value related to the column: weixin_password
	 * @param weixinPassword the weixin_password value
	 */
	public void setWeixinPassword (java.lang.String weixinPassword) {
		this.weixinPassword = weixinPassword;
	}


	/**
	 * Return the value associated with the column: charge_ratio
	 */
	public java.lang.Double getChargeRatio () {
		return chargeRatio;
	}

	/**
	 * Set the value related to the column: charge_ratio
	 * @param chargeRatio the charge_ratio value
	 */
	public void setChargeRatio (java.lang.Double chargeRatio) {
		this.chargeRatio = chargeRatio;
	}
	
	public java.lang.String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(java.lang.String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public java.lang.String getAlipayKey() {
		return alipayKey;
	}

	public void setAlipayKey(java.lang.String alipayKey) {
		this.alipayKey = alipayKey;
	}
	
	public java.lang.String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public void setAlipayPublicKey(java.lang.String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
	}

	public java.lang.String getAlipayPartnerId() {
		return alipayPartnerId;
	}

	public void setAlipayPartnerId(java.lang.String alipayPartnerId) {
		this.alipayPartnerId = alipayPartnerId;
	}

	public java.lang.String getAlipayAppId() {
		return alipayAppId;
	}

	public void setAlipayAppId(java.lang.String alipayAppId) {
		this.alipayAppId = alipayAppId;
	}

	public java.lang.String getAlipayPrivateKey() {
		return alipayPrivateKey;
	}

	public void setAlipayPrivateKey(java.lang.String alipayPrivateKey) {
		this.alipayPrivateKey = alipayPrivateKey;
	}

	public java.lang.Double getMinDrawAmount() {
		return minDrawAmount;
	}

	public void setMinDrawAmount(java.lang.Double minDrawAmount) {
		this.minDrawAmount = minDrawAmount;
	}

	/**
	 * Return the value associated with the column: commission_total
	 */
	public java.lang.Double getCommissionTotal () {
		return commissionTotal;
	}

	/**
	 * Set the value related to the column: commission_total
	 * @param commissionTotal the commission_total value
	 */
	public void setCommissionTotal (java.lang.Double commissionTotal) {
		this.commissionTotal = commissionTotal;
	}


	/**
	 * Return the value associated with the column: commission_year
	 */
	public java.lang.Double getCommissionYear () {
		return commissionYear;
	}

	/**
	 * Set the value related to the column: commission_year
	 * @param commissionYear the commission_year value
	 */
	public void setCommissionYear (java.lang.Double commissionYear) {
		this.commissionYear = commissionYear;
	}


	/**
	 * Return the value associated with the column: commission_month
	 */
	public java.lang.Double getCommissionMonth () {
		return commissionMonth;
	}

	/**
	 * Set the value related to the column: commission_month
	 * @param commissionMonth the commission_month value
	 */
	public void setCommissionMonth (java.lang.Double commissionMonth) {
		this.commissionMonth = commissionMonth;
	}


	/**
	 * Return the value associated with the column: commission_day
	 */
	public java.lang.Double getCommissionDay () {
		return commissionDay;
	}

	/**
	 * Set the value related to the column: commission_day
	 * @param commissionDay the commission_day value
	 */
	public void setCommissionDay (java.lang.Double commissionDay) {
		this.commissionDay = commissionDay;
	}

	public java.util.Date getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(java.util.Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	public java.lang.String getPayTransferPassword() {
		return payTransferPassword;
	}

	public void setPayTransferPassword(java.lang.String payTransferPassword) {
		this.payTransferPassword = payTransferPassword;
	}

	public java.lang.String getTransferApiPassword() {
		return transferApiPassword;
	}

	public void setTransferApiPassword(java.lang.String transferApiPassword) {
		this.transferApiPassword = transferApiPassword;
	}

	public java.lang.Double getRewardMin() {
		return rewardMin;
	}

	public void setRewardMin(java.lang.Double rewardMin) {
		this.rewardMin = rewardMin;
	}

	public java.lang.Double getRewardMax() {
		return rewardMax;
	}

	public void setRewardMax(java.lang.Double rewardMax) {
		this.rewardMax = rewardMax;
	}

	public java.lang.Boolean getRewardPattern() {
		return rewardPattern;
	}

	public void setRewardPattern(java.lang.Boolean rewardPattern) {
		this.rewardPattern = rewardPattern;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.assist.CmsConfigContentCharge)) return false;
		else {
			com.jeecms.cms.entity.assist.CmsConfigContentCharge cmsConfigContentCharge = (com.jeecms.cms.entity.assist.CmsConfigContentCharge) obj;
			if (null == this.getId() || null == cmsConfigContentCharge.getId()) return false;
			else return (this.getId().equals(cmsConfigContentCharge.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}