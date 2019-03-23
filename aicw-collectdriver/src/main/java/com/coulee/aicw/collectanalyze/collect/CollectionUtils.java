package com.coulee.aicw.collectanalyze.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.driver.Driver;
import com.coulee.aicw.collectanalyze.collect.driver.DriverFactory;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.exception.ParserException;
import com.coulee.aicw.collectanalyze.collect.parser.Parser;
import com.coulee.aicw.collectanalyze.collect.parser.ParserFactory;
import com.coulee.aicw.collectanalyze.collect.protocol.JdbcProtocol;
import com.coulee.aicw.collectanalyze.collect.protocol.Protocol;
import com.coulee.aicw.collectanalyze.collect.protocol.ProtocolFactory;
import com.coulee.aicw.collectanalyze.collect.valueobject.ConnectionArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.LoginArg;
import com.coulee.aicw.foundations.entity.Message;

/**
 * Description:操作工具类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class CollectionUtils {
	
	private static CollectionUtils collectionUtils;

	private CollectionUtils() {
	}
	
	/**
	 * Definition: 实例化工具类
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public static CollectionUtils instance() {
		if (collectionUtils == null) {
			collectionUtils = new CollectionUtils();
		}
		return collectionUtils;
	}
	
	/**
	 * Definition:登录非数据库设备
	 * @param conArg 登录对象
	 * @param switchCommand 目标设备切特权指令
	 * @param moreExecuter 目标设备翻页指令
	 * @param morePrompt 目标设备翻页提示符
	 * @return
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public Driver login(ConnectionArg conArg, String switchCommand, String moreExecuter, String morePrompt) throws DriverException {
		switchCommand = null2String(switchCommand);
		moreExecuter = null2String(moreExecuter);
		morePrompt = null2String(morePrompt);
		List<ConnectionArg> list = conArg.getJumps();
		if (list == null || list.isEmpty()) { //非跳转登录
			Protocol protocol = ProtocolFactory.createProtocol(conArg.getOpenArg().getProtocol());
			if (protocol == null) {
				throw new DriverException(ErrorCode.UNSUPPORT_PROTOCOL + "");
			}
			protocol.setMoreExecuter(moreExecuter);
			protocol.setMorePrompt(morePrompt);
			Driver driver = DriverFactory.getDriver(conArg.getDriverClass());
			if (driver == null) {
				throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
			}
			driver.setProtocol(protocol);
			this.checkIsSwitch(conArg.getLoginArg(), switchCommand);
			driver.login(conArg.getOpenArg(), conArg.getLoginArg());
			return driver;
		} else {
			list.add(conArg); //将目标机信息放在列表最后进行登录操作
			ConnectionArg first = list.get(0); //取第一个跳转机信息
			Protocol protocol = ProtocolFactory.createProtocol(first.getOpenArg().getProtocol());
			if (protocol == null) {
				throw new DriverException(ErrorCode.UNSUPPORT_PROTOCOL + "");
			}
			protocol.setMoreExecuter(moreExecuter);
			protocol.setMorePrompt(morePrompt);
			Driver driver = DriverFactory.getDriver(first.getDriverClass());
			if (driver == null) {
				throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
			}
			driver.setProtocol(protocol);
			boolean islogin = driver.login(first.getOpenArg(), first.getLoginArg()); //登录第一台跳转机
			if (islogin) {
				for (int i = 1; i < list.size(); i++) { //循环登录至目标机
					ConnectionArg arg = list.get(i);
					if (i == (list.size() - 1)) { //如果为目标设备，则检测是否需切换特权
						this.checkIsSwitch(arg.getLoginArg(), switchCommand);
					}
					Driver d = DriverFactory.getDriver(arg.getDriverClass());
					if (d == null) {
						throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
					}
					d.setProtocol(protocol);
					d.doJumpLogin(arg.getOpenArg(), arg.getLoginArg(), (i == (list.size() - 1)));
				}
			}
			return driver;
		}
	}
	
	/**
	 * Definition:检测是否需要切换至特权模式
	 * @param loginArg
	 * @param switchCommand
	 * @Author: LanChao
	 * @Created date: 2016-12-17
	 */
	private void checkIsSwitch(LoginArg loginArg, String switchCommand) {
		boolean isSwitch = true;
		if (loginArg.isSudoMode()) { //如果是sudo模式则不需切换
			isSwitch = false;
		} else {
			String adminUsername = loginArg.getAdminUsername();
			String adminPassword = loginArg.getAdminPassword();
			if (StringUtils.isNotEmpty(adminUsername)) { //如果存在特权用户且特权用户与普通用户一致则不需切换
				if (adminUsername.equals(loginArg.getUsername())) {
					isSwitch = false;
				}
			} else { //如果不存在特权用户切特权密码为空则不需切换
				if (StringUtils.isEmpty(adminPassword)) {
					isSwitch = false;
				}
			}
		}
		if (isSwitch) {
			loginArg.setSwitch(true);
			loginArg.setSwitchCommand(switchCommand);
		}
	}
	
	/**
	 * Definition:空字符串转为""
	 * @param str
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-17
	 */
	private String null2String(String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0 || "null".equals(str)) {
			return "";
		}
		return str;
	}
	
	/**
	 * Definition:登录数据库设备
	 * @param loginArg 登录对象
	 * @return
	 * @throws DriverException
	 * @Author: LanChao
	 * @Created date: 2016-12-16
	 */
	public Driver loginDB(ConnectionArg loginArg) throws DriverException {
		JdbcProtocol jdbcProtocol = ProtocolFactory.createJdbcProtocol();
		Driver driver = DriverFactory.getDBDriver();
		driver.setJdbcProtocol(jdbcProtocol);
		driver.login(loginArg.getDatabaseOpenArg(), loginArg.getDatabaseLoginArg());
		return driver;
	}
	
	/**
	 * Definition:解析并格式化
	 * @param original 原始内容
	 * @param parserType 解析类型
	 * @param regexOrScript 正则或解析脚本内容
	 * @param mapper 结果字段属性映射
	 * @return
	 * @throws ParserException
	 * @Author: LanChao
	 * @Created date: 2016-12-19
	 */
	public List<Map<String, String>> parseAndFormat(String original,
			String parserType, String regexOrScript, Map<String, String> mapper)
			throws ParserException {
		if (StringUtils.isEmpty(original) || StringUtils.isEmpty(parserType)) { //如果原始结果为空或者未设置解析方式
			return null;
		}
		Parser parser = ParserFactory.createParser(parserType);
		List<Map<String, String>> list = parser.parse(original, regexOrScript);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (mapper == null || mapper.isEmpty()) {
			return list;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			Map<String, String> newMap = new HashMap<String, String>();
			for (String ori : map.keySet()) {
				if (mapper.get(ori) != null) {
					newMap.put(mapper.get(ori), map.get(ori));
				} else {
					newMap.put(ori, map.get(ori));
				}
			}
			list.set(i, newMap);
		}
		return list;
	}
	/**
	 * add zyj
	 * @param conArg
	 * @param switchCommand
	 * @param moreExecuter
	 * @param morePrompt
	 * @return
	 * @throws DriverException
	 */
	public Message doLogin(ConnectionArg conArg, String switchCommand, String moreExecuter, String morePrompt) {
		Message mesVo = new Message();
		switchCommand = null2String(switchCommand);
		moreExecuter = null2String(moreExecuter);
		morePrompt = null2String(morePrompt);
		List<ConnectionArg> list = conArg.getJumps();
		try {
			if (list == null || list.isEmpty()) { // 非跳转登录
				Protocol protocol = ProtocolFactory.createProtocol(conArg.getOpenArg().getProtocol());
				if (protocol == null) {
					return Message.newFailureMessage(ErrorCode.msg(ErrorCode.UNSUPPORT_PROTOCOL+""));
					// throw new DriverException(ErrorCode.UNSUPPORT_PROTOCOL + "");
				}
				protocol.setMoreExecuter(moreExecuter);
				protocol.setMorePrompt(morePrompt);
				Driver driver = DriverFactory.getDriver(conArg.getDriverClass());
				if (driver == null) {
					return Message.newFailureMessage(ErrorCode.msg(ErrorCode.UNSUPPORT_DRIVER_ERROR+""));
					// throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
				}
				driver.setProtocol(protocol);
				this.checkIsSwitch(conArg.getLoginArg(), switchCommand);
				boolean isLogin = driver.login(conArg.getOpenArg(), conArg.getLoginArg());
				if (isLogin) {
					mesVo.setObj(driver);
					return mesVo;
				} else {
					return Message.newFailureMessage("登录失败");
				}

			} else {
				list.add(conArg); // 将目标机信息放在列表最后进行登录操作
				ConnectionArg first = list.get(0); // 取第一个跳转机信息
				Protocol protocol = ProtocolFactory.createProtocol(first.getOpenArg().getProtocol());
				if (protocol == null) {
					// throw new DriverException(ErrorCode.UNSUPPORT_PROTOCOL + "");
					return Message.newFailureMessage(ErrorCode.msg(ErrorCode.UNSUPPORT_PROTOCOL+""));
				}
				protocol.setMoreExecuter(moreExecuter);
				protocol.setMorePrompt(morePrompt);
				Driver driver = DriverFactory.getDriver(first.getDriverClass());
				if (driver == null) {
					// throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
					return Message.newFailureMessage(ErrorCode.msg(ErrorCode.UNSUPPORT_DRIVER_ERROR+""));
				}
				driver.setProtocol(protocol);
				boolean islogin = driver.login(first.getOpenArg(), first.getLoginArg()); // 登录第一台跳转机
				if (islogin) {
					for (int i = 1; i < list.size(); i++) { // 循环登录至目标机
						ConnectionArg arg = list.get(i);
						if (i == (list.size() - 1)) { // 如果为目标设备，则检测是否需切换特权
							this.checkIsSwitch(arg.getLoginArg(), switchCommand);
						}
						Driver d = DriverFactory.getDriver(arg.getDriverClass());
						if (d == null) {
							// throw new DriverException(ErrorCode.UNSUPPORT_DRIVER_ERROR + "");
							return Message.newFailureMessage(ErrorCode.msg(ErrorCode.UNSUPPORT_DRIVER_ERROR+""));
						}
						d.setProtocol(protocol);
						d.doJumpLogin(arg.getOpenArg(), arg.getLoginArg(), (i == (list.size() - 1)));
					}
				} else {
					return Message.newFailureMessage("登录失败");
				}

				mesVo.setObj(driver);
				return mesVo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.newFailureMessage("登录失败:" + ErrorCode.msg(e.getMessage()));
		}
	}
}
