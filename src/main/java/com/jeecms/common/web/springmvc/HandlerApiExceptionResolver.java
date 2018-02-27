package com.jeecms.common.web.springmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import com.jeecms.cms.api.ApiResponse;
import com.jeecms.cms.api.Constants;
import com.jeecms.cms.api.ResponseCode;
import com.jeecms.common.web.ResponseUtils;

/**
 * @author Tom
 */
public class HandlerApiExceptionResolver  implements org.springframework.web.servlet.HandlerExceptionResolver {
	private static final Logger log = LoggerFactory.getLogger(HandlerApiExceptionResolver.class);
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object o, Exception e) {
		e.printStackTrace();
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_FAIL;
		String body="\"\"";
		String message=Constants.API_STATUS_APPLICATION_ERROR;
	     Class<?> exceptionClass=e.getClass();
	     if(exceptionClass.equals(MissingServletRequestParameterException.class)){
	    	  code=ResponseCode.API_CODE_PARAM_REQUIRED;
	    	  message=Constants.API_MESSAGE_PARAM_REQUIRED;
	     }else if(exceptionClass.equals(MethodArgumentTypeMismatchException.class)
	    		 ||exceptionClass.equals(TypeMismatchException.class)){
	    	  code=ResponseCode.API_CODE_PARAM_TYPE_ERROR;
	    	  message=Constants.API_MESSAGE_PARAM_TYPE_ERROR;
	     }else if(exceptionClass.equals(ServletRequestBindingException.class)){
	    	  code=ResponseCode.API_CODE_PARAM_BIND_ERROR;
	    	  message=Constants.API_MESSAGE_PARAM_BIND_ERROR;
	     }else if(exceptionClass.equals(DataIntegrityViolationException.class)){
	    	  code=ResponseCode.API_CODE_DATA_INTERGER_VIOLATION;
	    	  message=Constants.API_MESSAGE_DATA_INTERGER_VIOLATION;
	     }else if(exceptionClass.equals(BadSqlGrammarException.class)){
	    	 code=ResponseCode.API_CODE_SQL_ERROR;
	    	 message=Constants.API_MESSAGE_SQL_ERROR;
	     }
	     log.error("msg->"+e.getMessage());
	     e.printStackTrace();
	     ApiResponse apiResponse=new ApiResponse(request, body, message,code);
	     ResponseUtils.renderApiJson(response, request, apiResponse);
	     ModelAndView modelAndView=new ModelAndView();  
	     return modelAndView;  
	}
}
