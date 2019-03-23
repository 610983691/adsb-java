package com.coulee.aicw.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.collectanalyze.collect.CollectionUtils;
import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.driver.Driver;
import com.coulee.aicw.collectanalyze.collect.driver.NetdeviceDriver;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ConnectionArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;
import com.coulee.aicw.dao.FirewallAssetEntityMapper;
import com.coulee.aicw.dao.FwOrderEntityMapper;
import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.FwOrderEntity;
import com.coulee.aicw.foundations.config.CryptoTools;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IOperateFirewallService;
import com.coulee.aicw.vo.OperateFirewallParam;

/**
 * 操控防火墙的接口类 Description: Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月27日 author：zyj
 * 
 * @version 1.0
 */
@Service
public class OperateFirewallServiceImpl extends AbstractBaseService implements IOperateFirewallService {
	private static Logger logger = LoggerFactory.getLogger(OperateFirewallServiceImpl.class);
	/**
	 * 加解密工具类
	 */
	@Autowired
	private CryptoTools cryptoTools;
	@Autowired
	private FirewallAssetEntityMapper fwMapper;
	@Autowired
	private FwOrderEntityMapper fwOrderMapper;

	@Autowired
	public FirewallAssetEntityMapper fwAssetEntityMapper;
	private static final String login_suc = "1";
	private static final String login_fail = "0";

	/**
	 * 暴露一个接口，根据防火墙ID，策略类型（增加、移除），外网IP，应用信息等参数，内部根据这些参数信息操作防火墙进行策略操作
	 * 
	 * 至于内部，可以根据配置，对不同类型防火墙生成不同的命令，然后登陆防火墙进行操作
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Message operateFw(OperateFirewallParam paramVo) {
		FirewallAssetEntity fwEntity = fwMapper.findById(paramVo.getFwId());
		/*
		 * 检查防火墙基础信息
		 */
		Message checkMes = this.checkFwInfo(fwEntity);
		if (!checkMes.isSuccess()) {
			/*
			 * 拨测操作记录 拨测结果
			 */
			if (paramVo.isTestLogin()) {
				saveTestLoginFailMsg(fwEntity, checkMes);
			}
			return checkMes;
		}

		/*
		 * 防火墙下发策略信息
		 */
		Message sendFwOrderMes = this.sendFwOrder(fwEntity, paramVo);
		return sendFwOrderMes;
	}

	private Message testFwLogin(FirewallAssetEntity fwEntity) {
		OperateFirewallParam param = new OperateFirewallParam();
		param.setFwId(fwEntity.getId());
		param.setTestLogin(true);
		ConnectionArg conArg = buildConnectParam(fwEntity, param);
		return doLoginFw(conArg);
	}

	/**
	 * 根据防火墙类型，命令类型 到 防火墙命令管理获取 下发策略集合
	 * 
	 * @param commandArg
	 * @param fwEntity
	 * @param paramVo
	 * @return
	 */
	private boolean sendFwOrder(CommandArg commandArg, FirewallAssetEntity fwEntity, OperateFirewallParam paramVo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("fwType", fwEntity.getFwType());
		queryMap.put("orderType", paramVo.getOperateType());
		PageList<FwOrderEntity> orderList = fwOrderMapper.findByParams(queryMap, null);
		if (BaseTools.isNull(orderList)) {
			return false;
		}
		logger.info("orderList  size--" + orderList.size());
		String cmd = null;
		for (FwOrderEntity item : orderList) {
			cmd = item.getFwOrder();
			commandArg.addInteractiveCmd(cmd, BaseTools.getDefStr(item.getSucPrompt()),
					BaseTools.getDefStr(item.getErrorPrompt()));
		}
		return true;
	}

	/**
	 * 防火墙下发策略信息
	 * 
	 * @param fwEntity
	 * @return
	 */
	Message checkMes = Message.newSuccessMessage(null);

	private Message sendFwOrder(FirewallAssetEntity fwEntity, OperateFirewallParam paramVo) {
		ConnectionArg conArg = buildConnectParam(fwEntity, paramVo);
		try {
			Message doLoginMes = doLoginFw(conArg);
			if (doLoginMes.isSuccess()) {
				fwEntity.setTestLoginStatus(login_suc);
				fwEntity.setTestLoginLog("");
				fwEntity.setTestLoginDate(new Date());
				fwMapper.update(fwEntity);
			} else {
				saveTestLoginFailMsg(fwEntity, doLoginMes);
				return doLoginMes;
			}
			/*
			 * 拨测功能，不走下面发送命令方法
			 */
			if (paramVo.isTestLogin()) {
				return doLoginMes;
			}
			Driver driver = (Driver) doLoginMes.getObj();
//			Driver driver = utils.login(conArg, switchCommand, moreExecuter, morePrompt);

//			String cmd = "ls\r\npwd\r\ncat /etc/passwd\r\ncat /etc/shadow\r\n";
//			CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_COMMON, cmd);
			CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_INTERACT);
			/*
			 * 根据防火墙类型，命令类型 到 防火墙命令管理获取 下发策略集合
			 */
			boolean isSend = this.sendFwOrder(commandArg, fwEntity, paramVo);
			if (!isSend) {
				logger.error("没有下发防火墙的策略信息");
				driver.logout();
			}
//			commandArg.addInteractiveCmd(cmd, prompt, errorPrompt)
			/*
			 * 传入参数 {0}外网ip {1}内网ip {2}开放端口号
			 */
			commandArg.addCmdArgs(paramVo.getNetIP(), paramVo.getInIp(), BaseTools.getDefStr(paramVo.getOpenPort()));
			ExecuteResult result = driver.executeCmd(commandArg);
			commandArg.getCmd();
//			System.out.println(result.getResult());
			logger.info(result.getResult());
			driver.logout();

		} catch (DriverException e) {
			logger.info("error msg : " + ErrorCode.msg(e.getMessage()));
		}
		return checkMes;
	}

	private Message doLoginFw(ConnectionArg conArg) {
		String switchCommand = "su -";
		String moreExecuter = "";
		String morePrompt = "";
		conArg.setDriverClass(NetdeviceDriver.class.getName());
		CollectionUtils collectionUtil = CollectionUtils.instance();
		Message doLoginMes = collectionUtil.doLogin(conArg, switchCommand, moreExecuter, morePrompt);
		return doLoginMes;
	}

	private ConnectionArg buildConnectParam(FirewallAssetEntity fwEntity, OperateFirewallParam paramVo) {
		String fwIp = fwEntity.getFwIp();
		logger.info("fwIp--" + fwIp + ",orderType--" + paramVo.getOperateType());
		int fwPort = BaseTools.getIntDef0(fwEntity.getFwPort());
		String protocol = fwEntity.getFwProtocol();
		String fwEncoding = fwEntity.getEncode();
//		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("HUAWEI", "id") // 设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
//				.setOpenArg(fwIp, 22, "SSH2") // 连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
//				.setEncode("UTF-8") // 设备编码
//				.setTimeout(10, 10) // 设备超时时间，分别为连接超时、读取回显超时，单位为秒
//				.setLoginArg("lanchao", "123456", "$") // 设备登录参数，分别为用户名、密码、提示符
//				.setAdminLoginArg("", "123456", "#");
		String adminPwd = this.cryptoTools.decode(fwEntity.getAdminPwd());
		String rootPwd = this.cryptoTools.decode(fwEntity.getRootPwd());
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo(fwEntity.getFwType(), fwEntity.getId()) // 设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setOpenArg(fwIp, fwPort, protocol) // 连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
				.setEncode(fwEncoding) // 设备编码
				.setTimeout(10, 10) // 设备超时时间，分别为连接超时、读取回显超时，单位为秒
				.setLoginArg(fwEntity.getAdminAcount(), adminPwd, fwEntity.getAdminPrompt()) // 设备登录参数，分别为用户名、密码、提示符
				.setAdminLoginArg("", rootPwd, fwEntity.getRootPrompt());
		return conArg;
	}

	/**
	 * 检查防火墙基础信息
	 * 
	 * @param fwEntity
	 * @return
	 */
	private Message checkFwInfo(FirewallAssetEntity fwEntity) {
		Message checkMes = Message.newSuccessMessage(null);
		StringBuffer sb = new StringBuffer();
		if (BaseTools.isNull(fwEntity.getFwIp())) {
			sb.append("IP地址");
		}
		int fwPort = BaseTools.getIntDef0(fwEntity.getFwPort());
		if (fwPort == 0) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("端口号");
		}
		if (BaseTools.isNull(fwEntity.getFwProtocol())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("协议");
		}
		if (BaseTools.isNull(fwEntity.getAdminAcount())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("管理员账号");
		}

		if (BaseTools.isNull(fwEntity.getAdminPwd())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("管理员密码");
		}
		if (BaseTools.isNull(fwEntity.getAdminPrompt())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("管理员提示符");
		}
		if (BaseTools.isNull(fwEntity.getRootPwd())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("特权密码");
		}
		if (BaseTools.isNull(fwEntity.getRootPrompt())) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append("特权提示符");
		}
		if (sb.length() > 0) {
			String errorMes = "防火墙IP:" + BaseTools.getDefStr(fwEntity.getFwIp()) + " 缺少:" + sb.toString();
			logger.error(errorMes);
			return Message.newFailureMessage(errorMes);
		}
		return checkMes;
	}

	@Override
	protected IBaseDao getBaseDao() {
		return fwMapper;
	}

	@Override
	public Message testFwStatus() {
		try {
			/** 1.获取所有防火墙列表 */
			Map<String, Object> params = ObjTransTools.entity2map(new FirewallAssetEntity());
			PageList<FirewallAssetEntity> pl = fwAssetEntityMapper.findByParams(params, null);
			/** 2.挨个登录防火墙 */
			for (FirewallAssetEntity firewallAssetEntity : pl) {
				 testLoginFwAndSaveLoginFwMsg(firewallAssetEntity);
			}
		} catch (Exception e) {
			logger.error("登录防火墙失败", e);
			throw e;
		}
		return Message.newSuccessMessage("拨测任务完成");
	}

	/***
	 * 登录防火墙进行拨测，并记录拨测日志到数据库
	 * @param firewallAssetEntity
	 * @return
	 */
	private Message testLoginFwAndSaveLoginFwMsg(FirewallAssetEntity firewallAssetEntity) {
		/** 2.1 检查防火墙基础信息,基础信息不完整直接保存测试失败信息，不用进行实际的拨测 */
		Message checkMes = this.checkFwInfo(firewallAssetEntity);
		if (!checkMes.isSuccess()) {
			saveTestLoginFailMsg(firewallAssetEntity, checkMes);
			return checkMes;
		}
		/** 2.2 实际拨测登录防火墙 */
		Message loginMsg=null;
		try {
			loginMsg = testFwLogin(firewallAssetEntity);
			if (loginMsg.isSuccess()) {
				saveTestLoginSuccessMsg(firewallAssetEntity);
			} else {
				saveTestLoginFailMsg(firewallAssetEntity, loginMsg);
				return loginMsg;
			}
		} catch (Exception e) {
			logger.error("登录防火墙失败",e);
			loginMsg = Message.newSuccessMessage("登录防火墙失败",e);
		}
		return Message.newSuccessMessage("拨测成功");
	}

	/**
	 * 登录防火墙成功的登录信息保存
	 * @param firewallAssetEntity
	 */
	private void saveTestLoginSuccessMsg(FirewallAssetEntity firewallAssetEntity) {
		firewallAssetEntity.setTestLoginStatus(login_suc);
		firewallAssetEntity.setTestLoginLog("成功");
		firewallAssetEntity.setTestLoginDate(new Date());
		fwMapper.update(firewallAssetEntity);
	}

	/***
	 * 登录失败的防火墙信息保存
	 * @param firewallAssetEntity
	 * @param checkMes
	 */
	private void saveTestLoginFailMsg(FirewallAssetEntity firewallAssetEntity, Message checkMes) {
		firewallAssetEntity.setTestLoginStatus(login_fail);
		if (!BaseTools.isNull(checkMes.getMsg())) {
			String errorMsg = checkMes.getMsg();
			if (errorMsg.length() > 3500) {
				errorMsg = errorMsg.substring(0, 3500);
				firewallAssetEntity.setTestLoginLog(errorMsg);
			} else {
				firewallAssetEntity.setTestLoginLog(errorMsg);
			}
		}
		firewallAssetEntity.setTestLoginDate(new Date());
		fwMapper.update(firewallAssetEntity);
	}
}
