package com.coulee.aicw.service.user.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.coulee.aicw.dao.UserEntityMapper;
import com.coulee.aicw.entity.DictionaryEntity;
import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.config.CryptoTools;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.service.user.IUserService;

/**
 * 实现类 Description: Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月26日下午4:56:02 author：HongZhang
 * 
 * @version 1.0
 */
@Service
public class UserServiceImpl extends AbstractBaseService implements IUserService {
	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);
	/**
	 * 加解密工具类
	 */
	@Autowired
	private CryptoTools cryptoTools;
	@Autowired
	private UserEntityMapper userMapper;

	@Override
	protected IBaseDao getBaseDao() {
		return userMapper;
	}

	@Override
	public Message checkUser(String userAccount, String password) {
		try {
			if (StringUtils.isEmpty(userAccount) || StringUtils.isEmpty(password)) {
				return Message.newFailureMessage("用户名或密码不能为空！");
			} else {
				UserEntity ue = this.userMapper.findByUserAccount(userAccount);
				if (ue == null) {
					return Message.newFailureMessage("该用户不存在！");
				} else if (ue.getUserStatus().equals("1") || ue.getUserStatus().equals("4")
						|| ue.getUserStatus().equals("3")) {
					return Message.newFailureMessage("该用户不能登录，请联系管理员！");
				} else {
					String userPassword = this.cryptoTools.decode(ue.getLoginPassword());
					if (password.equalsIgnoreCase(userPassword)) {
						return Message.newSuccessMessage("登录成功！", ue);
					} else {
						return Message.newFailureMessage("用户名或密码错误！");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("check user error:", e);
			return Message.newFailureMessage("服务器内部错误！");
		}
	}

	@Override
	public UserEntity findByUserTelphone(String telphone) {
		try {
			UserEntity ue = this.userMapper.findByUserTel(telphone);
			return ue;
		} catch (Exception e) {
			LOGGER.error("finduser by tel err:", e);
			return null;
		}

	}

	@Transactional(rollbackFor = Exception.class)
	public Message add(UserEntity entity) {
		entity.setLoginPassword(cryptoTools.encode(entity.getLoginPassword()));
		Date now = new Date();
		entity.setUpdateDate(now);
		entity.setCreateDate(now);
		int i = userMapper.add(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	/***
	 * 修改用户信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Message updateUser(UserEntity entity) {
		entity.setLoginPassword(cryptoTools.encode(entity.getLoginPassword()));
		Date now = new Date();
		entity.setUpdateDate(now);
		int i = userMapper.update(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@Override
	public boolean isExistAccount(String account) {
		UserEntity existUser = this.userMapper.findByUserAccount(account);
		return existUser != null;
	}

}
