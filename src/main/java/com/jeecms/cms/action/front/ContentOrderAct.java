package com.jeecms.cms.action.front;


import static com.jeecms.cms.Constants.TPLDIR_SPECIAL;
import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.cms.entity.main.ContentBuy;
import com.jeecms.cms.entity.main.ContentCharge;
import com.jeecms.cms.manager.assist.CmsConfigContentChargeMng;
import com.jeecms.cms.manager.main.ContentBuyMng;
import com.jeecms.cms.manager.main.ContentChargeMng;
import com.jeecms.cms.manager.main.ContentMng;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.Constants;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserAccountMng;
import com.jeecms.core.manager.CmsUserMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AliPay;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;

@Controller
public class ContentOrderAct {
	//收费
	public static final Integer CONTENT_PAY_MODEL_CHARGE=1;
	//打赏
	public static final Integer CONTENT_PAY_MODEL_REWARD=2;
	public static final String WEIXIN_PAY_URL="weixin.pay.url";
	public static final String ALI_PAY_URL="alipay.openapi.url";
	

	public static final String CONTENT_REWARD="tpl.content.reward";
	public static final String CONTENT_ALIPAY_MOBILE="tpl.content.alipay.mobile";
	public static final String CONTENT_ORDERS="tpl.content.orders";
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getCodeUrl";
	
	//支付购买（先选择支付方式，在进行支付）
	@RequestMapping(value = "/content/buy.jspx")
	public String contentBuy(Integer contentId,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(contentId==null){
				errors.addErrorCode("error.required","contentId");
				return FrontUtils.showError(request, response, model, errors);
			}else{
				Content content=contentMng.findById(contentId);
			    if(content!=null){
			  	    if(content.getChargeAmount()<=0){
			  	    	errors.addErrorCode("error.contentChargeAmountError");
			  	    	return FrontUtils.showError(request, response, model, errors);
			  	    }else{
			  	    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
					  	          .toLowerCase();
				  		boolean webCatBrowser=false;
				  		String wxopenid=null;
			  	        if (ua.indexOf("micromessenger") > 0) {
			  	        	// 是微信浏览器
			  	        	webCatBrowser=true;
			  	        	wxopenid=(String) session.getAttribute(request, "wxopenid");
			  	        }
			  	    	String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
			  	    	FrontUtils.frontData(request, model, site);
				  		model.addAttribute("contentId", contentId);
				  		model.addAttribute("orderNumber", orderNumber);
				  		model.addAttribute("content", content);
				  		model.addAttribute("type", ContentCharge.MODEL_CHARGE);
				  		model.addAttribute("webCatBrowser", webCatBrowser);
				  		model.addAttribute("wxopenid", wxopenid);
				  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
								TPLDIR_SPECIAL, CONTENT_REWARD);
			  	    }
			    }else{
			    	errors.addErrorCode("error.beanNotFound","content");
			    	return FrontUtils.showError(request, response, model, errors);
			    }
			}
		}
	}
	
	//打赏（先选择打赏金额，在选择支付方式）
	@RequestMapping(value = "/content/reward.jspx")
	public String contentReward(Integer contentId,String code,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		if(contentId==null){
			errors.addErrorCode("error.required","contentId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			Content content=contentMng.findById(contentId);
		    if(content!=null){
	  	    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
			  	          .toLowerCase();
		  		boolean webCatBrowser=false;
		  		String wxopenid=null;
	  	        if (ua.indexOf("micromessenger") > 0) {
	  	        	// 是微信浏览器
	  	        	webCatBrowser=true;
	  	        	wxopenid=(String) session.getAttribute(request, "wxopenid");
	  	        }
	  	      
	  	        CmsConfigContentCharge config=configContentChargeMng.getDefault(); 
				Double max=config.getRewardMax();
				Double min=config.getRewardMin();
				List<Double>randomList=new ArrayList<Double>();
				Double s=1d;
				for(int i=0;i<6;i++){
					s=StrUtils.retainTwoDecimal(min + ((max - min) * new Random().nextDouble()));
					randomList.add(s);
				}
	  	    	String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
	  	    	FrontUtils.frontData(request, model, site);
	  			model.addAttribute("contentId", contentId);
		  		model.addAttribute("orderNumber", orderNumber);
		  		model.addAttribute("content", content);
		  		model.addAttribute("type", ContentCharge.MODEL_REWARD);
		  		model.addAttribute("webCatBrowser", webCatBrowser);
		  		model.addAttribute("wxopenid", wxopenid);
		  		model.addAttribute("randomList", randomList);
		  		model.addAttribute("randomOne", s);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, CONTENT_REWARD);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","content");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	@RequestMapping(value = "/content/fixSelect.jspx")
	public String contentFixSelect(
			Integer contentId,String orderNumber,
			Double rewardAmount,Short chargeReward,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
	  	          .toLowerCase();
  		boolean webCatBrowser=false;
  		String wxopenid=null;
        if (ua.indexOf("micromessenger") > 0) {
        	// 是微信浏览器
        	webCatBrowser=true;
        	wxopenid=(String) session.getAttribute(request, "wxopenid");
        }
        if(contentId==null){
			errors.addErrorCode("error.required","contentId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			Content content=contentMng.findById(contentId);
		    if(content!=null){
		    	FrontUtils.frontData(request, model, site);
				model.addAttribute("contentId", contentId);
		  		model.addAttribute("orderNumber", orderNumber);
		  		model.addAttribute("chargeReward", chargeReward);
		  		model.addAttribute("content", content);
		  		model.addAttribute("type", ContentCharge.MODEL_REWARD);
		  		model.addAttribute("webCatBrowser", webCatBrowser);
		  		model.addAttribute("wxopenid", wxopenid);
		  		model.addAttribute("rewardAmount", rewardAmount);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, CONTENT_REWARD);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","content");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	//内容购买或打赏记录
	@RequestMapping(value = "/content/orders.jspx")
	public String contentOrders(Integer contentId,Short type,Integer pageNo,
			HttpServletRequest request,HttpServletResponse response
			,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		if(type==null){
			type=ContentCharge.MODEL_REWARD;
		}
		if(contentId==null){
			errors.addErrorCode("error.required","contentId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			Content content=contentMng.findById(contentId);
		    if(content!=null){
	  	    	FrontUtils.frontData(request, model, site);
	  	    	Pagination pagination=contentBuyMng.getPageByContent(contentId,
	  	    			type, cpn(pageNo), CookieUtils.getPageSize(request));
		  		model.addAttribute("contentId", contentId);
		  		model.addAttribute("type", type);
		  		model.addAttribute("pagination", pagination);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, CONTENT_ORDERS);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","content");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
		
	@RequestMapping(value = "/reward/random.jspx")
	public void randomReward(HttpServletRequest request,
			HttpServletResponse response) {
		 CmsConfigContentCharge config=configContentChargeMng.getDefault(); 
		 Double max=config.getRewardMax();
		 Double min=config.getRewardMin();
	     Double s =StrUtils.retainTwoDecimal(min + ((max - min) * new Random().nextDouble()));
	     ResponseUtils.renderJson(response, s.toString());
	}
	
	/**
	 * 选择支付方式
	 * @param contentId 内容ID
	 * @param orderNumber 订单号
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 * @param rewardAmount 金额
	 */
	@RequestMapping(value = "/content/selectPay.jspx")
	public String selectPay(Integer contentId,String orderNumber,
			Integer payMethod,Double rewardAmount,Short chargeReward,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		initWeiXinPayUrl();
		initAliPayUrl();
		if(contentId==null){
			errors.addErrorCode("error.required","contentId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			Content content=contentMng.findById(contentId);
		    if(content!=null){
		    	//收费模式金额必须大于0
		    	if(content.getChargeModel().equals(ContentCharge.MODEL_CHARGE)
		    			&&content.getChargeAmount()<=0){
		  	    	errors.addErrorCode("error.contentChargeAmountError");
		  	    	return FrontUtils.showError(request, response, model, errors);
		  	    }else{
		  	    	CmsConfigContentCharge config=configContentChargeMng.getDefault();
		  			//收取模式（收费 和打赏）
		  	    	if(chargeReward==null){
		  	    		chargeReward=ContentCharge.MODEL_CHARGE;
		  	    	}
		  	    	if(user!=null){
		  	    		cache.put(new Element(orderNumber,
			  	    			contentId+","+user.getId()+","+rewardAmount+","+chargeReward));
		  	    	}else{
		  	    		cache.put(new Element(orderNumber,
			  	    			contentId+",,"+rewardAmount+","+chargeReward));
		  	    	}
  	    			Double totalAmount=content.getChargeAmount();
  	    			if(rewardAmount!=null){
  	    				totalAmount=rewardAmount;
  	    			}
		  	    	if(payMethod!=null){
		  	    		if(payMethod==1){
		  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,content,
									orderNumber,rewardAmount,request, response, model);
		  	    		}else if(payMethod==3){
		  	    			String openId=(String) session.getAttribute(request, "wxopenid");
		  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(),config,
		  	    					openId,content, orderNumber, rewardAmount,
		  	    					request, response, model);
		  	    		}else if(payMethod==2){
		  	    			return AliPay.enterAliPayImmediate(config,orderNumber,content, rewardAmount,
									request, response, model);
		  	    		}else if(payMethod==4){
		  	    			return AliPay.enterAlipayScanCode(request,response, model,
		  	    					getAliPayUrl(), config, content, 
		  	    					orderNumber, totalAmount);
		  	    		}else if(payMethod==5){
		  	    			model.addAttribute("orderNumber",orderNumber);
		  					model.addAttribute("content", content);
		  					model.addAttribute("type", chargeReward);
		  					model.addAttribute("rewardAmount", rewardAmount);
		  	    			FrontUtils.frontData(request, model, site);
		  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
		  							TPLDIR_SPECIAL, CONTENT_ALIPAY_MOBILE);
		  	    		}
					}//支付宝
					return AliPay.enterAliPayImmediate(config,orderNumber,content, rewardAmount,
							request, response, model);
		  	    }
		    }else{
		    	errors.addErrorCode("error.beanNotFound","content");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	@RequestMapping(value = "/content/alipayInMobile.jspx")
	public String enterAlipayInMobile(Integer contentId,String orderNumber,
			Double rewardAmount,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		initAliPayUrl();
		if(contentId==null){
			errors.addErrorCode("error.required","contentId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			Content content=contentMng.findById(contentId);
			CmsConfigContentCharge config=configContentChargeMng.getDefault();
			if(content!=null){
				Double totalAmount=content.getChargeAmount();
    			if(rewardAmount!=null){
    				totalAmount=rewardAmount;
    			}
				AliPay.enterAlipayInMobile(request, response,
						getAliPayUrl(), config, content, orderNumber, totalAmount);
				return "";
			}else{
		    	errors.addErrorCode("error.beanNotFound","content");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	
	/**
	 * 微信回调
	 * @param code
	 */
	@RequestMapping(value = "/order/payCallByWeiXin.jspx")
	public void orderPayCallByWeiXin(String orderNumber,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JDOMException, IOException, JSONException {
		JSONObject json = new JSONObject();
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		if (StringUtils.isNotBlank(orderNumber)) {
			ContentBuy order=contentBuyMng.findByOrderNumber(orderNumber);
			if (order!=null&&StringUtils.isNotBlank(order.getOrderNumWeiXin())) {
				//已成功支付过
				WeixinPay.noticeWeChatSuccess(getWeiXinPayUrl());
				json.put("status", 4);
			} else {
				//订单未成功支付
				json.put("status", 6);
			}
		}else{
			// 回调结果
			String xml_receive_result = PayUtil.getWeiXinResponse(request);
			if (StringUtils.isBlank(xml_receive_result)) {
				//检测到您可能没有进行扫码支付，请支付
				json.put("status", 5);
			} else {
				Map<String, String> result_map = PayUtil.parseXMLToMap(xml_receive_result);
				String sign_receive = result_map.get("sign");
				result_map.remove("sign");
				String key = config.getWeixinPassword();
				if (key == null) {
					//微信扫码支付密钥错误，请通知商户
					json.put("status", 1);
				}
				String checkSign = PayUtil.createSign(result_map, key);
				if (checkSign != null && checkSign.equals(sign_receive)) {
					try {
						if (result_map != null) {
							String return_code = result_map.get("return_code");
							if ("SUCCESS".equals(return_code)
									&& "SUCCESS".equals(result_map
											.get("result_code"))) {
								// 微信返回的微信订单号（属于微信商户管理平台的订单号，跟自己的系统订单号不一样）
								String transaction_id = result_map
										.get("transaction_id");
								// 商户系统的订单号，与请求一致。
								String out_trade_no = result_map.get("out_trade_no");
								// 通知微信该订单已处理
								WeixinPay.noticeWeChatSuccess(getWeiXinPayUrl());
								payAfter(out_trade_no,config.getChargeRatio(),transaction_id, null);
								//支付成功
								json.put("status", 0);
							} else if ("SUCCESS".equals(return_code)
									&& result_map.get("err_code") != null) {
								String message = result_map.get("err_code_des");
								json.put("status", 2);
								json.put("error", message);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Map<String, String> parames = new HashMap<String, String>();
					parames.put("return_code", "FAIL");
					parames.put("return_msg", "校验错误");
					// 将参数转成xml格式
					String xmlWeChat = PayUtil.assembParamToXml(parames);
					try {
						HttpClientUtil.post(getWeiXinPayUrl(), xmlWeChat, Constants.UTF8);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//支付参数错误，请重新支付!
					json.put("status", 3);
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	//支付宝即时支付回调地址
	@RequestMapping(value = "/order/payCallByAliPay.jspx")
	public String payCallByAliPay(HttpServletRequest request,
			HttpServletResponse response, ModelMap model)
					throws UnsupportedEncodingException {
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		CmsSite site=CmsUtils.getSite(request);
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		FrontUtils.frontData(request, model, site);
		if(PayUtil.verifyAliPay(params,config.getAlipayPartnerId(),config.getAlipayKey())){//验证成功
			if(trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				Content content=payAfter(out_trade_no,config.getChargeRatio(), null, trade_no);
				try {
					response.sendRedirect(content.getUrl());
				} catch (IOException e) {
					//e.printStackTrace();
				}
				return  content.getUrl();
				//注意：TRADE_FINISHED
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				//TRADE_SUCCESS
				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
			}
		}else{//验证失败
			return  FrontUtils.showMessage(request, model,"error.alipay.status.valifail");
		}
		return  FrontUtils.showMessage(request, model,"error.alipay.status.payfail");
	}
	
	//支付宝查询订单状态（扫码支付和手机网页支付均由此处理订单）
	@RequestMapping(value = "/content/orderQuery.jspx")
	public void aliPayOrderQuery(String orderNumber,HttpServletRequest request,
			HttpServletResponse response, ModelMap model)
					throws UnsupportedEncodingException {
		CmsConfigContentCharge config=configContentChargeMng.getDefault();
		JSONObject json = new JSONObject();
		CmsSite site=CmsUtils.getSite(request);
		initAliPayUrl();
		FrontUtils.frontData(request, model, site);
		AlipayTradeQueryResponse res=AliPay.query(getAliPayUrl(), config,
				orderNumber,null);
		try {
			if (null != res && res.isSuccess()) {
				if (res.getCode().equals("10000")) {
					if ("TRADE_SUCCESS".equalsIgnoreCase(res
							.getTradeStatus())) {
							json.put("status", 0);
							payAfter(orderNumber, config.getChargeRatio(),
									null, res.getTradeNo());
					} else if ("WAIT_BUYER_PAY".equalsIgnoreCase(res
							.getTradeStatus())) {
						// 等待用户付款状态，需要轮询查询用户的付款结果
						json.put("status", 1);
					} else if ("TRADE_CLOSED".equalsIgnoreCase(res.getTradeStatus())) {
						// 表示未付款关闭，或已付款的订单全额退款后关闭
						json.put("status", 2);
					} else if ("TRADE_FINISHED".equalsIgnoreCase(res.getTradeStatus())) {
						// 此状态，订单不可退款或撤销
						json.put("status", 0);
					}
				} else {
					// 如果请求未成功，请重试
					json.put("status", 3);
				}
			}else{
				json.put("status", 4);
			}
		} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	private Content payAfter(String orderNumber,Double ratio,
			String weixinOrderNum,
			String alipyOrderNum){
		Element e = cache.get(orderNumber);
	    Content content = null;
		if(e!=null&&StringUtils.isNotBlank(orderNumber)){
		    ContentBuy b=contentBuyMng.findByOrderNumber(orderNumber);
		    //不能重复提交
		    if(b==null){
		    	Object obj= e.getObjectValue();
				String[] objArray=new String[4];
				if(obj!=null){
					objArray=obj.toString().split(",");
				}
				Double rewardAmount=null;
				Integer contentId=null;
				Integer buyUserId=null;
				Short chargeReward = ContentCharge.MODEL_REWARD;
				if(objArray!=null&&objArray[0]!=null){
					contentId=Integer.parseInt(objArray[0]) ;
				}
				if(objArray!=null&&objArray[1]!=null&&StringUtils.isNotBlank(objArray[1])){
					buyUserId=Integer.parseInt(objArray[1]);
				}
				if(objArray!=null&&objArray[2]!=null&&StringUtils.isNotBlank(objArray[2])
						&&!objArray[2].toLowerCase().equals("null")){
					rewardAmount=Double.parseDouble(objArray[2]);
				}
				if(objArray!=null&&objArray[3]!=null){
					chargeReward=Short.valueOf(objArray[3]);
				}
			    ContentBuy contentBuy=new ContentBuy();
			    if(contentId!=null){
			    	content=contentMng.findById(contentId);
			   	    contentBuy.setAuthorUser(content.getUser());
			   	    //打赏可以匿名
			   	    if(buyUserId!=null){
			   	    	contentBuy.setBuyUser(userMng.findById(buyUserId));
			   	    }
			   	    contentBuy.setContent(content);
			   	    contentBuy.setHasPaidAuthor(false);
			   	    contentBuy.setOrderNumber(orderNumber);
			   	    contentBuy.setBuyTime(Calendar.getInstance().getTime());
			   	    Double chargeAmount=content.getChargeAmount();
			   	    Double platAmount=content.getChargeAmount()*ratio;
			     	Double authorAmount=content.getChargeAmount()*(1-ratio);
			   	    if(rewardAmount!=null){
			   	    	chargeAmount=rewardAmount;
			   	    	platAmount=rewardAmount*ratio;
			   	    	authorAmount=rewardAmount*(1-ratio);
			   	    }
			   	    if(chargeReward.equals(ContentCharge.MODEL_REWARD)){
			   	    	contentBuy.setChargeReward(ContentCharge.MODEL_REWARD);
			   	    }else{
			   	    	contentBuy.setChargeReward(ContentCharge.MODEL_CHARGE);
			   	    }
			   	    contentBuy.setChargeAmount(chargeAmount);
			   	    contentBuy.setPlatAmount(platAmount);
			   	    contentBuy.setAuthorAmount(authorAmount);
			 		// 这里是把微信商户的订单号放入了交易号中
		 			contentBuy.setOrderNumWeiXin(weixinOrderNum);
		 			contentBuy.setOrderNumAliPay(alipyOrderNum);
		 			contentBuy=contentBuyMng.save(contentBuy);
		 			CmsUser authorUser=contentBuy.getAuthorUser();
		 			//笔者所得统计
		 			userAccountMng.userPay(contentBuy.getAuthorAmount(), authorUser);
		 			//平台所得统计
		 			configContentChargeMng.afterUserPay(contentBuy.getPlatAmount());
		 			//内容所得统计
		 			contentChargeMng.afterUserPay(contentBuy.getChargeAmount(), contentBuy.getContent());
			 	}
		    }
		}
	    return content;
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
					new File(realPathResolver.get(com.jeecms.cms.Constants.JEECMS_CONFIG)),WEIXIN_PAY_URL));
		}
	}
	
	private String weiXinPayUrl;
	
	private String aliPayUrl;
	private String weixinAuthCodeUrl;
	
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
	
	public String getWeixinAuthCodeUrl() {
		return weixinAuthCodeUrl;
	}

	public void setWeixinAuthCodeUrl(String weixinAuthCodeUrl) {
		this.weixinAuthCodeUrl = weixinAuthCodeUrl;
	}

	@Autowired
	private ContentMng contentMng;
	@Autowired
	private ContentChargeMng contentChargeMng;
	@Autowired
	private ContentBuyMng contentBuyMng;
	@Autowired
	private CmsConfigContentChargeMng configContentChargeMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsUserAccountMng userAccountMng;
	@Autowired
	private CmsUserMng userMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	@Qualifier("contentOrderTemp")
	private Ehcache cache;
}

