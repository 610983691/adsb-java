package com.coulee.aicw.collectanalyze.collect.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;

/**
 * Description:常用的工具类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-6
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class CommonUtils {

	/**
	 * Definition:读取文本文件
	 * @param in
	 * @param charsetName
	 * @return
	 * @throws Exception
	 * @Author: LanChao
	 * @Created date: 2016-12-6
	 */
	public static String readTxtFileContent(InputStream in, String charsetName) throws Exception {
		try {
			charsetName = StringUtils.isEmpty(charsetName) ? "UTF-8" : charsetName;
			Reader reader = new BufferedReader(new InputStreamReader(in, charsetName));
			char[] readBuffer = new char[2048];
			StringBuffer buffer = new StringBuffer();
			int n;
			while ((n = reader.read(readBuffer)) > 0) {
				buffer.append(readBuffer, 0, n);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Definition:Copy文件夹
	 * @param srcPath 源文件夹
	 * @param destPath 目的文件夹
	 * @throws Exception
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public static void copyFolder(String srcPath, String destPath) throws Exception {
		File destFiel = new File(destPath);
		if (!destFiel.exists()) {// 如果新文件夹不存在 则建立新文件夹
			destFiel.mkdirs();
		}
		String[] file = new File(srcPath).list(); // 旧路径下所有文件名 包括文件夹名
		File temp = null;
		if (file == null || file.length == 0) {
			return;
		}
		for (int i = 0; i < file.length; i++) {
			if (srcPath.endsWith(File.separator)) {
				temp = new File(srcPath + file[i]);
			} else {
				temp = new File(srcPath + File.separator + file[i]);
			}
			try {
				if (temp.isFile()) { // 如果是文件则拷贝到新路径下
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(destPath + File.separator + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
			} catch (IOException e) {
				throw new Exception(e.getMessage());
			}
			if (temp.isDirectory() && !temp.getAbsolutePath().equals(destPath)) {// 如果是文件夹 则递归调用该方法继续拷贝文件 直至全部拷贝完毕
				copyFolder(srcPath + File.separator + file[i], destPath + File.separator + file[i]);
			}
		}
	}
	
	
	/**
	 * Definition:删除文件或文件夹以下所有的文件
	 * @param file 文件类
	 * @Author: LanChao
	 * @Created date: 2014年12月2日
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null && files.length > 0) {
					for (File f : files) {
						deleteFile(f);
					}
				}
				file.delete();
			}
		}
	}
	
	/**
	 * Definition:删除文件或文件夹以下所有的文件
	 * @param filePath 文件路径
	 * @Author: LanChao
	 * @Created date: 2014年12月2日
	 */
	public static void deleteFile(String filePath) {
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath.concat(File.separator);
		}
		deleteFile(new File(filePath));
	}
	
	/**
	 * Definition:向文件内写内容
	 * @param filePath 文件路径
	 * @param fileContent 文件内容
	 * @param encode 编码
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public static void writeFile(String filePath, String fileContent, String encode) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), encode));
			writer.write(fileContent);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
