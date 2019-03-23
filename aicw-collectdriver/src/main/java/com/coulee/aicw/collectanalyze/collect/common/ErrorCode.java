package com.coulee.aicw.collectanalyze.collect.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Description:错误码定义
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-2
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ErrorCode {

	/**
	 * 错误码及错误消息、解决方案映射表
	 */
	private static Map<Integer, String[]> CODE_MSG_MAP = new HashMap<Integer, String[]>();
	
	//ssh telnet protocol
	public static final int CON_RES = 1001;
	public static final int CON_RES_CONNECTION = 1002;
	public static final int CON_RES_AUTHENTICATION = 1003;
	public static final int PARAMS_NULL = 1004;
	public static final int UNSUPPORT_AUTH_MODE = 1005;
	public static final int LOGIN_PASSWORD_EXPIRED = 1006;
	public static final int SESSION_NOT_CREATE = 1007;
	public static final int SEND_COMMAND_EXCEPTION = 1008;
	public static final int USER_PWD_ERROR = 1009;
	public static final int RECEIVE_ECHO_ERROR = 1010;
	public static final int RECEIVE_ECHO_ENCODE_ERROR = 1011;
	public static final int RECEIVE_ECHO_TIMEOUT = 1012;
	public static final int USER_PROMPT_ERROR = 1013;
	public static final int All_USER_INTERFACES_ARE_USED = 1014;
	public static final int USRENAME_PASSWORD_PARAMS_NULL = 1015;
	
	//jdbc protocol
	public static final int UNSUPPORT_DATABASE_TYPE = 1016;
	public static final int PARAMS_NULL_DRIVERCLASS = 1017;
	public static final int PARAMS_NULL_DBNAME = 1018;
	public static final int PARAMS_NULL_DBSERVER = 1019;
	public static final int EXECUTE_SQL_ERROR = 1020;
	public static final int EXECUTE_QUERY_SQL_ERROR = 1021;
	public static final int UNAUTHENTICATION_CONNECTION = 1022;
	
	//rdp protocol
	public static final int EXECUTE_CMD_NULL = 1023;
	public static final int BUILD_SCRIPT_FILE_ERROR = 1024;
	public static final int EXEC_RDP_MODE_ERROR = 1025;
	public static final int READ_RDP_RESULT_ERROR = 1026;
	public static final int RDP_SCRIPT_ERROR = 1027;
	public static final int RDP_CONNECT_TIMEOUT = 1028;
	public static final int RDP_UNKNOW_ERROR = 1029;
	public static final int RDP_EXECUTE_TIMEOUT = 1030;
	public static final int GET_RESULT_FILE_TIMEOUT = 1031;
	
	//samba protocol
	public static final int SAMBA_CONFIG_ERROR = 1032;
	
	//driver
	public static final int UNSUPPORT_JUMP_PROTOCOL = 2001;
	public static final int SU_USER_NOT_EXIST = 2002;
	public static final int SU_PROMPT_ERROR = 2003;
	public static final int JUMP_LOGIN_CONNECTION_ERROR = 2004;
	public static final int JUMP_LOGIN_RECEIVE_ECHO_ERROR = 2005;
	public static final int JUMP_LOGIN_USER_PWD_ERROR = 2006;
	public static final int UNSUPPORT_DRIVER_ERROR = 2007;
	public static final int RSA_HOST_KEY_ERROR = 2008;
	public static final int UNSUPPORT_JUMPLOGIN_ERROR = 2009;
	public static final int SQL_SCRIPT_TYPE_ERROR = 2010;
	public static final int UNSUPPORT_PROTOCOL = 2011;
	public static final int CONNECT_IP_PORT_NULL = 2012;
	public static final int LOGIN_NAME_PASSWORD_NULL = 2013;
	public static final int LOGIN_PROMPT_NULL = 2014;
	public static final int ADMIN_LOGIN_PASSWORD_NULL = 2015;
	public static final int ADMIN_LOGIN_PROMPT_NULL = 2016;
	public static final int CONNECT_DRIVERCLASS_NULL = 2017;
	public static final int CONNECT_DBNAME_NULL = 2018;
	
	//executer
	public static final int NO_SUPPORT_EXECUTER = 3001;
	public static final int EXTEND_INSTANCE_ERROR = 3002;
	public static final int EXECUTE_SCRIPT_NULL = 3003;
	public static final int SCRIPT_ARGS_NOT_MATCHED = 3004;
	public static final int PROTOCOL_NULL = 3005;
	public static final int JDBC_PROTOCOL_NULL = 3006;
	
	//parser
	public static final int REGEX_PARSER_ERROR = 4001;
	public static final int SCRIPT_PARSER_INSTANCE_ERROR = 4002;
	public static final int SCRIPT_PARSER_CODE_NULL = 4003;
	public static final int SCRIPT_PARSER_REGEX_NULL = 4004;
	
	//service
	public static final int DEVICE_TYPE_CONFIGINFO_ERROR = 5001;
	public static final int JUMPER_DEVICE_TYPE_CONFIGINFO_ERROR = 5002;
	public static final int POLICY_NULL_ERROR = 5003;
	public static final int SCRIPT_NULL_ERROR = 5004;
	public static final int RDP_UNSUPPORT_SCRIPT_TYPE = 5005;
	public static final int BELONG_DEVICE_TYPE_CONFIGINFO_ERROR = 5006;
	
	static {
		CODE_MSG_MAP.put(CON_RES, new String[] { "打开资源连接失败！！", "请检查设备IP、端口、连接协议是否正确" });
		CODE_MSG_MAP.put(CON_RES_CONNECTION, new String[] { "打开资源连接失败，传输层异常！！", "请检查设备IP、端口、连接协议是否正确" });
		CODE_MSG_MAP.put(CON_RES_AUTHENTICATION, new String[] { "登录资源失败,认证失败！！", "请检查设备用户名密码配置是否正确" });
		CODE_MSG_MAP.put(PARAMS_NULL, new String[] { "参数为null！！", "请检查所传参数是否完整" });
		CODE_MSG_MAP.put(UNSUPPORT_AUTH_MODE, new String[] { "不支持的认证方式！！", "不支持该认证方式，请修改设备配置" });
		CODE_MSG_MAP.put(LOGIN_PASSWORD_EXPIRED, new String[] { "登录密码过期！！", "请修改设备及本系统中该设备的登录密码" });
		CODE_MSG_MAP.put(SESSION_NOT_CREATE, new String[] { "会话未建立！！", "未成功建立会话连接，请重试" });
		CODE_MSG_MAP.put(SEND_COMMAND_EXCEPTION, new String[] { "发送命令异常！！", "会话连接已断开，请重试" });
		CODE_MSG_MAP.put(USER_PWD_ERROR, new String[] { "用户密码错误！！", "请检查用户名、密码是否正确设置" });
		CODE_MSG_MAP.put(RECEIVE_ECHO_ERROR, new String[] { "接收回显异常！！", "请检查提示符配置是否正确" });
		CODE_MSG_MAP.put(RECEIVE_ECHO_ENCODE_ERROR, new String[] { "回显编码转换异常！！", "请检查设备配置的编码是否合法" });
		CODE_MSG_MAP.put(RECEIVE_ECHO_TIMEOUT, new String[] { "获取回显超时！！", "请检查提示符配置是否正确" });
		CODE_MSG_MAP.put(USER_PROMPT_ERROR, new String[] { "用户提示符错误！！", "请检查提示符配置是否正确" });
		CODE_MSG_MAP.put(All_USER_INTERFACES_ARE_USED, new String[] { "无空闲连接！！", "设备连接数已满，请更改设备配置" });
		CODE_MSG_MAP.put(USRENAME_PASSWORD_PARAMS_NULL, new String[] { "用户名或密码为空！！", "请检查用户名密码是否完整" });
		
		CODE_MSG_MAP.put(UNSUPPORT_DATABASE_TYPE, new String[] { "不支持的数据库类型！！", "请检查所配置的设备类型是否正确" });
		CODE_MSG_MAP.put(PARAMS_NULL_DRIVERCLASS, new String[] { "数据库驱动为空！！", "请检查所配置的数据库驱动信息是否完善" });
		CODE_MSG_MAP.put(PARAMS_NULL_DBNAME, new String[] { "数据库名称为空！！", "请检查所配置的数据库名称信息是否完善" });
		CODE_MSG_MAP.put(PARAMS_NULL_DBSERVER, new String[] { "Informix数据库服务名为空！！", "请检查所配置的数据库服务名称信息是否完善" });
		CODE_MSG_MAP.put(EXECUTE_SQL_ERROR, new String[] { "执行SQL语句失败！！", "请检查所配置的SQL执行语句是否合法" });
		CODE_MSG_MAP.put(EXECUTE_QUERY_SQL_ERROR, new String[] { "执行查询SQL语句失败！！", "请检查所配置的SQL查询语句是否合法" });
		CODE_MSG_MAP.put(UNAUTHENTICATION_CONNECTION, new String[] { "未验证登录的数据库连接！！", "未登录该数据库，请检查相关代码" });
		
		CODE_MSG_MAP.put(EXECUTE_CMD_NULL, new String[] { "待执行脚本为空！！", "请检查相关代码" });
		CODE_MSG_MAP.put(BUILD_SCRIPT_FILE_ERROR, new String[] { "创建脚本文件错误！！", "请检查用户权限或磁盘空间是否充足" });
		CODE_MSG_MAP.put(EXEC_RDP_MODE_ERROR, new String[] { "执行RDP命令错误！！", "请检查rdesktop工具安装环境是否正常" });
		CODE_MSG_MAP.put(READ_RDP_RESULT_ERROR, new String[] { "读取RDP命令结果错误！！", "请检查RDP命令是否存在输出内容" });
		CODE_MSG_MAP.put(RDP_SCRIPT_ERROR, new String[] { "执行RDP脚本错误！！", "请检查执行的脚本内容" });
		CODE_MSG_MAP.put(RDP_CONNECT_TIMEOUT, new String[] { "RDP连接超时！！", "请检查设备IP、端口、用户名及密码配置是否正确" });
		CODE_MSG_MAP.put(RDP_UNKNOW_ERROR, new String[] { "RDP未知错误！！", "请尝试手动执行命令进行问题排查" });
		CODE_MSG_MAP.put(RDP_EXECUTE_TIMEOUT, new String[] { "RDP执行命令超时！！", "请尝试手动执行命令进行问题排查" });
		CODE_MSG_MAP.put(GET_RESULT_FILE_TIMEOUT, new String[] { "获取RDP执行结果文件超时！！", "请尝试手动执行命令进行问题排查" });
		
		CODE_MSG_MAP.put(SAMBA_CONFIG_ERROR, new String[] { "Samba服务配置信息错误！！", "请检查Samba配置信息是否正确" });
		
		CODE_MSG_MAP.put(UNSUPPORT_JUMP_PROTOCOL, new String[] { "不支持的跳转协议！！", "请检查跳转机协议配置，目前支持SSH1、SSH2及TELNET协议" });
		CODE_MSG_MAP.put(SU_USER_NOT_EXIST, new String[] { "特权用户不存在！！", "请检查设备特权用户配置是否正确" });
		CODE_MSG_MAP.put(SU_PROMPT_ERROR, new String[] { "特权提示符不正确！！", "请检查设备特权提示符配置是否正确" });
		CODE_MSG_MAP.put(JUMP_LOGIN_CONNECTION_ERROR, new String[] { "连接跳转机异常！！", "请检查跳转机登录信息配置是否正确，请检查设备网络是否可达" });
		CODE_MSG_MAP.put(JUMP_LOGIN_RECEIVE_ECHO_ERROR, new String[] { "连接跳转机接收回显异常！！", "请检查跳转机提示符配置是否正确" });
		CODE_MSG_MAP.put(JUMP_LOGIN_USER_PWD_ERROR, new String[] { "连接跳转机用户密码错误！！", "请检查跳转机用户名密码配置是否正确" });
		CODE_MSG_MAP.put(UNSUPPORT_DRIVER_ERROR, new String[] { "不支持的驱动类型！！", "请检查该设备类型所绑定的驱动类是否正确" });
		CODE_MSG_MAP.put(RSA_HOST_KEY_ERROR, new String[] { "SSH协议RSA公钥错误！！", "请在发起SSH命令设备上的known_hosts文件内删除目标设备的RSA公钥" });
		CODE_MSG_MAP.put(UNSUPPORT_JUMPLOGIN_ERROR, new String[] { "不支持跳转登录！！", "该设备类型不支持跳转登录，请检查登录参数设置" });
		CODE_MSG_MAP.put(SQL_SCRIPT_TYPE_ERROR, new String[] { "错误的脚本类型！！", "请检查策略对应的脚本类型是否为SQL脚本" });
		CODE_MSG_MAP.put(UNSUPPORT_PROTOCOL, new String[] { "不支持的连接协议！！", "请检查连接协议配置，目前支持SSH1、SSH2、WIN_SSH2及TELNET协议" });
		CODE_MSG_MAP.put(CONNECT_IP_PORT_NULL, new String[] { "IP或端口为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(LOGIN_NAME_PASSWORD_NULL, new String[] { "登录用户名或密码为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(LOGIN_PROMPT_NULL, new String[] { "登录提示符为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(ADMIN_LOGIN_PASSWORD_NULL, new String[] { "管理员登录密码为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(ADMIN_LOGIN_PROMPT_NULL, new String[] { "管理员登录提示符为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(CONNECT_DRIVERCLASS_NULL, new String[] { "数据库连接驱动类名为空！！", "请检查传入的连接参数是否正确" });
		CODE_MSG_MAP.put(CONNECT_DBNAME_NULL, new String[] { "数据库名为空！！", "请检查传入的连接参数是否正确" });

		CODE_MSG_MAP.put(NO_SUPPORT_EXECUTER, new String[] { "不支持此类脚本！！", "请检查该策略对应脚本类型是否合法" });
		CODE_MSG_MAP.put(EXTEND_INSTANCE_ERROR, new String[] { "扩展类实例化错误！！", "请检查扩展类全名是否正确，且该类是否处于采集引擎lib库内" });
		CODE_MSG_MAP.put(EXECUTE_SCRIPT_NULL, new String[] { "待执行的脚本为空！！", "请检查是否配置了对应的脚本内容" });
		CODE_MSG_MAP.put(SCRIPT_ARGS_NOT_MATCHED, new String[] { "脚本参数不匹配！！", "请检查脚本及其传入的参数数目是否匹配" });
		CODE_MSG_MAP.put(PROTOCOL_NULL, new String[] { "连接协议为空！！", "请检查设备连接参数是否正确" });
		CODE_MSG_MAP.put(JDBC_PROTOCOL_NULL, new String[] { "JDBC连接协议为空！！", "请检查设备数据库连接参数是否正确" });
		
		CODE_MSG_MAP.put(REGEX_PARSER_ERROR, new String[] { "正则表达式解析错误！！", "请检查采集策略内解析正则配置是否合法" });
		CODE_MSG_MAP.put(SCRIPT_PARSER_INSTANCE_ERROR, new String[] { "解析代码初始化错误！！", "请检查解析代码是否符合Java语法" });
		CODE_MSG_MAP.put(SCRIPT_PARSER_CODE_NULL, new String[] { "解析代码为空！！", "请配置Java语法的解析代码" });
		CODE_MSG_MAP.put(SCRIPT_PARSER_REGEX_NULL, new String[] { "解析正则为空！！", "请配置解析正则表达式" });
		
		CODE_MSG_MAP.put(DEVICE_TYPE_CONFIGINFO_ERROR, new String[] { "设备类型配置信息错误！！", "请检查该设备类型的配置信息是否正确" });
		CODE_MSG_MAP.put(JUMPER_DEVICE_TYPE_CONFIGINFO_ERROR, new String[] { "跳转机设备类型配置信息错误！！", "请检查该跳转机设备类型的配置信息是否正确" });
		CODE_MSG_MAP.put(POLICY_NULL_ERROR, new String[] { "采集策略为空或不完整！！", "请检查所要执行的采集策略是否存在于采集分析引擎系统内" });
		CODE_MSG_MAP.put(SCRIPT_NULL_ERROR, new String[] { "脚本为空或不完整！！", "请检查所要执行的采集策略脚本是否存在于采集分析引擎系统内" });
		CODE_MSG_MAP.put(RDP_UNSUPPORT_SCRIPT_TYPE, new String[] { "RDP协议不支持此类型脚本！！", "请检查所要执行的采集策略脚本是否为普通采集脚本" });
		CODE_MSG_MAP.put(BELONG_DEVICE_TYPE_CONFIGINFO_ERROR, new String[] { "宿主机设备类型配置信息错误！！", "请检查宿主机设备类型的配置信息是否正确" });
		
	}

	/**
	 * Definition:根据错误码获取错误信息
	 * @param errorCode
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-9
	 */
	public static String msg(String errorCode) {
		if (StringUtils.isNumeric(errorCode)) { //自定义错误码
			return CODE_MSG_MAP.get(Integer.valueOf(errorCode))[0];
		}
		//异常信息
		return errorCode.length() > 4000 ? errorCode.substring(0, 3900).concat("...") : errorCode;
	}
	
	/**
	 * Definition:根据错误码获取错误解决方案信息
	 * @param errorCode
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-17
	 */
	public static String solution(String errorCode) {
		if (StringUtils.isNumeric(errorCode)) { //自定义错误码
			return CODE_MSG_MAP.get(Integer.valueOf(errorCode))[1];
		}
		//异常信息
		return errorCode.length() > 4000 ? errorCode.substring(0, 3900).concat("...") : errorCode;
	}
}
