package com.jeecms.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.jeecms.common.web.Constants;


public class PayUtil {

	/**
     * 支付宝提供给商户的服务接入网关URL(不推荐)
     */
    public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
    
    /**
     * 支付宝消息验证地址
     */
    public static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";


	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * @param sParaTemp 请求参数数组
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildAliPayRequest(Map<String, String> sParaTemp,String key, String strMethod, String strButtonName) {
	//待请求参数数组
	    Map<String, String> sPara = buildAliPayRequestPara(sParaTemp,key);
	    List<String> keys = new ArrayList<String>(sPara.keySet());
	    StringBuffer sbHtml = new StringBuffer();
	    sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW + "_input_charset=" + "utf-8" + "\" method=\"" + strMethod + "\">");
	    for (int i = 0; i < keys.size(); i++) {
	        String name = keys.get(i);
	        String value = sPara.get(name);
	        sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
	    }
	    //submit按钮控件请不要含有name属性
	    sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
	    sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
	    return sbHtml.toString();
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * @param sParaTemp 请求前的参数数组
	 * @return 要请求的参数数组
	 */
	 public static Map<String, String> buildAliPayRequestPara(Map<String, String> sParaTemp,String key) {
	    //除去数组中的空值和签名参数
	    Map<String, String> sPara = paraFilter(sParaTemp);
	    //生成签名结果
	    String mysign = buildRequestMysign(sPara,key);
	    //签名结果与签名方式加入请求提交参数组中
	    sPara.put("sign", mysign);
	    sPara.put("sign_type", "MD5");
	    return sPara;
	}

	/**
	  * 验证消息是否是支付宝发出的合法消息
	  * @param params 通知返回来的参数数组
	  * @return 验证结果
	  */
	 public static boolean verifyAliPay(Map<String, String> params,String partner,String key) {
	     //判断responsetTxt是否为true，isSign是否为true
	     //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
	     //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
	 	String responseTxt = "true";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			responseTxt = verifyAliPayResponse(notify_id,partner);
		}
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = getSignVeryfy(params, sign,key);
	     //写日志记录（若要调试，请取消下面两行注释）
	     //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
	    //AlipayCore.logResult(sWord);
	     if (isSign && responseTxt.equals("true")) {
	         return true;
	     } else {
	         return false;
	     }
	 }

	/**
	  * 获取远程服务器ATN结果,验证返回URL
	  * @param notify_id 通知校验ID
	  * @return 服务器ATN结果
	  * 验证结果集：
	  * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	  * true 返回正确信息
	  * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	  */
	  public static String verifyAliPayResponse(String notify_id,String partner) {
	      //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
	      String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
	      return checkAliPayUrl(veryfy_url);
	  }

	/**
	   * 获取远程服务器ATN结果
	   * @param urlvalue 指定URL路径地址
	   * @return 服务器ATN结果
	   * 验证结果集：
	   * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
	   * true 返回正确信息
	   * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	   */
	   public static String checkAliPayUrl(String urlvalue) {
	       String inputLine = "";
	       try {
	           URL url = new URL(urlvalue);
	           HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	           BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
	               .getInputStream()));
	           inputLine = in.readLine().toString();
	       } catch (Exception e) {
	           e.printStackTrace();
	           inputLine = "";
	       }
	       return inputLine;
	   }

	/**
	    * 根据反馈回来的信息，生成签名结果
	    * @param Params 通知返回来的参数数组
	    * @param sign 比对的签名结果
	    * @return 生成的签名结果
	    */
	public static boolean getSignVeryfy(Map<String, String> Params, String sign,String key) {
	   	//过滤空值、sign与sign_type参数
	   	Map<String, String> sParaNew = paraFilter(Params);
	       //获取待签名字符串
	       String preSignStr = createLinkString(sParaNew);
	       //获得签名验证结果
	       boolean isSign = false;
	       isSign = verify(preSignStr, sign,key, "utf-8");
	       return isSign;
	 }

	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param sign 签名结果
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if(mysign.equals(sign)) {
			return true;
		}
		else {
			return false;
		}
	}

	/** 
	  * 除去数组中的空值和签名参数
	  * @param sArray 签名参数组
	  * @return 去掉空值与签名参数后的新签名参数组
	  */
	 public static Map<String, String> paraFilter(Map<String, String> sArray) {
	     Map<String, String> result = new HashMap<String, String>();
	     if (sArray == null || sArray.size() <= 0) {
	         return result;
	     }
	     for (String key : sArray.keySet()) {
	         String value = sArray.get(key);
	         if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
	             || key.equalsIgnoreCase("sign_type")) {
	             continue;
	         }
	         result.put(key, value);
	     }
	     return result;
	 }

	/**
	  * 生成签名结果
	  * @param sPara 要签名的数组
	  * @return 签名结果字符串
	  */
	public static String buildRequestMysign(Map<String, String> sPara,String key) {
	 	String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
	     String mysign = "";
	     mysign = sign(prestr, key, "utf-8");
	     return mysign;
	 }

	/** 
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
	    List<String> keys = new ArrayList<String>(params.keySet());
	    Collections.sort(keys);
	    String prestr = "";
	    for (int i = 0; i < keys.size(); i++) {
	        String key = keys.get(i);
	        String value = params.get(key);
	        if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
	            prestr = prestr + key + "=" + value;
	        } else {
	            prestr = prestr + key + "=" + value + "&";
	        }
	    }
	    return prestr;
	}

	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param key 密钥
	 * @param input_charset 编码格式
	 * @return 签名结果
	 */
	public static String sign(String text, String key, String input_charset) {
		text = text + key;
	    return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}

	public static byte[] getContentBytes(String content, String charset) {
	    if (charset == null || "".equals(charset)) {
	        return content.getBytes();
	    }
	    try {
	        return content.getBytes(charset);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
	    }
	}

	/**
	* 将需要传递给微信的参数转成xml格式
	* @param parameters
	* @return
	*/
	public static String assembParamToXml(Map<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<String> es = parameters.keySet();
		List<Object> list=new ArrayList<Object>(es);
		Object[] ary =list.toArray();
		Arrays.sort(ary);
		list=Arrays.asList(ary);
		Iterator<Object> it = list.iterator();
		while(it.hasNext()) {
			String key =  (String) it.next();
			String val=(String) parameters.get(key);
			if ("attach".equalsIgnoreCase(key)||"body".equalsIgnoreCase(key)||"sign".equalsIgnoreCase(key)) {
				sb.append("<"+key+">"+"<![CDATA["+val+"]]></"+key+">");
			}else {
				sb.append("<"+key+">"+val+"</"+key+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	public static String getRequestXml(Map<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k=(String)entry.getKey();
			String v=(String) entry.getValue();
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	* 处理xml请求信息
	* @param reequest
	* @return
	*/
	public static String getWeiXinResponse(HttpServletRequest request){
		BufferedReader bis=null;
		String result="";
		try{
			bis=new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line=null;
			while((line=bis.readLine())!=null){
				result+=line;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bis!=null){
				try{
					bis.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	* 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	* @param strxml
	* @return
	* @throws JDOMException
	* @throws IOException
	*/
	public static Map parseXMLToMap(String strxml) throws JDOMException, IOException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\""+Constants.UTF8+"\"");   	 
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		Map m = new HashMap();
		InputStream in = new ByteArrayInputStream(strxml.getBytes(Constants.UTF8));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v =PayUtil.getChildrenText(children);
			}
			m.put(k, v);
		}
		//关闭流
		in.close();
		return m;
	}

	/**
	* 获取子结点的xml
	* @param children
	* @return String
	*/
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}

	/**
	* 微信支付签名sign
	* @param characterEncoding
	* @param param
	* @param key
	* @return
	*/
	@SuppressWarnings("unchecked")
	public static String createSign(Map<String, String> param,String key){
		//签名步骤一：按字典排序参数
		List list=new ArrayList(param.keySet());
		Object[] ary =list.toArray();
		Arrays.sort(ary);
		list=Arrays.asList(ary);
		String str="";
		for(int i=0;i<list.size();i++){
			str+=list.get(i)+"="+param.get(list.get(i)+"")+"&";
		}
		//签名步骤二：加上key
		str+="key="+key;
		//步骤三：加密并大写
		str=PayUtil.MD5Encode(str,"utf-8").toUpperCase();
		return str;
	}

	public static String MD5Encode(String origin,String charsetName){
		String resultString=null;
		try{
			resultString=new String(origin);
			MessageDigest md=MessageDigest.getInstance("MD5");
			if(StringUtils.isBlank(charsetName)){
				resultString=byteArrayToHexString(md.digest(resultString.getBytes()));
			}else{
				resultString=byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
			}
		}catch(Exception e){
		
		}
		return resultString;
	}

	public static String byteArrayToHexString(byte b[]){
		StringBuffer resultSb=new StringBuffer();
		for(int i=0;i<b.length;i++){
			resultSb.append(PayUtil.byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String byteToHexString(byte b){
		int n=b;
		if(n<0){
			n+=256;
		}
		int d1=n/16;
		int d2=n%16;
		return PayUtil.hexDigits[d1]+PayUtil.hexDigits[d2];
	}

	public static final String hexDigits[]={ "0", "1", "2", "3", "4", "5",  
	"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


	/**
	 * 元转换为分
	 * @param amount
	 */
	public static String changeY2F(Double amount){    
	        String currency =  amount.toString();  
	        int index = currency.indexOf(".");    
	        int length = currency.length();    
	        Long amLong = 0l;    
	        if(index == -1){    
	            amLong = Long.valueOf(currency+"00");    
	        }else if(length - index >= 3){    
	            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
	        }else if(length - index == 2){    
	            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
	        }else{    
	            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
	        }    
	        return amLong.toString();    
	}

}
