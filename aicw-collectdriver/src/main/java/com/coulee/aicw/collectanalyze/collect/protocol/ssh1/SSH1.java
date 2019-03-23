package com.coulee.aicw.collectanalyze.collect.protocol.ssh1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.common.KeyWord;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.AbstractProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.lib.jssh.Options;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.lib.jssh.SSHAuthFailedException;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.lib.jssh.SSHClient;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.lib.jssh.STDIN_OutputStream;
import com.coulee.aicw.collectanalyze.collect.protocol.ssh1.lib.jssh.STDOUT_InputStream;
import com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal.VT;

/**
 * Description:SSH1协议
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-6
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class SSH1 extends AbstractProtocol {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private String ip;
	
	private int port;
	
	private Options options = new Options();
	
	private SSHClient client = null;

	private STDOUT_InputStream dis = null;
	
	private STDIN_OutputStream dos = null;
	
	private byte[] buffer = new byte[51200];
	
	private VT vt = new VT();
	
	private String prompt;

	@Override
	public void connect(String ip, int port, int connectTimeout, int echoTimeout)
			throws ProtocolException {
		this.ip = ip;
		this.port = port;
		this.setConnectTimeout(connectTimeout);
		this.setEchotimeout(echoTimeout);
		options.setHostname(this.ip);
		options.setPort(this.port);
		options.setConnectTimeout(this.connectTimeout);
		logger.debug("connect to ip [{}] port [{}] with ssh1 protocol, connect timeout [{}]",
				new Object[] { ip, port, this.connectTimeout });
	}

	@Override
	public String authentication(String username, String password,
			String[] defaultPrompts) throws ProtocolException {
		logger.debug("login remote with username [{}], prompts [{}]", username, Arrays.toString(defaultPrompts));
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			this.throwProtocolException(ErrorCode.USRENAME_PASSWORD_PARAMS_NULL);
		}
		options.setUser(username);
		options.setPassword(password);
		options.setTerminalType("vt100");
		options.setTerminalSize(400, 4000);
		this.client = new SSHClient(options);
		try {
			client.connect();
			this.dis = client.getSTDOUT();
	        this.dos = client.getSTDIN();
	        try {
		        logger.debug("wait 2 seconds ...");
				Thread.sleep(2000L);
				String str = this.readEcho();
				if (str.toString().toLowerCase().contains("your password has expired")) {
					logger.error("login password is expired. ");
					this.throwProtocolException(ErrorCode.LOGIN_PASSWORD_EXPIRED);
				}
				if (str.trim().endsWith("The password needs to be changed. Change now? [Y/N]:")) {
					sendCommand("N", Protocol.CR);
				}
			} catch (InterruptedException ex) {
			}
	        this.sendCommand("", Protocol.CR);
			return this.checkPrompt(defaultPrompts);
		} catch (Exception e) {
			if (e instanceof SSHAuthFailedException) {
				logger.error("authenticated faild, password error. ");
				this.throwProtocolException(ErrorCode.USER_PWD_ERROR, e);
			} else if (e instanceof ProtocolException) {
				throw (ProtocolException)e;
			}
			this.throwProtocolException(ErrorCode.CON_RES_CONNECTION, e);
		}
		return null;
	}
	
	/**
	 * Definition:检测真实的系统提示符
	 * @param defaultPrompts
	 * @return
	 * @throws ProtocolException 
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	private String checkPrompt(String[] defaultPrompts) throws ProtocolException {
		String promptLine = null;
		StringBuffer sb = new StringBuffer();
		String echo = "";
		long startReadEcho = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() - startReadEcho >= this.connectTimeout) { //此操作属于登录操作，所以使用登录连接超时时间
	    		logger.debug("receive echo by prompt quited forcibly. received : \r\n{}", sb.toString());
				this.throwProtocolException(ErrorCode.USER_PROMPT_ERROR);
			}
			echo = this.readEcho();
			sb.append(echo);
			String str = KeyWord.endsWith(echo, defaultPrompts);
			if (str != null) {
				this.prompt = str;
				logger.debug("ssh2 authenticating succeeded, login prompt is {}", this.prompt);
				String[] lineArray = sb.toString().split("\n");
				for (int i = lineArray.length - 1; i >= 0; i--) {
					if (StringUtils.isNotEmpty(lineArray[i].trim())) {
						logger.debug("last not empty line is {}", lineArray[i]);
						if (!lineArray[i].trim().endsWith(this.prompt)) {
							break;
						}
						if (lineArray[i].trim().indexOf(this.prompt) == lineArray[i].trim().lastIndexOf(this.prompt)) {
							promptLine = lineArray[i].trim();
							break;
						}
						String[] promptLineArray = lineArray[i].trim().split(this.prompt);
						if ((promptLineArray.length == 2) && (promptLineArray[0].trim().equals(promptLineArray[1].trim()))) {
							promptLine = promptLineArray[0].trim() + this.prompt;
							logger.debug("two prompt line, lastLine is {}, promptLine is {}", lineArray[i], promptLine);
						}
						break;
					}
				}
				if (promptLine == null) {
					promptLine = this.prompt;
				}
				logger.debug("ssh2 authenticating succeeded, promptLine {}", promptLine);
				break;
			}
		}
		return promptLine;
	}

	@Override
	public void sendCommand(String cmd, String cmdExeSign)
			throws ProtocolException {
		this.testSession();
		try {
			if (cmd != null) {
				logger.debug("ssh1 send command is [{}]", cmd);
				this.dos.write(cmd.getBytes(getEncode()));
				this.dos.write(cmdExeSign.getBytes());
			}
			try {
				Thread.sleep(Protocol.EXEC_WAIT_TIME);
			} catch (InterruptedException e) {
			}
		} catch (IOException e) {
			this.throwProtocolException(ErrorCode.SEND_COMMAND_EXCEPTION, e);
		}
	}
	
	@Override
	public void sendPassword(String cmd, String cmdExeSign)
			throws ProtocolException {
		this.testSession();
		try {
			if (cmd != null) {
				this.dos.write(cmd.getBytes(getEncode()));
				this.dos.write(cmdExeSign.getBytes());
			}
			try {
				Thread.sleep(Protocol.EXEC_WAIT_TIME);
			} catch (InterruptedException e) {
			}
		} catch (IOException e) {
			this.throwProtocolException(ErrorCode.SEND_COMMAND_EXCEPTION, e);
		}
	}

	@Override
	public String readEcho() throws ProtocolException {
		this.testSession();
		List<Byte> bsList = new ArrayList<Byte>();
		try {
			while(true) {
				int len = dis.read(this.buffer);
				if (len <= 0) {
					break;
				}
				for (int i = 0; i < len; i++) {
					char c = toChar(this.buffer[i]);
					if (c != 255) {
						if (this.buffer[i] >= 0) {// 处理特殊字符，转换后拼入字符串
							Byte b = vt.process(c);
							if (b != null) {
								bsList.add(b);
							}
						} else {// 中文字符时，不需要处理
							bsList.add(this.buffer[i]);
						}
						continue;
					}
				}
			}
		} catch (IOException ioe) {
			logger.error("receive echo error, " + ioe.getMessage());
			this.throwProtocolException(ErrorCode.RECEIVE_ECHO_ERROR, ioe);
		}
		if (bsList == null || bsList.size() <= 0) {
			return "";
		}
		int size = bsList.size();
		byte[] bs = new byte[size];
		for (int i = 0; i < size; i++) {
			bs[i] = bsList.get(i).byteValue();
		}
		try {
			String temp = new String(bs, this.getEncode());
			logger.debug("reveive echo :\r\n{}", temp);
			return temp;
		} catch (UnsupportedEncodingException ex) {
			logger.error("encode echo to [{}] error. ", this.getEncode());
			this.throwProtocolException(ErrorCode.RECEIVE_ECHO_ENCODE_ERROR, ex);
		}
		return "";
	}

	private boolean isTimeout = true;
	boolean threadFlag = true;
	@Override
	public String read2ColonEcho() throws ProtocolException {
		final StringBuffer inputStr = new StringBuffer();
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!KeyWord.endsWithColon(inputStr.toString()) && threadFlag) {
						try {
							String echo = readEcho();
							inputStr.append(echo);
							if ((echo.trim().endsWith(KeyWord.SIGN_QES_EN)) || (echo.trim().endsWith(KeyWord.SIGN_QES_CN))) {
								logger.debug("Are you sure you want to continue connecting (yes/no)?");
								sendCommand("yes", Protocol.CR);
							}
						} catch (ProtocolException e) {
							e.printStackTrace();
						}
					}
					isTimeout = false;
				}
			});
			thread.start();
			thread.join(this.echoTimeout);
			if (this.isTimeout) {
				threadFlag = false;
				logger.debug("receive echo timeout in {} ms, reveived : \r\n{}", this.echoTimeout, inputStr.toString());
				this.throwProtocolException(ErrorCode.RECEIVE_ECHO_ERROR);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return inputStr.toString();
	}

	@Override
	public String read2KeyWordEcho(String cmd, String... keywords)
			throws ProtocolException {
		StringBuffer sb = new StringBuffer();
//	    int count = 0;
	    long startReadEcho = System.currentTimeMillis();
	    String morePageStr = "";
	    String echoStr = "";
	    boolean isSuccess = false;
	    while(true) {
			if (System.currentTimeMillis() - startReadEcho >= this.echoTimeout) {
				logger.error("receive echo timeout in {} ms, reveived : \r\n{}", this.echoTimeout, sb.toString());
				this.throwProtocolException(ErrorCode.RECEIVE_ECHO_TIMEOUT);
			}
			echoStr = this.readEcho();
	        sb.append(echoStr);
	        morePageStr = morePageStr + echoStr;
			if (this.isMore(morePageStr.toString())) { //翻页情况
				logger.debug("enter into page model, execute {} to next page ...", this.getMoreExecuter());
				morePageStr = "";
				this.sendCommand(this.getMoreExecuter(), "");
			}
			if ((keywords != null) && keywords.length > 0) {
				for (String keyword : keywords) {
					if ((keyword != null) && (sb.toString().trim().endsWith(keyword.trim()))) {
						logger.debug("execute command successful by keyword [{}], command is [{}]", keyword, cmd);
						isSuccess = true;
						break;
					}
					if (isCommandEnd(sb.toString(), keyword)) {
						logger.debug("execute command successful by keyword of remove '(config)', keyword is [{}], command is [{}]", keyword, cmd);
						isSuccess = true;
						break;
					}
				}
			}
			if (isSuccess) {
				break;
			}
			this.processSpecil(morePageStr);
//			if (echoStr.trim().equals("")) {
//				count++;
//				logger.debug("empty echo count is:" + count);
//				if (count > 50) {
//					logger.debug("Reading echo by prompt quited forcibly...");
//					this.throwProtocolException(ErrorCode.RECEIVE_ECHO_ERROR);
//				}
//				try {
//					Thread.sleep(Protocol.EXEC_WAIT_TIME);
//				} catch (InterruptedException ex) {
//				}
//			} else {
//				count = 0;
//			}
	    }
	    return sb.toString();
	}
	
	private boolean isCommandEnd(String result, String keyword) {
		if (result != null && keyword != null) {
			String prompt = keyword.replaceAll("\\(.*\\)", "").trim();
			if (prompt.equals("")) {
				return false;
			}
			String[] lines = result.split("\n");
			for (int i = lines.length - 1; i >= 0; i--) {
				if (StringUtils.isNotEmpty(lines[i].trim())) {
					String lastLine = lines[i].trim();
					String begin = prompt.substring(0, prompt.length() - 1).trim();
					String end = prompt.substring(prompt.length() - 1).trim();
					if ((lastLine.startsWith(begin)) && (lastLine.endsWith(end))) {
						return true;
					}
					if (lastLine.replaceAll("\\(.*\\)", "").endsWith(prompt)) {
						return true;
					}
					String[] arr = keyword.split("\\s+");
					if ((arr.length <= 1) || (!lastLine.startsWith(arr[0])) || (!lastLine.endsWith(end))) {
						break;
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void close() throws ProtocolException {
		try {
			if (this.client != null) {
				this.client.close();
			}
			if (this.dis != null) {
				this.dis.close();
				this.dis = null;
			}
			if (this.dos != null) {
				this.dos.close();
				this.dos = null;
			}
			logger.debug("close connection for ip [{}], port [{}]", this.ip, this.port);
		} catch (IOException e) {
			logger.error("close connection for ip [{}], port [{}] error. ", this.ip, this.port);
			e.printStackTrace();
		}
	}
	
	/**
	 * Definition:测试会话连接
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	private void testSession() throws ProtocolException {
		if (this.client == null || this.dis == null || this.dos == null) {
			logger.error("session doesn`t created or disconnected");
			this.throwProtocolException(ErrorCode.SESSION_NOT_CREATE);
		}
	}
	
	
	public static void main(String[] args) {
		SSH1 ssh1 = new SSH1();
		try {
			ssh1.connect("192.168.1.16", 22, 10, 60);
			ssh1.authentication("lanchao", "123456", new String[]{"$"});
			ssh1.sendCommand("ls", Protocol.CR);
			ssh1.readEcho();
			ssh1.sendCommand("LANG=c", Protocol.CR);
			ssh1.readEcho();
			ssh1.sendCommand("su -", Protocol.CR);
			ssh1.read2ColonEcho();
			ssh1.sendCommand("123456", Protocol.CR);
			ssh1.readEcho();
			ssh1.sendCommand("pwd", Protocol.CR);
			ssh1.readEcho();
			ssh1.sendCommand("ls", Protocol.CR);
			ssh1.readEcho();
		} catch (ProtocolException e) {
			e.printStackTrace();
			System.out.println("错误信息：" + ErrorCode.msg(e.getMessage()));
		} finally {
			try {
				ssh1.close();
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
		}
	}

}
