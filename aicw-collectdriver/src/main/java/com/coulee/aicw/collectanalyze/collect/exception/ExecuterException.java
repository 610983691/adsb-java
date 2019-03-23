package com.coulee.aicw.collectanalyze.collect.exception;

/**
 * Description:执行层异常
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ExecuterException extends Exception {

	private static final long serialVersionUID = 6270865380092572535L;

	public ExecuterException() {
	}

	public ExecuterException(String message) {
		super(message);
	}

	public ExecuterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecuterException(Throwable cause) {
		super(cause);
	}
}
