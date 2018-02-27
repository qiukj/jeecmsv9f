package com.jeecms.common.util;

import static com.jeecms.cms.Constants.TPLDIR_SPECIAL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

public class AliPay {

	public static final String CONTENT_CODE_ALIPAY="tpl.content.code.alipay";
	private static final Logger log = LoggerFactory.getLogger(AliPay.class);
	
	/**
	 * 支付宝当面付(扫码付款)
	 * @param request
	 * @param response
	 * @param model
	 * @param serverUrl
	 * @param config
	 * @param content
	 * @param outTradeNo
	 * @param totalAmount
	 * @return
	 */
	public static String enterAlipayScanCode(
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model,String serverUrl,
			CmsConfigContentCharge config,Content content,
			String outTradeNo,Double totalAmount){
		CmsSite site=CmsUtils.getSite(request);
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
        		serverUrl,config.getAlipayAppId()
        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"utf-8");
        AlipayTradePrecreateRequest aliRequest = new AlipayTradePrecreateRequest();
        aliRequest.setBizContent("{" +
        //商户订单号
		"    \"out_trade_no\":\""+outTradeNo+"\"," +
        //卖家支付宝用户 ID
		"    \"seller_id\":\""+config.getAlipayPartnerId()+"\"," +
		//订单标题
		"    \"subject\":\""+content.getTitle()+"\"," +
		//订单总金额
		"    \"total_amount\":"+totalAmount+"," +
		//支付超时时间
		"    \"timeout_express\":\"90m\"" +
		"  }");
        //设置回转地址
        aliRequest.setReturnUrl(content.getUrlWhole());
        AlipayTradePrecreateResponse aliResponse = null;
		try {
			aliResponse = alipayClient.execute(aliRequest);
		} catch (AlipayApiException e) {
			log.error(e.getErrMsg());
			e.printStackTrace();
		}
        // TODO 根据response中的结果继续业务逻辑处理
		WebErrors errors=WebErrors.create(request);
		if(aliResponse!=null&&aliResponse.isSuccess()){
			if(aliResponse.getCode().equals("10000")){
				//"支付宝预下单成功
				model.addAttribute("code_url", aliResponse.getQrCode());
				model.addAttribute("orderNumber",outTradeNo);
				model.addAttribute("payAmount", totalAmount);
				model.addAttribute("content", content);
				FrontUtils.frontData(request, model, site);
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, CONTENT_CODE_ALIPAY);
			}else{
				errors.addErrorString(aliResponse.getMsg());
			}
        }
		errors.addErrorCode("error.alipay.params.fail");
		return FrontUtils.showError(request, response, model, errors);
	}
	
	/**
	 * 手机网站快速支付
	 * @param request
	 * @param response
	 * @param model
	 * @param serverUrl
	 * @param config
	 * @param content
	 * @param outTradeNo
	 * @param totalAmount
	 */
	public static void enterAlipayInMobile(
			HttpServletRequest request,HttpServletResponse response,
			String serverUrl,CmsConfigContentCharge config,Content content,
			String outTradeNo,Double totalAmount){
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
        		serverUrl,config.getAlipayAppId()
        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"UTF-8");
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        //在公共参数中设置回跳地址
        alipayRequest.setReturnUrl(content.getUrlWhole());
        alipayRequest.setBizContent("{" +
        //商户订单号
		"    \"out_trade_no\":\""+outTradeNo+"\"," +
        //卖家支付宝用户 ID
		"    \"seller_id\":\""+config.getAlipayPartnerId()+"\"," +
		//订单标题
		"    \"subject\":\""+content.getTitle()+"\"," +
		//订单总金额
		"    \"total_amount\":"+totalAmount+"," +
		//支付超时时间
		"    \"timeout_express\":\"90m\"" +
		"  }");
        String form;
		try {
			 //调用SDK生成表单
			form = alipayClient.pageExecute(alipayRequest).getBody();
	        response.setContentType("text/html;charset=UTF-8");
	        response.getWriter().write(form);//直接将完整的表单html输出到页面
	        response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 支付宝在线支付订单
	 * @param config
	 * @param orderNumber
	 * @param content
	 * @param rewardAmount
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public static String enterAliPayImmediate(CmsConfigContentCharge config,
			String orderNumber,Content content,Double rewardAmount,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		CmsSite site=CmsUtils.getSite(request);
		if(orderNumber!=null){
			if(content!=null){
				PrintWriter out = null;
				String aliURL = null;
				try {
					aliURL = alipayImmediate(config,site,orderNumber,content, rewardAmount, request, model);
					response.setContentType("text/html;charset=UTF-8");
					out = response.getWriter();
					out.print(aliURL);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					out.close();
				}
			}
			return null;	
		}else {
			return FrontUtils.pageNotFound(request, response, model);
		}		
	}
	/**
	 * 支付宝即时到账（支付宝不推荐）
	 * @param
	 */
	private static String alipayImmediate(CmsConfigContentCharge config,
			CmsSite site,String orderNumber,Content content, 
			Double rewardAmount,HttpServletRequest request,ModelMap model){
		//支付类型
		String payment_type = "1";//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://"+site.getDomain()+"/order/payCallByAliPay.jspx";
		//页面跳转同步通知页面路径
		String return_url = content.getUrlWhole();
		//卖家支付宝帐户
		String seller_email = config.getAlipayAccount();//必填
		//商户订单号
		String out_trade_no = orderNumber;//商户网站订单系统中唯一订单号，必填
		//订单名称
		String subject = "("+content.getTitle()+")";//必填
		//付款金额
		Double payAmount=content.getChargeAmount();
		if(rewardAmount!=null){
			payAmount=rewardAmount;
		}
		String total_fee = String.valueOf(payAmount);//必填
		//订单描述
		String body = "("+content.getTitle()+")";
		//商品展示地址
		String show_url = "http://"+content.getUrl()+"/";
		//防钓鱼时间戳
		String anti_phishing_key = "";//若要使用请调用类文件submit中的query_timestamp函数
		//客户端的IP地址
		String exter_invoke_ip = RequestUtils.getIpAddr(request);//非局域网的外网IP地址，如：221.0.0.1
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
	    sParaTemp.put("partner", config.getAlipayPartnerId());
	    sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		//建立请求
		String sHtmlText = PayUtil.buildAliPayRequest(sParaTemp,config.getAlipayKey(),"get","确认");	
		return sHtmlText;
	}
	

	/**
	 * 交易查询
	 * 
	 * @param out_trade_no  业务系统订单号
	 * @param trade_no      支付宝交易订单号
	 */
	public static AlipayTradeQueryResponse query(String serverUrl,
			CmsConfigContentCharge config,
			final String out_trade_no,final String trade_no) {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
        		serverUrl,config.getAlipayAppId()
        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"UTF-8");
		AlipayTradeQueryRequest alipayQueryRequest = new AlipayTradeQueryRequest();
		//String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";

		String biz_content="";
		if(StringUtils.isNotBlank(out_trade_no)){
			//商户订单号
			biz_content= "{\"out_trade_no\":\"" + out_trade_no + "\"}";
		}else if(StringUtils.isNotBlank(trade_no)){
			//支付宝交易订单号
			biz_content= "{\"trade_no\":\"" + trade_no + "\"}";
		}
		
		alipayQueryRequest.setBizContent(biz_content);
		AlipayTradeQueryResponse alipayQueryResponse = null;
		try {
			alipayQueryResponse = alipayClient.execute(alipayQueryRequest);
			
			if (null != alipayQueryResponse && alipayQueryResponse.isSuccess()) {
				if (alipayQueryResponse.getCode().equals("10000")) {
					if ("TRADE_SUCCESS".equalsIgnoreCase(alipayQueryResponse
							.getTradeStatus())) {

						List<TradeFundBill> fund_bill_list = alipayQueryResponse
								.getFundBillList();
						if (null != fund_bill_list) {
							doFundBillList(out_trade_no, fund_bill_list);
						}
					} else if ("TRADE_CLOSED".equalsIgnoreCase(alipayQueryResponse
							.getTradeStatus())) {
						// 表示未付款关闭，或已付款的订单全额退款后关闭
					} else if ("TRADE_FINISHED".equalsIgnoreCase(alipayQueryResponse
							.getTradeStatus())) {
						// 此状态，订单不可退款或撤销
					}
				} else {
					// 如果请求未成功，请重试

				}
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return alipayQueryResponse;
	}
	
	/**
	 * 轮询查询订单状态
	 * 
	 * @param out_trade_no
	 */
	public static void queryRetry(final String serverUrl,
			final CmsConfigContentCharge config,
			final String out_trade_no) {

		final ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		final int queryTime = 600;// 总共轮询查询时间，单位秒
		final int queryPeriod = 5;// 间隔时间，单位秒
		
		boolean hasPay=false;
		Runnable queryRunnable = new Runnable() {
			int i = 0;
			int n = queryTime / queryPeriod;
			
			boolean paid=false;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (++i <= n) {
					AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
							serverUrl,config.getAlipayAppId()
			        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"UTF-8");
					AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
					String biz_content = "{\"out_trade_no\":\"" + out_trade_no
							+ "\"}";
					request.setBizContent(biz_content);

					try {
						AlipayTradeQueryResponse response = alipayClient
								.execute(request);
						if (null != response && response.isSuccess()) {
							if (response.getCode().equals("10000")
									&& "TRADE_SUCCESS"
											.equalsIgnoreCase(response
													.getTradeStatus())) {
								paid=true;
								// 收款成功，退出轮询
								service.shutdownNow();
							}
						}
					} catch (AlipayApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (i == n) {
						// 最后一次查询时，仍然没有查询到付款成功，需要撤销订单
						 cancelOrder(serverUrl, config, out_trade_no);
						 //退出轮询
						 service.shutdownNow();
					}
				}
			}
		};

		service.scheduleAtFixedRate(queryRunnable, 0, queryPeriod,
				TimeUnit.SECONDS);

	}
	
	
	public static void doFundBillList(String out_trade_no,
			List<TradeFundBill> fund_bill_list) {
		// 根据付款的资金渠道，来决定哪些是商户优惠，哪些是支付宝优惠。 对账时要注意商户优惠部分
		for (TradeFundBill tfb : fund_bill_list) {
			System.out.println("付款资金渠道：" + tfb.getFundChannel() + " 付款金额："
					+ tfb.getAmount());
		}
	}
	
	/**
	 * 撤销订单
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public static AlipayTradeCancelResponse cancelOrder(
			String serverUrl,CmsConfigContentCharge config,
			final String out_trade_no) {
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
        		serverUrl,config.getAlipayAppId()
        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"UTF-8");
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
		request.setBizContent(biz_content);
		AlipayTradeCancelResponse response = null;

		try {
			response = alipayClient.execute(request);
			if (null != response && response.isSuccess()) {
				if (response.getCode().equals("10000")) {
				} else {
					// 没有撤销成功，需要重试几次
					if (response.getRetryFlag().equals("Y")) {
						// 如果重试标识为Y，表示支付宝撤销失败，需要轮询重新发起撤销
						cancelOrderRetry(serverUrl,config,out_trade_no);
					}
				}
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 轮询发起撤销重试
	 * 
	 * @param out_trade_no
	 */
	public static void cancelOrderRetry(
			String serverUrl,CmsConfigContentCharge config,
			final String out_trade_no) {
		final AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient(
        		serverUrl,config.getAlipayAppId()
        		,config.getAlipayPrivateKey(),config.getAlipayPublicKey(),"UTF-8");
		final AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
		request.setBizContent(biz_content);

		// 子线程异步方式，每个10秒钟重试一次，重试5次,加上重试前的1次，总共6次1分钟
		new Thread(new Runnable() {
			int i = 0;
			int n = 5;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (++i <= n) {
					try {
						Thread.sleep(10000);
						AlipayTradeCancelResponse response = alipayClient
								.execute(request);
						if (null != response && response.isSuccess()) {
							if (response.getCode().equals("10000")
									&& response.getBody().contains(
											"\"retry_flag\":\"N\"")) {
								break;
							}
						}

						if (i == n) {
							// 处理到最后一次，还是未撤销成功，需要在商户数据库中对此单最标记，人工介入处理

						}

					} catch (AlipayApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

	}
	
	private String serverUrl;
	private CmsConfigContentCharge config;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public CmsConfigContentCharge getConfig() {
		return config;
	}

	public void setConfig(CmsConfigContentCharge config) {
		this.config = config;
	}

}
