package com.jeecms.plug.store.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.plug.store.entity.PlugStoreConfig;

public interface PlugStoreConfigMng {
	public Pagination getPage(int pageNo, int pageSize);

	public PlugStoreConfig findById(Integer id);
	
	public PlugStoreConfig getDefault();

	public PlugStoreConfig save(PlugStoreConfig bean);

	public PlugStoreConfig update(PlugStoreConfig bean,String password);

	public PlugStoreConfig deleteById(Integer id);
	
	public PlugStoreConfig[] deleteByIds(Integer[] ids);
}