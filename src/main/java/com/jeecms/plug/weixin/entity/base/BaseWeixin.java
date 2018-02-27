package com.jeecms.plug.weixin.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jg_weixin table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jg_weixin"
 */

public abstract class BaseWeixin  implements Serializable {

	public static String REF = "Weixin";
	public static String PROP_SITE = "site";
	public static String PROP_ID = "id";
	public static String PROP_PIC = "pic";


	// constructors
	public BaseWeixin () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseWeixin (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	
	private java.lang.String welcome;
	private java.lang.String pic;

	// one to one
	private com.jeecms.core.entity.CmsSite site;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="site_id"
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


	public java.lang.String getWelcome() {
		return welcome;
	}

	public void setWelcome(java.lang.String welcome) {
		this.welcome = welcome;
	}

	/**
	 * Return the value associated with the column: wx_pic
	 */
	public java.lang.String getPic () {
		return pic;
	}

	/**
	 * Set the value related to the column: wx_pic
	 * @param pic the wx_pic value
	 */
	public void setPic (java.lang.String pic) {
		this.pic = pic;
	}


	/**
	 * Return the value associated with the column: site
	 */
	public com.jeecms.core.entity.CmsSite getSite () {
		return site;
	}

	/**
	 * Set the value related to the column: site
	 * @param site the site value
	 */
	public void setSite (com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.weixin.entity.Weixin)) return false;
		else {
			com.jeecms.plug.weixin.entity.Weixin weixin = (com.jeecms.plug.weixin.entity.Weixin) obj;
			if (null == this.getId() || null == weixin.getId()) return false;
			else return (this.getId().equals(weixin.getId()));
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