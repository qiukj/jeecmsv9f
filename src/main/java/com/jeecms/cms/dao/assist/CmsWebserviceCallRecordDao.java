package com.jeecms.cms.dao.assist;

import com.jeecms.cms.entity.assist.CmsWebserviceCallRecord;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsWebserviceCallRecordDao {
	public Pagination getPage(int pageNo, int pageSize);

	public CmsWebserviceCallRecord findById(Integer id);

	public CmsWebserviceCallRecord save(CmsWebserviceCallRecord bean);

	public CmsWebserviceCallRecord updateByUpdater(Updater<CmsWebserviceCallRecord> updater);

	public CmsWebserviceCallRecord deleteById(Integer id);
}