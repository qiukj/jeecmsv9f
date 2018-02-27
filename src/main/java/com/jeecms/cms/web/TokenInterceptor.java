package com.jeecms.cms.web;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.util.CmsUtils;


public class TokenInterceptor  extends HandlerInterceptorAdapter{
    private static final Logger LOG = Logger.getLogger(Token.class);
    @Override
    public boolean preHandle(HttpServletRequest request, 
    		HttpServletResponse response,Object handler) throws Exception {
    	CmsSite site=CmsUtils.getSite(request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Token annotation = method.getAnnotation(Token.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                	String token=UUID.randomUUID().toString();
                	request.setAttribute("token", token);
                    sessionProvider.setAttribute(request, response, "token", token);
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                         LOG.warn("please don't repeat submit,url:"+ request.getServletPath());
                         response.sendRedirect(site.getUrl());
                         return false;
                    }
                    request.setAttribute("token", null);
                    sessionProvider.setAttribute(request, response, "token", null);
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken =(String) sessionProvider.getAttribute(request, "token");
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("token");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
    
    @Autowired
    private SessionProvider sessionProvider;
}
