package com.jeecms.cms.manager.main;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.cms.entity.main.ContentBuy;

public interface ContentBuyMng {
	
	public ContentBuy contentOrder(Integer contentId,Integer orderType,
			Short chargeReward,Integer buyUserId,String outOrderNum);
	
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId
			,Short payMode,int pageNo, int pageSize);
	
	public List<ContentBuy> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Integer first, Integer count);
	
	public Pagination getPageByContent(Integer contentId,
			Short payMode,int pageNo, int pageSize);
	
	public List<ContentBuy> getListByContent(Integer contentId,
			Short payMode,Integer first, Integer count);

	public ContentBuy findById(Long id);
	
	public ContentBuy findByOrderNumber(String orderNumber);
	
	public ContentBuy findByOutOrderNum(String orderNum,Integer payMethod);
	
	public boolean hasBuyContent(Integer buyUserId,Integer contentId);

	public ContentBuy save(ContentBuy bean);

	public ContentBuy update(ContentBuy bean);

	public ContentBuy deleteById(Long id);
	
	public ContentBuy[] deleteByIds(Long[] ids);
}