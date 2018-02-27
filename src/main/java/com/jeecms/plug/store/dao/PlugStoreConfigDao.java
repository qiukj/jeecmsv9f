package com.jeecms.plug.store.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.store.entity.PlugStoreConfig;

public interface PlugStoreConfigDao {
	public Pagination getPage(int pageNo, int pageSize);

	public PlugStoreConfig findById(Integer id);

	public PlugStoreConfig save(PlugStoreConfig bean);

	public PlugStoreConfig updateByUpdater(Updater<PlugStoreConfig> updater);

	public PlugStoreConfig deleteById(Integer id);
}