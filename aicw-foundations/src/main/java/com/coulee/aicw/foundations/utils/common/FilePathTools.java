package com.coulee.aicw.foundations.utils.common;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/**
 * Description: 获得类文件绝对路径<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class FilePathTools {

	/**
	 * Definition:获得WEB工程的WEB-ROOT绝对路径
	 * 
	 * @param clazz本方法调用者类
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016年2月1日
	 */
	public static String getResourcePathForWeb(Class<?> clazz) {
		String result = clazz.getResource(clazz.getSimpleName().concat(".class")).toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		if (index != -1) {
			result = result.substring(0, index);
		}
		if (result.startsWith("jar")) {
			int k = result.indexOf("jar");
			result = result.substring(k + 10);// 当class文件在jar文件中时，返回”jar:file:/F:/…”样的路径
		} else if (result.startsWith("file")) {
			int k = result.indexOf("file");
			result = result.substring(k + 6);// 当class文件在file文件中时，返回”file:/F:/…”样的路径
		} else if (result.startsWith("zip:")) {
			int k = result.indexOf("zip");
			result = result.substring(k + 5);// 当class文件在jar文件中时，返回”/zip:/F:/…”样的路径
		}
		result = result.replace("%20", " ");
		String osname = System.getProperty("os.name");
		if (osname.startsWith("Windows")) {
			return result;
		} else {
			return "/" + result;
		}
	}

	/**
	 * Definition:获得JAVA工程JAR文件所在绝对路径<br>
	 * 
	 * @param clazz
	 *            调用者所在的类
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年5月15日
	 */
	public static String getResourcePathForJar() {
		String filePath = null;
		try {
			File path = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!path.exists()) {
				path = new File("");
			}
			filePath = path.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/**
	 * Definition:判断是否为部署模式
	 * 
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年11月2日
	 */
	public static boolean isDeployModel() {
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		if (path.indexOf(".jar") > 0) {
			return true;
		}
		return false;
	}
}
