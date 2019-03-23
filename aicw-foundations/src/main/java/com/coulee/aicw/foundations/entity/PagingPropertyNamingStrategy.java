package com.coulee.aicw.foundations.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.coulee.aicw.foundations.Constants;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;

/**
 * Description: Json序列化和反序列化时属性命名策略<br>
 * 主要根据配置对分页属性进行动态命名以适应不同的前端框架<br>
 * Create Date: 2018年3月14日<br>
 * Modified By：<br>
 * Modified Date：<br>
 * Why & What is modified：<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * 
 * @author LanChao
 * @version 1.0
 */
public class PagingPropertyNamingStrategy extends PropertyNamingStrategyBase {

	private static final long serialVersionUID = -7385915433126346191L;
	
	@Autowired
	private Environment env;

	/**
	 * BaseEntity._offset
	 * 起始条数
	 */
	private final String PAGE_OFFSET_ORI = "_offset";
	
	/**
	 * BaseEntity._page
	 * 起始条数
	 */
	private final String PAGE_CURRENT_PAGE = "_page";
	
	/**
	 * BaseEntity._limit
	 * 页大小
	 */
	private final String PAGE_LIMIT_ORI = "_limit";
	
	/**
	 * PageEntity._total
	 * 总条数
	 */
	private final String PAGE_TOTAL_ORI = "_total";
	
	/**
	 * PageEntity._rows
	 * 本页数据
	 */
	private final String PAGE_ROWS_ORI = "_rows";
	
	/**
	 * 通过读取分页参数属性配置来变更json属性名
	 */
	@Override
	public String translate(String oriName) {
		switch (oriName) {
		case PAGE_OFFSET_ORI:
			return this.getValue("common.page.offset", Constants.DEFAULT_PAGE_START);
		case PAGE_LIMIT_ORI:
			return this.getValue("common.page.limit", Constants.DEFAULT_PAGE_LIMIT);
		case PAGE_TOTAL_ORI:
			return this.getValue("common.page.total", Constants.DEFAULT_READER_TOTALPROPERTY);
		case PAGE_ROWS_ORI:
			return this.getValue("common.page.rows", Constants.DEFAULT_READER_ROOT);
		case PAGE_CURRENT_PAGE:
			return this.getValue("common.page.page", Constants.DEFAULT_CURRENT_ROOT);
		default:
			return oriName;
		}
	}

	/**
	 * Description: 根据属性名获取配置值<br> 
	 * Created date: 2018年3月14日
	 * @param property
	 * @param defaultValue
	 * @return
	 * @author LanChao
	 */
	private String getValue(String property, String defaultValue) {
		if (env != null) {
			return env.getProperty(property, defaultValue);
		}
		return defaultValue;
	}
}
