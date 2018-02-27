package com.jeecms.cms.entity.main.base;

import java.io.Serializable;

import com.jeecms.cms.entity.main.CmsModel;


/**
 * This is an object that contains data related to the jc_channel table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_channel"
 */

public abstract class BaseChannelModel  implements Serializable {

	public static String REF = "ChannelModel";
	public static String PROP_TPLCONTENT = "tplContent";


	// constructors
	public BaseChannelModel () {
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	
	protected void initialize () {}
	
	public BaseChannelModel(String tplContent, CmsModel model) {
		super();
		this.tplContent = tplContent;
		this.model = model;
	}



	// fields
	private java.lang.String tplContent;
	private java.lang.String tplMoibleContent;
	private CmsModel model;


	public java.lang.String getTplContent() {
		return tplContent;
	}

	public void setTplContent(java.lang.String tplContent) {
		this.tplContent = tplContent;
	}
	
	public java.lang.String getTplMoibleContent() {
		return tplMoibleContent;
	}

	public void setTplMoibleContent(java.lang.String tplMoibleContent) {
		this.tplMoibleContent = tplMoibleContent;
	}

	public CmsModel getModel() {
		return model;
	}

	public void setModel(CmsModel model) {
		this.model = model;
	}

	public String toString () {
		return super.toString();
	}


}