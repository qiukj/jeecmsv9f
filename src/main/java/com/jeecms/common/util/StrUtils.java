package com.jeecms.common.util;


import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.jeecms.cms.api.Constants;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 * 
 */
public class StrUtils {
	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if (url.equals("") || url.startsWith("http://")
				|| url.startsWith("https://")) {
			return url;
		} else {
			return "http://" + url.trim();
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 把html内容转为文本
	 * @param html 需要处理的html文本
	 * @return
	 */
	public static String trimHtml2Txt(String html){	
		html = html.replaceAll("\\<head>[\\s\\S]*?</head>(?i)", "");//去掉head
		html = html.replaceAll("\\<!--[\\s\\S]*?-->", "");//去掉注释
		html = html.replaceAll("\\<![\\s\\S]*?>", "");
		html = html.replaceAll("\\<style[^>]*>[\\s\\S]*?</style>(?i)", "");//去掉样式
		html = html.replaceAll("\\<script[^>]*>[\\s\\S]*?</script>(?i)", "");//去掉js
		html = html.replaceAll("\\<w:[^>]+>[\\s\\S]*?</w:[^>]+>(?i)", "");//去掉word标签
		html = html.replaceAll("\\<xml>[\\s\\S]*?</xml>(?i)", "");
		html = html.replaceAll("\\<table>[\\s\\S]*?</table>(?i)", "");
		html = html.replaceAll("\\<html[^>]*>|<body[^>]*>|</html>|</body>(?i)", "");
		html = html.replaceAll("\\\r\n|\n|\r", "");//去掉换行
		html = html.replaceAll("\\<br[^>]*>(?i)", "\r\n");
		html = html.replaceAll("\\</p>(?i)", "\r\n");
		html = html.replaceAll("\\<p>(?i)", "\r\n");
		//图片替换特殊标记，客户端自己下载图片并替换标记
		//<img[^>]*/>
		String regular="<(?i)img(.*?)src=\"(.*?)>";  
        String img_pre="<(?i)img(.*?)src=\"";
        String img_sub="\"(.*?)>";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);  
        String src = null;  
        String newSrc=null;
        while (m.find()) {  
        	src=m.group();
        	newSrc=src.replaceAll(img_pre, Constants.API_PLACEHOLDER_IMAGE_BEGIN)
        			.replaceAll(img_sub,  Constants.API_PLACEHOLDER_IMAGE_END).trim();
        	html=html.replace(src, newSrc);
        }  
        String videoregular="<video(.*?)src=\"(.*?)\"(.*?)</video>";  
        String video_pre="<video(.*?)src=\"";
        String video_sub="\"(.*?)</video>";
        Pattern videop=Pattern.compile(videoregular,Pattern.CASE_INSENSITIVE);
        Matcher videom = videop.matcher(html);  
        String videosrc = null;  
        String videonewSrc=null;
        while (videom.find()) {  
        	videosrc=videom.group();
        	videonewSrc=videosrc.replaceAll(video_pre, Constants.API_PLACEHOLDER_VIDEO_BEGIN)
        			.replaceAll(video_sub,  Constants.API_PLACEHOLDER_VIDEO_END).trim();
        	html=html.replace(videosrc, videonewSrc);
        }  
        //去除分页
		html=html.replace("[NextPage][/NextPage]", "");
		html = html.replaceAll("\\<[^>]+>", "");
		//html = html.replaceAll("\\ ", " ");
		return html.trim();
	}
	
	public static  List<String> getVideoSrc(String htmlCode) {  
        List<String> imageSrcList = new ArrayList<String>();  
        String regular="<video(.*?)src=\"(.*?)\"(.*?)</video>";  
        String video_pre="<video(.*?)src=\"";
        String video_sub="\"(.*?)</video>";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null;  
        while (m.find()) {  
        	src=m.group();
        	src=src.replaceAll(video_pre, "").replaceAll(video_sub, "").trim();
            imageSrcList.add(src);  
        }  
        return imageSrcList;  
    }
	
	

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	public static String htmlCut(String s, int len, String append) {
		String text = html2Text(s, len * 2);
		return textCut(text, len, append);
	}

	public static String html2Text(String html, int len) {
		try {
			Lexer lexer = new Lexer(html);
			Node node;
			StringBuilder sb = new StringBuilder(html.length());
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof TextNode) {
					sb.append(node.toHtml());
				}
				if (sb.length() > len) {
					break;
				}
			}
			return sb.toString();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param keyword 源词汇
	 * @param smart 是否智能分词
	 * @return 分词词组(,拼接)
	 */
	public static String getKeywords(String keyword, boolean smart) {
		StringReader reader = new StringReader(keyword);
		IKSegmenter iks = new IKSegmenter(reader, smart);
		StringBuilder buffer = new StringBuilder();
		try {
			Lexeme lexeme;
			while ((lexeme = iks.next()) != null) {
				buffer.append(lexeme.getLexemeText()).append(',');
			}
		} catch (IOException e) {
		}
		//去除最后一个,
		if (buffer.length() > 0) {
			buffer.setLength(buffer.length() - 1);
		}
		return buffer.toString();
	}
	
	/**
	 * p换行
	 * @param inputString
	 * @return
	 */
	public static String removeHtmlTagP(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    java.util.regex.Pattern p_script;  
	    java.util.regex.Matcher m_script;  
	    java.util.regex.Pattern p_style;  
	    java.util.regex.Matcher m_style;  
	    java.util.regex.Pattern p_html;  
	    java.util.regex.Matcher m_html;  
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
	        m_script = p_script.matcher(htmlStr);  
	        htmlStr = m_script.replaceAll(""); // 过滤script标签  
	        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
	        m_style = p_style.matcher(htmlStr);  
	        htmlStr = m_style.replaceAll(""); // 过滤style标签  
	        htmlStr.replace("</p>", "\n");
	        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	        m_html = p_html.matcher(htmlStr);  
	        htmlStr = m_html.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  
	
	public static String removeHtmlTag(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    java.util.regex.Pattern p_script;  
	    java.util.regex.Matcher m_script;  
	    java.util.regex.Pattern p_style;  
	    java.util.regex.Matcher m_style;  
	    java.util.regex.Pattern p_html;  
	    java.util.regex.Matcher m_html;  
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
	        m_script = p_script.matcher(htmlStr);  
	        htmlStr = m_script.replaceAll(""); // 过滤script标签  
	        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
	        m_style = p_style.matcher(htmlStr);  
	        htmlStr = m_style.replaceAll(""); // 过滤style标签  
	        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	        m_html = p_html.matcher(htmlStr);  
	        htmlStr = m_html.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  

	/**
	 * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * 
	 * @param str
	 * @param search
	 * @return
	 */
	public static boolean contains(String str, String search) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}

	public static boolean containsKeyString(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		if (str.contains("'") || str.contains("\"") || str.contains("\r")
				|| str.contains("\n") || str.contains("\t")
				|| str.contains("\b") || str.contains("\f")) {
			return true;
		}
		return false;
	}
	
	
	public static String addCharForString(String str, int strLength,char c,int position) {
		  int strLen = str.length();
		  if (strLen < strLength) {
			  while (strLen < strLength) {
			  StringBuffer sb = new StringBuffer();
			  if(position==1){
				  //右補充字符c
				  sb.append(c).append(str);
			  }else{
				//左補充字符c
				  sb.append(str).append(c);
			  }
			  str = sb.toString();
			  strLen = str.length();
			  }
			}
		  return str;
	 }

	// 将""和'转义
	public static String replaceKeyString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	//单引号转化成双引号
	public static String replaceString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\"").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	public static String getSuffix(String str) {
		int splitIndex = str.lastIndexOf(".");
		return str.substring(splitIndex + 1);
	}
	
	/**
     * 补齐不足长度
     * @param length 长度
     * @param number 数字
     * @return
     */
	public static String lpad(int length, Long number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }
	
	/**
	 * 保留两位小数（四舍五入）
	 * @param value
	 * @return
	 */
	public static Double retainTwoDecimal(double value){
		long l1 = Math.round(value*100); //四舍五入
		double ret = l1/100.0; //注意:使用 100.0 而不是 100
		return ret;
	}
	
	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	public static  String xssEncode(String s) {
		if (s == null || s.equals("")) {
			return s;
		}
		//< > ' " \ / # & 
//		s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");  
//		s = s.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");  
//		s = s.replaceAll("'", "&#39;");  
//		s = s.replaceAll("eval\\((.*)\\)", "");  
//		s = s.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");  
//		s = s.replaceAll("script", "");
//		s = s.replaceAll("#", "＃");
		//s = s.replaceAll("%", "％");
		/*
		try {
			s = URLDecoder.decode(s, UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		String result = stripXSS(s);  
        if (null != result) {  
            result = escape(result);  
        }  
		return result;
	}
	
	public static String escape(String s) {  
        StringBuilder sb = new StringBuilder(s.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':  
                sb.append('＞');// 全角大于号  
                break;  
            case '<':  
                sb.append('＜');// 全角小于号  
                break;  
            case '\'':  
                sb.append('‘');// 全角单引号  
                break;  
            case '\"':  
                sb.append('“');// 全角双引号  
                break;  
            case '\\':  
                sb.append('＼');// 全角斜线  
                break;    
            case ';':  
                sb.append('；'); // 全角分号  
                break;  
            default:  
                sb.append(c);  
                break;  
            }  
  
        }  
        return sb.toString();  
    }  
	private static String stripXSS(String value) {  
        if (value != null) {  
            // NOTE: It's highly recommended to use the ESAPI library and  
            // uncomment the following line to  
            // avoid encoded attacks.  
            // value = ESAPI.encoder().canonicalize(value);  
            // Avoid null characters  
            value = value.replaceAll("", "");  
            // Avoid anything between script tags  
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",  
                    Pattern.CASE_INSENSITIVE);  
  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid anything in a src='...' type of expression  
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Remove any lonesome </script> tag  
            scriptPattern = Pattern.compile("</script>",  
                    Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Remove any lonesome <script ...> tag  
            scriptPattern = Pattern.compile("<script(.*?)>",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid eval(...) expressions  
            scriptPattern = Pattern.compile("eval\\((.*?)\\)",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid expression(...) expressions  
            scriptPattern = Pattern.compile("expression\\((.*?)\\)",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid javascript:... expressions  
            scriptPattern = Pattern.compile("javascript:",  
                    Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid vbscript:... expressions  
            scriptPattern = Pattern.compile("vbscript:",  
                    Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid onload= expressions  
            scriptPattern = Pattern.compile("onload(.*?)=",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            
            scriptPattern = Pattern.compile("onmouseover(.*?)=",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>",  
                    Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            scriptPattern = Pattern.compile("</iframe>",  
                    Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Remove any lonesome <script ...> tag  
            scriptPattern = Pattern.compile("<iframe(.*?)>",  
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
                            | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            value = value.replace(";", "");  
            value = value.replace("<", "");  
            value = value.replace(">", "");  
        }  
        return value;  
    }
	
	public static boolean isGreaterZeroNumeric(String str) {
        Pattern pattern = Pattern.compile("[1-9]*");
        return pattern.matcher(str).matches();
    }
	
	 /**  
     * 检查整数  
     * @param num  
     * @param type "0+":非负整数 "+":正整数 "-0":非正整数 "-":负整数 "":整数  
     * @return  
     */  
    public static boolean checkNumber(String num,String type){   
        String eL = "";   
        if(type.equals("0+"))eL = "^\\d+$";//非负整数   
        else if(type.equals("+"))eL = "^\\d*[1-9]\\d*$";//正整数   
        else if(type.equals("-0"))eL = "^((-\\d+)|(0+))$";//非正整数   
        else if(type.equals("-"))eL = "^-\\d*[1-9]\\d*$";//负整数   
        else eL = "^-?\\d+$";//整数   
        Pattern p = Pattern.compile(eL);   
        Matcher m = p.matcher(num);   
        boolean b = m.matches();   
        return b;   
    }   
    /**  
     * 检查浮点数  
     * @param num  
     * @param type "0+":非负浮点数 "+":正浮点数 "-0":非正浮点数 "-":负浮点数 "":浮点数  
     * @return  
     */  
    public static boolean checkFloat(String num,String type){   
        String eL = "";   
        if(type.equals("0+"))eL = "^\\d+(\\.\\d+)?$";//非负浮点数   
        else if(type.equals("+"))eL = "^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";//正浮点数   
        else if(type.equals("-0"))eL = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";//非正浮点数   
        else if(type.equals("-"))eL = "^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";//负浮点数   
        else eL = "^(-?\\d+)(\\.\\d+)?$";//浮点数   
        Pattern p = Pattern.compile(eL);   
        Matcher m = p.matcher(num);   
        boolean b = m.matches();   
        return b;   
    } 
    
    public static Integer[]getInts(String ids){
    	Integer[]idArrays=null;
    	List<Integer>idList=new ArrayList<Integer>();
    	if(StringUtils.isNotBlank(ids)){
    		String[]objArray=ids.split(",");
    		if(objArray!=null&&objArray.length>0){
    			for(int i=0;i<objArray.length;i++){
    				if(checkNumber(objArray[i], "0+")){
    					idList.add(Integer.parseInt(objArray[i]));
    				}
    			}
    		}
			idArrays=new Integer[idList.size()];
    		for(int i=0;i<idList.size();i++){
    			idArrays[i]=idList.get(i);
			}
    	}
    	return idArrays;
    }
    
    public static String[]getStrArray(String ids){
    	String[]idArrays=null;
    	if(StringUtils.isNotBlank(ids)){
    		idArrays=ids.split(",");
    	}
    	return idArrays;
    }
    
    public static String[]getStrArrayComplete(String ids){
    	String[]idArrays=null;
    	if(StringUtils.isNotBlank(ids)){
    		idArrays=ids.split(",",-1);
    	}
    	return idArrays;
    }
    
    public static Long[] getLongs(String ids){
		Long[]idArrays=null;
    	List<Long>idList=new ArrayList<Long>();
    	if(StringUtils.isNotBlank(ids)){
    		String[]objArray=ids.split(",");
    		if(objArray!=null&&objArray.length>0){
    			for(int i=0;i<objArray.length;i++){
    				if(checkNumber(objArray[i], "0+")){
    					idList.add(Long.parseLong(objArray[i]));
    				}
    			}
    		}
			idArrays=new Long[idList.size()];
    		for(int i=0;i<idList.size();i++){
    			idArrays[i]=idList.get(i);
			}
    	}
    	return idArrays;
	}
    


	public static void main(String args[]) {
		System.out.println(replaceKeyString("&nbsp;\r" + "</p>"));
		System.out.println(isGreaterZeroNumeric("12"));
	}

}
