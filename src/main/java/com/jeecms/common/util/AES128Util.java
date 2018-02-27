package com.jeecms.common.util;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
 
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
 
public class AES128Util {
     
    //算法名
    public static final String KEY_ALGORITHM = "AES";
    //加解密算法/模式/填充方式
    //可以任意选择，为了方便后面与iOS端的加密解密，采用与其相同的模式与填充方式
    //ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
 
    //生成密钥
    private static byte[] generateKey(String aesKey) throws Exception{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        /*
        KeyGenerator kgen =KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(128, new SecureRandom(aesKey.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		byte[] encodeFormat = secretKey.getEncoded();
		SecretKeySpec keySpec = new SecretKeySpec(encodeFormat, "AES");
        return keySpec.getEncoded();
        */
        return aesKey.getBytes();
    }
     
    //生成iv
    private static AlgorithmParameters generateIV(String ivVal) throws Exception{
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        //byte[] iv = new byte[16];
        //Arrays.fill(iv, (byte) 0x00);
        //Arrays.fill(iv,ivVal.getBytes());
        byte[]iv=ivVal.getBytes();
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }
     
    //转化成JAVA的密钥格式
    private static Key convertToKey(byte[] keyBytes) throws Exception{
        SecretKey secretKey = new SecretKeySpec(keyBytes,KEY_ALGORITHM);
        return secretKey;
    }
     
    //加密
    public static String encrypt(String plainText,String aesKey,String ivVal) throws Exception {
    	byte[] data=plainText.getBytes();
    	AlgorithmParameters iv=generateIV(ivVal);
    	byte[] keyBytes = generateKey(aesKey);
    	//转化为密钥
        Key key = convertToKey(keyBytes);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);
        byte[] encryptData= cipher.doFinal(data);
        return bytesToHexString(encryptData);
    }
     
    //解密
    public static String decrypt(String encryptedStr,String aesKey,String ivVal) throws Exception{
    	byte[] encryptedData=hexStringToByte(encryptedStr);
    	byte[] keyBytes = generateKey(aesKey);
    	Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv=generateIV(ivVal);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key,iv);
        byte[] decryptData=cipher.doFinal(encryptedData);
        return new String(decryptData);
    }
    

    
    /**
     * 十六进制字符串转换成数组
     * @param hex
     * @return
     */
    private static byte[] hexStringToByte(String hex) {   
        int len = (hex.length() / 2);   
        byte[] result = new byte[len];   
        char[] achar = hex.toCharArray();   
        for (int i = 0; i < len; i++) {   
         int pos = i * 2;   
         result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));   
        }   
        return result;   
    }  
    
    private static byte toByte(char c) {   
        byte b = (byte) "0123456789abcdef".indexOf(c);   
        return b;   
    }  
      
    
    /** 
     * 把字节数组转换成16进制字符串  
     * @param bArray  
     * @return  
     */   
    private static final String bytesToHexString(byte[] bArray) {   
	     StringBuffer sb = new StringBuffer(bArray.length);   
	     String sTemp;   
	     for (int i = 0; i < bArray.length; i++) {   
	      sTemp = Integer.toHexString(0xFF & bArray[i]);   
	      if (sTemp.length() < 2)   
	       sb.append(0);   
	      sb.append(sTemp.toLowerCase());   
	     }   
	     return sb.toString();   
	 }  
     
    public static void main(String[] args) {
        //明文
        String plainTextString = "B5F7E21EAB2C6FE119DD9AC5E38187A5";
        System.out.println("明文 : "+plainTextString);
        String aesKey="S9u978Q31NGPGc5H";
        String ivVal="X83yESM9iShLxfwS";
        try {
            //进行加密
            String encryptedData = encrypt(plainTextString, aesKey,ivVal);
            //输出加密后的数据
            System.out.println("加密后的数据 : ");
            System.out.println(encryptedData);
            System.out.println();
            String data = AES128Util.decrypt(encryptedData, aesKey,ivVal);
            System.out.println("解密得到的数据 : " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
}