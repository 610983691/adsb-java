package com.coulee.aicw.collectanalyze.collect.driver;

/**
 * Description:Driver工厂类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-15
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class DriverFactory {

	/**
	 * Definition:创建Driver驱动类
	 * @param driverClass
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public static Driver getDriver(String driverClass) {
		if (driverClass == null || "".equals(driverClass)) {
			return null;
		}
		try {
			Class<?> c = Class.forName(driverClass);
			return (Driver) c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Definition:创建数据库驱动类
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-15
	 */
	public static Driver getDBDriver() {
		return new DatabaseDriver();
	}
}
