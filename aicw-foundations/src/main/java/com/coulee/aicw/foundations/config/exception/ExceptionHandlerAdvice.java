package com.coulee.aicw.foundations.config.exception;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coulee.aicw.foundations.entity.Message;

/**
 * Description: 在controller层面统一处理后端异常，防止抛出到页面或接口服务调用者<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

	/**
	 * Description: 处理Exception异常<br>
	 * Created date: 2017年12月8日
	 * 
	 * @param e
	 * @return
	 * @author oblivion
	 */
	@ExceptionHandler(Exception.class)
	public Message handleException(Exception e) {
		e.printStackTrace();
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode());
		msg.setMsg(ExceptionCode.INTERNAL_SERVER_ERROR.getErrorMsg());
		return msg;
	}

	/**
	 * Description: 处理DataAccessException异常<br>
	 * Created date: 2017年12月8日
	 * 
	 * @param e
	 * @return
	 * @author oblivion
	 */
	@ExceptionHandler(DataAccessException.class)
	public Message handleDataAccessException(DataAccessException e) {
		e.printStackTrace();
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(ExceptionCode.DATABASE_ERROR.getCode());
		msg.setMsg(ExceptionCode.DATABASE_ERROR.getErrorMsg());
		return msg;
	}
	
	/**
	 * Description: 处理CustomRuntimeException异常<br> 
	 * Created date: 2018年9月12日
	 * @param e
	 * @return
	 * @author oblivion
	 */
	@ExceptionHandler(CustomRuntimeException.class)
	public Message handleCustomRuntimeException(CustomRuntimeException e) {
		e.printStackTrace();
		Message msg = new Message();
		msg.setSuccess(false);
		int code = e.getCode() != 0 ? e.getCode() : ExceptionCode.OPERATE_ERROR.getCode();
		msg.setCode(code);
		String errorMsg = e.getMessage() != null ? e.getMessage() : ExceptionCode.OPERATE_ERROR.getErrorMsg();
		msg.setMsg(errorMsg);
		return msg;
	}

	/**
	 * Description: 处理MethodArgumentNotValidException异常<br>
	 * Created date: 2018年2月27日
	 * 
	 * @param e
	 * @return
	 * @author oblivion
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Message handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		StringBuffer errorMsg = new StringBuffer();
		for (int i = 0; i < errors.size(); i++) {
			if (i > 0) {
				errorMsg.append(";");
			}
			errorMsg.append(errors.get(i).getDefaultMessage());
		}
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode());
		msg.setMsg(errorMsg.toString());
		return msg;
	}

	/**
	 * Description: 处理ConstraintViolationException异常<br>
	 * Created date: 2018年2月27日
	 * 
	 * @param e
	 * @return
	 * @author oblivion
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public Message handleConstraintViolationException(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		int i = 0;
		StringBuffer errorMsg = new StringBuffer();
		for (ConstraintViolation<?> item : violations) {
			if (i > 0) {
				errorMsg.append(";");
			}
			errorMsg.append(item.getMessage());
			i++;
		}
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(ExceptionCode.INTERNAL_SERVER_ERROR.getCode());
		msg.setMsg(errorMsg.toString());
		return msg;
	}
	
	
	/***
	 * HttpMediaTypeNotSupportedException异常拦截
	 * @param e
	 * @return
	 * @author tongjie
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public Message handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
		msg.setMsg("不支持的contentyType:"+e.getContentType().getType()+"/"+e.getContentType().getSubtype());
		return msg;
	}
	
	
	/***
	 * HttpMessageNotReadableException异常拦截
	 * @param e
	 * @return
	 * @author tongjie
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Message handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		Message msg = new Message();
		msg.setSuccess(false);
		msg.setCode(HttpStatus.BAD_REQUEST.value());
		msg.setMsg("请求参数格式错误.");
		return msg;
	}
	
}
