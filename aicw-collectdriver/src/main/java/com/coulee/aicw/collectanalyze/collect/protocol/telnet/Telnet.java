package com.coulee.aicw.collectanalyze.collect.protocol.telnet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
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
import com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal.NegotiationCmd;
import com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal.NegotiationCmdChain;
import com.coulee.aicw.collectanalyze.collect.protocol.virtualterminal.VT;

/**
 * Description:Telnet协议
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-5
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class Telnet extends AbstractProtocol {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private String ip;
	
	private int port;
	
	private Socket socket = null;

	private DataInputStream dis = null;
	
	private DataOutputStream dos = null;
	
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
		logger.debug("connect to ip [{}] port [{}] with telnet protocol, connect timeout [{}]",
				new Object[] { ip, port, this.connectTimeout });
		this.socket = new Socket();
        try {
			this.socket.connect(new InetSocketAddress(ip, port), this.connectTimeout);
	        this.dis = new DataInputStream(this.socket.getInputStream());
	        this.dos = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			logger.error("connect to ip [{}] port [{}] with telnet protocol error", 
					new Object[] { ip, port, e });
			this.throwProtocolException(ErrorCode.CON_RES, e);
		}

	}

	@Override
	public String authentication(String username, String password,
			String[] defaultPrompts) throws ProtocolException {
		this.testConnection();
		logger.debug("login remote with username [{}], prompts [{}]", username, Arrays.toString(defaultPrompts));
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			this.throwProtocolException(ErrorCode.USRENAME_PASSWORD_PARAMS_NULL);
		}
	    String echo = this.read2ColonEcho();
	    if (echo.trim().endsWith("(y/n)：") || echo.trim().endsWith("(y/n):")) {
	    	this.sendCommand("y", Protocol.CR);
	    	echo = read2ColonEcho();
		}
	    this.sendCommand(username, Protocol.CR);
	    echo = read2ColonEcho();
		this.sendPassword(password, Protocol.CR);
		try {
	        logger.debug("wait 5 seconds ...");
			Thread.sleep(5000L);
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
	    String temp = null;
	    String echo = "";
	    long startReadEcho = System.currentTimeMillis();
	    while (true) {
	    	if (System.currentTimeMillis() - startReadEcho >= this.connectTimeout) { //此操作属于登录操作，所以使用登录连接超时时间
	    		logger.debug("receive echo by prompt quited forcibly. received : \r\n{}", sb.toString());
				this.throwProtocolException(ErrorCode.USER_PROMPT_ERROR);
			}
	    	echo = this.readEcho();
	        sb.append(echo);
	        temp = sb.toString().trim();
			if (temp.toLowerCase().contains("your password has expired")) {
				logger.error("login password is expired. ");
				this.throwProtocolException(ErrorCode.LOGIN_PASSWORD_EXPIRED);
			}
			if (temp.endsWith("All user interfaces are used, please try later!".toLowerCase())) {
				logger.error("all user interfaces are used. ");
				this.throwProtocolException(ErrorCode.All_USER_INTERFACES_ARE_USED);
			}
			if (temp.toLowerCase().endsWith("login:") || temp.toLowerCase().endsWith("login：")
					|| temp.toLowerCase().endsWith("login name:") || temp.toLowerCase().endsWith("login name：")
					|| temp.toLowerCase().endsWith("username:") || temp.toLowerCase().endsWith("username：")
					|| temp.toLowerCase().endsWith("password:") || temp.toLowerCase().endsWith("password：")) {
				logger.error("authenticated faild, password error. ");
				this.throwProtocolException(ErrorCode.USER_PWD_ERROR);
			}
			if (temp.endsWith("Telnet Server has closed the connection".toLowerCase())) {
				logger.error("telnet authenticating failed, telnet connection close");
				this.throwProtocolException(ErrorCode.CON_RES_CONNECTION);
			}
			if (temp.endsWith("Terminal type?".toLowerCase())) {
				logger.debug("send terminal type dumb. ");
				this.sendCommand("dumb", Protocol.CR);
			}
			String prompt = KeyWord.endsWith(temp, defaultPrompts);
			if (prompt != null) {
				this.prompt = prompt;
				logger.debug("telnet authenticating succeeded, promptLine {}", this.prompt);
				String[] lineArray = temp.split("\n");
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
				logger.debug("telnet authenticating succeeded, promptLine {}", promptLine);
				break;
			}
	    }
		return promptLine;
	}

	@Override
	public void sendCommand(String cmd, String cmdExeSign)
			throws ProtocolException {
		this.testConnection();
		try {
			if (cmd != null) {
				logger.debug("telnet send command is [{}]", cmd);
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
		this.testConnection();
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
		this.testConnection();
		List<Byte> bsList = new ArrayList<Byte>();
		try {
			if(dis.available() == 0) {
				return "";
			}
			int len = dis.read(this.buffer);
			NegotiationCmdChain cmdList = new NegotiationCmdChain();
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
				NegotiationCmd negotiationCmd = new NegotiationCmd();
				if (toChar(this.buffer[i + 2]) == 1 || toChar(this.buffer[i + 2]) == 3) {
					int verb = toChar(this.buffer[i + 1]);
					switch (verb) {
					case 253:
						negotiationCmd.option = (byte) 251;
						break;
					case 254:
						negotiationCmd.option = (byte) 252;
						break;
					case 251:
						negotiationCmd.option = (byte) 253;
						break;
					case 252:
						negotiationCmd.option = (byte) 251;
						break;
					case 250:

					}
				} else if (toChar(this.buffer[i + 1]) == 253 || toChar(this.buffer[i + 1]) == 254) {
					negotiationCmd.option = (byte) 252;
				} else if (toChar(this.buffer[i + 1]) == 251 || toChar(this.buffer[i + 1]) == 252) {
					negotiationCmd.option = (byte) 254;
				}
				negotiationCmd.optionValue = this.buffer[i + 2];
				cmdList.add(negotiationCmd);
				i += 2;
			}
			if (cmdList != null && cmdList.size() > 0) {
				this.dos.write(cmdList.getBytes());
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

	boolean isTimeout = true;
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
			if (this.dis != null) {
				this.dis.close();
				this.dis = null;
			}
			if (this.dos != null) {
				this.dos.close();
				this.dos = null;
			}
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}
			logger.debug("close connection for ip [{}], port [{}]", this.ip, this.port);
		} catch (IOException ex) {
			logger.error("close connection for ip [{}], port [{}] error. ", this.ip, this.port);
			throw new ProtocolException(ex);
		}
	}

	/**
	 * Definition:测试网络连接
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	private void testConnection() throws ProtocolException {
		if (this.dis == null || this.dos == null || this.socket == null) {
			logger.error("network connection is missing");
			this.throwProtocolException(ErrorCode.CON_RES_CONNECTION);
		}
	}
	
	
	public static void main(String[] args) {
		Telnet telnet = new Telnet();
		try {
			telnet.connect("192.168.1.112", 23, 10, 60);
			telnet.setEncode("GBK");
			String echo = telnet.authentication("lanchao", "woshilanchao", new String[] {">"});
			String cmd = "wmic OS Get DataExecutionPrevention_SupportPolicy";
			telnet.sendCommand(cmd, Protocol.CR);
			echo = telnet.read2KeyWordEcho(cmd, ">");
			telnet.close();
			System.out.println(echo);
		} catch (ProtocolException e) {
			e.printStackTrace();
			System.out.println("错误信息：" + ErrorCode.msg(e.getMessage()));
		}
	}
}
