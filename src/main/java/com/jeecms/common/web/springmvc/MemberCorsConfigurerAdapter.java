package com.jeecms.common.web.springmvc;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jeecms.cms.web.MemberApiInterceptor;
import com.jeecms.common.util.PropertyUtils;


/**
 * 
 * @author tom
 * @date 2017年11月17日
 */
@Configuration
@EnableWebMvc
//WebMvcConfigurationSupport
public class MemberCorsConfigurerAdapter extends  WebMvcConfigurerAdapter {
	private static final String cross_prefix="Cross.domain";
	
    public void addCorsMappings(CorsRegistry registry) {
    	String pathPattern="/**";
        CorsRegistration corsRe=(CorsRegistration) registry.addMapping(pathPattern);
        corsRe.allowedHeaders("*").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS");
        List<String>urls=getUrls();
        int urlSize=urls.size();
        //*开放访问
        if(urlSize<=0){
        	corsRe.allowedOrigins("*");
        }else if(urlSize<=1){
        	corsRe.allowedOrigins(urls.get(0));
        }else if(urlSize<=2){
        	corsRe.allowedOrigins(urls.get(0),urls.get(1));
        }else if(urlSize<=3){
        	corsRe.allowedOrigins(urls.get(0),urls.get(1),urls.get(2));
        }else if(urlSize<=4){
        	corsRe.allowedOrigins(urls.get(0),urls.get(1),urls.get(2),urls.get(3));
        }else if(urlSize<=5){
        	corsRe.allowedOrigins(urls.get(0),urls.get(1),urls.get(2),urls.get(3),urls.get(4));
        }
    }
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
    	registry.addInterceptor(memberApiInterceptor);
	}
    
    @Override
   	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
   		// TODO Auto-generated method stub
       	exceptionResolvers.add(handlerExceptionResolver);
   		super.configureHandlerExceptionResolvers(exceptionResolvers);
   	}

    
    @PostConstruct
    public void init(){
    	List<String>urls=propertyUtils.getList(cross_prefix);
		setUrls(urls);
    }

    @Autowired
	private MemberApiInterceptor memberApiInterceptor;
    @Autowired
    private HandlerApiExceptionResolver handlerExceptionResolver;
    @Autowired
    private PropertyUtils propertyUtils;
    private List<String>urls;

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
	
    
    
}

