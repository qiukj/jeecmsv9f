package com.jeecms.cms.dao.main;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.cms.entity.main.ContentRecord;

public interface ContentRecordDao {
	public Pagination getPage(int pageNo, int pageSize);

	public ContentRecord findById(Long id);

	public ContentRecord save(ContentRecord bean);

	public ContentRecord updateByUpdater(Updater<ContentRecord> updater);

	public ContentRecord deleteById(Long id);

	public List<ContentRecord> getListByContentId(Integer contentId);
}