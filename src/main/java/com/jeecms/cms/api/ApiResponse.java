package com.jeecms.cms.api;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.web.springmvc.MessageResolver;

public class ApiResponse {

	
	public ApiResponse(HttpServletRequest request, 
			String body, String message, String code,Object...msgParam) {
		super();
		this.body = body;
		//this.message = message;
		this.code = code;
		try {
			this.message ="\""+MessageResolver.getMessage(request, message,msgParam)+"\"";
		} catch (Exception e) {
			// TODO: handle exception
			this.message ="\""+message+"\"";
		}
	}

	/**
	 * 返回信息主体
	 */
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * API调用提示信息
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * API接口调用状态
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "{\"body\":" + body + ", \"message\":" + message + ", \"code\":" + code + "}";
	}
	
	public String sourceToString() {
		return "{\"source\":" + body + ", \"message\":" + message + ", \"code\":" + code + "}";
	}

	private String body;
	private String message;
	private String status;
	private String code;
}
