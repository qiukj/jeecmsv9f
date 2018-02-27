package com.jeecms.cms.dao.main;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.cms.entity.main.ApiInfo;

public interface ApiInfoDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public ApiInfo findByUrl(String url);

	public ApiInfo findById(Integer id);

	public ApiInfo save(ApiInfo bean);

	public ApiInfo updateByUpdater(Updater<ApiInfo> updater);

	public ApiInfo deleteById(Integer id);
}