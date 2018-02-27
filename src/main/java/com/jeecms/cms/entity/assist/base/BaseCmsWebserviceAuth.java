package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_webservice_auth table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_webservice_auth"
 */

public abstract class BaseCmsWebserviceAuth  implements Serializable {

	public static String REF = "CmsWebserviceAuth";
	public static String PROP_ENABLE = "enable";
	public static String PROP_PASSWORD = "password";
	public static String PROP_USERNAME = "username";
	public static String PROP_ID = "id";
	public static String PROP_SYSTEM = "system";


	// constructors
	public BaseCmsWebserviceAuth () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsWebserviceAuth (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsWebserviceAuth (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.lang.String system,
		boolean enable) {

		this.setId(id);
		this.setUsername(username);
		this.setPassword(password);
		this.setSystem(system);
		this.setEnable(enable);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String username;
	private java.lang.String password;
	private java.lang.String system;
	private boolean enable;

	// collections
	private java.util.Set<com.jeecms.cms.entity.assist.CmsWebserviceCallRecord> webserviceCallRecords;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="auth_id"
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
	 * Return the value associated with the column: username
	 */
	public java.lang.String getUsername () {
		return username;
	}

	/**
	 * Set the value related to the column: username
	 * @param username the username value
	 */
	public void setUsername (java.lang.String username) {
		this.username = username;
	}


	/**
	 * Return the value associated with the column: password
	 */
	public java.lang.String getPassword () {
		return password;
	}

	/**
	 * Set the value related to the column: password
	 * @param password the password value
	 */
	public void setPassword (java.lang.String password) {
		this.password = password;
	}


	/**
	 * Return the value associated with the column: system
	 */
	public java.lang.String getSystem () {
		return system;
	}

	/**
	 * Set the value related to the column: system
	 * @param system the system value
	 */
	public void setSystem (java.lang.String system) {
		this.system = system;
	}


	/**
	 * Return the value associated with the column: is_enable
	 */
	public boolean isEnable () {
		return enable;
	}

	/**
	 * Set the value related to the column: is_enable
	 * @param enable the is_enable value
	 */
	public void setEnable (boolean enable) {
		this.enable = enable;
	}


	/**
	 * Return the value associated with the column: webserviceCallRecords
	 */
	public java.util.Set<com.jeecms.cms.entity.assist.CmsWebserviceCallRecord> getWebserviceCallRecords () {
		return webserviceCallRecords;
	}

	/**
	 * Set the value related to the column: webserviceCallRecords
	 * @param webserviceCallRecords the webserviceCallRecords value
	 */
	public void setWebserviceCallRecords (java.util.Set<com.jeecms.cms.entity.assist.CmsWebserviceCallRecord> webserviceCallRecords) {
		this.webserviceCallRecords = webserviceCallRecords;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.assist.CmsWebserviceAuth)) return false;
		else {
			com.jeecms.cms.entity.assist.CmsWebserviceAuth cmsWebserviceAuth = (com.jeecms.cms.entity.assist.CmsWebserviceAuth) obj;
			if (null == this.getId() || null == cmsWebserviceAuth.getId()) return false;
			else return (this.getId().equals(cmsWebserviceAuth.getId()));
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