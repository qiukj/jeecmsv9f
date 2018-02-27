package com.jeecms.cms.dao.main;

import java.util.Date;
import java.util.List;

import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface ContentChargeDao {
	
	public List<ContentCharge> getList(String contentTitle,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count);
	
	public Pagination getPage(String contentTitle,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public ContentCharge findById(Integer id);

	public ContentCharge save(ContentCharge bean);

	public ContentCharge updateByUpdater(Updater<ContentCharge> updater);
}