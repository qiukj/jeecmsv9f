package com.jeecms.cms.web;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * 
 * @author tom
 *
 */
public class ApplicationContextUtil{
	private static ApplicationContext applicationContext;  
    public static Object getBean(String name){
    	if(applicationContext==null){
    		applicationContext = ContextLoader.getCurrentWebApplicationContext(); 
    	}
        return applicationContext.getBean(name);
    }
}
