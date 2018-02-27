package com.jeecms.plug.weixin.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.plug.weixin.entity.Weixin;

public interface WeixinMng {

	public Pagination getPage(Integer siteId,int pageNo,int pageSize);
	
	public Weixin findById(Integer id);
	
	public Weixin find(Integer siteId);
	
	public Weixin save(Weixin bean);
	
	public Weixin update(Weixin bean);
	
	public Weixin deleteById(Integer id);
	
	public Weixin[] delete(Integer[] id);
}
