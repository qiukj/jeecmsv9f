package com.jeecms.cms.dao.main;

import java.util.List;

import com.jeecms.cms.entity.main.ContentType;
import com.jeecms.common.hibernate4.Updater;

public interface ContentTypeDao {
	public List<ContentType> getList(Boolean containDisabled);

	public ContentType getDef();

	public ContentType findById(Integer id);

	public ContentType save(ContentType bean);

	public ContentType updateByUpdater(Updater<ContentType> updater);

	public ContentType deleteById(Integer id);
}