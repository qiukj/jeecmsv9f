package com.jeecms.cms.manager.assist;

import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsUser;

import java.util.Date;
import java.util.List;

import com.jeecms.cms.entity.assist.CmsAccountDraw;

public interface CmsAccountDrawMng {
	
	public CmsAccountDraw draw(CmsUser user,Double amount,String applyAccount);
	
	public Double getAppliedSum(Integer userId);
	
	public Pagination getPage(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,int pageNo, int pageSize);
	
	public List<CmsAccountDraw> getList(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,Integer first,Integer count);

	public CmsAccountDraw findById(Integer id);

	public CmsAccountDraw save(CmsAccountDraw bean);

	public CmsAccountDraw update(CmsAccountDraw bean);

	public CmsAccountDraw deleteById(Integer id);
	
	public CmsAccountDraw[] deleteByIds(Integer[] ids);
}