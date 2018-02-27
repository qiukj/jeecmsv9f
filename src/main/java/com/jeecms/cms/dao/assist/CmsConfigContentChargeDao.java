package com.jeecms.cms.dao.assist;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;

public interface CmsConfigContentChargeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public CmsConfigContentCharge findById(Integer id);

	public CmsConfigContentCharge save(CmsConfigContentCharge bean);

	public CmsConfigContentCharge updateByUpdater(Updater<CmsConfigContentCharge> updater);

	public CmsConfigContentCharge deleteById(Integer id);
}