package com.jeecms.core.entity.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the jc_oss table. Do not
 * modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class table="jc_oss"
 */

public abstract class BaseCmsOss implements Serializable {

	public static String REF = "CmsOss";
	public static String PROP_BUCKET_NAME = "bucketName";
	public static String PROP_APP_ID = "appId";
	public static String PROP_APP_KEY = "appKey";
	public static String PROP_BUCKET_AREA = "bucketArea";
	public static String PROP_ID = "id";
	public static String PROP_OSS_TYPE = "ossType";

	// constructors
	public BaseCmsOss() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsOss(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */

	protected void initialize() {
	}

	public BaseCmsOss(String ossAppId, String secretId, String appKey, String bucketName, String bucketArea,
			String endPoint, String accessDomain, Byte ossType) {
		super();
		this.ossAppId = ossAppId;
		this.secretId = secretId;
		this.appKey = appKey;
		this.bucketName = bucketName;
		this.bucketArea = bucketArea;
		this.endPoint = endPoint;
		this.accessDomain = accessDomain;
		this.ossType = ossType;
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String ossAppId;
	private java.lang.String secretId;
	private java.lang.String appKey;
	private java.lang.String bucketName;
	private java.lang.String bucketArea;
	private java.lang.String endPoint;
	private java.lang.String accessDomain;
	private java.lang.String name;
	private java.lang.Byte ossType;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="sequence" column="id"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public java.lang.String getOssAppId() {
		return ossAppId;
	}

	public void setOssAppId(java.lang.String ossAppId) {
		this.ossAppId = ossAppId;
	}

	public java.lang.String getSecretId() {
		return secretId;
	}

	public void setSecretId(java.lang.String secretId) {
		this.secretId = secretId;
	}

	/**
	 * Return the value associated with the column: app_key
	 */
	public java.lang.String getAppKey() {
		return appKey;
	}

	/**
	 * Set the value related to the column: app_key
	 * 
	 * @param appKey
	 *            the app_key value
	 */
	public void setAppKey(java.lang.String appKey) {
		this.appKey = appKey;
	}

	/**
	 * Return the value associated with the column: bucket_name
	 */
	public java.lang.String getBucketName() {
		return bucketName;
	}

	/**
	 * Set the value related to the column: bucket_name
	 * 
	 * @param bucketName
	 *            the bucket_name value
	 */
	public void setBucketName(java.lang.String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * Return the value associated with the column: bucket_area
	 */
	public java.lang.String getBucketArea() {
		return bucketArea;
	}

	/**
	 * Set the value related to the column: bucket_area
	 * 
	 * @param bucketArea
	 *            the bucket_area value
	 */
	public void setBucketArea(java.lang.String bucketArea) {
		this.bucketArea = bucketArea;
	}

	public java.lang.String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(java.lang.String endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * 阿里云存储accessDomain设置不带http://
	 * 七牛云存储accessDomain得设置带http://
	 * @return
	 */
	public java.lang.String getAccessDomain() {
		return accessDomain;
	}

	public void setAccessDomain(java.lang.String accessDomain) {
		this.accessDomain = accessDomain;
	}

	/**
	 * Return the value associated with the column: oss_type
	 */
	public java.lang.Byte getOssType() {
		return ossType;
	}

	/**
	 * Set the value related to the column: oss_type
	 * 
	 * @param ossType
	 *            the oss_type value
	 */
	public void setOssType(java.lang.Byte ossType) {
		this.ossType = ossType;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.jeecms.core.entity.CmsOss))
			return false;
		else {
			com.jeecms.core.entity.CmsOss cmsOss = (com.jeecms.core.entity.CmsOss) obj;
			if (null == this.getId() || null == cmsOss.getId())
				return false;
			else
				return (this.getId().equals(cmsOss.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}

}