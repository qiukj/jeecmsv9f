package com.jeecms.common.util;

import java.util.Properties;

public class SystemUtil {
	public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }
	
	public static void main(String[] args) {
		System.out.println(isOSLinux());
	}
}
