package com.coulee.aicw.collectanalyze.collect.executer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;

/**
 * Description:自定义扩展脚本执行类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ExtendExecuter extends AbstractExecuter {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public ExecuteResult execute(CommandArg commandArg)
			throws ExecuterException {
		ICustomScript script = null;
		try {
			logger.debug("execute extend script, extend classname {}", commandArg.getCmd());
			script = (ICustomScript) Class.forName(commandArg.getCmd()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EXTEND_INSTANCE_ERROR : {}", e.getMessage());
			this.throwExecuterException(ErrorCode.EXTEND_INSTANCE_ERROR);
		}
		if (this.protocol != null) {
			try {
				return script.execute(protocol, this.prompt, this.promptLine, commandArg);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExecuterException(e);
			}
		}
		if (this.jdbcProtocol != null) {
			try {
				return script.execute(jdbcProtocol, commandArg);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExecuterException(e);
			}
		}
		return null;
	}

}
