package com.coulee.aicw.foundations.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.coulee.aicw.foundations.Constants;

/**
 * Description: 消息实体，用于封装操作结果信息返回给调用者<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 4927961398212069904L;

	/**
	 * 操作结果 Constants.SUCCESS 1：成功 Constants.FAILURE 0：失败
	 */
	private int code;

	/**
	 * 成功/失败消息 该提示消息需支持国际化
	 */
	private String msg;

	/**
	 * 操作对象实体，增加操作时需返回<br>
	 * 该对象内包含了增加成功后产生的主键ID
	 */
	private Object obj;

	/**
	 * 成功或失败 布尔型
	 */
	private boolean success;

	/**
	 * 操作对象
	 */
	private List<OperObject> operateObj;
	
	/**
	 * token证书
	 */
	private String token;

	/**
	 * Description : 构造消息对象
	 * 
	 * @param code
	 *            操作结果标识
	 * @param msg
	 *            操作结果信息
	 * @param obj
	 *            操作对象
	 */
	public Message(int code, String msg, Object obj) {
		this.code = code;
		this.msg = msg;
		this.obj = obj;
		if (code == Constants.SUCCESS) {
			this.success = true;
		} else {
			this.success = false;
		}
	}

	/**
	 * Description : 构造消息对象
	 * 
	 * @param code
	 *            操作结果标识
	 * @param msg
	 *            操作结果信息
	 */
	public Message(int code, String msg) {
		this.code = code;
		this.msg = msg;
		if (code == Constants.SUCCESS) {
			this.success = true;
		} else {
			this.success = false;
		}
	}

	/**
	 * Definition:构造消息对象
	 * 
	 * @param code
	 *            操作结果标识
	 * @param msg
	 *            操作结果信息
	 * @param operateObj
	 *            操作日志信息
	 * @return
	 * @throws ServiceException
	 * @author yan chong Create Date: 2015年7月10日 上午10:33:26
	 */
	public Message(int code, String msg, List<OperObject> operateObj) {
		this.code = code;
		this.msg = msg;
		if (code == Constants.SUCCESS) {
			this.success = true;
		} else {
			this.success = false;
		}
		this.operateObj = operateObj;
	}

	/**
	 * Definition:构造消息对象
	 * 
	 * @param code
	 *            操作结果标识
	 * @param msg
	 *            操作结果信息
	 * @param obj
	 *            操作对象
	 * @param operateObj
	 *            操作日志信息
	 * @return
	 * @throws ServiceException
	 * @author yan chong Create Date: 2015年7月10日 上午10:33:26
	 */
	public Message(int code, String msg, Object obj, List<OperObject> operateObj) {
		this.code = code;
		this.msg = msg;
		this.obj = obj;
		if (code == Constants.SUCCESS) {
			this.success = true;
		} else {
			this.success = false;
		}
		this.operateObj = operateObj;
	}

	public Message() {
	}

	/**
	 * Definition:增加操作对象信息
	 * 
	 * @param operId
	 *            操作对象ID
	 * @param operName
	 *            操作对象名称
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public void addOperObject(String operId, String operName) {
		if (operateObj == null) {
			operateObj = new ArrayList<OperObject>();
		}
		operateObj.add(new OperObject(operId, operName));
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<OperObject> getOperateObj() {
		return operateObj;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static Message newSuccessMessage(String mesInfo, Object obj) {
		Message mesVo = new Message();
		mesVo.setCode(Constants.SUCCESS);
		mesVo.setMsg(mesInfo);
		mesVo.setSuccess(true);
		mesVo.setObj(obj);
		return mesVo;
	}
	
	public static Message newSuccessMessage(String mesInfo) {
		Message mesVo = new Message();
		mesVo.setCode(Constants.SUCCESS);
		mesVo.setMsg(mesInfo);
		mesVo.setSuccess(true);
		mesVo.setObj(null);
		return mesVo;
	}

	public static Message newFailureMessage(String mesInfo, Object obj) {
		Message mesVo = new Message();
		mesVo.setCode(Constants.FAILURE);
		mesVo.setMsg(mesInfo);
		mesVo.setSuccess(false);
		mesVo.setObj(obj);
		return mesVo;
	}
	
	public static Message newFailureMessage(String mesInfo) {
		Message mesVo = new Message();
		mesVo.setCode(Constants.FAILURE);
		mesVo.setMsg(mesInfo);
		mesVo.setSuccess(false);
		mesVo.setObj(null);
		return mesVo;
	}

}
