package com.jeecms.cms.manager.assist;

import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import com.jeecms.cms.entity.assist.CmsSiteAccessCountHour;

public interface CmsSiteAccessCountHourMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<CmsSiteAccessCountHour> getList(Date date);
	
	public void statisticCount(Date date, Integer siteId);

	public CmsSiteAccessCountHour findById(Integer id);

	public CmsSiteAccessCountHour save(CmsSiteAccessCountHour bean);
}