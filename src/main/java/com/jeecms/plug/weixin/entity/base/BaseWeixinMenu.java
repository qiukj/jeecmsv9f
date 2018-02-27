package com.jeecms.plug.weixin.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jg_weixinmenu table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jg_weixinmenu"
 */

public abstract class BaseWeixinMenu  implements Serializable {

	public static String REF = "WeixinMenu";
	public static String PROP_NAME = "name";
	public static String PROP_PARENT = "parent";
	public static String PROP_SITE = "site";
	public static String PROP_KEY = "key";
	public static String PROP_URL = "url";
	public static String PROP_TYPE = "type";
	public static String PROP_ID = "id";


	// constructors
	public BaseWeixinMenu () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseWeixinMenu (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String type;
	private java.lang.String url;
	private java.lang.String key;

	// many to one
	private com.jeecms.plug.weixin.entity.WeixinMenu parent;
	private com.jeecms.core.entity.CmsSite site;

	// collections
	private java.util.Set<com.jeecms.plug.weixin.entity.WeixinMenu> child;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="wm_id"
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
	 * Return the value associated with the column: wm_name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: wm_name
	 * @param name the wm_name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: wm_type
	 */
	public java.lang.String getType () {
		return type;
	}

	/**
	 * Set the value related to the column: wm_type
	 * @param type the wm_type value
	 */
	public void setType (java.lang.String type) {
		this.type = type;
	}



	/**
	 * Return the value associated with the column: wm_url
	 */
	public java.lang.String getUrl () {
		return url;
	}

	/**
	 * Set the value related to the column: wm_url
	 * @param url the wm_url value
	 */
	public void setUrl (java.lang.String url) {
		this.url = url;
	}



	/**
	 * Return the value associated with the column: wm_key
	 */
	public java.lang.String getKey () {
		return key;
	}

	/**
	 * Set the value related to the column: wm_key
	 * @param key the wm_key value
	 */
	public void setKey (java.lang.String key) {
		this.key = key;
	}



	/**
	 * Return the value associated with the column: wm_parent_id
	 */
	public com.jeecms.plug.weixin.entity.WeixinMenu getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: wm_parent_id
	 * @param parent the wm_parent_id value
	 */
	public void setParent (com.jeecms.plug.weixin.entity.WeixinMenu parent) {
		this.parent = parent;
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



	/**
	 * Return the value associated with the column: child
	 */
	public java.util.Set<com.jeecms.plug.weixin.entity.WeixinMenu> getChild () {
		return child;
	}

	/**
	 * Set the value related to the column: child
	 * @param child the child value
	 */
	public void setChild (java.util.Set<com.jeecms.plug.weixin.entity.WeixinMenu> child) {
		this.child = child;
	}

	public void addTochild (com.jeecms.plug.weixin.entity.WeixinMenu weixinMenu) {
		if (null == getChild()) setChild(new java.util.TreeSet<com.jeecms.plug.weixin.entity.WeixinMenu>());
		getChild().add(weixinMenu);
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.weixin.entity.WeixinMenu)) return false;
		else {
			com.jeecms.plug.weixin.entity.WeixinMenu weixinMenu = (com.jeecms.plug.weixin.entity.WeixinMenu) obj;
			if (null == this.getId() || null == weixinMenu.getId()) return false;
			else return (this.getId().equals(weixinMenu.getId()));
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