package com.coulee.aicw.collectanalyze.collect.executer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.foundations.utils.common.StringTools;

/**
 * Description:shell脚本执行类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ShellExecuter extends AbstractExecuter {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private final String SH = "/bin/sh";
	
	private final String BASH = "/bin/bash";
	
	private final String KSH = "/bin/ksh";
	
	private final String CSH = "/bin/csh";
	
	private final String REPLACER = "SCRIPT_REPLACER";
	
	/**
	 * 命令参数长度，主要为了解决echo命令写shell脚本文件时内容超长问题<br>
	 * 不同操作系统不相同，solais默认为1024，linux默认较大，可用getconf ARG_MAX命令查看
	 */
	private final int ECHO_ARG_LENGTH = 1024;

	@Override
	public ExecuteResult execute(CommandArg commandArg)
			throws ExecuterException {
		String cmd = commandArg.getCmd();
		if (StringTools.isEmpty(cmd)) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		boolean isSudoMode = commandArg.getUnixSudoInfo() == null ? false : commandArg.getUnixSudoInfo().isSudoMode();
		String shellFileName = UUID.randomUUID().toString().replaceAll("-", "");
		cmd = cmd.replaceAll("\r\n", "\r").replace("'", REPLACER); //将回车换行替换为回车，将单引号替换为SCRIPT_REPLACER
		String[] writeCmds = this.makeWriteCmd(cmd, shellFileName); //将脚本内容分批写入脚本文件内
		String replaceCmd = "sed -i \"s/" + REPLACER + "/'/g\" " + shellFileName; //将文件内的SCRIPT_REPLACER还原为单引号
		String chmodCmd = "chmod 755 " + shellFileName; //修改文件为可执行权限
		String executeCmd = (isSudoMode ? ("sudo " + this.getShellType(cmd) + " ") : "./") + shellFileName; //执行shell脚本文件
		String removeCmd = "rm -rf " + shellFileName; //将脚本文件删除
		try {
			for (String writeCmd : writeCmds) {
				this.protocol.sendCommand(writeCmd, Protocol.CR);
				this.protocol.read2KeyWordEcho(writeCmd, this.promptLine);
			}
			this.protocol.sendCommand(replaceCmd, Protocol.CR);
			this.protocol.read2KeyWordEcho(replaceCmd, this.promptLine);
			this.protocol.sendCommand(chmodCmd, Protocol.CR);
			this.protocol.read2KeyWordEcho(chmodCmd, this.promptLine);
			this.protocol.sendCommand(executeCmd, Protocol.CR);
//			String resultStr = this.protocol.read2KeyWordEcho(executeCmd, this.promptLine);
			String resultStr = this.readUnSqlCmdEcho(protocol, executeCmd, new String[] { this.promptLine }, commandArg.getUnixSudoInfo());
			this.protocol.sendCommand(removeCmd, Protocol.CR);
			this.protocol.read2KeyWordEcho(removeCmd, this.promptLine);
			ExecuteResult result = new ExecuteResult();
			result.setResult(this.replaceBlankLine(resultStr.replace(executeCmd, "")));
			return result;
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.error("execute common cmd error, cmd is [{}], error is {}", commandArg.getCmd(), e.getMessage());
			throw new ExecuterException(e.getMessage());
		}
	}
	
	/**
	 * Definition:构造写脚本文件的命令，以ECHO_ARG_LENGTH长度分割shell脚本内容
	 * @param cmd 脚本内容
	 * @param shellFileName 脚本文件名
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017年10月19日
	 */
	private String[] makeWriteCmd(String cmd, String shellFileName) {
		int length = cmd.length();
		if (length <= ECHO_ARG_LENGTH) {
			return new String[] { "echo '" + cmd + "' > " + shellFileName };
		}
		int splitCount = (length / ECHO_ARG_LENGTH) + (length % ECHO_ARG_LENGTH > 0 ? 1 : 0);
		String[] cmds = new String[splitCount];
		for (int i = 0; i < splitCount; i++) {
			String tmp = cmd.substring(i * ECHO_ARG_LENGTH, (i + 1) * ECHO_ARG_LENGTH <= length ? (i + 1) * ECHO_ARG_LENGTH : length);
			cmds[i] = "echo '" + tmp + (i == 0 ? "' > " : "' >> ") + shellFileName;
		}
		return cmds;
	}
	
	/**
	 * Description: 判断shell解释器类型<br> 
	 * Created date: 2017年11月28日
	 * @param cmd
	 * @return
	 * @author LanChao
	 */
	private String getShellType(String cmd) {
		String prefix = "#!";
		cmd = cmd.trim();
		if (cmd.startsWith(prefix + SH)) {
			return SH;
		} else if (cmd.startsWith(prefix + BASH)) {
			return BASH;
		} else if (cmd.startsWith(prefix + KSH)) {
			return KSH;
		} else if (cmd.startsWith(prefix + CSH)) {
			return CSH;
		} else {
			return BASH;
		}
	}
	
	
}
