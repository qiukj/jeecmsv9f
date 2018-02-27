package com.jeecms.core.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_user_content_charge table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_user_content_charge"
 */

public abstract class BaseCmsUserAccount  implements Serializable {

	public static String REF = "CmsUserContentCharge";
	public static String PROP_CONTENT_PAID_COUNT = "contentPaidCount";
	public static String PROP_CONTENT_TOTAL_AMOUNT = "contentTotalAmount";
	public static String PROP_CONTENT_NO_PAY_AMOUNT = "contentNoPayAmount";
	public static String PROP_USER = "user";
	public static String PROP_CONTENT_DAY_AMOUNT = "contentDayAmount";
	public static String PROP_ACCOUNT_WEIXIN_NAME = "accountWeixinName";
	public static String PROP_LAST_PAID_TIME = "lastPaidTime";
	public static String PROP_ACCOUNT_WEIXIN_OPEN_ID = "accountWeixinOpenId";
	public static String PROP_CONTENT_MONTH_AMOUNT = "contentMonthAmount";
	public static String PROP_ID = "id";
	public static String PROP_CONTENT_YEAR_AMOUNT = "contentYearAmount";


	// constructors
	public BaseCmsUserAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsUserAccount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsUserAccount (
		java.lang.Integer id,
		java.lang.Double contentYearAmount,
		java.lang.Double contentMonthAmount,
		java.lang.Double contentDayAmount,
		java.util.Date lastDrawTime) {

		this.setId(id);
		this.setContentYearAmount(contentYearAmount);
		this.setContentMonthAmount(contentMonthAmount);
		this.setContentDayAmount(contentDayAmount);
		this.setLastDrawTime(lastDrawTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String accountWeixin;
	private java.lang.String accountWeixinOpenId;
	private java.lang.String accountAlipy;
	private java.lang.Double contentTotalAmount;
	private java.lang.Double contentNoPayAmount;
	private java.lang.Double contentYearAmount;
	private java.lang.Double contentMonthAmount;
	private java.lang.Double contentDayAmount;
	private java.lang.Integer drawCount;
	private java.lang.Integer contentBuyCount;
	private java.util.Date lastDrawTime;
	private java.util.Date lastBuyTime;
	private java.lang.Short drawAccount;

	// one to one
	private com.jeecms.core.entity.CmsUser user;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="user_id"
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

	public java.lang.String getAccountWeixin() {
		return accountWeixin;
	}

	public void setAccountWeixin(java.lang.String accountWeixin) {
		this.accountWeixin = accountWeixin;
	}

	public java.lang.String getAccountAlipy() {
		return accountAlipy;
	}

	public void setAccountAlipy(java.lang.String accountAlipy) {
		this.accountAlipy = accountAlipy;
	}

	/**
	 * Return the value associated with the column: account_weixin_openId
	 */
	public java.lang.String getAccountWeixinOpenId () {
		return accountWeixinOpenId;
	}

	/**
	 * Set the value related to the column: account_weixin_openId
	 * @param accountWeixinOpenId the account_weixin_openId value
	 */
	public void setAccountWeixinOpenId (java.lang.String accountWeixinOpenId) {
		this.accountWeixinOpenId = accountWeixinOpenId;
	}


	/**
	 * Return the value associated with the column: content_total_amount
	 */
	public java.lang.Double getContentTotalAmount () {
		return contentTotalAmount;
	}

	/**
	 * Set the value related to the column: content_total_amount
	 * @param contentTotalAmount the content_total_amount value
	 */
	public void setContentTotalAmount (java.lang.Double contentTotalAmount) {
		this.contentTotalAmount = contentTotalAmount;
	}


	/**
	 * Return the value associated with the column: content_no_pay_amount
	 */
	public java.lang.Double getContentNoPayAmount () {
		return contentNoPayAmount;
	}

	/**
	 * Set the value related to the column: content_no_pay_amount
	 * @param contentNoPayAmount the content_no_pay_amount value
	 */
	public void setContentNoPayAmount (java.lang.Double contentNoPayAmount) {
		this.contentNoPayAmount = contentNoPayAmount;
	}


	/**
	 * Return the value associated with the column: content_year_amount
	 */
	public java.lang.Double getContentYearAmount () {
		return contentYearAmount;
	}

	/**
	 * Set the value related to the column: content_year_amount
	 * @param contentYearAmount the content_year_amount value
	 */
	public void setContentYearAmount (java.lang.Double contentYearAmount) {
		this.contentYearAmount = contentYearAmount;
	}


	/**
	 * Return the value associated with the column: content_month_amount
	 */
	public java.lang.Double getContentMonthAmount () {
		return contentMonthAmount;
	}

	/**
	 * Set the value related to the column: content_month_amount
	 * @param contentMonthAmount the content_month_amount value
	 */
	public void setContentMonthAmount (java.lang.Double contentMonthAmount) {
		this.contentMonthAmount = contentMonthAmount;
	}


	/**
	 * Return the value associated with the column: content_day_amount
	 */
	public java.lang.Double getContentDayAmount () {
		return contentDayAmount;
	}

	/**
	 * Set the value related to the column: content_day_amount
	 * @param contentDayAmount the content_day_amount value
	 */
	public void setContentDayAmount (java.lang.Double contentDayAmount) {
		this.contentDayAmount = contentDayAmount;
	}


	public java.lang.Integer getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(java.lang.Integer drawCount) {
		this.drawCount = drawCount;
	}

	public java.lang.Integer getContentBuyCount() {
		return contentBuyCount;
	}

	public void setContentBuyCount(java.lang.Integer contentBuyCount) {
		this.contentBuyCount = contentBuyCount;
	}

	public java.util.Date getLastDrawTime() {
		return lastDrawTime;
	}

	public void setLastDrawTime(java.util.Date lastDrawTime) {
		this.lastDrawTime = lastDrawTime;
	}

	public java.util.Date getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(java.util.Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	public java.lang.Short getDrawAccount() {
		return drawAccount;
	}

	public void setDrawAccount(java.lang.Short drawAccount) {
		this.drawAccount = drawAccount;
	}

	/**
	 * Return the value associated with the column: user
	 */
	public com.jeecms.core.entity.CmsUser getUser () {
		return user;
	}

	/**
	 * Set the value related to the column: user
	 * @param user the user value
	 */
	public void setUser (com.jeecms.core.entity.CmsUser user) {
		this.user = user;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.core.entity.CmsUserAccount)) return false;
		else {
			com.jeecms.core.entity.CmsUserAccount cmsUserContentCharge = (com.jeecms.core.entity.CmsUserAccount) obj;
			if (null == this.getId() || null == cmsUserContentCharge.getId()) return false;
			else return (this.getId().equals(cmsUserContentCharge.getId()));
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