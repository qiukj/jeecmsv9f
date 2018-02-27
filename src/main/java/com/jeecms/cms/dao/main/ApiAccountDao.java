package com.jeecms.cms.dao.main;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.cms.entity.main.ApiAccount;

public interface ApiAccountDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<ApiAccount> getList(int first, int count);
	
	public ApiAccount findByAppId(String appId);
	
	public ApiAccount findAdmin();

	public ApiAccount findById(Integer id);

	public ApiAccount save(ApiAccount bean);

	public ApiAccount updateByUpdater(Updater<ApiAccount> updater);

	public ApiAccount deleteById(Integer id);
}