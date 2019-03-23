package com.coulee.aicw.foundations.config.exception;

/**
 * Description: 自定义运行时异常<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class CustomRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3774875303113404628L;
	
	/**
	 * 错误码
	 */
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CustomRuntimeException() {
		super();
	}

	public CustomRuntimeException(String errorMsg) {
		super(errorMsg);
	}
	
	public CustomRuntimeException(int code, String errorMsg) {
		super(errorMsg);
		this.code = code;
	}
	
	public CustomRuntimeException(Throwable cause) {
		super(cause);
	}
}
