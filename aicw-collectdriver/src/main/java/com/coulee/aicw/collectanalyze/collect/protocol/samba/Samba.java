package com.coulee.aicw.collectanalyze.collect.protocol.samba;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.exception.ProtocolException;
import com.coulee.aicw.collectanalyze.collect.valueobject.SambaConfig;

/**
 * Description:SAMBA协议类
 * Copyright (C) 2017 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2017-1-18
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class Samba {
	
	private static Pattern pattern = Pattern.compile("\\.exe|\\.bat");
	
	/**
	 * Definition:判断原始命令是否需要使用samba协议来传输命令依赖文件
	 * @param oriCmd
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-1-18
	 */
	public static boolean isSambaCmd(String oriCmd) {
		if (oriCmd == null || "".equals(oriCmd)) {
			return false;
		}
		Matcher matcher = pattern.matcher(oriCmd);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Definition:验证Samba配置
	 * @param config
	 * @throws ProtocolException
	 * @Author: LanChao
	 * @Created date: 2017-1-19
	 */
	private static void checkConfig(SambaConfig config) throws ProtocolException {
		if (config == null) {
			throw new ProtocolException(String.valueOf(ErrorCode.SAMBA_CONFIG_ERROR));
		}
		if (StringUtils.isEmpty(config.getSambaIp()) || StringUtils.isEmpty(config.getSambaUsername())
				|| StringUtils.isEmpty(config.getSambaPassword()) || StringUtils.isEmpty(config.getSharePathAlias())) {
			throw new ProtocolException(String.valueOf(ErrorCode.SAMBA_CONFIG_ERROR));
		}
	}
	
	/**
	 * Definition:构造samba执行命令
	 * @param oriCmd 待执行的原始命令
	 * @param subPath 共享路径下级路径，如不存在传空
	 * @return
	 * @throws ProtocolException 
	 * @Author: LanChao
	 * @Created date: 2017-1-18
	 */
	public static String buildSambaCmd(String oriCmd, SambaConfig sambaConfig, String subPath) throws ProtocolException {
//		net use l: \\192.168.1.16\aicwshare "123456" /user:"username"
//		set aicw_temp=c:\\aicw_script_temp
//		mkdir %aicw_temp%
//		xcopy "l:\\VULN" %aicw_temp% /E /Y /Q
//		cd /d %aicw_temp%
//		vf_startup.exe > tmp.txt
//		echo.
//		type result.txt
//		cd ..
//		rd %aicw_temp% /Q /S
//		net use l: /delete /y
		if (oriCmd == null || "".equals(oriCmd)) {
			return oriCmd;
		}
		checkConfig(sambaConfig);
		StringBuffer sb = new StringBuffer();
		sb.append("net use l: \\\\").append(sambaConfig.getSambaIp()).append("\\").append(sambaConfig.getSharePathAlias())
		.append(" \"").append(sambaConfig.getSambaPassword()).append("\" /user:\"").append(sambaConfig.getSambaUsername()).append("\"\r\n");
		sb.append("set aicw_temp=c:\\aicw_script_temp").append("\r\n");
		sb.append("mkdir %aicw_temp%").append("\r\n");
		sb.append("xcopy \"l:\\").append(subPath == null ? "" : subPath).append("\" %aicw_temp% /E /Y /Q").append("\r\n");
		sb.append("cd /d %aicw_temp%").append("\r\n");
		sb.append(oriCmd);
		sb.append("\r\n");
		sb.append("cd ..").append("\r\n");
		sb.append("rd %aicw_temp% /Q /S").append("\r\n");
		sb.append("net use l: /delete /y").append("\r\n");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		SambaConfig config = new SambaConfig("192.168.1.16", "lanchao", "123456", "aicwshare");
		String oriCmd = "vf_startup.exe > tmp.txt\r\necho.\r\ntype result.txt\r\n";
		System.out.println(Samba.isSambaCmd(oriCmd));
		try {
			System.out.println(Samba.buildSambaCmd(oriCmd, config, "VULN"));
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
