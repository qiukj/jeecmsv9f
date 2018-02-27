package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_account_pay table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_account_pay"
 */

public abstract class BaseCmsAccountPay  implements Serializable {

	public static String REF = "CmsAccountPay";
	public static String PROP_DRAW_ACCOUNT = "drawAccount";
	public static String PROP_WEIXIN_NUM = "weixinNum";
	public static String PROP_ALIPAY_NUM = "alipayNum";
	public static String PROP_PAY_USER = "payUser";
	public static String PROP_ID = "id";
	public static String PROP_DRAW_NUM = "drawNum";
	public static String PROP_PAY_TIME = "payTime";
	public static String PROP_PAY_ACCOUNT = "payAccount";
	public static String PROP_DRAW_USER = "drawUser";


	// constructors
	public BaseCmsAccountPay () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsAccountPay (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsAccountPay (
		java.lang.Long id,
		com.jeecms.core.entity.CmsUser payUser,
		com.jeecms.core.entity.CmsUser drawUser,
		java.lang.String drawNum,
		java.lang.String payAccount,
		java.lang.String drawAccount,
		java.util.Date payTime) {

		this.setId(id);
		this.setPayUser(payUser);
		this.setDrawUser(drawUser);
		this.setDrawNum(drawNum);
		this.setPayAccount(payAccount);
		this.setDrawAccount(drawAccount);
		this.setPayTime(payTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String drawNum;
	private java.lang.String payAccount;
	private java.lang.String drawAccount;
	private java.util.Date payTime;
	private java.lang.String weixinNum;
	private java.lang.String alipayNum;

	// many to one
	private com.jeecms.core.entity.CmsUser payUser;
	private com.jeecms.core.entity.CmsUser drawUser;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="account_pay_id"
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
	 * Return the value associated with the column: draw_num
	 */
	public java.lang.String getDrawNum () {
		return drawNum;
	}

	/**
	 * Set the value related to the column: draw_num
	 * @param drawNum the draw_num value
	 */
	public void setDrawNum (java.lang.String drawNum) {
		this.drawNum = drawNum;
	}


	/**
	 * Return the value associated with the column: pay_account
	 */
	public java.lang.String getPayAccount () {
		return payAccount;
	}

	/**
	 * Set the value related to the column: pay_account
	 * @param payAccount the pay_account value
	 */
	public void setPayAccount (java.lang.String payAccount) {
		this.payAccount = payAccount;
	}


	/**
	 * Return the value associated with the column: draw_account
	 */
	public java.lang.String getDrawAccount () {
		return drawAccount;
	}

	/**
	 * Set the value related to the column: draw_account
	 * @param drawAccount the draw_account value
	 */
	public void setDrawAccount (java.lang.String drawAccount) {
		this.drawAccount = drawAccount;
	}


	/**
	 * Return the value associated with the column: pay_time
	 */
	public java.util.Date getPayTime () {
		return payTime;
	}

	/**
	 * Set the value related to the column: pay_time
	 * @param payTime the pay_time value
	 */
	public void setPayTime (java.util.Date payTime) {
		this.payTime = payTime;
	}


	/**
	 * Return the value associated with the column: weixin_num
	 */
	public java.lang.String getWeixinNum () {
		return weixinNum;
	}

	/**
	 * Set the value related to the column: weixin_num
	 * @param weixinNum the weixin_num value
	 */
	public void setWeixinNum (java.lang.String weixinNum) {
		this.weixinNum = weixinNum;
	}


	/**
	 * Return the value associated with the column: alipay_num
	 */
	public java.lang.String getAlipayNum () {
		return alipayNum;
	}

	/**
	 * Set the value related to the column: alipay_num
	 * @param alipayNum the alipay_num value
	 */
	public void setAlipayNum (java.lang.String alipayNum) {
		this.alipayNum = alipayNum;
	}


	/**
	 * Return the value associated with the column: pay_user_id
	 */
	public com.jeecms.core.entity.CmsUser getPayUser () {
		return payUser;
	}

	/**
	 * Set the value related to the column: pay_user_id
	 * @param payUser the pay_user_id value
	 */
	public void setPayUser (com.jeecms.core.entity.CmsUser payUser) {
		this.payUser = payUser;
	}


	/**
	 * Return the value associated with the column: draw_user_id
	 */
	public com.jeecms.core.entity.CmsUser getDrawUser () {
		return drawUser;
	}

	/**
	 * Set the value related to the column: draw_user_id
	 * @param drawUser the draw_user_id value
	 */
	public void setDrawUser (com.jeecms.core.entity.CmsUser drawUser) {
		this.drawUser = drawUser;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.assist.CmsAccountPay)) return false;
		else {
			com.jeecms.cms.entity.assist.CmsAccountPay cmsAccountPay = (com.jeecms.cms.entity.assist.CmsAccountPay) obj;
			if (null == this.getId() || null == cmsAccountPay.getId()) return false;
			else return (this.getId().equals(cmsAccountPay.getId()));
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