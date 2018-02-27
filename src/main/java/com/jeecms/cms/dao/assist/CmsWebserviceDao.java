package com.jeecms.cms.dao.assist;

import java.util.List;

import com.jeecms.cms.entity.assist.CmsWebservice;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsWebserviceDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<CmsWebservice> getList(String type);

	public CmsWebservice findById(Integer id);

	public CmsWebservice save(CmsWebservice bean);

	public CmsWebservice updateByUpdater(Updater<CmsWebservice> updater);

	public CmsWebservice deleteById(Integer id);
}