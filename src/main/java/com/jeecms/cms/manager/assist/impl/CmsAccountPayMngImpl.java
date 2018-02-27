package com.jeecms.cms.manager.assist.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.entity.CmsUserAccount;
import com.jeecms.core.manager.CmsUserAccountMng;

import com.jeecms.cms.dao.assist.CmsAccountPayDao;
import com.jeecms.cms.entity.assist.CmsAccountDraw;
import com.jeecms.cms.entity.assist.CmsAccountPay;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.manager.assist.CmsAccountDrawMng;
import com.jeecms.cms.manager.assist.CmsAccountPayMng;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;

@Service
@Transactional
public class CmsAccountPayMngImpl implements CmsAccountPayMng {
	private static final Logger log = LoggerFactory.getLogger(CmsAccountPayMng.class);

	public static final String WEIXINPAY_CERT = "WEB-INF/cert/weixinpay_cert.p12";
	//微信企业转账
	public String weixinTransferPay(String serverUrl,Integer drawId,
			CmsUser drawUser,CmsUser payUser,Double payAmount,String orderNum,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		if(getPkcFile()==null){
			setPkcFile(new File(realPathResolver.get(WEIXINPAY_CERT)));
		}
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		CmsUserAccount drawUserAccount=drawUser.getUserAccount();
		if(drawUserAccount==null){
			return MessageResolver.getMessage(request,"transferPay.fail.userAccount.notfound"); 
		}else{
			if(StringUtils.isBlank(drawUser.getUserAccount().getAccountWeixinOpenId())){
				return MessageResolver.getMessage(request,"transferPay.fail.userAccount.notAuth"); 
			}
		}
		if (StringUtils.isNotBlank(config.getWeixinAppId())
				&& StringUtils.isNotBlank(config.getWeixinAccount())) {
			Object result[]=WeixinPay.payToUser(config, getPkcFile(), serverUrl, orderNum,
					drawUser.getUserAccount().getAccountWeixinOpenId(),
					drawUser.getRealname(),  payAmount, 
					MessageResolver.getMessage(request, "cmsAccountDraw.payAccount"),
					RequestUtils.getIpAddr(request));
			String resXml=(String) result[1];
			boolean postError=(Boolean) result[0];
			if(!postError){
				Map<String, String> map=new HashMap<String, String>();
				try {
					map = PayUtil.parseXMLToMap(resXml);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				String returnCode = map.get("return_code");
				if (returnCode.equalsIgnoreCase("FAIL")) {
					//支付失败
					return map.get("return_msg");
				} else if (returnCode.equalsIgnoreCase("SUCCESS")) {
					if (map.get("err_code") != null) {
						//支付失败
						return map.get("err_code_des");
					} else if (map.get("result_code").equalsIgnoreCase(
							"SUCCESS")) {
						//支付成功
						String paymentNo = map.get("payment_no");
						String payment_time = map.get("payment_time");
						try {
							afterPay(drawId, drawUser, payUser, payAmount, orderNum, paymentNo, null, 
									DateUtils.common_format.parse(payment_time),config);
						} catch (ParseException e) {
							//e.printStackTrace();
						}
						return MessageResolver.getMessage(request,"transferPay.success");
					}
				}
			}
			//通信失败
			return MessageResolver.getMessage(request,"error.connect.timeout");
		} else {
			//参数缺失
			return MessageResolver.getMessage(request,"error.contentCharge.need.appid");
		}
	}
	
	private void afterPay(Integer drawId,CmsUser drawUser,CmsUser payUser,
			Double payAmount,String orderNum,
			String weixinNo,String alipyNo,Date payTime,CmsConfigContentCharge config){
		CmsAccountPay pay=new CmsAccountPay();
		//保存支付记录
		if(drawUser.getDrawAccount()==CmsUserAccount.DRAW_WEIXIN){
			pay.setDrawAccount(drawUser.getAccountWeixin());
			pay.setPayAccount(config.getWeixinAccount());
		}else{
			pay.setDrawAccount(drawUser.getAccountAlipy());
			pay.setPayAccount(config.getAlipayAccount());
		}
		pay.setDrawNum(orderNum);
		pay.setDrawUser(drawUser);
		pay.setPayTime(payTime);
		pay.setPayUser(payUser);
		pay.setWeixinNum(weixinNo);
		pay.setAlipayNum(alipyNo);
		pay=accountPayMng.save(pay);
		//处理申请状态
		CmsAccountDraw draw=accountDrawMng.findById(drawId);
		if(draw!=null){
			draw.setAccountPay(pay);
			draw.setApplyStatus(CmsAccountDraw.DRAW_SUCC);
			accountDrawMng.update(draw);
		}
		//处理提现者账户
		userAccountMng.payToAuthor(payAmount, drawUser, payTime);
	}
		
	@Transactional(readOnly = true)
	public Pagination getPage(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,int pageNo, int pageSize) {
		Pagination page = dao.getPage(drawNum,payUserId,drawUserId,payTimeBegin
				,payTimeEnd,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public CmsAccountPay findById(Long id) {
		CmsAccountPay entity = dao.findById(id);
		return entity;
	}

	public CmsAccountPay save(CmsAccountPay bean) {
		dao.save(bean);
		return bean;
	}

	public CmsAccountPay update(CmsAccountPay bean) {
		Updater<CmsAccountPay> updater = new Updater<CmsAccountPay>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public CmsAccountPay deleteById(Long id) {
		CmsAccountPay bean = dao.deleteById(id);
		return bean;
	}
	
	public CmsAccountPay[] deleteByIds(Long[] ids) {
		CmsAccountPay[] beans = new CmsAccountPay[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private File pkcFile;
	public File getPkcFile() {
		return pkcFile;
	}

	public void setPkcFile(File pkcFile) {
		this.pkcFile = pkcFile;
	}
	private CmsAccountPayDao dao;
	@Autowired
	private CmsAccountDrawMng accountDrawMng;
	@Autowired
	private CmsAccountPayMng accountPayMng;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsUserAccountMng userAccountMng;

	@Autowired
	public void setDao(CmsAccountPayDao dao) {
		this.dao = dao;
	}
}