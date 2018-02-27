package com.jeecms.common.util;

import static com.jeecms.cms.Constants.TPLDIR_SPECIAL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.springframework.ui.ModelMap;

import com.jeecms.cms.entity.assist.CmsConfigContentCharge;
import com.jeecms.cms.entity.main.Content;
import com.jeecms.common.web.ClientCustomSSL;
import com.jeecms.common.web.Constants;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.web.util.CmsUtils;
import com.jeecms.core.web.util.FrontUtils;

public class WeixinPay {
	public static final String CONTENT_CODE_WEIXIN="tpl.content.code.weixin";
	public static final String CONTENT_PREPAY="tpl.content.prePay";
	//微信统一下单
	public  static Map<String, String>  weixinUniformOrder(String trade_type,String openId,
			HttpServletRequest request,String serverUrl,CmsConfigContentCharge config,
			Content content,String orderNum,Double rewardAmount){
		CmsSite site=CmsUtils.getSite(request);
		CmsUser user=CmsUtils.getUser(request);
		Map<String, String> paramMap = new HashMap<String, String>();
		// 微信分配的公众账号ID（企业号corpid即为此appId）[必填]
		paramMap.put("appid", config.getWeixinAppId());
		// 微信支付分配的商户号 [必填]
		paramMap.put("mch_id", config.getWeixinAccount());
		// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB" [非必填]
		paramMap.put("device_info", "WEB");
		// 随机字符串，不长于32位。 [必填]
		paramMap.put("nonce_str", RandomStringUtils.random(10,Num62.N62_CHARS));
		// 商品或支付单简要描述 [必填]
		paramMap.put("body", content.getTitle());
		// 商户系统内部的订单号,32个字符内、可包含字母, [必填]
		paramMap.put("out_trade_no", orderNum);
		// 符合ISO 4217标准的三位字母代码，默认人民币：CNY. [非必填]
		paramMap.put("fee_type", "CNY");
		Double amount=content.getChargeAmount();
		if(rewardAmount!=null){
			amount=rewardAmount;
		}
		// 金额必须为整数 单位为分 [必填]
		paramMap.put("total_fee", PayUtil.changeY2F(amount));
		// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP [必填]
		paramMap.put("spbill_create_ip", RequestUtils.getIpAddr(request));
		// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。 [必填]
		paramMap.put("notify_url", "http://" + site.getDomain()
				+ "/order/payCallByWeiXin.jspx");
		// 交易类型{取值如下：JSAPI，NATIVE，APP，(JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付)}
		// [必填]
		paramMap.put("trade_type",trade_type);
		//openid trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		if(trade_type.equals("JSAPI")){
			if(StringUtils.isNotBlank(openId)){
				paramMap.put("openid",openId);
			}else if(user!=null&&user.getUserAccount()!=null){
				paramMap.put("openid",user.getUserAccount().getAccountWeixinOpenId());
			}
		}
		// 商品ID{trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。}
		paramMap.put("product_id",content.getId().toString());
		if (StringUtils.isNotBlank(config.getTransferApiPassword())) {
			// 根据微信签名规则，生成签名
			paramMap.put("sign",
					PayUtil.createSign(paramMap, config.getTransferApiPassword()));
		}
		// 把参数转换成XML数据格式
		String xmlWeChat = PayUtil.assembParamToXml(paramMap);
		String resXml = HttpClientUtil.post(serverUrl,xmlWeChat);
		Map<String, String> map=new HashMap<String, String>();
		try {
			if(StringUtils.isNotBlank(resXml)){
				map = PayUtil.parseXMLToMap(resXml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
	
	public  static Map<String, String>  weixinOrderQuery(
			String transaction_id,String orderNum,
			String serverUrl,CmsConfigContentCharge config){
		Map<String, String> paramMap = new HashMap<String, String>();
		// 微信分配的公众账号ID（企业号corpid即为此appId）[必填]
		paramMap.put("appid", config.getWeixinAppId());
		// 微信支付分配的商户号 [必填]
		paramMap.put("mch_id", config.getWeixinAccount());
		// 微信订单号
		paramMap.put("transaction_id", transaction_id);
		//商户订单号
		paramMap.put("out_trade_no", orderNum);
		// 随机字符串，不长于32位。 [必填]
		paramMap.put("nonce_str", RandomStringUtils.random(10,Num62.N62_CHARS));
		// 签名类型
		paramMap.put("sign_type","MD5");
		if (StringUtils.isNotBlank(config.getTransferApiPassword())) {
			// 根据微信签名规则，生成签名
			paramMap.put("sign",
					PayUtil.createSign(paramMap, config.getTransferApiPassword()));
		}
		// 把参数转换成XML数据格式
		String xmlWeChat = PayUtil.assembParamToXml(paramMap);
		String resXml = HttpClientUtil.post(serverUrl,xmlWeChat);
		Map<String, String> map=new HashMap<String, String>();
		try {
			if(StringUtils.isNotBlank(resXml)){
				map = PayUtil.parseXMLToMap(resXml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return map;
	}
	
	//微信扫码支付
	public static  String enterWeiXinPay(String serverUrl,
			CmsConfigContentCharge config,Content content,String orderNumber,
			Double rewardAmount,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		if (StringUtils.isNotBlank(config.getWeixinAppId())
				&& StringUtils.isNotBlank(config.getWeixinAccount())) {
			CmsSite site=CmsUtils.getSite(request);
			if (StringUtils.isNotBlank(config.getTransferApiPassword())) {
				Map<String, String> map=weixinUniformOrder( "NATIVE",null,
						request, serverUrl,config, content, orderNumber, rewardAmount);
				String returnCode = map.get("return_code");
				if(StringUtils.isNotBlank(returnCode)){
					if (returnCode.equalsIgnoreCase("FAIL")) {
						return FrontUtils.showMessage(request, model, map.get("return_msg"));
					} else if (returnCode.equalsIgnoreCase("SUCCESS")) {
						if (map.get("err_code") != null) {
							return FrontUtils.showMessage(request, model, map.get("err_code_des"));
						} else if (map.get("result_code").equalsIgnoreCase(
								"SUCCESS")) {
							String code_url = map.get("code_url");
							model.addAttribute("code_url", code_url);
							model.addAttribute("orderNumber",orderNumber);
							if(rewardAmount!=null){
								model.addAttribute("payAmount", rewardAmount);
							}else{
								model.addAttribute("payAmount", content.getChargeAmount());
							}
							model.addAttribute("content", content);
							FrontUtils.frontData(request, model, site);
							return FrontUtils.getTplPath(request, site.getSolutionPath(),
									TPLDIR_SPECIAL,CONTENT_CODE_WEIXIN);
						}
					}
				}
				return FrontUtils.showMessage(request, model, "error.connect.timeout");
			} else {
				return FrontUtils.showMessage(request, model,"error.contentCharge.need.key");
			}
		} else {
			return FrontUtils.showMessage(request, model,"error.contentCharge.need.appid");
		}
	}

	//微信公众号支付(手机端)
	public static String weixinPayByMobile(String serverUrl,CmsConfigContentCharge config,
			String openId,Content content,String orderNum,Double rewardAmount,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		CmsSite site=CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if (StringUtils.isNotBlank(config.getWeixinAppId())
				&& StringUtils.isNotBlank(config.getWeixinAccount())) {
			if (StringUtils.isNotBlank(config.getTransferApiPassword())) {
				Map<String, String> map=weixinUniformOrder("JSAPI",openId,
						request, serverUrl,config, content, orderNum, rewardAmount);
				String returnCode = map.get("return_code");
				if (returnCode.equalsIgnoreCase("FAIL")) {
					return FrontUtils.showMessage(request, model, map.get("return_msg"));
				} else if (returnCode.equalsIgnoreCase("SUCCESS")) {
					if (map.get("err_code") != null) {
						return FrontUtils.showMessage(request, model, map.get("err_code_des"));
					} else if (map.get("result_code").equalsIgnoreCase(
							"SUCCESS")) {
						String prepay_id = map.get("prepay_id");
						Long time=System.currentTimeMillis()/1000;
						String nonceStr=RandomStringUtils.random(16,Num62.N10_CHARS);
						//公众号appid
						model.addAttribute("appId",config.getWeixinAppId());
						//时间戳 当前的时间 需要转换成秒
						model.addAttribute("timeStamp",time);
						//随机字符串  不长于32位
						model.addAttribute("nonceStr",nonceStr);
						//订单详情扩展字符串 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
						model.addAttribute("package","prepay_id="+prepay_id);
						//签名方式 签名算法，暂支持MD5
						model.addAttribute("signType","MD5");
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("appId",config.getWeixinAppId());
						paramMap.put("timeStamp",time.toString());
						paramMap.put("nonceStr",nonceStr);
						paramMap.put("package","prepay_id="+prepay_id);
						paramMap.put("signType","MD5");
						//签名
						model.addAttribute("paySign",PayUtil.createSign(paramMap, config.getTransferApiPassword()));
						model.addAttribute("orderNumber",orderNum);
						if(rewardAmount!=null){
							model.addAttribute("payAmount", rewardAmount);
						}else{
							model.addAttribute("payAmount", content.getChargeAmount());
						}
						model.addAttribute("content", content);
						return FrontUtils.getTplPath(request, site.getSolutionPath(),
								TPLDIR_SPECIAL,CONTENT_PREPAY);
					}
				}
				return FrontUtils.showMessage(request, model, "error.connect.timeout");
			} else {
				return FrontUtils.showMessage(request, model,"error.contentCharge.need.key");
			}
		} else {
			return FrontUtils.showMessage(request, model,"error.contentCharge.need.appid");
		}
	}
	
	//企业付款接口
	public  static Object[] payToUser(CmsConfigContentCharge config,File pkcFile,
			String serverUrl,String orderNum,String weixinOpenId,String realname
			,Double payAmount,String desc,String ip){
		Map<String, String> paramMap = new HashMap<String, String>();
		// 公众账号appid[必填]
		paramMap.put("mch_appid", config.getWeixinAppId());
		// 微信支付分配的商户号 [必填]
		paramMap.put("mchid", config.getWeixinAccount());
		// 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB" [非必填]
		paramMap.put("device_info", "WEB");
		// 随机字符串，不长于32位。 [必填]
		paramMap.put("nonce_str", RandomStringUtils.random(16,Num62.N62_CHARS));
		
		// 商户订单号,需保持唯一性[必填]
		paramMap.put("partner_trade_no", orderNum);
		// 商户appid下，某用户的openid[必填]
		paramMap.put("openid", weixinOpenId);
		//校验用户姓名选项
		paramMap.put("check_name", "OPTION_CHECK");
		//收款用户姓名,如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
		paramMap.put("re_user_name", realname);
		// 企业付款金额，金额必须为整数 单位为分 [必填]
		paramMap.put("amount", PayUtil.changeY2F(payAmount));
		// 企业付款描述信息 [必填]
		paramMap.put("desc",  desc);
		// 调用接口的机器Ip地址[必填]
		paramMap.put("spbill_create_ip", ip);
		if (StringUtils.isNotBlank(config.getTransferApiPassword())) {
			// 根据微信签名规则，生成签名
			paramMap.put("sign",
					PayUtil.createSign(paramMap, config.getTransferApiPassword()));
		}
		// 把参数转换成XML数据格式
		String xmlWeChat = PayUtil.assembParamToXml(paramMap);
		String resXml="";
		boolean postError=false;
		try {
			resXml = ClientCustomSSL.getInSsl(serverUrl, pkcFile, config.getWeixinAccount()
					,xmlWeChat,"application/xml");
		} catch (Exception e1) {
			postError=true;
			e1.printStackTrace();
		}
		Object[] result=new Object[2];
		result[0]=postError;
		result[1]=resXml;
		return result;
	}
	
	//通知微信正确接收
	public  static void noticeWeChatSuccess(String weiXinPayUrl){
		Map<String,String> parames=new HashMap<String,String>();
		parames.put("return_code", "SUCCESS");
		parames.put("return_msg", "OK");
		//将参数转成xml格式
		String xmlWeChat = PayUtil.assembParamToXml(parames);
		try{    		
			if(StringUtils.isNotBlank(weiXinPayUrl)){
				String s=HttpClientUtil.post(weiXinPayUrl, xmlWeChat, Constants.UTF8);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
