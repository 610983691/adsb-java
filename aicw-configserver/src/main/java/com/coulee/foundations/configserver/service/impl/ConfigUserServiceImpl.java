package com.coulee.foundations.configserver.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.crypt.MD5;
import com.coulee.foundations.configserver.dao.ConfigUserMapper;
import com.coulee.foundations.configserver.entity.ConfigUser;
import com.coulee.foundations.configserver.service.IConfigUserService;

/**
 * Description: 用户管理service实现类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigUserServiceImpl extends AbstractBaseService implements IConfigUserService {

	@Autowired
	private ConfigUserMapper configUserMapper;
	
	@Override
	protected IBaseDao getBaseDao() {
		return configUserMapper;
	}

	@Override
	public Message checkUser(String uuid, String password) {
		if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(password)) {
			return Message.newFailureMessage("用户名或密码不能为空！");
		}
		ConfigUser user = this.configUserMapper.findByUuid(uuid);
		if (user == null) {
			return Message.newFailureMessage("该用户不存在！");
		} else {
			String encryptPassword = MD5.md5(password);
			if (user.getUserPassword().equalsIgnoreCase(encryptPassword)) {
				return Message.newSuccessMessage("登录成功！", user);
			}
		}
		return Message.newFailureMessage("用户名或密码错误！");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message selfUpdate(ConfigUser user) {
		if (StringUtils.isEmpty(user.getUuid()) || StringUtils.isEmpty(user.getUserName())
				|| StringUtils.isEmpty(user.getUserPassword())) {
			return Message.newFailureMessage("修改失败，信息不完整！");
		}
		ConfigUser oldUser = this.configUserMapper.findByUuid(user.getUuid());
		if (oldUser == null) {
			return Message.newFailureMessage("修改失败，该用户不存在！");
		}
		oldUser.setUserName(user.getUserName());
		oldUser.setUserPassword(MD5.md5(user.getUserPassword()));
		return this.update(oldUser);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message batchDelete(List<ConfigUser> users) {
		if (users == null || users.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要删除的用户！");
		}
		for (ConfigUser user : users) {
			this.delete(user.getId());
		}
		return Message.newSuccessMessage("删除成功！");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message addUser(ConfigUser user) {
		if (user == null) {
			return Message.newFailureMessage("增加失败！");
		}
		ConfigUser existUser = this.configUserMapper.findByUuid(user.getUuid());
		if (existUser != null) {
			return Message.newFailureMessage("增加失败，UID为" + user.getUuid() + "已存在！");
		}
		user.setUserPassword(MD5.md5(user.getUserPassword()));
		int i = this.configUserMapper.add(user);
		if (i > 0) {
			user.setUserPassword(null);
			return Message.newSuccessMessage("增加成功！", user);
		} else {
			return Message.newFailureMessage("增加失败！");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message updateUser(ConfigUser user) {
		if (user == null || user.getId() == null) {
			return Message.newFailureMessage("修改失败！");
		}
		String isModifyPasword = user.getIsModifyPassword();
		//1为修改密码
		if ("1".equals(isModifyPasword)) {
			user.setUserPassword(MD5.md5(user.getUserPassword()));
		} else {
			ConfigUser oldUser = this.configUserMapper.findByUuid(user.getUuid());
			user.setUserPassword(oldUser.getUserPassword());
		}
		int i = this.configUserMapper.update(user);
		if (i > 0) {
			user.setUserPassword(null);
			return Message.newSuccessMessage("修改成功！", user);
		} else {
			return Message.newFailureMessage("修改失败！");
		}
	}

}
