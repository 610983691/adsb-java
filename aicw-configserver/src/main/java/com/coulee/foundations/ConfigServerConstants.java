package com.coulee.foundations;

/**
 * Description: 配置中心静态常量类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ConfigServerConstants {
	
	/**
	 * ZK存储配置信息根路径
	 */
	public static String ZK_CONFIG_ROOT = "";
	
	/**
	 * sqlite数据库文件名
	 */
	public static final String SQLITE_FILE_NAME = "configserver.db";
	
	/**
	 * 加解密类型配置项
	 */
	public static final String CRYPTO_TYPE_TYPE = "crypto.type";
	
	/**
	 * 加解密密钥配置项
	 */
	public static final String CRYPTO_TYPE_KEY = "crypto.key";
	
	/**
	 * 数据库临时文件
	 */
	public static final String TEMP_DB_FILE = "TEMP_DB_FILE";
	
	/**
	 * 新版数据
	 */
	public static final String DATA_VERSION_NEW = "new";
	
	/**
	 * 旧版本数据
	 */
	public static final String DATA_VERSION_OLD = "old";
}
