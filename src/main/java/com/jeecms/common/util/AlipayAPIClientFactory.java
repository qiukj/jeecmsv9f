/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.jeecms.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;


/**
 * API调用客户端工厂
 */
public class AlipayAPIClientFactory {

    /** API调用客户端 */
    private static AlipayClient alipayClient;
    
    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public static AlipayClient getAlipayClient(String url,String appId,
    		String privateKey,String publicKey,String charset){
        
        if(null == alipayClient){
            alipayClient = new DefaultAlipayClient(url, appId, 
            		privateKey, "json",charset,publicKey);
        }
        return alipayClient;
    }
}
