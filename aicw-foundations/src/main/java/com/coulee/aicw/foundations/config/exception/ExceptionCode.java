package com.coulee.aicw.foundations.config.exception;

/**
 * Description: 异常编码枚举类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public enum ExceptionCode {

	/**
	 * http未授权异常
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	
	/**
	 * http服务器内部错误
	 */
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	
	/**
	 * 数据库操作异常
	 */
	DATABASE_ERROR(1001, "Database Operate Error"),

	/**
	 * 操作异常
	 */
	OPERATE_ERROR(1002, "Operation Failure");

	private final int code;

	private final String errorMsg;

	private ExceptionCode(int code, String errorMsg) {
		this.code = code;
		this.errorMsg = errorMsg;
	}

	/**
	 * Description: 获取异常码<br> 
	 * Created date: 2017年12月8日
	 * @return
	 * @author oblivion
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Description: 获取异常信息<br> 
	 * Created date: 2017年12月8日
	 * @return
	 * @author oblivion
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
	
}
