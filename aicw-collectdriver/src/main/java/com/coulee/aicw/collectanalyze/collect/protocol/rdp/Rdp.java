package com.coulee.aicw.collectanalyze.collect.protocol.rdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.collectanalyze.collect.common.CommonUtils;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.foundations.utils.common.UUIDHexGenerator;

/**
 * Description:RDP协议类<br>目前仅支持linux系统下的rdesktop工具（修改版）<br>
 * 实现思路：<br>
 * 1、通过远程桌面静默方式连接win主机，并指定磁盘映射名称<br>
 * 2、连接成功后，rdesktop自动执行磁盘映射内的start.bat批处理文件(cmd /c //tsclient/bms/start.bat)<br>
 * 3、start.bat批处理脚本将磁盘映射内的业务脚本及其依赖的文件copy至win主机<br>
 * 4、start.bat批处理脚本执行业务脚本，将脚本内容重定向输出至磁盘映射目录的文本文件内<br>
 * 5、监听执行过程产生的日志信息，以及结果文件大小变化，直至执行结束退出或超时退出，结束rdesktop进程，读取结果文件。<br>
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-27
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class Rdp {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	/**
	 * RDP默认端口
	 */
	private final int DEFAULT_PORT = 3389;
	
	/**
	 * 默认字符编码
	 */
	private final String DEFAULT_ENCODE = "GBK";
	
	/**
	 * 默认工作空间
	 * /opt/aicw_rdpworkspace
	 */
	private final String DEFAULT_WORKPACE = "/opt/aicw_rdpworkspace";
	
	/**
	 * 磁盘映射别名
	 */
	private final String SHARE_PATH_ALIAS = "bms";
	
	/**
	 * 引导脚本名称
	 */
	private final String START_FILE_NAME = "start.bat";
	
	/**
	 * 执行的业务脚本名称
	 */
	private final String EXECUTE_FILE_NAME = "script.bat";
	
	/**
	 * 执行的业务脚本结果文件名
	 */
	private final String EXECUTE_RESULT_NAME = "script.result";
	
	/**
	 * 字符编码
	 */
	private String encode = DEFAULT_ENCODE;
	
	/**
	 * 域名
	 */
	private String domain;
	
	/**
	 * IP
	 */
	private String ip;
	
	/**
	 * 端口
	 */
	private int port;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeout = Protocol.CONNECT_TIME_OUT * 1000;
	
	/**
	 * 读取回显超时时间
	 */
	private int echoTimeout = Protocol.ECHO_TIME_OUT * 1000;
	
	/**
	 * 工作空间
	 */
	private String workspace = DEFAULT_WORKPACE;
	
	/**
	 * 错误码
	 */
	private String errorCode = "";
	
	private Pattern digPattern = Pattern.compile("\\d+");
	
	private Pattern statePattern = Pattern.compile("(?<=CURRENT_STATE:)[\\w\\d_]+");
	
	/**
	 * Definition:设置编码
	 * @param encode
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void setEncode(String encode) {
		if (StringUtils.isNotEmpty(encode)) {
			this.encode = encode;
		}
	}

	/**
	 * Definition:设置超时时间
	 * @param connectTimeout 连接超时时间
	 * @param echoTimeout 读取执行结果超时时间
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public void setTimeout(int connectTimeout, int echoTimeout) {
		if (echoTimeout != 0) {
			this.echoTimeout = echoTimeout * 1000;
		}
		if (connectTimeout != 0) {
			this.connectTimeout = connectTimeout * 1000;
		}
	}
	
	public static void main(String[] args) {
		String uuid = UUIDHexGenerator.getInstance().generate();
		System.out.println(uuid);
	}
	
	/**
	 * Definition:构造引导脚本内容
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private String getStartBat() {
		String uuid = UUIDHexGenerator.getInstance().generate();
		StringBuffer sb = new StringBuffer();
		sb.append("@echo OFF").append("\r\n");
		sb.append("set aicw_temp=c:\\").append(uuid).append("\r\n");
		sb.append("mkdir %aicw_temp%").append("\r\n");
		sb.append("xcopy \"\\\\tsclient\\").append(SHARE_PATH_ALIAS).append("\" %aicw_temp%").append(" /E /Y /Q\r\n");
		sb.append("cd /d %aicw_temp%").append("\r\n");
		sb.append("\"%aicw_temp%\\").append(EXECUTE_FILE_NAME).append("\" 2>&1 1 | more > \"\\\\tsclient\\").append(SHARE_PATH_ALIAS).append("\\").append(EXECUTE_RESULT_NAME).append("\"").append("\r\n");
		sb.append("cd ..\r\n");
		sb.append("rd %aicw_temp% /Q /S").append("\r\n");
		return sb.toString();
	}
	
	/**
	 * Definition:构造带文件共享的rdesktop连接命令
	 * @param sharePath 共享目录
	 * @param ip IP地址
	 * @param port 端口
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private String getExecuteCmd(String sharePath) {
		//rdesktop -u Administrator -p 'Ab123456' -r disk:bms=/opt/test -0 192.168.1.133:3389
		StringBuffer sb = new StringBuffer("rdesktop ");
		if (StringUtils.isNotEmpty(domain)) {
			sb.append("-d ").append(domain).append(" "); //域名
		}
		sb.append("-u ").append(username).append(" "); //用户名
		sb.append("-p '").append(password).append("' "); //密码
		if (sharePath != null && !"".equals(sharePath)) {
			sb.append("-r disk:").append(SHARE_PATH_ALIAS).append("=").append(sharePath).append(" "); //共享磁盘
		}
		sb.append("-0 "); //console模式
		sb.append(ip).append(":").append(port == 0 ? DEFAULT_PORT : port); //ip:port
		return sb.toString();
	}
	
	/**
	 * Definition:获取kill进程命令
	 * @param processId 进程ID
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private String getKillCmd(String processId) {
		return "kill -s 35 " + processId;
	}
	
	/**
	 * Definition:获取正在使用文件进程命令
	 * @param filePath
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-1-12
	 */
	private String getFuserCmd(String filePath) {
		return "fuser -v " + filePath;
	}
	
	private Rdp() {
	}
	
	/**
	 * Description :实例化RDP工具类
	 * @param domain
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	private Rdp(String domain, String ip, int port, String username, String password) {
		this.domain = domain;
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Definition:初始化工具类
	 * @param domain 域名
	 * @param ip IP
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public static Rdp init(String domain, String ip, int port, String username, String password) {
		return new Rdp(domain, ip, port, username, password);
	}
	
	/**
	 * Definition:初始化工具类
	 * @param ip IP
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public static Rdp init(String ip, int port, String username, String password) {
		return new Rdp(null, ip, port, username, password);
	}
	
	
	/**
	 * Definition:执行命令，返回结果
	 * @param cmd 命令
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public String executeCmd(String cmd) throws ProtocolException {
		return this.executeCmd(cmd, null);
	}
	
	/**
	 * Definition:执行命令，返回结果
	 * @param cmd 命令
	 * @param dependPath 命令所依赖的环境路径
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	public String executeCmd(String cmd, String dependPath) throws ProtocolException {
		if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			logger.error("RDP REQUIRED PARAMS NULL");
			throw new ProtocolException(String.valueOf(ErrorCode.PARAMS_NULL));
		}
		if (StringUtils.isEmpty(cmd)) {
			logger.error("RDP EXECUTE CMD CONTENT NULL");
			throw new ProtocolException(String.valueOf(ErrorCode.EXECUTE_CMD_NULL));
		}
		logger.info("connect to [{}:{}] with rdp protocol, connect timeout [{}]", new Object[] { ip, port, this.connectTimeout });
		String workspacePath = this.buildFiles(cmd, dependPath); //构造执行工作空间
		Process process = this.executeRdpCmd(workspacePath); //执行rdp命令
		process = this.checkProcess(process, workspacePath); //监听执行进度
		String result = "";
		if (StringUtils.isEmpty(this.errorCode)) { //读取结果文件
			result = this.readResult(workspacePath);
		} 
		this.close(process, workspacePath);
		if (StringUtils.isNotEmpty(this.errorCode)) {
			throw new ProtocolException(this.errorCode);
		}
		return result;
	}
	
	/**
	 * Definition:关闭RDP连接，删除临时工作空间
	 * @param process
	 * @param workspacePath
	 * @Author: LanChao
	 * @Created date: 2017-1-3
	 */
	private void close(Process process, String workspacePath) {
		if (process != null) {
			try { //关闭rdp连接
				logger.info("close rdp connection from {}:{}", this.ip, this.port);
				process.destroy();
			} catch (Exception e) {
				logger.error("close rdp connection error : \r\n" + e.getMessage());
			}
		}
		if (workspacePath != null && !"".equals(workspacePath)) {
			CommonUtils.deleteFile(workspacePath); //删除工作空间
		}
	}
	
	
	/**
	 * Definition:构造工作空间
	 * @param cmd 脚本内容
	 * @param dependPath 脚本依赖环境路径
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private String buildFiles(String cmd, String dependPath) throws ProtocolException {
		if (!this.workspace.endsWith(File.separator)) {
			this.workspace = this.workspace.concat(File.separator);
		}
		String workspacePath = this.workspace.concat(UUID.randomUUID().toString().replace("-", ""));
		File workspace = new File(workspacePath);
		if (!workspace.exists()) {
			workspace.mkdirs();
		}
		//创建start.bat引导脚本
		CommonUtils.writeFile(workspacePath.concat(File.separator).concat(this.START_FILE_NAME), this.getStartBat(), this.encode);
		//创建script.bat业务脚本
		CommonUtils.writeFile(workspacePath.concat(File.separator).concat(this.EXECUTE_FILE_NAME), cmd, this.encode);
		if (StringUtils.isNotEmpty(dependPath)) { //将执行脚本依赖的文件拷贝到临时工作空间
			try {
				CommonUtils.copyFolder(dependPath, workspacePath);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new ProtocolException(String.valueOf(ErrorCode.BUILD_SCRIPT_FILE_ERROR));
			}
		}
		return workspacePath;
	}
	
	/**
	 * Definition:执行RDP命令
	 * @param workspacePath 工作空间路径
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private Process executeRdpCmd(String workspacePath) throws ProtocolException {
		String rdpCmd = this.getExecuteCmd(workspacePath);
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", rdpCmd });
			return process;
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ProtocolException(String.valueOf(ErrorCode.EXEC_RDP_MODE_ERROR));
		}
	}

	
	/**
	 * Definition:查看执行结果
	 * @param process 进程对象
	 * @param workspacePath 工作空间
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private Process checkProcess(Process process, String workspacePath) {
		String echo = this.readRdpLog(process.getInputStream());
		Matcher matcher = digPattern.matcher(echo);
		if (!matcher.find()) {
			logger.error("can`t find rdesktop process id, please check rdesktop tool envionment.");
			this.errorCode = String.valueOf(ErrorCode.EXEC_RDP_MODE_ERROR);
			return process;
		}
		String processId = matcher.group();
	    logger.debug("rdesktop process id : {}", processId);
	    int reconnCount = 1;
	    long start = System.currentTimeMillis();
	    while(true) {
		    try {
		    	try { Thread.sleep(2000); } catch (InterruptedException e) {}
				//执行kill指令，发送信号
				Process killProcess = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", this.getKillCmd(processId) });
				echo = this.readRdpLog(process.getInputStream());
		        matcher = statePattern.matcher(echo);
		        try { killProcess.destroy(); } catch (Exception e) {}
				if (!matcher.find() && (System.currentTimeMillis() - start >= this.connectTimeout)) { //连接超时进程自动停止
					logger.debug("get rdesktop process state faild.");
					this.errorCode = String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT);
					break;
				}
				echo = matcher.group().toLowerCase();
			    logger.debug("rdp process current state : {}", echo);
				if ("start_conn".equals(echo)) { //开始连接
					logger.debug("start connect to {}:{} with rdp protocol... ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("connecting {}:{} with rdp protocol timeout ", this.ip, this.port);
						this.errorCode = String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT);
						break;
					}
				} else if ("conn_error".equals(echo)) { //网络连接错误
					logger.error("connection {}:{} with rdp protocol fail. ", this.ip, this.port);
					this.errorCode = String.valueOf(ErrorCode.CON_RES_CONNECTION);
					break;
				} else if ("reconnect".equals(echo)) { //重连
					if (reconnCount >= 3) {
						logger.error("reconnect {}:{} three times with rdp protocol fail. ", this.ip, this.port);
						this.errorCode = String.valueOf(ErrorCode.CON_RES_CONNECTION);
						break;
					}
					reconnCount++;
					logger.debug("reconnect {}:{} with rdp protocol, times {} ", new Object[] { this.ip, this.port, reconnCount });
					process.destroy();
					process = this.executeRdpCmd(workspacePath);
					echo = this.readRdpLog(process.getInputStream());
					matcher = digPattern.matcher(echo);
					if (matcher.find()) {
						processId = matcher.group();
					    logger.debug("rdesktop reconnect process id : {}", processId);
					    start = System.currentTimeMillis();
					} else {
						logger.error("get rdesktop reconnect process id faild.");
						this.errorCode = String.valueOf(ErrorCode.EXEC_RDP_MODE_ERROR);
						break;
					}
				} else if ("connecting".equals(echo)) { //正在连接
					logger.debug("connecting {}:{} with rdp protocol... ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("connecting {}:{} with rdp protocol timeout ", this.ip, this.port);
						this.errorCode = String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT);
						break;
					}
				} else if ("login_error".equals(echo)) { //用户名密码错误
					logger.error("login {}:{} with rdp protocol fail. ", this.ip, this.port);
					this.errorCode = String.valueOf(ErrorCode.USER_PWD_ERROR);
					break;
				} else if ("login_success".equals(echo)) { //登录成功
					logger.debug("login {}:{} with rdp protocol success, waitting for send command. ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("waitting for send command to {}:{} timeout. ", this.ip, this.port);
						this.errorCode = String.valueOf(ErrorCode.RDP_EXECUTE_TIMEOUT);
						break;
					}
				} else if ("send_keys".equals(echo)) { //正在发送指令
					logger.debug("send command to {}:{}, waitting for send result echo. ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("waitting for send command result from {}:{} timeout. ", this.ip, this.port);
						this.errorCode = String.valueOf(ErrorCode.RDP_EXECUTE_TIMEOUT);
						break;
					}
				} else if ("script_running".equals(echo)) { //脚本执行中
					logger.debug("script is running, start read file length thread ...");
					break;
				} else if ("script_error".equals(echo)) { //脚本错误
					logger.error("execute rdp command error, {}:{}", this.ip, this.port);
					this.errorCode = String.valueOf(ErrorCode.RDP_SCRIPT_ERROR);
					break;
				} else { //未知错误
					logger.error("execute rdp unknow error, {}:{}, state {}", new Object[]{ this.ip, this.port, echo });
					this.errorCode = String.valueOf(ErrorCode.RDP_UNKNOW_ERROR);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return process;
	}
	
	/**
	 * Definition:读取回显
	 * @param is
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private boolean isTimeoutFlag = true;
	private String readRdpLog(InputStream is) {
		final StringBuffer sb = new StringBuffer();
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					String str = null;
					if ((str = br.readLine()) != null) {
						sb.append(str).append("\n");
					}
					isTimeoutFlag = false;
				} catch (Exception ex) {
				}
			}
		};
		try {
			t.start();
			t.join(this.echoTimeout);
			if (this.isTimeoutFlag) {
				logger.debug("receive echo timeout in {} ms, reveived : \r\n{}", this.echoTimeout, sb.toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.debug("receive echo from rdesktop process : \r\n{}", sb.toString());
		return sb.toString();
	}
	
	/**
	 * Definition:监听结果文件直至文件不再写入或超时退出，读取执行结果
	 * @param workspacePath
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-30
	 */
	private String readResult(String workspacePath) {
		long begin = System.currentTimeMillis();
		File resultFile = new File(workspacePath.concat(File.separator).concat(this.EXECUTE_RESULT_NAME));
		Process fuserProcess = null;
		String echo = "";
		while (true) {
			if (System.currentTimeMillis() - begin >= this.echoTimeout && !resultFile.exists()) {
	    		logger.debug("read {}:{} rdp cmd result file time out. ", this.ip, this.port);
				this.errorCode = String.valueOf(ErrorCode.GET_RESULT_FILE_TIMEOUT);
				break;
			}
			if (resultFile.exists()) {
				try {
					fuserProcess = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", this.getFuserCmd(resultFile.getAbsolutePath()) });
					echo = this.readRdpLog(fuserProcess.getInputStream());
					fuserProcess.destroy();
					if (echo != null && !"".equals(echo.trim())) {
						logger.debug("script.result is always writting, file size {}, sleep 5s... processId is {}", resultFile.length(), echo);
						Thread.sleep(5000);
					} else {
						break;
					}
				} catch (Exception e) {
				}
			}
		}
		if (resultFile.length() == 0) {
			logger.error("execute rdp command error, result file size is 0. ");
			this.errorCode = String.valueOf(ErrorCode.READ_RDP_RESULT_ERROR);
		}
		try {
			return CommonUtils.readTxtFileContent(new FileInputStream(resultFile), this.encode);
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.errorCode = String.valueOf(ErrorCode.READ_RDP_RESULT_ERROR);
		}
		return null;
	}
	
	/**
	 * Definition:RDP连接测试
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2017-1-18
	 */
	public boolean testRdpConnection() throws ProtocolException {
		logger.info("test connect to [{}:{}] with rdp protocol, connect timeout [{}]", new Object[] { ip, port, this.connectTimeout });
		Process process = this.executeRdpCmd(null); //执行rdp命令
		process = this.checkProcessForTest(process); //检查执行结果
		this.close(process, null);
		return true;
	}
	
	/**
	 * Definition:rdp连接拨测，查看执行结果
	 * @param process
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2017-1-18
	 */
	private Process checkProcessForTest(Process process) throws ProtocolException {
		String echo = this.readRdpLog(process.getInputStream());
		Matcher matcher = digPattern.matcher(echo);
		if (!matcher.find()) {
			logger.error("can`t find rdesktop process id, please check rdesktop tool envionment.");
			throw new ProtocolException(String.valueOf(ErrorCode.EXEC_RDP_MODE_ERROR));
		}
		String processId = matcher.group();
	    logger.debug("rdesktop process id : {}", processId);
	    int reconnCount = 1;
	    long start = System.currentTimeMillis();
	    while(true) {
		    try {
		    	try { Thread.sleep(2000); } catch (InterruptedException e) {}
				//执行kill指令，发送信号
				Process killProcess = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", this.getKillCmd(processId) });
				echo = this.readRdpLog(process.getInputStream());
		        matcher = statePattern.matcher(echo);
		        try { killProcess.destroy(); } catch (Exception e) {}
				if (!matcher.find()) { //连接超时进程自动停止
					logger.debug("get rdesktop process state faild.");
					throw new ProtocolException(String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT));
				}
				echo = matcher.group().toLowerCase();
			    logger.debug("rdp process current state : {}", echo);
				if ("start_conn".equals(echo)) { //开始连接
					logger.debug("start connect to {}:{} with rdp protocol... ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("connecting {}:{} with rdp protocol timeout ", this.ip, this.port);
						throw new ProtocolException(String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT));
					}
				} else if ("conn_error".equals(echo)) { //网络连接错误
					logger.error("connection {}:{} with rdp protocol fail. ", this.ip, this.port);
					throw new ProtocolException(String.valueOf(ErrorCode.CON_RES_CONNECTION));
				} else if ("reconnect".equals(echo)) { //重连
					if (reconnCount >= 3) {
						logger.error("reconnect {}:{} three times with rdp protocol fail. ", this.ip, this.port);
						throw new ProtocolException(String.valueOf(ErrorCode.CON_RES_CONNECTION));
					}
					reconnCount++;
					logger.debug("reconnect {}:{} with rdp protocol, times {} ", new Object[] { this.ip, this.port, reconnCount });
					process.destroy();
					process = this.executeRdpCmd(null);
					echo = this.readRdpLog(process.getInputStream());
					matcher = digPattern.matcher(echo);
					if (matcher.find()) {
						processId = matcher.group();
					    logger.debug("rdesktop reconnect process id : {}", processId);
					    start = System.currentTimeMillis();
					} else {
						logger.error("get rdesktop reconnect process id faild.");
						throw new ProtocolException(String.valueOf(ErrorCode.EXEC_RDP_MODE_ERROR));
					}
				} else if ("connecting".equals(echo)) { //正在连接
					logger.debug("connecting {}:{} with rdp protocol... ", this.ip, this.port);
					if (System.currentTimeMillis() - start >= this.connectTimeout) {
			    		logger.error("connecting {}:{} with rdp protocol timeout ", this.ip, this.port);
						throw new ProtocolException(String.valueOf(ErrorCode.RDP_CONNECT_TIMEOUT));
					}
				} else if ("login_error".equals(echo)) { //用户名密码错误
					logger.error("login {}:{} with rdp protocol fail. ", this.ip, this.port);
					throw new ProtocolException(String.valueOf(ErrorCode.USER_PWD_ERROR));
				} else if ("login_success".equals(echo)) { //登录成功
					logger.debug("login {}:{} with rdp protocol success, waitting for send command. ", this.ip, this.port);
					break;
				} else if ("send_keys".equals(echo)) { //正在发送指令
					logger.debug("send command to {}:{}, waitting for send result echo. ", this.ip, this.port);
					break;
				} else if ("script_running".equals(echo)) { //脚本执行中
					logger.debug("script is running, start read file length thread ...");
					break;
				} else if ("script_error".equals(echo)) { //脚本错误
					logger.error("execute rdp command error, {}:{}", this.ip, this.port);
					break;
				} else { //未知错误
					logger.error("execute rdp unknow error, {}:{}, state {}", new Object[]{ this.ip, this.port, echo });
					throw new ProtocolException(String.valueOf(ErrorCode.RDP_UNKNOW_ERROR));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return process;
	}
}
