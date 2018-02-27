package com.jeecms.common.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.jeecms.common.util.StrUtils;
import com.jeecms.core.entity.CmsOss;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.DelFolderRequest;
import com.qcloud.cos.request.ListFolderRequest;
import com.qcloud.cos.request.MoveFileRequest;
import com.qcloud.cos.request.StatFolderRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

/**
 * 腾讯云 cos Client Created by TOM
 */
public class QCoscloudClient {

	/**
	 * 自动判断文件的大小,已使用是否是分片的方式上传文件
	 * 
	 * @param remoteFolderPath
	 *            远程文件夹的名称
	 * @param loaclPath
	 *            文件的本地路径
	 * @return
	 */
	public static String uploadFileByLocal(CmsOss oss, String cosPath, String localPath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(oss.getBucketName(), cosPath, localPath);
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}

	}

	public static String uploadFileByByte(CmsOss oss, String cosPath, byte[] content) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(oss.getBucketName(), cosPath, content);
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}
	}

	public static String uploadFileByInputStream(CmsOss oss, String cosPath, InputStream inputStream) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			UploadFileRequest uploadFileRequest = new UploadFileRequest(oss.getBucketName(), cosPath,
					toByteArray(inputStream));
			String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
			return uploadFileRet;
		} else {
			return "";
		}
	}

	public static String moveFile(CmsOss oss, String cosFilePath, String dstCosFilePath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			MoveFileRequest moveRequest = new MoveFileRequest(oss.getBucketName(), cosFilePath, dstCosFilePath);
			String moveFileResult = cosClient.moveFile(moveRequest);
			return moveFileResult;
		}else{
			return "";
		}
	}

	public static String deleteFile(CmsOss oss, String cosFilePath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			DelFileRequest delFileRequest = new DelFileRequest(oss.getBucketName(), cosFilePath);
			String delFileRet = cosClient.delFile(delFileRequest);
			return delFileRet;
		}else{
			return "";
		}
	}

	public static String listFolder(CmsOss oss, String cosPath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			ListFolderRequest listFolderRequest = new ListFolderRequest(oss.getBucketName(), cosPath);
			String listFolderRet = cosClient.listFolder(listFolderRequest);
			return listFolderRet;
		}else{
			return "";
		}
	}

	public static String delFolder(CmsOss oss, String cosPath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			DelFolderRequest delFolderRequest = new DelFolderRequest(oss.getBucketName(), cosPath);
			String delFolderRet = cosClient.delFolder(delFolderRequest);
			return delFolderRet;
		}else{
			return "";
		}
	}

	public static String statFolder(CmsOss oss, String cosPath) {
		COSClient cosClient = getCOSClient(oss);
		if (cosClient != null && oss != null) {
			StatFolderRequest statFolderRequest = new StatFolderRequest(oss.getBucketName(), cosPath);
			String statFolderRet = cosClient.statFolder(statFolderRequest);
			return statFolderRet;
		}else{
			return "";
		}
	}

	public static COSClient getCOSClient(CmsOss oss) {
		if (StringUtils.isNotBlank(oss.getOssAppId()) && StringUtils.isNumeric(oss.getOssAppId())) {
			// 初始化秘钥信息
			Credentials cred = new Credentials(Integer.parseInt(oss.getOssAppId()), oss.getSecretId(), oss.getAppKey());
			// 初始化客户端配置
			ClientConfig clientConfig = new ClientConfig();
			// 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
			clientConfig.setRegion(oss.getBucketArea());
			// 初始化cosClient
			COSClient cosClient = new COSClient(clientConfig, cred);
			return cosClient;
		} else {
			return null;
		}
	}

	public static byte[] toByteArray(InputStream input) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try {
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output.toByteArray();
	}

	public static void main(String[] args) {
		// System.out.println("upload-->"+uploadFileByLocal("/u/cms/www/1.jpg",
		// "D:\\test\\1.jpg"));;
		// System.out.println(moveFile("/u/cms/www/1.jpg", "/u/cms/www/2.jpg"));
		// deleteFile("/upload/1.jpg");
		// System.out.println(listFolder("/u/"));
		// System.out.println(delFolder("/upload/"));
		try {
			File file = new File("D:\\test\\1.jpg");
			FileInputStream fileInput = new FileInputStream(file);
			byte[] content = toByteArray(fileInput);
			System.out.println(file.length());
			System.out.println("content->" + content.length);
			CmsOss oss = new CmsOss("app_id", "SecretId", "SecretKey", "jeecms", "cd", "", "", (byte) 1);
			System.out.println(uploadFileByByte(oss, "/u/cms/www/3.jpg", content));
			;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}