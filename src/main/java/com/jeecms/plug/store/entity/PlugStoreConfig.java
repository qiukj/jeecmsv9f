package com.jeecms.plug.store.entity;

import com.jeecms.plug.store.entity.base.BasePlugStoreConfig;



public class PlugStoreConfig extends BasePlugStoreConfig {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PlugStoreConfig () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PlugStoreConfig (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public PlugStoreConfig (
		java.lang.Integer id,
		java.lang.String serverUrl,
		java.lang.String passwod) {

		super (
			id,
			serverUrl,
			passwod);
	}

/*[CONSTRUCTOR MARKER END]*/


}