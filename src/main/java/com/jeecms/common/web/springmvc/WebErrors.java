package com.jeecms.common.web.springmvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * WEB错误信息
 * 
 * 可以通过MessageSource实现国际化。
 */
public abstract class WebErrors {
	/**
	 * email正则表达式
	 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("^\\w+(\\.\\w+)*@\\w+(\\.\\w+)+$");
	/**
	 * username正则表达式
	 */
	public static final Pattern USERNAME_PATTERN = Pattern
			.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5\\.\\-@_]+$");

	/**
	 * 通过HttpServletRequest创建WebErrors
	 * 
	 * @param request
	 *            从request中获得MessageSource和Locale，如果存在的话。
	 */
	public WebErrors(HttpServletRequest request) {
		WebApplicationContext webApplicationContext = RequestContextUtils.getWebApplicationContext(request);
		//需要serverlet3.0支持 ，tomcat6不支持serverlet3.0
		//WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
		if (webApplicationContext != null) {
			LocaleResolver localeResolver = RequestContextUtils
					.getLocaleResolver(request);
			Locale locale;
			if (localeResolver != null) {
				locale = localeResolver.resolveLocale(request);
				this.messageSource = webApplicationContext;
				this.locale = locale;
			}
		}
	}

	public WebErrors() {
	}

	/**
	 * 构造器
	 * 
	 * @param messageSource
	 * @param locale
	 */
	public WebErrors(MessageSource messageSource, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
	}

	public String getMessage(String code, Object... args) {
		if (messageSource == null) {
			throw new IllegalStateException("MessageSource cannot be null.");
		}
		return messageSource.getMessage(code, args, locale);
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @param args
	 *            错误参数
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code, Object... args) {
		getErrors().add(getMessage(code, args));
	}

	/**
	 * 添加错误代码
	 * 
	 * @param code
	 *            错误代码
	 * @see org.springframework.context.MessageSource#getMessage
	 */
	public void addErrorCode(String code) {
		getErrors().add(getMessage(code));
	}

	/**
	 * 添加错误字符串
	 * 
	 * @param error
	 */
	public void addErrorString(String error) {
		getErrors().add(error);
	}
	/**
	 * 添加错误，根据MessageSource是否存在，自动判断为code还是string。
	 * 
	 * @param error
	 */
	public void addError(String error) {
		// if messageSource exist
		if (messageSource != null) {
			error = messageSource.getMessage(error, null, error, locale);
		}
		getErrors().add(error);
	}

	/**
	 * 是否存在错误
	 * 
	 * @return
	 */
	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	/**
	 * 错误数量
	 * 
	 * @return
	 */
	public int getCount() {
		return errors == null ? 0 : errors.size();
	}

	/**
	 * 错误列表
	 * 
	 * @return
	 */
	public List<String> getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		return errors;
	}
	
	

	/**
	 * 将错误信息保存至ModelMap，并返回错误页面。
	 * 
	 * @param model
	 * @return 错误页面地址
	 * @see org.springframework.ui.ModelMap
	 */
	public String showErrorPage(ModelMap model) {
		toModel(model);
		return getErrorPage();
	}

	/**
	 * 将错误信息保存至ModelMap
	 * 
	 * @param model
	 */
	public void toModel(Map<String, Object> model) {
		Assert.notNull(model);
		if (!hasErrors()) {
			throw new IllegalStateException("no errors found!");
		}
		model.put(getErrorAttrName(), getErrors());
	}

	public boolean ifNull(Object o, String field, boolean isCode) {
		if (o == null) {
			if(isCode){
				addErrorCode("error.required", field);
			}else{
				addErrorString("error.required");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean ifEmpty(Object[] o, String field, boolean isCode) {
		if (o == null || o.length <= 0) {
			if(isCode){
				addErrorCode("error.required", field);
			}else{
				addError("error.required");
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean ifBlank(String s, String field, int maxLength, boolean isCode) {
		if (StringUtils.isBlank(s)) {
			if(isCode){
				addErrorCode("error.required", field);
			}else{
				addErrorString("error.required");
			}
			return true;
		}
		if (ifMaxLength(s, field, maxLength, true)) {
			return true;
		}
		return false;
	}

	public boolean ifMaxLength(String s, String field, int maxLength, boolean isCode) {
		if (s != null && s.length() > maxLength) {
			if(isCode){
				addErrorCode("error.maxLength", field, maxLength);
			}else{
				addErrorString("error.maxLength");
			}
			return true;
		}
		return false;
	}

	public boolean ifOutOfLength(String s, String field, int minLength,
			int maxLength, boolean isCode) {
		if (s == null) {
			if(isCode){
				addErrorCode("error.required", field);
			}else{
				addErrorString("error.required");
			}
			return true;
		}
		int len = s.length();
		if (len < minLength || len > maxLength) {
			if(isCode){
				addErrorCode("error.outOfLength", field, minLength, maxLength);
			}else{
				addErrorString("error.outOfLength");
			}
			return true;
		}
		return false;
	}

	public boolean ifNotEmail(String email, String field, int maxLength, boolean isCode) {
		if (ifBlank(email, field, maxLength, true)) {
			return true;
		}
		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			if(isCode){
				addErrorCode("error.email", field);
			}else{
				addErrorString("error.email");
			}
			return true;
		}
		return false;
	}

	public boolean ifNotUsername(String username, String field, int minLength,
			int maxLength, boolean isCode) {
		if (ifOutOfLength(username, field, minLength, maxLength, true)) {
			return true;
		}
		Matcher m = USERNAME_PATTERN.matcher(username);
		if (!m.matches()) {
			if(isCode){
				addErrorCode("error.username", field);
			}else{
				addErrorString("error.username");
			}
			return true;
		}
		return false;
	}

	public boolean ifNotExist(Object o, Class<?> clazz, Serializable id, boolean isCode) {
		if (o == null) {
			if(isCode){
				addErrorCode("error.notExist", clazz.getSimpleName(), id);
			}else{
				addErrorString("error.notExist");
			}
			return true;
		} else {
			return false;
		}
	}

	public void noPermission(Class<?> clazz, Serializable id, boolean isCode) {
		if(isCode){
			addErrorCode("error.noPermission", clazz.getSimpleName(), id);
		}else{
			addErrorString("error.noPermission");
		}
	}

	private MessageSource messageSource;
	private Locale locale;
	private List<String> errors;
	private List<String> codes;

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 获得本地化信息
	 * 
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 设置本地化信息
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * 获得错误页面的地址
	 * 
	 * @return
	 */
	protected abstract String getErrorPage();

	/**
	 * 获得错误参数名称
	 * 
	 * @return
	 */
	protected abstract String getErrorAttrName();
}
