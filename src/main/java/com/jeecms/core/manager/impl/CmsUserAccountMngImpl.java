package com.jeecms.core.manager.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.core.entity.CmsUserAccount;
import com.jeecms.core.manager.CmsUserAccountMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.dao.CmsUserAccountDao;
import com.jeecms.core.entity.CmsUser;

@Service
@Transactional
public class CmsUserAccountMngImpl implements CmsUserAccountMng {
	
	@Transactional(readOnly = true)
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize){
		return dao.getPage(username,drawTimeBegin,drawTimeEnd,orderBy,pageNo,pageSize);
	}
	
	@Transactional(readOnly = true)
	public CmsUserAccount findById(Integer userId){
		return dao.findById(userId);
	}
	

	public CmsUserAccount updateAccountInfo(String accountWeiXin,
			String accountAlipy, Short drawAccount,CmsUser user) {
		CmsUserAccount entity = dao.findById(user.getId());
		if (entity == null) {
			CmsUserAccount account=new CmsUserAccount();
			account.setAccountAlipy(accountAlipy);
			account.setAccountWeixin(accountWeiXin);
			account.setDrawAccount(drawAccount);
			account.setContentDayAmount(0d);
			account.setContentMonthAmount(0d);
			account.setContentYearAmount(0d);
			account.setContentNoPayAmount(0d);
			account.setContentTotalAmount(0d);
			account.setDrawCount(0);
			account.setContentBuyCount(0);
			entity = save(account, user);
			user.getUserAccountSet().add(account);
			return entity;
		} else {
			entity.setAccountAlipy(accountAlipy);
			entity.setAccountWeixin(accountWeiXin);
			entity.setDrawAccount(drawAccount);
			return entity;
		}
	}
	
	/**
	 * 用户打赏或者购买
	 * @param payAmout 作者所得
	 * @param authorUser 作者
	 * @return
	 */
	public CmsUserAccount userPay(Double payAmout, CmsUser authorUser) {
		CmsUserAccount entity = dao.findById(authorUser.getId());
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if (entity != null) {
			if(entity.getLastBuyTime()!=null){
				last.setTime(entity.getLastBuyTime());
				int currDay = curr.get(Calendar.DAY_OF_YEAR);
				int lastDay = last.get(Calendar.DAY_OF_YEAR);
				int currYear=curr.get(Calendar.YEAR);
				int lastYear=last.get(Calendar.YEAR);
				int currMonth = curr.get(Calendar.MONTH);
				int lastMonth = last.get(Calendar.MONTH);
				if(lastYear!=currYear){
					entity.setContentYearAmount(0d);
					entity.setContentMonthAmount(0d);
					entity.setContentDayAmount(0d);
				}else{
					if(currMonth!=lastMonth){
						entity.setContentMonthAmount(0d);
						entity.setContentDayAmount(0d);
					}else{
						if (currDay != lastDay) {
							entity.setContentDayAmount(0d);
						}
					}
				}
			}
			entity.setContentDayAmount(entity.getContentDayAmount()+payAmout);
			entity.setContentMonthAmount(entity.getContentMonthAmount()+payAmout);
			entity.setContentYearAmount(entity.getContentYearAmount()+payAmout);
			entity.setContentTotalAmount(entity.getContentTotalAmount()+payAmout);
			entity.setLastBuyTime(curr.getTime());
			entity.setContentBuyCount(entity.getContentBuyCount()+1);
			entity.setContentNoPayAmount(entity.getContentNoPayAmount()+payAmout);
		} 
		return entity;
	}
	
	
	public CmsUserAccount payToAuthor(Double drawAmout, CmsUser user,Date payTime){
		CmsUserAccount entity = dao.findById(user.getId());
		if (entity != null&&drawAmout!=null) {
			if(entity.getContentNoPayAmount()>=drawAmout){
				entity.setDrawCount(entity.getDrawCount()+1);
				entity.setLastDrawTime(payTime);
				entity.setContentNoPayAmount(entity.getContentNoPayAmount()-drawAmout);
			}
		} 
		return entity;
	}
	
	public CmsUserAccount updateWeiXinOpenId(Integer userId,String weiXinOpenId){
		CmsUserAccount account=dao.findById(userId);
		account.setAccountWeixinOpenId(weiXinOpenId);
		return account;
	}

	private CmsUserAccount save(CmsUserAccount account, CmsUser user) {
		account.setUser(user);
		dao.save(account);
		return account;
	}

	private CmsUserAccountDao dao;

	@Autowired
	public void setDao(CmsUserAccountDao dao) {
		this.dao = dao;
	}
}