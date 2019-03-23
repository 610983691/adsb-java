package com.coulee.foundations.configserver.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.coulee.aicw.foundations.utils.common.FilePathTools;
import com.coulee.foundations.ConfigServerConstants;

/**
 * Description: 配置中心常用工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigServerUtils {

	/**
	 * Description: 获取sqlite数据库文件路径<br> 
	 * Created date: 2017年12月13日
	 * @return
	 * @author oblivion
	 */
	public static String getDbFilePath() {
		String dbFilePath = null;
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("data");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File dbFile = new File(dbFolder, ConfigServerConstants.SQLITE_FILE_NAME);
			if (!dbFile.exists()) {
				ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				try {
					InputStream in = resolver.getResource(ConfigServerConstants.SQLITE_FILE_NAME).getInputStream();
					FileUtils.copyInputStreamToFile(in, dbFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dbFilePath = dbFile.getAbsolutePath();
		} else {
			String resourcesPath = "src".concat(File.separator).concat("main").concat(File.separator).concat("resources")
					.concat(File.separator).concat(ConfigServerConstants.SQLITE_FILE_NAME);
			File dbFile = new File(resourcesPath);
			dbFilePath = dbFile.getAbsolutePath();
		}
		return dbFilePath;
	}
	
	/**
	 * Description: 获取临时数据库文件路径<br> 
	 * Created date: 2018年1月10日
	 * @param tempDbFileName
	 * @return
	 * @author oblivion
	 */
	public static File getTempDbFilePath(String tempDbFileName) {
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("data");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File tempDbFolder = new File(dbFolder, "temp");
			if (!tempDbFolder.exists()) {
				tempDbFolder.mkdir();
			}
			return new File(tempDbFolder, tempDbFileName);
		} else {
			String resourcesPath = FilePathTools.getResourcePathForJar();
			return new File(new File(resourcesPath).getParentFile(), tempDbFileName);
		}
	}
	
}
