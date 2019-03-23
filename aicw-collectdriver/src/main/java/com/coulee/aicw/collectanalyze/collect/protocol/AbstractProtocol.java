package com.coulee.aicw.collectanalyze.collect.protocol;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ReadSpecilConfig;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;

/**
 * Description:协议抽象层
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-3
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public abstract class AbstractProtocol implements Protocol {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * 字符编码
	 */
	protected String encode;
	
	/**
	 * 读取回显超时时间
	 */
	protected int echoTimeout = Protocol.ECHO_TIME_OUT * 1000;
	
	/**
	 * 连接超时时间
	 */
	protected int connectTimeout = Protocol.CONNECT_TIME_OUT * 1000;
	
	/**
	 * More翻页提示符
	 */
	protected String[] morePrompt;
	
	/**
	 * More翻页执行指令
	 */
	protected String moreExecuter;
	
	/**
	 * 特殊关键字提示信息及对应的操作
	 */
	protected Map<String, String> specilMap = ReadSpecilConfig.readKeywordCommand();
	
	/**
	 * Definition:处理特殊提示信息，发送对应的操作指令
	 * @param result
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	protected void processSpecil(String result) throws ProtocolException {
		if (StringUtils.isEmpty(result)) {
			return;
		}
		for (String keyword : this.specilMap.keySet()) {
			if (result.trim().endsWith(keyword)) {
				logger.debug("process specil keyword '{}', send command {}", keyword, this.specilMap.get(keyword));
				this.sendCommand(this.specilMap.get(keyword), Protocol.CR);
			}
		}
	}

	@Override
	public String getEncode() {
		if (this.encode == null || "".equals(this.encode)) {
			return Protocol.DEFAULT_ECHO_ENCODE;
		}
		return this.encode;
	}

	@Override
	public void setEncode(String encode) {
		this.encode = encode;
	}
	
	/**
	 * Definition:设置读取回显超时时间
	 * @param echoTimeout
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void setEchotimeout(int echoTimeout) {
		if (echoTimeout != 0) {
			this.echoTimeout = echoTimeout * 1000;
		}
	}
	
	/**
	 * Definition:设置连接超时时间
	 * @param connectTimeout
	 * @Author: LanChao
	 * @Created date: 2016-12-23
	 */
	protected void setConnectTimeout(int connectTimeout) {
		if (connectTimeout != 0) {
			this.connectTimeout = connectTimeout * 1000;
		}
	}
	
	@Override
	public void setMorePrompt(String... morePrompt) {
		if (morePrompt == null || morePrompt.length == 0) {
			return;
		}
		for (String more : morePrompt) {
			if (StringUtils.isEmpty(more)) {
				continue;
			}
			this.morePrompt = (String[]) ArrayUtils.add(this.getMorePrompt(), more);
		}
	}
	
	@Override
	public String[] getMorePrompt() {
		if (this.morePrompt == null || this.morePrompt.length == 0) {
			return Protocol.DEFAULT_MORE;
		}
		return this.morePrompt;
	}

	@Override
	public void setMoreExecuter(String cmd) {
		if (CollectConstants.NEXT_PAGE_CMD_SPACE.equals(cmd)) {
			this.moreExecuter = Protocol.SPACE;
		} else if (CollectConstants.NEXT_PAGE_CMD_ENTER.equals(cmd)) {
			this.moreExecuter = Protocol.CR;
		} else {
			this.moreExecuter = cmd;
		}
	}
	
	@Override
	public String getMoreExecuter() {
		if (StringUtils.isEmpty(this.moreExecuter)) {
			return Protocol.DEFAULT_MORE_EXECUTER;
		}
		return this.moreExecuter;
	}
	
	@Override
	public boolean isMore(String echo) {
		if (StringUtils.isEmpty(echo)) {
			return false;
		}
		String[] mores = this.getMorePrompt();
		Matcher matcher = null;
		for (String more : mores) {
			if (echo.contains(more)) {
				return true;
			}
			matcher = Pattern.compile(more).matcher(echo);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Definition:字节转字符
	 * @param b
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-6
	 */
	protected char toChar(byte b) {
		return (char) (b & 0xff);
	}
	
	/**
	 * Definition:抛出协议层异常
	 * @param errorCode 错误码
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwProtocolException(int errorCode) throws ProtocolException {
		throw new ProtocolException(String.valueOf(errorCode));
	}
	
	/**
	 * Definition:抛出协议层异常
	 * @param errorCode 错误码
	 * @param e 异常
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	protected void throwProtocolException(int errorCode, Exception e) throws ProtocolException {
		throw new ProtocolException(String.valueOf(errorCode), e);
	}

}
