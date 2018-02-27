package com.jeecms.cms.dao.main;

import java.util.List;

import com.jeecms.cms.entity.main.CmsModelItem;
import com.jeecms.common.hibernate4.Updater;

public interface CmsModelItemDao {
	public List<CmsModelItem> getList(Integer modelId, boolean isChannel,
			Boolean hasDisabled);

	public CmsModelItem findById(Integer id);

	public CmsModelItem save(CmsModelItem bean);

	public CmsModelItem updateByUpdater(Updater<CmsModelItem> updater);

	public CmsModelItem deleteById(Integer id);
}