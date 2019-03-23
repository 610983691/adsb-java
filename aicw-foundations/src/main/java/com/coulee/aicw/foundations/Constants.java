package com.coulee.aicw.foundations;

/**
 * Description: 全局静态常量类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class Constants {
	
	/**
	 * 默认分页参数：起始数据条数
	 */
	public static final String DEFAULT_PAGE_START = "offset";

	/**
	 * 默认分页参数：页条数大小
	 */
	public static final String DEFAULT_PAGE_LIMIT = "limit";
	
	/**
	 * 默认返回前端分页数据集合属性标识
	 */
	public static final String DEFAULT_READER_ROOT = "rows";
	
	/**
	 * 默认返回前端分页数据集合属性标识
	 */
	public static final String DEFAULT_CURRENT_ROOT = "page";

	/**
	 * 默认返回前端分页总条数属性标识
	 */
	public static final String DEFAULT_READER_TOTALPROPERTY = "total";
	
	/**
	 * ExtJs JsonReader root 属性名
	 */
	public static final String EXT_READER_ROOT = "result";
	
	/**
	 * ExtJs JsonReader totalProperty 属性名
	 */
	public static final String EXT_READER_TOTALPROPERTY = "pageCount";
	
	/**
	 * EasyUI JsonReader root 属性名
	 */
	public static final String EASYUI_READER_ROOT = "rows";
	
	/**
	 * EasyUI JsonReader totalProperty 属性名
	 */
	public static final String EASYUI_READER_TOTALPROPERTY = "total";

	/**
	 * UTF-8编码
	 */
	public static final String CHARSET_UTF8 = "UTF-8";
	
	/**
	 * GBK编码
	 */
	public static final String CHARSET_GBK = "GBK";
	
	/**
	 * ISO-8859-1编码
	 */
	public static final String CHARSET_ISO8858_1 = "ISO-8859-1";
	
	/**
	 * GB2312编码
	 */
	public static final String CHARSET_GB2312 = "GB2312";
	
	/**
	 * 加密
	 */
	public static final boolean ENCRYPT = true;
	
	/**
	 * 解密
	 */
	public static final boolean DECRYPT = false;
	
	/**
	 * 读操作
	 */
	public static final int READ = 0;
	
	/**
	 * 写操作
	 */
	public static final int WRITE = 1;
	
	/**
	 * 操作成功标识
	 */
	public static final int SUCCESS = 1;
	
	/**
	 * 操作失败标识
	 */
	public static final int FAILURE = 0;

	/**
	 * 导出文件类型-PDF
	 */
	public static final String EXPORT_TYPE_PDF = "pdf";

	/**
	 * 导出文件类型-HTML
	 */
	public static final String EXPORT_TYPE_HTML = "html";

	/**
	 * 导出文件类型-EXCEL
	 */
	public static final String EXPORT_TYPE_EXCEL = "excel";

	/**
	 * 导出文件类型-WORD
	 */
	public static final String EXPORT_TYPE_WORD = "word";

	/**
	 * 导出文件类型-PPT
	 */
	public static final String EXPORT_TYPE_PPT = "ppt";
	
	public static final String FW_LOG_STATUS_WAIT_APPROVA="等待管理员审批";
	public static final String FW_LOG_STATUS_WATING="策略待下发";
	public static final String FW_LOG_STATUS_SUC ="成功";
	public static final String FW_LOG_STATUS_FAIL="失败";
	public static final String FW_LOG_STATUS_WAIT_FAIL="管理员未同意";
	public static final String FW_ACL_TYPE_ADD="add";
	public static final String FW_ACL_TYPE_REMOVE="remove";
	
}

	