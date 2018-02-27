package com.jeecms.cms.entity.main.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_content_record table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_content_record"
 */

public abstract class BaseContentRecord  implements Serializable {

	public static String REF = "ContentRecord";
	public static String PROP_OPERATE_TIME = "operateTime";
	public static String PROP_USER = "user";
	public static String PROP_CONTENT = "content";
	public static String PROP_ID = "id";
	public static String PROP_OPERATE_TYPE = "operateType";


	// constructors
	public BaseContentRecord () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseContentRecord (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseContentRecord (
		java.lang.Long id,
		com.jeecms.cms.entity.main.Content content,
		com.jeecms.core.entity.CmsUser user,
		java.util.Date operateTime,
		java.lang.Byte operateType) {

		this.setId(id);
		this.setContent(content);
		this.setUser(user);
		this.setOperateTime(operateTime);
		this.setOperateType(operateType);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.util.Date operateTime;
	private java.lang.Byte operateType;

	// many to one
	private com.jeecms.cms.entity.main.Content content;
	private com.jeecms.core.entity.CmsUser user;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="content_record_id"
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
	 * Return the value associated with the column: operate_time
	 */
	public java.util.Date getOperateTime () {
		return operateTime;
	}

	/**
	 * Set the value related to the column: operate_time
	 * @param operateTime the operate_time value
	 */
	public void setOperateTime (java.util.Date operateTime) {
		this.operateTime = operateTime;
	}


	/**
	 * Return the value associated with the column: operate_type
	 */
	public java.lang.Byte getOperateType () {
		return operateType;
	}

	/**
	 * Set the value related to the column: operate_type
	 * @param operateType the operate_type value
	 */
	public void setOperateType (java.lang.Byte operateType) {
		this.operateType = operateType;
	}


	/**
	 * Return the value associated with the column: content_id
	 */
	public com.jeecms.cms.entity.main.Content getContent () {
		return content;
	}

	/**
	 * Set the value related to the column: content_id
	 * @param content the content_id value
	 */
	public void setContent (com.jeecms.cms.entity.main.Content content) {
		this.content = content;
	}


	/**
	 * Return the value associated with the column: user_id
	 */
	public com.jeecms.core.entity.CmsUser getUser () {
		return user;
	}

	/**
	 * Set the value related to the column: user_id
	 * @param user the user_id value
	 */
	public void setUser (com.jeecms.core.entity.CmsUser user) {
		this.user = user;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.main.ContentRecord)) return false;
		else {
			com.jeecms.cms.entity.main.ContentRecord contentRecord = (com.jeecms.cms.entity.main.ContentRecord) obj;
			if (null == this.getId() || null == contentRecord.getId()) return false;
			else return (this.getId().equals(contentRecord.getId()));
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