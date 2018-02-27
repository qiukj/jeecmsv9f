package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_webservice table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_webservice"
 */

public abstract class BaseCmsWebserviceParam  implements Serializable {

	public static String REF = "CmsWebserviceParam";
	public static String PROP_PARAM_NAME = "paramName";
	public static String PROP_DEFAULT_VALUE = "defaultValue";


	// constructors
	public BaseCmsWebserviceParam () {
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsWebserviceParam (
		java.lang.String paramName) {

		this.setParamName(paramName);
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.String paramName;
	private java.lang.String defaultValue;






	/**
	 * Return the value associated with the column: param_name
	 */
	public java.lang.String getParamName () {
		return paramName;
	}

	/**
	 * Set the value related to the column: param_name
	 * @param paramName the param_name value
	 */
	public void setParamName (java.lang.String paramName) {
		this.paramName = paramName;
	}


	/**
	 * Return the value associated with the column: default_value
	 */
	public java.lang.String getDefaultValue () {
		return defaultValue;
	}

	/**
	 * Set the value related to the column: default_value
	 * @param defaultValue the default_value value
	 */
	public void setDefaultValue (java.lang.String defaultValue) {
		this.defaultValue = defaultValue;
	}






	public String toString () {
		return super.toString();
	}


}