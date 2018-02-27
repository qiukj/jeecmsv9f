package com.jeecms.cms.entity.main.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_api_account table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_api_account"
 */

public abstract class BaseApiAccount  implements Serializable {

	public static String REF = "ApiAccount";
	public static String PROP_APP_ID = "appId";
	public static String PROP_APP_KEY = "appKey";
	public static String PROP_ID = "id";
	public static String PROP_DISABLED = "disabled";


	// constructors
	public BaseApiAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseApiAccount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseApiAccount (
		java.lang.Integer id,
		java.lang.String appId,
		java.lang.String appKey,
		java.lang.Boolean disabled) {

		this.setId(id);
		this.setAppId(appId);
		this.setAppKey(appKey);
		this.setDisabled(disabled);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String appId;
	private java.lang.String appKey;
	private java.lang.String aesKey;
	private java.lang.String ivKey;
	private java.lang.Boolean disabled;
	private java.lang.Boolean admin;
	private java.lang.Boolean limitSingleDevice;

	// collections
	private java.util.Set<com.jeecms.cms.entity.main.ApiRecord> callRecords;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="id"
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
	 * Return the value associated with the column: app_id
	 */
	public java.lang.String getAppId () {
		return appId;
	}

	/**
	 * Set the value related to the column: app_id
	 * @param appId the app_id value
	 */
	public void setAppId (java.lang.String appId) {
		this.appId = appId;
	}


	/**
	 * Return the value associated with the column: app_key
	 */
	public java.lang.String getAppKey () {
		return appKey;
	}

	/**
	 * Set the value related to the column: app_key
	 * @param appKey the app_key value
	 */
	public void setAppKey (java.lang.String appKey) {
		this.appKey = appKey;
	}

	public java.lang.String getAesKey() {
		return aesKey;
	}

	public void setAesKey(java.lang.String aesKey) {
		this.aesKey = aesKey;
	}

	public java.lang.String getIvKey() {
		return ivKey;
	}

	public void setIvKey(java.lang.String ivKey) {
		this.ivKey = ivKey;
	}

	/**
	 * Return the value associated with the column: disabled
	 */
	public java.lang.Boolean getDisabled () {
		return disabled;
	}

	/**
	 * Set the value related to the column: disabled
	 * @param disabled the disabled value
	 */
	public void setDisabled (java.lang.Boolean disabled) {
		this.disabled = disabled;
	}

	public java.lang.Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(java.lang.Boolean admin) {
		this.admin = admin;
	}

	public java.lang.Boolean getLimitSingleDevice() {
		return limitSingleDevice;
	}

	public void setLimitSingleDevice(java.lang.Boolean limitSingleDevice) {
		this.limitSingleDevice = limitSingleDevice;
	}

	/**
	 * Return the value associated with the column: callRecords
	 */
	public java.util.Set<com.jeecms.cms.entity.main.ApiRecord> getCallRecords () {
		return callRecords;
	}

	/**
	 * Set the value related to the column: callRecords
	 * @param callRecords the callRecords value
	 */
	public void setCallRecords (java.util.Set<com.jeecms.cms.entity.main.ApiRecord> callRecords) {
		this.callRecords = callRecords;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.main.ApiAccount)) return false;
		else {
			com.jeecms.cms.entity.main.ApiAccount apiAccount = (com.jeecms.cms.entity.main.ApiAccount) obj;
			if (null == this.getId() || null == apiAccount.getId()) return false;
			else return (this.getId().equals(apiAccount.getId()));
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