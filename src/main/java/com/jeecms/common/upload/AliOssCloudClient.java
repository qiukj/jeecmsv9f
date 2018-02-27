package com.jeecms.common.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.StorageClass;
import com.jeecms.core.entity.CmsOss;

public class AliOssCloudClient {
	private static final Logger log = LoggerFactory.getLogger(AliOssCloudClient.class);

	public static boolean uploadFileByLocal(CmsOss oss, String ossPath, String localPath) {
		OSSClient ossClient = getOSSClient(oss);
		// 判断bucket是否已经存在
		if (!doesBucketExist(oss)) {
			createBucket(oss);
		}
		// 上传文件
		try {
			if(StringUtils.isNotBlank(ossPath)){
				//u/cms/www/201712/xxxxx.jpg
				String filename=ossPath;
				if(ossPath.startsWith("/")){
					filename = ossPath.substring(1);
				}
				//不带进度条
				ossClient.putObject(oss.getBucketName(),  filename, new File(localPath));
				// 关闭client
				ossClient.shutdown();
				return true;
			}
		} catch (Exception e) {
			log.equals(e.getMessage());
		}
		return false;
	}

	public static boolean uploadFileByByte(CmsOss oss, String ossPath, byte[] content) {
		OSSClient ossClient = getOSSClient(oss);
		// 判断bucket是否已经存在
		if (!doesBucketExist(oss)) {
			createBucket(oss);
		}
		// 上传
		try {
			if(StringUtils.isNotBlank(ossPath)){
				//u/cms/www/201712/xxxxx.jpg
				String filename=ossPath;
				if(ossPath.startsWith("/")){
					filename = ossPath.substring(1);
				}
				//不带进度条
				ossClient.putObject(oss.getBucketName(), filename, new ByteArrayInputStream(content));
				// 关闭client
				ossClient.shutdown();
				return true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public static boolean uploadFileByInputStream(CmsOss oss, String ossPath, InputStream inputStream) {
		OSSClient ossClient = getOSSClient(oss);
		// 判断bucket是否已经存在
		if (!doesBucketExist(oss)) {
			createBucket(oss);
		}
		// 上传
		try {
			if(StringUtils.isNotBlank(ossPath)){
				///u/cms/www/201712/xxxxx.jpg 截成u/cms/www/201712/xxxxx.jpg
				String filename=ossPath;
				if(ossPath.startsWith("/")){
					filename = ossPath.substring(1);
				}
				//不带进度条
				ossClient.putObject(oss.getBucketName(), filename, inputStream);
				// 关闭client
				ossClient.shutdown();
				/*
				try {            
			        // 带进度条的上传 
					PutObjectRequest putObjectRequest = new PutObjectRequest(oss.getBucketName(), filename, inputStream).
			                <PutObjectRequest>withProgressListener(new PutObjectProgressListener()); 
			        Callback callback = new Callback();
		            callback.setCallbackUrl(oss.getCallbackUrl());
		            callback.setCallbackHost("oss-cn-hangzhou.aliyuncs.com");
		            callback.setCallbackBody("{\\\"bucket\\\":${bucket},\\\"object\\\":${object},"
		                    + "\\\"mimeType\\\":${mimeType},\\\"size\\\":${size},"
		                    + "\\\"my_var1\\\":${x:var1},\\\"my_var2\\\":${x:var2}}");
		            callback.setCalbackBodyType(CalbackBodyType.JSON);
		            callback.addCallbackVar("x:var1", "value1");
		            callback.addCallbackVar("x:var2", "value2");
		            putObjectRequest.setCallback(callback);
		            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
		            // 读取上传回调返回的消息内容
		            byte[] buffer = new byte[1024];
		            putObjectResult.getCallbackResponseBody().read(buffer);
		            // 一定要close，否则会造成连接资源泄漏
		            putObjectResult.getCallbackResponseBody().close();
			    } catch (Exception e) {
			        //e.printStackTrace();
			    	log.error(e.getMessage());
			    }*/
				return true;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	public static boolean doesBucketExist(CmsOss oss) {
		OSSClient ossClient = getOSSClient(oss);
		boolean exists = ossClient.doesBucketExist(oss.getBucketName());
		return exists;
	}

	public static void createBucket(CmsOss oss) {
		CreateBucketRequest createBucketRequest = new CreateBucketRequest(oss.getBucketName());
		// 设置bucket权限为公共读，默认是私有读写
		createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
		// 设置bucket存储类型为低频访问类型，默认是标准类型,是否必須？？？
		createBucketRequest.setStorageClass(StorageClass.IA);
		OSSClient ossClient = getOSSClient(oss);
		boolean exists = ossClient.doesBucketExist(oss.getBucketName());
		if (!exists) {
			try {
				ossClient.createBucket(createBucketRequest);
				// 关闭client
				ossClient.shutdown();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	public static OSSClient getOSSClient(CmsOss oss) {
		String endpoint = oss.getEndPoint();
		// 云账号AccessKey有所有API访问权限
		String accessKeyId = oss.getAppKey();
		String accessKeySecret = oss.getSecretId();
		// 创建ClientConfiguration实例，按照您的需要修改默认参数
		ClientConfiguration conf = new ClientConfiguration();
		// 开启支持CNAME选项
		conf.setSupportCname(true);
		// 创建OSSClient实例
		OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
		return client;
	}
	
	public static void main(String[] args) {
		File file = new File("D:\\test\\1.jpg");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CmsOss oss = new CmsOss("LTAIIgZrqWiA4A7R", "GX8PEMMVIEWTmlzsDdPFyrR9SAELMk",
				"LTAIIgZrqWiA4A7R1", "testaddbucket", "bucketArea3", 
				"oss-cn-beijing.aliyuncs.com","oss-cn-beijing.aliyuncs.com",(byte) 2);
		// uploadFileByInputStream(oss, "/u/cms/www/3.jpg", fileInput);
	}

}
