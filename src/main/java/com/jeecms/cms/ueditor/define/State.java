package com.jeecms.cms.ueditor.define;

/**
 * 处理状态接口
 *
 */
public interface State {
	
	public boolean isSuccess ();
	
	public void putInfo( String name, String val );
	
	public void putInfo ( String name, long val );
	
	public String toJSONString ();
	
	public String toJSONString2();

}
