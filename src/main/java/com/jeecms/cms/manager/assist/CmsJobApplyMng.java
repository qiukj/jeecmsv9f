package com.jeecms.cms.manager.assist;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.cms.entity.assist.CmsJobApply;

public interface CmsJobApplyMng {
	public Pagination getPage(Integer userId,Integer contentId,Integer siteId,
			boolean cacheable,String title,int pageNo, int pageSize);
	
	public List<CmsJobApply> getList(Integer userId,Integer contentId,Integer siteId,
			boolean cacheable,String title,Integer first, Integer count);

	public CmsJobApply findById(Integer id);

	public CmsJobApply save(CmsJobApply bean);

	public CmsJobApply update(CmsJobApply bean);

	public CmsJobApply deleteById(Integer id);
	
	public CmsJobApply[] deleteByIds(Integer[] ids);
}