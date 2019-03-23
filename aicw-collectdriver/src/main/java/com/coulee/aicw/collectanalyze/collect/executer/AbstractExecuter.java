package com.coulee.aicw.collectanalyze.collect.executer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.coulee.aicw.collectanalyze.collect.common.KeyWord;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.UnixSudoInfo;

/**
 * Description:执行层抽象类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public abstract class AbstractExecuter implements Executer {
	
	/**
	 * ssh、telnet协议
	 */
	protected Protocol protocol;
	
	/**
	 * jdbc协议
	 */
	protected JdbcProtocol jdbcProtocol;
	
	/**
	 * 登录成功后最终提示符
	 */
	protected String prompt;
	
	/**
	 * 登录成功后最终提示符行
	 */
	protected String promptLine;

	@Override
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public void setJdbcProtocol(JdbcProtocol jdbcProtocol) {
		this.jdbcProtocol = jdbcProtocol;
	}

	@Override
	public void setCurrentPrompt(String prompt, String promptLine) {
		this.prompt = prompt;
		this.promptLine = promptLine;
	}
	
	/**
	 * Definition:抛出执行层异常
	 * @param errorCode 错误码
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwExecuterException(int errorCode) throws ExecuterException {
		throw new ExecuterException(String.valueOf(errorCode));
	}
	
	/**
	 * Definition:抛出执行层异常
	 * @param errorCode 错误码
	 * @param e 异常
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwExecuterException(int errorCode, Exception e) throws ExecuterException {
		throw new ExecuterException(String.valueOf(errorCode), e);
	}

	/**
	 * Definition:将结果内的空行去掉
	 * @param result
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	protected String replaceBlankLine(String result) {
		StringBuffer sb = new StringBuffer();
		String[] resultLine = result.split("\n");
		for (String str : resultLine) {
			if (!"".equals(str.trim())) {
				sb.append(str).append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Definition:构造sql执行原始结果
	 * @param resultList
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017年10月23日
	 */
	protected String buildOriSqlResult(List<Map<String, String>> resultList) {
		Map<String, String> firstMap = resultList.get(0);
		Set<String> columns = firstMap.keySet();
		StringBuffer sb = new StringBuffer(StringUtils.join(columns, "\t").trim()).append("\r\n");
		int columnLength = sb.length();
		for (int i = 0; i <= (columnLength + firstMap.keySet().size() * 4 - 4); i++) {
			sb.append("-");
		}
		sb.append("\r\n");
		for (Map<String, String> map : resultList) {
			sb.append(StringUtils.join(map.values(), "\t").trim()).append("\r\n");
		}
		return sb.toString().trim();
	}
	
	/**
	 * Description: 读取非SQL命令回显<br> 
	 * Created date: 2017年11月29日
	 * @param protocol
	 * @param cmd
	 * @param oriPrompt
	 * @param unixSudoInfo
	 * @return
	 * @throws ProtocolException
	 * @author LanChao
	 */
	protected String readUnSqlCmdEcho(Protocol protocol, String cmd, String[] oriPrompt, UnixSudoInfo unixSudoInfo) throws ProtocolException {
		//如果非unix sudo模式，则按正常情况返回回显
		if (unixSudoInfo == null || !unixSudoInfo.isSudoMode()) {
			return protocol.read2KeyWordEcho(cmd, oriPrompt);
		} else {
			String[] sudoPwdPrompt = new String[] { unixSudoInfo.getSudoer() + KeyWord.SIGN_COLON_EN,
					unixSudoInfo.getSudoer() + KeyWord.SIGN_COLON_CN };
			String[] newPrompt = (String[]) ArrayUtils.addAll(oriPrompt, sudoPwdPrompt);
			String echo = protocol.read2KeyWordEcho(cmd, newPrompt);
			if (echo == null || "".equals(echo)) {
				return "";
			} else {
				//如果是unix sudo模式，并且回显信息为提示输入sudoer密码，则输入密码后返回命令回显
				if (echo.trim().endsWith(unixSudoInfo.getSudoer() + KeyWord.SIGN_COLON_EN)
						|| echo.trim().endsWith(unixSudoInfo.getSudoer() + KeyWord.SIGN_COLON_CN)) {
					protocol.sendPassword(unixSudoInfo.getSudoerPassword(), Protocol.CR);
					return protocol.read2KeyWordEcho(cmd, oriPrompt);
				} else {
					return echo;
				}
			}
		}
	}
}
