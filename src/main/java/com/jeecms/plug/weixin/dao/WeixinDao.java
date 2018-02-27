package com.jeecms.plug.weixin.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.entity.Weixin;

public interface WeixinDao {
	
	public Pagination getPage(Integer siteId,int pageNo,int pageSize);
	
	public Weixin save(Weixin bean);
	
	public Weixin deleteById(Integer id);
	
	public Weixin findById(Integer id);
	
	public Weixin find(Integer siteId);

	public Weixin updateByUpdater(Updater<Weixin> updater);
}
