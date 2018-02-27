package com.jeecms.cms.manager.main;

import com.jeecms.common.page.Pagination;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.cms.entity.main.ApiAccount;

public interface ApiAccountMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public ApiAccount getApiAccount(HttpServletRequest request);
	
	public ApiAccount findByDefault();
	
	public ApiAccount findByAppId(String appId);

	public ApiAccount findById(Integer id);

	public ApiAccount save(ApiAccount bean);

	public ApiAccount update(ApiAccount bean,String appKey,String aesKey,String ivKey);

	public ApiAccount deleteById(Integer id);
	
	public ApiAccount[] deleteByIds(Integer[] ids);
}