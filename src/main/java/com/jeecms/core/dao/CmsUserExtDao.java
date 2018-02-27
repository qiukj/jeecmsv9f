package com.jeecms.core.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.core.entity.CmsUserExt;

public interface CmsUserExtDao {
	public CmsUserExt findById(Integer id);

	public CmsUserExt save(CmsUserExt bean);

	public CmsUserExt updateByUpdater(Updater<CmsUserExt> updater);
	
	public void clearDayCount();
}