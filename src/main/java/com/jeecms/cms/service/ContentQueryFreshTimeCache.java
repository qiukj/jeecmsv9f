package com.jeecms.cms.service;

import java.util.Date;

/**
 * 内容查询时间缓存接口
 */
public interface ContentQueryFreshTimeCache {

	/**
	 * 内容查询时间缓存立即更新
	 */
	public void clearCache();
	/**
	 * 获取缓存时间
	 * @return
	 */
	public Date getTime();
	/**
	 * 修改缓存时间(分钟)
	 * @param interval
	 */
	public void setInterval(int interval);
}
