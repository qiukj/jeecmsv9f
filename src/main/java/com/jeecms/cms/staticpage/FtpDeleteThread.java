package com.jeecms.cms.staticpage;

import com.jeecms.core.entity.Ftp;

public class FtpDeleteThread implements Runnable {
	private String fileName;
	private Ftp ftp;
	public FtpDeleteThread(Ftp ftp,String fileName) {
		// TODO Auto-generated constructor stub
		this.ftp = ftp;
		this.fileName = fileName;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(ftp!=null){
			boolean flag;
			try {
				flag=ftp.deleteFile(fileName);
				System.out.println("flag - >"+flag);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

}
