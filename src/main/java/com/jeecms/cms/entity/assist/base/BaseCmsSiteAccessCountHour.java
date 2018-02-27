package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_site_access_count_hour table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_site_access_count_hour"
 */

public abstract class BaseCmsSiteAccessCountHour  implements Serializable {

	public static String REF = "CmsSiteAccessCountHour";
	public static String PROP_SITE = "site";
	public static String PROP_ACCESS_HOUR = "accessHour";
	public static String PROP_HOUR_UV = "hourUv";
	public static String PROP_HOUR_PV = "hourPv";
	public static String PROP_ACCESS_DATE = "accessDate";
	public static String PROP_HOUR_IP = "hourIp";
	public static String PROP_ID = "id";


	// constructors
	public BaseCmsSiteAccessCountHour () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsSiteAccessCountHour (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsSiteAccessCountHour (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.Long hourPv,
		java.lang.Long hourIp,
		java.lang.Long hourUv,
		java.util.Date accessDate,
		java.lang.Integer accessHour) {

		this.setId(id);
		this.setSite(site);
		this.setHourPv(hourPv);
		this.setHourIp(hourIp);
		this.setHourUv(hourUv);
		this.setAccessDate(accessDate);
		this.setAccessHour(accessHour);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Long hourPv;
	private java.lang.Long hourIp;
	private java.lang.Long hourUv;
	private java.util.Date accessDate;
	private java.lang.Integer accessHour;

	// many to one
	private com.jeecms.core.entity.CmsSite site;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="access_count_hour_id"
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
	 * Return the value associated with the column: hour_pv
	 */
	public java.lang.Long getHourPv () {
		return hourPv;
	}

	/**
	 * Set the value related to the column: hour_pv
	 * @param hourPv the hour_pv value
	 */
	public void setHourPv (java.lang.Long hourPv) {
		this.hourPv = hourPv;
	}


	/**
	 * Return the value associated with the column: hour_ip
	 */
	public java.lang.Long getHourIp () {
		return hourIp;
	}

	/**
	 * Set the value related to the column: hour_ip
	 * @param hourIp the hour_ip value
	 */
	public void setHourIp (java.lang.Long hourIp) {
		this.hourIp = hourIp;
	}


	/**
	 * Return the value associated with the column: hour_uv
	 */
	public java.lang.Long getHourUv () {
		return hourUv;
	}

	/**
	 * Set the value related to the column: hour_uv
	 * @param hourUv the hour_uv value
	 */
	public void setHourUv (java.lang.Long hourUv) {
		this.hourUv = hourUv;
	}


	/**
	 * Return the value associated with the column: access_date
	 */
	public java.util.Date getAccessDate () {
		return accessDate;
	}

	/**
	 * Set the value related to the column: access_date
	 * @param accessDate the access_date value
	 */
	public void setAccessDate (java.util.Date accessDate) {
		this.accessDate = accessDate;
	}


	/**
	 * Return the value associated with the column: access_hour
	 */
	public java.lang.Integer getAccessHour () {
		return accessHour;
	}

	/**
	 * Set the value related to the column: access_hour
	 * @param accessHour the access_hour value
	 */
	public void setAccessHour (java.lang.Integer accessHour) {
		this.accessHour = accessHour;
	}


	/**
	 * Return the value associated with the column: site_id
	 */
	public com.jeecms.core.entity.CmsSite getSite () {
		return site;
	}

	/**
	 * Set the value related to the column: site_id
	 * @param site the site_id value
	 */
	public void setSite (com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.assist.CmsSiteAccessCountHour)) return false;
		else {
			com.jeecms.cms.entity.assist.CmsSiteAccessCountHour cmsSiteAccessCountHour = (com.jeecms.cms.entity.assist.CmsSiteAccessCountHour) obj;
			if (null == this.getId() || null == cmsSiteAccessCountHour.getId()) return false;
			else return (this.getId().equals(cmsSiteAccessCountHour.getId()));
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