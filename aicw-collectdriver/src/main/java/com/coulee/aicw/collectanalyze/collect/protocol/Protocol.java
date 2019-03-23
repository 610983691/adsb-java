package com.coulee.aicw.collectanalyze.collect.protocol;

import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;

/**
 * Description:连接协议接口 
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-3
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface Protocol {
	
	/**
	 * 缺省字符集
	 */
	public static final String DEFAULT_ECHO_ENCODE = "UTF-8";
	
	/**
	 * 连接超时时间，单位为秒
	 */
	public static final int CONNECT_TIME_OUT = 30;
	
	/**
	 * 读取回显默认时间，单位为秒
	 */
	public static final int ECHO_TIME_OUT = 60;
	
	/**
	 * 执行命令后等待时间，单位为毫秒
	 */
	public static final long EXEC_WAIT_TIME = 500L;
	
	/**
	 * 回车
	 */
	public static final String CR = "\r";
	
	/**
	 * 换行
	 */
	public static final String LF = "\n";
	
	/**
	 * 回车换行
	 */
	public static final String CRLF = "\r\n";
	
	/**
	 * 空格
	 */
	public static final String SPACE = " ";
	
	/**
	 * 默认的More提示符正则
	 */
	public static final String[] DEFAULT_MORE = new String[] { "<?-{2,4} ?\\(? ?[Mm][Oo][Rr][Ee]( ?\\d{1,3}%)? ?\\)? ?-{2,4}>?" };
	
	/**
	 * 默认执行More翻页指令
	 */
	public static final String DEFAULT_MORE_EXECUTER = SPACE;
	
	/**
	 * Definition:获取编码
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public String getEncode();

	/**
	 * Definition:设置编码
	 * @param encode
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void setEncode(String encode);
	
	/**
	 * Definition:获取More提示符
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public String[] getMorePrompt();
	
	/**
	 * Definition:设置More提示符
	 * @param morePrompt
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public void setMorePrompt(String... morePrompt);
	
	/**
	 * Definition:获取More执行命令
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public String getMoreExecuter();
	
	/**
	 * Definition:设置More执行命令
	 * @param cmd
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public void setMoreExecuter(String cmd);
	
	/**
	 * Definition:判断回显是否为More翻页情况
	 * @param echo
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	public boolean isMore(String echo);
	
	
	
	/**
	 * Definition: 与目标网络设备建立网络连接
	 * @param ip IP地址
	 * @param port 端口
	 * @param connectTimeout 连接超时时间，单位：秒
	 * @param echoTimeout 读取回显超时时间，单位：秒
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void connect(String ip, int port, int connectTimeout, int echoTimeout) throws ProtocolException;

	/**
	 * Definition: 登录目标设备
	 * @param username 用户名
	 * @param password 密码
	 * @param defaultPrompts 默认提示符
	 * @return 
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public String authentication(String username, String password, String[] defaultPrompts) throws ProtocolException;

	/**
	 * Definition:执行命令
	 * @param cmd 命令
	 * @param cmdExeSign 命令执行符号
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void sendCommand(String cmd, String cmdExeSign) throws ProtocolException;
	
	/**
	 * Definition:发送密码
	 * @param password 密码
	 * @param cmdExeSign
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-13
	 */
	public void sendPassword(String password, String cmdExeSign) throws ProtocolException;

	/**
	 * Definition:获取命令执行回显
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public String readEcho() throws ProtocolException;

	/**
	 * Definition:读取回显至冒号提示符
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public String read2ColonEcho() throws ProtocolException;

	/**
	 * Definition:读取命令回显至指定提示符
	 * @param cmd 执行的命令
	 * @param keywords 提示符
	 * @return
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public String read2KeyWordEcho(String cmd, String... keywords) throws ProtocolException;

	/**
	 * Definition:关闭所有连接
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2016-12-3
	 */
	public void close() throws ProtocolException;

}
