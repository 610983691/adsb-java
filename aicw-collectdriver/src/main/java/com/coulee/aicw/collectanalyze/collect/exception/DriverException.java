package com.coulee.aicw.collectanalyze.collect.exception;

/**
 * Description:驱动层异常
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class DriverException extends Exception {

	private static final long serialVersionUID = 6270865380092572535L;

	public DriverException() {
	}

	public DriverException(String message) {
		super(message);
	}

	public DriverException(String message, Throwable cause) {
		super(message, cause);
	}

	public DriverException(Throwable cause) {
		super(cause);
	}
}
