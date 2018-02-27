package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_account_draw table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_account_draw"
 */

public abstract class BaseCmsAccountDraw  implements Serializable {

	public static String REF = "CmsAccountDraw";
	public static String PROP_APPLY_TIME = "applyTime";
	public static String PROP_ACCOUNT_PAY = "accountPay";
	public static String PROP_APPLY_AMOUNT = "applyAmount";
	public static String PROP_APPLY_STATUS = "applyStatus";
	public static String PROP_ID = "id";
	public static String PROP_APPLY_ACCOUNT = "applyAccount";
	public static String PROP_DRAW_USER = "drawUser";


	// constructors
	public BaseCmsAccountDraw () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCmsAccountDraw (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseCmsAccountDraw (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsUser drawUser,
		java.lang.String applyAccount,
		java.lang.Double applyAmount,
		java.lang.Short applyStatus,
		java.util.Date applyTime) {

		this.setId(id);
		this.setDrawUser(drawUser);
		this.setApplyAccount(applyAccount);
		this.setApplyAmount(applyAmount);
		this.setApplyStatus(applyStatus);
		this.setApplyTime(applyTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String applyAccount;
	private java.lang.Double applyAmount;
	private java.lang.Short applyStatus;
	private java.util.Date applyTime;

	// many to one
	private com.jeecms.core.entity.CmsUser drawUser;
	private com.jeecms.cms.entity.assist.CmsAccountPay accountPay;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="account_draw_id"
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
	 * Return the value associated with the column: apply_account
	 */
	public java.lang.String getApplyAccount () {
		return applyAccount;
	}

	/**
	 * Set the value related to the column: apply_account
	 * @param applyAccount the apply_account value
	 */
	public void setApplyAccount (java.lang.String applyAccount) {
		this.applyAccount = applyAccount;
	}


	/**
	 * Return the value associated with the column: apply_amount
	 */
	public java.lang.Double getApplyAmount () {
		return applyAmount;
	}

	/**
	 * Set the value related to the column: apply_amount
	 * @param applyAmount the apply_amount value
	 */
	public void setApplyAmount (java.lang.Double applyAmount) {
		this.applyAmount = applyAmount;
	}


	/**
	 * Return the value associated with the column: apply_status
	 */
	public java.lang.Short getApplyStatus () {
		return applyStatus;
	}

	/**
	 * Set the value related to the column: apply_status
	 * @param applyStatus the apply_status value
	 */
	public void setApplyStatus (java.lang.Short applyStatus) {
		this.applyStatus = applyStatus;
	}


	/**
	 * Return the value associated with the column: apply_time
	 */
	public java.util.Date getApplyTime () {
		return applyTime;
	}

	/**
	 * Set the value related to the column: apply_time
	 * @param applyTime the apply_time value
	 */
	public void setApplyTime (java.util.Date applyTime) {
		this.applyTime = applyTime;
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


	/**
	 * Return the value associated with the column: account_pay_id
	 */
	public com.jeecms.cms.entity.assist.CmsAccountPay getAccountPay () {
		return accountPay;
	}

	/**
	 * Set the value related to the column: account_pay_id
	 * @param accountPay the account_pay_id value
	 */
	public void setAccountPay (com.jeecms.cms.entity.assist.CmsAccountPay accountPay) {
		this.accountPay = accountPay;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.cms.entity.assist.CmsAccountDraw)) return false;
		else {
			com.jeecms.cms.entity.assist.CmsAccountDraw cmsAccountDraw = (com.jeecms.cms.entity.assist.CmsAccountDraw) obj;
			if (null == this.getId() || null == cmsAccountDraw.getId()) return false;
			else return (this.getId().equals(cmsAccountDraw.getId()));
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