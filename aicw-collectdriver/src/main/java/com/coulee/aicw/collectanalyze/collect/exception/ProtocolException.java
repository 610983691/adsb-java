package com.coulee.aicw.collectanalyze.collect.exception;

/**
 * Description:协议层异常定义
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-2
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ProtocolException extends Exception {

	private static final long serialVersionUID = -4041224674962618184L;

	public ProtocolException() {
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}
}
