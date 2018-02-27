package com.jeecms.cms.entity.main;

import com.jeecms.cms.entity.main.base.BaseApiUserLogin;



public class ApiUserLogin extends BaseApiUserLogin {
	private static final long serialVersionUID = 1L;
	public static Short USER_STATUS_LOGIN=1;
	public static Short USER_STATUS_LOGOUT=2;
	public static Short USER_STATUS_LOGOVERTIME=3;
	public static Short USER_STATUS_FORGE=4;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiUserLogin () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiUserLogin (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiUserLogin (
		java.lang.Long id,
		java.lang.String sesssionKey,
		java.lang.String username,
		java.util.Date loginTime,
		java.lang.Integer loginCount) {

		super (
			id,
			sesssionKey,
			username,
			loginTime,
			loginCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}