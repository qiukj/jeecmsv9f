package com.jeecms.cms.entity.main.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_api_record table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_api_record"
 */

public abstract class BaseApiRecord  implements Serializable {

	public static String REF = "ApiRecord";
	public static String PROP_CALL_TIME_STAMP = "callTimeStamp";
	public static String PROP_API_INFO = "apiInfo";
	public static String PROP_CALL_IP = "callIp";
	public static String PROP_CALL_TIME = "callTime";
	public static String PROP_ID = "id";
	public static String PROP_API_ACCOUNT = "apiAccount";


	// constructors
	public BaseApiRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseApiRecord (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseApiRecord (
		java.lang.Long id,
		com.jeecms.cms.entity.main.ApiAccount apiAccount,
		com.jeecms.cms.entity.main.ApiInfo apiInfo,
		java.util.Date callTime,
		java.lang.Long callTimeStamp) {

		this.setId(id);
		this.setApiAccount(apiAccount);
		this.setApiInfo(apiInfo);
		this.setCallTime(callTime);
		this.setCallTimeStamp(callTimeStamp);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String callIp;
	private java.util.Date callTime;
	private java.lang.Long callTimeStamp;
	private java.lang.String sign;

	// many to one
	private com.jeecms.cms.entity.main.ApiAccount apiAccount;
	private com.jeecms.cms.entity.main.ApiInfo apiInfo;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: call_ip
	 */
	public java.lang.String getCallIp () {
		return callIp;
	}

	/**
	 * Set the value related to the column: call_ip
	 * @param callIp the call_ip value
	 */
	public void setCallIp (java.lang.String callIp) {
		this.callIp = callIp;
	}


	/**
	 * Return the value associated with the column: api_call_time
	 */
	public java.util.Date getCallTime () {
		return callTime;
	}

	/**
	 * Set the value related to the column: api_call_time
	 * @param callTime the api_call_time value
	 */
	public void setCallTime (java.util.Date callTime) {
		this.callTime = callTime;
	}


	/**
	 * Return the value associated with the column: call_time_stamp
	 */
	public java.lang.Long getCallTimeStamp () {
		return callTimeStamp;
	}

	/**
	 * Set the value related to the column: call_time_stamp
	 * @param callTimeStamp the call_time_stamp value
	 */
	public void setCallTimeStamp (java.lang.Long callTimeStamp) {
		this.callTimeStamp = callTimeStamp;
	}

	public java.lang.String getSign() {
		return sign;
	}

	public void setSign(java.lang.String sign) {
		this.sign = sign;
	}

	/**
	 * Return the value associated with the column: api_account
	 */
	public com.jeecms.cms.entity.main.ApiAccount getApiAccount () {
		return apiAccount;
	}

	/**
	 * Set the value related to the column: api_account
	 * @param apiAccount the api_account value
	 */
	public void setApiAccount (com.jeecms.cms.entity.main.ApiAccount apiAccount) {
		this.apiAccount = apiAccount;
	}


	/**
	 * Return the value associated with the column: api_info_id
	 */
	public com.jeecms.cms.entity.main.ApiInfo getApiInfo () {
		return apiInfo;
	}

	/**
	 * Set the value related to the column: api_info_id
	 * @param apiInfo the api_info_id value
	 */
	public void setApiInfo (com.jeecms.cms.entity.main.ApiInfo apiInfo) {
		this.apiInfo = apiInfo;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.main.ApiRecord)) return false;
		else {
			com.jeecms.cms.entity.main.ApiRecord apiRecord = (com.jeecms.cms.entity.main.ApiRecord) obj;
			if (null == this.getId() || null == apiRecord.getId()) return false;
			else return (this.getId().equals(apiRecord.getId()));
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