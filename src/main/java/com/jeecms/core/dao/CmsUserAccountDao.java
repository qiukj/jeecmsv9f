package com.jeecms.core.dao;

import java.util.Date;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUserAccount;

public interface CmsUserAccountDao {
	
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public CmsUserAccount findById(Integer id);

	public CmsUserAccount save(CmsUserAccount bean);

	public CmsUserAccount updateByUpdater(Updater<CmsUserAccount> updater);
}