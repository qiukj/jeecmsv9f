package com.jeecms.cms.dao.assist;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.entity.assist.CmsSearchWords;

public interface CmsSearchWordsDao {
	public Pagination getPage(Integer siteId,String name,Integer recommend,
			Integer orderBy,int pageNo, int pageSize);

	public List<CmsSearchWords> getList(Integer siteId,String name,
			Integer recommend,Integer orderBy,Integer first,
			Integer count,boolean cacheable);

	public CmsSearchWords findById(Integer id);
	
	public CmsSearchWords findByName(String name);

	public CmsSearchWords save(CmsSearchWords bean);

	public CmsSearchWords updateByUpdater(Updater<CmsSearchWords> updater);

	public CmsSearchWords deleteById(Integer id);

	
}