package com.coulee.aicw.collectanalyze.collect.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ExecuterException;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.collectanalyze.collect.valueobject.InteractiveCmd;
import com.coulee.aicw.collectanalyze.collect.valueobject.UnixSudoInfo;

/**
 * Description:交互类脚本执行类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class InteractExecuter extends AbstractExecuter {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * 匹配参数占位符正则表达式
	 */
	private Pattern pattern = Pattern.compile("\\{(\\d+)\\}");
	
	/**
	 * 提示符分隔标识
	 */
	private final String PROMPT_SPLITER = "\\|";
	
	/**
	 * Unix sudo模式信息
	 */
	private UnixSudoInfo unixSudoInfo;

	@Override
	public ExecuteResult execute(CommandArg commandArg)
			throws ExecuterException {
		this.unixSudoInfo = commandArg.getUnixSudoInfo();
		String interactiveType = commandArg.getInteractiveType(); //交互脚本类型
		List<InteractiveCmd> cmds = commandArg.getCmds(); //交互脚本
		List<String> args = commandArg.getArgs(); //脚本参数
		if (cmds == null || cmds.isEmpty()) {
			this.throwExecuterException(ErrorCode.EXECUTE_SCRIPT_NULL);
		}
		this.formatCmds(cmds, args);
		if (CollectConstants.SCRIPT_COMMON.equals(interactiveType)) {
			if (this.protocol == null) {
				this.throwExecuterException(ErrorCode.PROTOCOL_NULL);
			}
			return this.executeCommon(cmds);
		} else if (CollectConstants.SCRIPT_SQL.equals(interactiveType)) {
			if (this.jdbcProtocol == null) {
				this.throwExecuterException(ErrorCode.JDBC_PROTOCOL_NULL);
			}
			return this.executeSql(cmds);
		}
		return null;
	}
	
	/**
	 * Definition:执行普通类交互脚本
	 * @param cmds
	 * @return
	 * @throws ExecuterException
	 * @Author: LanChao
	 * @Created date: 2016-12-27
	 */
	private ExecuteResult executeCommon(List<InteractiveCmd> cmds) throws ExecuterException {
		StringBuffer resultSb = new StringBuffer();
		for (InteractiveCmd cmd : cmds) {
			String c = cmd.getCmd().trim().replaceAll("\\\r", "");
			try {
				this.protocol.sendCommand(c, Protocol.CR);
				String[] echo = this.readCmdEcho(c, cmd.getPrompt(), cmd.getErrorPrompt());
				resultSb.append(echo[0]);
				if (echo[1] != null) {
					resultSb.append(echo[1]);
					logger.error("waitted error prompt [{}], execute will be exit...", echo[1]);
					throw new ExecuterException("execute cmd [" + c + "] error, waitted error prompt [" + echo[1] + "], echo [" + echo[0] + "]");
				}
			} catch (ProtocolException e) {
				e.printStackTrace();
				logger.error("execute interactive common cmd error, cmd is [{}], error is {}", cmd, e.getMessage());
				throw new ExecuterException(e.getMessage());
			}
		}
		return new ExecuteResult(resultSb.toString());
	}
	
	/**
	 * Definition:执行SQL类交互脚本
	 * @param cmds
	 * @throws ExecuterException
	 * @Author: LanChao
	 * @Created date: 2016-12-27
	 */
	private ExecuteResult executeSql(List<InteractiveCmd> cmds) throws ExecuterException {
		ExecuteResult result = new ExecuteResult();
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		StringBuffer oriResult = new StringBuffer();
		for (InteractiveCmd cmd : cmds) {
			String sql = cmd.getCmd().trim();
			if (sql.endsWith(";")) {
				sql = sql.substring(0, sql.length() - 1);
			}
			try {
				if (sql.toLowerCase().startsWith("select") || sql.toLowerCase().startsWith("show")) {
					List<Map<String, String>> list = this.jdbcProtocol.executeQuerySql(sql);
					if (list != null && !list.isEmpty()) {
						resultList.addAll(list);
						oriResult.append(this.buildOriSqlResult(list)).append("\r\n\r\n");
					}
				} else {
					boolean b = this.jdbcProtocol.executeSql(sql);
					Map<String, String> resultMap = new HashMap<String, String>();
					resultMap.put("EXECUTE_RESULT", String.valueOf(b));
					List<Map<String, String>> tmpList = new ArrayList<>();
					tmpList.add(resultMap);
					oriResult.append(this.buildOriSqlResult(tmpList)).append("\r\n\r\n");
					resultList.addAll(tmpList);
				}
			} catch (ProtocolException e) {
				e.printStackTrace();
				logger.error("execute interactive sql error, sql is [{}], error is {}", sql, e.getMessage());
				throw new ExecuterException(e.getMessage());
			}
		}
		if (resultList != null && !resultList.isEmpty()) {
			result.setSelectSqlResult(resultList);
			result.setResult(oriResult.toString());
		}
		return result;
	}
	

	/**
	 * Definition:格式化脚本，补齐参数位
	 * @param cmds
	 * @param args
	 * @return
	 * @throws ExecuterException
	 * @Author: LanChao
	 * @Created date: 2016-12-27
	 */
	private void formatCmds(List<InteractiveCmd> cmds, List<String> args) throws ExecuterException {
		for (int i = 0; i < cmds.size(); i++) {
			InteractiveCmd cmd = cmds.get(i);
			cmds.set(i, new InteractiveCmd(this.getCmd(cmd.getCmd(), args), 
					this.getWaitPrompt(cmd.getPrompt()), cmd.getErrorPrompt()));
		}
	}
	
	/**
	 * Definition:格式化脚本，加入参数
	 * @param cmd
	 * @param args
	 * @return
	 * @throws ExecuterException
	 * @Author: LanChao
	 * @Created date: 2016-12-27
	 */
	private String getCmd(String cmd, List<String> args) throws ExecuterException {
		if (args == null || args.isEmpty()) {
			return cmd;
		}
		Matcher matcher = pattern.matcher(cmd);
		while (matcher.find()) {
			int i = Integer.parseInt(matcher.group(1));
			String arg = "";
			if (i < args.size()) {
				arg = StringUtils.isNotEmpty(args.get(i)) ? args.get(i) : "";
			} else {
				logger.error("SCRIPT_ARGS_NOT_MATCHED, cmd parameter flag {}, parameter list size {}", i, args.size());
				this.throwExecuterException(ErrorCode.SCRIPT_ARGS_NOT_MATCHED);
			}
			cmd = cmd.replaceAll("\\{" + i + "\\}", arg);
		}
		return cmd;
	}
	
	/**
	 * Definition:获取提示符
	 * @param prompt
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-27
	 */
	private String getWaitPrompt(String prompt) {
		if (StringUtils.isEmpty(prompt)) {
			return this.promptLine;
		}
		return CollectConstants.DEFAULT_PROMPT.equals(prompt) ? this.promptLine : prompt.concat("|").concat(this.promptLine);
	}

	/**
	 * Definition:获取执行交互命令回显信息
	 * @param cmd 执行的命令
	 * @param prompt 期待的正常回显信息
	 * @param errorPrompt 期待的异常回显信息
	 * @return index0:echo  index1:errorPrompt
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2017-3-6
	 */
	private String[] readCmdEcho(String cmd, String prompt, String errorPrompt) throws ProtocolException {
		String[] prompts = prompt.split(PROMPT_SPLITER);
		String[] errorPrompts = null;
		if (errorPrompt != null && !"".equals(errorPrompt)) {
			errorPrompts = errorPrompt.split(PROMPT_SPLITER);
		}
		String[] allPrompts = errorPrompts == null ? prompts : (String[]) ArrayUtils.addAll(prompts, errorPrompts);
		String[] result = new String[2];
//		String echo = this.protocol.read2KeyWordEcho(cmd, allPrompts);
		String echo = this.readUnSqlCmdEcho(this.protocol, cmd, allPrompts, this.unixSudoInfo);
		result[0] = echo;
		if (errorPrompts == null || errorPrompts.length == 0 || echo == null || "".equals(echo.trim())) {
			return result;
		}
		for (String errorTemp : errorPrompts) {
			if (echo.contains(errorTemp)) {
				logger.warn("execute interactive common cmd warnning, cmd is [{}], echo is : {}", cmd, echo);
				result[1] = errorTemp;
				break;
			}
		}
		return result;
	}
	
}
