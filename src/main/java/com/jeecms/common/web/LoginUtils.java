package com.jeecms.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;

import com.jeecms.core.entity.CmsUser;
import com.jeecms.core.manager.CmsUserMng;

public class LoginUtils {
	
	public static void loginShiro(HttpServletRequest request,
			HttpServletResponse response,String username){
		PrincipalCollection principals = new SimplePrincipalCollection(username, username); 
		Builder builder = new WebSubject.Builder( request,response);  
		builder.principals(principals);  
		builder.authenticated(true);  
		WebSubject subject = builder.buildWebSubject();   
		ThreadContext.bind(subject); 
	}
	
	public static void logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
	}
}
