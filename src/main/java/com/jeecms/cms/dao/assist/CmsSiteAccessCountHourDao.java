package com.jeecms.cms.dao.assist;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;

public interface CmsSiteAccessCountHourDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<CmsSiteAccessCountHour> getList(Date date);

	public CmsSiteAccessCountHour findById(Integer id);

	public CmsSiteAccessCountHour save(CmsSiteAccessCountHour bean);

	public CmsSiteAccessCountHour updateByUpdater(Updater<CmsSiteAccessCountHour> updater);

	public CmsSiteAccessCountHour deleteById(Integer id);
}