package com.jeecms.common.web.freemarker;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Version;

public class DefaultObjectWrapperBuilderFactory {
	
	private DefaultObjectWrapperBuilderFactory() {} 
	
	private static final Version v=new Version(2, 3, 24);
	private static final DefaultObjectWrapperBuilder defaultObjectWrapperBuilder =  new DefaultObjectWrapperBuilder(v);  

    //静态工厂方法   
    public static DefaultObjectWrapperBuilder getInstance() {  
        return defaultObjectWrapperBuilder;  
    }
    
    public static DefaultObjectWrapper getDefaultObjectWrapper() {  
        return defaultObjectWrapperBuilder.build();  
    }
    
}
