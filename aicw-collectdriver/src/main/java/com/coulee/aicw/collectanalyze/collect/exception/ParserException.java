package com.coulee.aicw.collectanalyze.collect.exception;

/**
 * Description:解析层异常
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-13
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ParserException extends Exception {

	private static final long serialVersionUID = 6270865380092572535L;

	public ParserException() {
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}
}
