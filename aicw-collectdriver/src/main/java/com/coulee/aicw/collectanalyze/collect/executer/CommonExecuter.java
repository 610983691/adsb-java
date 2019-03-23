package com.coulee.aicw.collectanalyze.collect.executer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.foundations.utils.common.BaseTools;

/**
 * Description:普通命令执行类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class CommonExecuter extends AbstractExecuter {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public ExecuteResult execute(CommandArg commandArg)
			throws ExecuterException {
		String cmd = commandArg.getCmd();
		if (BaseTools.isNull(cmd)) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		String[] cmds = cmd.split("\n");
		if (cmds == null || cmds.length == 0) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		StringBuffer resultSb = new StringBuffer();
		try {
			for (String c : cmds) {
				c = c.replaceAll("\\\r", "");
				this.protocol.sendCommand(c, Protocol.CR);
				if(StringUtils.isNoneBlank(c)&&!c.contains("#")){
					resultSb.append(c);
				}
				String echo = this.readUnSqlCmdEcho(this.protocol, c, new String[] { this.prompt, this.promptLine },
						commandArg.getUnixSudoInfo());
//				resultSb.append(this.protocol.read2KeyWordEcho(c, this.prompt, this.promptLine));
				resultSb.append(echo);
			}
			ExecuteResult result = new ExecuteResult();
			result.setResult(this.replaceBlankLine(resultSb.toString()));
			return result;
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("execute common cmd error, cmd is [{}], error is {}", cmd, e.getMessage());
			throw new ExecuterException(e.getMessage());
		}
	}

}
