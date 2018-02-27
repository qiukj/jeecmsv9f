package com.jeecms.cms.dao.assist;

import com.jeecms.cms.entity.assist.CmsWebserviceAuth;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsWebserviceAuthDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public CmsWebserviceAuth findByUsername(String username);

	public CmsWebserviceAuth findById(Integer id);

	public CmsWebserviceAuth save(CmsWebserviceAuth bean);

	public CmsWebserviceAuth updateByUpdater(Updater<CmsWebserviceAuth> updater);

	public CmsWebserviceAuth deleteById(Integer id);
}