package com.jeecms.common.office;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ConverEncoding {

	static String CODE = "UTF-8";
	static String FILE_SUFFIX = ".txt";// 文件扩展名
	// static String FILE_SUFFIX = ".css";
	// static String FILE_SUFFIX = ".js";
	// static String FILE_SUFFIX = ".htm";
	static String srcDir = "C:\\Users\\Administrator\\Desktop";// 文件所在目录

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		List<String> files = new ArrayList<String>();
		fetchFileList(srcDir, files, FILE_SUFFIX);
		String filecode = "";
		for (String fileName : files) {
			filecode = codeString(fileName);
			if (!filecode.equals(CODE)) {
				convert(fileName, filecode, fileName, CODE);
			}
		}
	}

	public static void convert(String oldFile, String oldCharset, String newFlie, String newCharset) {
		BufferedReader bin;
		FileOutputStream fos;
		StringBuffer content = new StringBuffer();
		try {
			bin = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile), oldCharset));
			String line = null;
			while ((line = bin.readLine()) != null) {
				content.append(line);
				content.append(System.getProperty("line.separator"));
			}
			bin.close();
			File dir = new File(newFlie.substring(0, newFlie.lastIndexOf("\\")));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			fos = new FileOutputStream(newFlie);
			Writer out = new OutputStreamWriter(fos, newCharset);
			out.write(content.toString());
			out.close();
			fos.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fetchFileList(String strPath, List<String> filelist, final String regex) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		Pattern p = Pattern.compile(regex);
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				fetchFileList(files[i].getAbsolutePath(), filelist, regex);
			} else {
				String strFileName = files[i].getAbsolutePath().toLowerCase();
				Matcher m = p.matcher(strFileName);
				if (m.find()) {
					filelist.add(strFileName);
				}
			}
		}
	}

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));

		String code = null;
		byte[] b = new byte[3];
		bin.read(b);
		int p = (bin.read() << 8) + bin.read();
		bin.close();

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		return code;
	}

	public static String getFilecharset(String fileName) {
		File sourceFile=new File(fileName);
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}
}