package com.jeecms.cms.manager.main.impl;


import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AliPay;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserAccountMng;
import com.jeecms.core.manager.CmsUserMng;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jeecms.cms.dao.main.ContentBuyDao;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentBuy;
import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;
import com.jeecms.cms.manager.main.ContentBuyMng;
import com.jeecms.cms.manager.main.ContentChargeMng;
import com.jeecms.cms.manager.main.ContentMng;

@Service
@Transactional
public class ContentBuyMngImpl implements ContentBuyMng {
	
	public static final String WEIXIN_ORDER_QUERY_URL="weixin.orderquery.url";
	public static final String ALI_PAY_URL="alipay.openapi.url";
	
	public ContentBuy contentOrder(Integer contentId,Integer orderType,
			Short chargeReward,Integer buyUserId,String outOrderNum){
		ContentBuy contentBuy=new ContentBuy();
	    if(contentId!=null){
	    	Content content=contentMng.findById(contentId);
	    	if(content!=null){
   	    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
	    		//内容收费模式且本次订单是购买流程
	    		boolean buy=false;
	    		if(content.getCharge()&&chargeReward.equals(ContentCharge.MODEL_CHARGE)){
	    			buy=true;
	    		}
	    		CmsConfigContentCharge config=configContentChargeMng.getDefault();
	    		initWeiXinPayUrl();
	    		initAliPayUrl();
	    		Double orderAmount = 0d;
		 		// 这里是把微信商户的订单号放入了交易号中
		   	    if(orderType.equals(ContentBuy.PAY_METHOD_WECHAT)){
		   	    	contentBuy.setOrderNumWeiXin(outOrderNum);
		   	    	orderAmount=getWeChatOrderAmount(outOrderNum, config);
		   	    }else if(orderType.equals(ContentBuy.PAY_METHOD_ALIPAY)){
		   	    	contentBuy.setOrderNumAliPay(outOrderNum);
		   	    	orderAmount=getAliPayOrderAmount(outOrderNum, config);
		   	    }
		   	    //订单金额不能等于0
		   	    if(!orderAmount.equals(0d)){
		   	    	//购买行为订单金额需要大于内容收费金额 或者是打赏新闻
		   	    	if((buy&&orderAmount>=content.getChargeAmount())||!buy){
		   	    		Double ratio=config.getChargeRatio();
				   	    contentBuy.setAuthorUser(content.getUser());
				   	    //打赏可以匿名
				   	    if(buyUserId!=null){
				   	    	contentBuy.setBuyUser(userMng.findById(buyUserId));
				   	    }
				   	    contentBuy.setContent(content);
				   	    contentBuy.setHasPaidAuthor(false);
				   	    String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
				   	    contentBuy.setOrderNumber(orderNumber);
				   	    contentBuy.setBuyTime(Calendar.getInstance().getTime());
				   	    Double chargeAmount=content.getChargeAmount();
				   	    Double platAmount=content.getChargeAmount()*ratio;
				     	Double authorAmount=content.getChargeAmount()*(1-ratio);
				   	    if(orderAmount!=null){
				   	    	chargeAmount=orderAmount;
				   	    	platAmount=orderAmount*ratio;
				   	    	authorAmount=orderAmount*(1-ratio);
				   	    }
				   	    if(chargeReward.equals(ContentCharge.MODEL_REWARD)){
				   	    	contentBuy.setChargeReward(ContentCharge.MODEL_REWARD);
				   	    }else{
				   	    	contentBuy.setChargeReward(ContentCharge.MODEL_CHARGE);
				   	    }
				   	    contentBuy.setChargeAmount(chargeAmount);
				   	    contentBuy.setPlatAmount(platAmount);
				   	    contentBuy.setAuthorAmount(authorAmount);
			 			contentBuy=contentBuyMng.save(contentBuy);
			 			CmsUser authorUser=contentBuy.getAuthorUser();
			 			//笔者所得统计
			 			userAccountMng.userPay(contentBuy.getAuthorAmount(), authorUser);
			 			//平台所得统计
			 			configContentChargeMng.afterUserPay(contentBuy.getPlatAmount());
			 			//内容所得统计
			 			contentChargeMng.afterUserPay(contentBuy.getChargeAmount(), contentBuy.getContent());
			 			contentBuy.setPrePayStatus(ContentBuy.PRE_PAY_STATUS_SUCCESS);
		   	    	}else{
		   	    		contentBuy.setPrePayStatus(ContentBuy.PRE_PAY_STATUS_SUCCESS);
		   	    	}
		   	    }else{
		   	    	contentBuy.setPrePayStatus(ContentBuy.PRE_PAY_STATUS_ORDER_NUM_ERROR);
		   	    }
	    	}
	 	}
	    return contentBuy;
	}
	@Transactional(readOnly = true)
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId,
			Short payMode,int pageNo, int pageSize) {
		Pagination page = dao.getPage(orderNum,buyUserId,
				authorUserId,payMode,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<ContentBuy> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Integer first, Integer count){
		return dao.getList(orderNum, buyUserId, authorUserId,
				payMode, first, count);
	}
	
	@Transactional(readOnly = true)
	public Pagination getPageByContent(Integer contentId,
			Short payMode,int pageNo, int pageSize){
		return dao.getPageByContent(contentId,payMode,pageNo,pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<ContentBuy> getListByContent(Integer contentId, Short payMode, Integer first, Integer count) {
		return dao.getListByContent(contentId, payMode, first, count);
	}

	@Transactional(readOnly = true)
	public ContentBuy findById(Long id) {
		ContentBuy entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ContentBuy findByOrderNumber(String orderNumber){
		return dao.findByOrderNumber(orderNumber);
	}
	
	@Transactional(readOnly = true)
	public ContentBuy findByOutOrderNum(String orderNum,Integer payMethod){
		return dao.findByOutOrderNum(orderNum, payMethod);
	}
	
	@Transactional(readOnly = true)
	public boolean hasBuyContent(Integer buyUserId,Integer contentId){
		ContentBuy buy=dao.find(buyUserId, contentId);
		//用户已经购买并且是收费订单非打赏订单
		if(buy!=null&&buy.getUserHasPaid()&&buy.getChargeReward()==ContentCharge.MODEL_CHARGE){
			return true;
		}else{
			return false;
		}
	}

	public ContentBuy save(ContentBuy bean) {
		dao.save(bean);
		return bean;
	}

	public ContentBuy update(ContentBuy bean) {
		Updater<ContentBuy> updater = new Updater<ContentBuy>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ContentBuy deleteById(Long id) {
		ContentBuy bean = dao.deleteById(id);
		return bean;
	}
	
	public ContentBuy[] deleteByIds(Long[] ids) {
		ContentBuy[] beans = new ContentBuy[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	private Double getWeChatOrderAmount(String outOrderNum,
			CmsConfigContentCharge config){
		Map<String, String>map=WeixinPay.weixinOrderQuery(outOrderNum,
	    			null, getWeiXinPayUrl(), config);
	    String returnCode = map.get("return_code");
	    Double orderAmount=0d;
		if(StringUtils.isNotBlank(returnCode)){
			if (returnCode.equalsIgnoreCase("SUCCESS")) {
			 if (map.get("result_code").equalsIgnoreCase(
					"SUCCESS")) {
				String trade_state = map.get("trade_state");
				//支付成功
				if(trade_state.equalsIgnoreCase("SUCCESS")){
					String total_fee= map.get("total_fee");
					Integer totalFee=Integer.parseInt(total_fee);
					if(totalFee!=0){
						orderAmount=totalFee/100.0;
					}
				}
			 }
			}
		}
		return orderAmount;
	}
	
	private Double getAliPayOrderAmount(String outOrderNum,
			CmsConfigContentCharge config){
		AlipayTradeQueryResponse res=AliPay.query(getAliPayUrl(), config,
				null,outOrderNum);
		Double orderAmount=0d;
		if (null != res && res.isSuccess()) {
			if (res.getCode().equals("10000")) {
				if ("TRADE_SUCCESS".equalsIgnoreCase(res
						.getTradeStatus())) {
					String totalAmout=res.getTotalAmount();
					if(StringUtils.isNotBlank(totalAmout)){
						orderAmount=Double.parseDouble(totalAmout);
					}
				} 
			}
		}
		return orderAmount;
	}
	
	private void initAliPayUrl(){
		if(getAliPayUrl()==null){
			setAliPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),ALI_PAY_URL));
		}
	}
	
	private void initWeiXinPayUrl(){
		if(getWeiXinPayUrl()==null){
			setWeiXinPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_ORDER_QUERY_URL));
		}
	}
	
	private String weiXinPayUrl;
	
	private String aliPayUrl;
	
	public String getWeiXinPayUrl() {
		return weiXinPayUrl;
	}

	public void setWeiXinPayUrl(String weiXinPayUrl) {
		this.weiXinPayUrl = weiXinPayUrl;
	}

	public String getAliPayUrl() {
		return aliPayUrl;
	}

	public void setAliPayUrl(String aliPayUrl) {
		this.aliPayUrl = aliPayUrl;
	}

	private ContentBuyDao dao;
	@Autowired
	private ContentMng contentMng;
	@Autowired
	private ContentChargeMng contentChargeMng;
	@Autowired
	private ContentBuyMng contentBuyMng;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
	@Autowired
	private CmsUserAccountMng userAccountMng;
	@Autowired
	private CmsUserMng userMng;
	@Autowired
	private RealPathResolver realPathResolver;

	@Autowired
	public void setDao(ContentBuyDao dao) {
		this.dao = dao;
	}
}