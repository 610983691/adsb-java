package com.coulee.foundations.configserver.service.impl;

import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.utils.zookeeper.ZookeeperTools;
import com.coulee.foundations.ConfigServerConstants;
import com.coulee.foundations.configserver.service.IConfigService;

/**
 * Description: 维护zookeeper服务上配置信息<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigServiceImpl implements IConfigService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private ZookeeperTools zkTools;
	
	private final String configSplitStr = ".";

	@Override
	public boolean setConfigValue(String product, String appName, String key, String value) {
		StringBuffer path = new StringBuffer(ConfigServerConstants.ZK_CONFIG_ROOT);
		path.append("/").append(product).append("/").append(appName);
		if (key.contains(configSplitStr)) {
			String[] keys = key.split("\\.");
			for (String tmp : keys) {
				path.append("/").append(tmp);
			}
		} else {
			path.append("/").append(key);
		}
		boolean b = zkTools.setData(path.toString(), value, CreateMode.PERSISTENT);
		logger.info(path.toString() + " == " + b);
		return b;
	}

	@Override
	public String getConfigValue(String product, String appName, String key) {
		StringBuffer path = new StringBuffer(ConfigServerConstants.ZK_CONFIG_ROOT);
		path.append("/").append(product).append("/").append(appName);
		if (key.contains(configSplitStr)) {
			String[] keys = key.split("\\.");
			for (String tmp : keys) {
				path.append("/").append(tmp);
			}
		} else {
			path.append("/").append(key);
		}
		try {
			return zkTools.getData(path.toString());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<String, String> getAllConfigValue(String product, String appName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteConfig(String product) {
		if (StringUtils.isEmpty(product)) {
			return false;
		}
		StringBuffer path = new StringBuffer(ConfigServerConstants.ZK_CONFIG_ROOT);
		path.append("/").append(product);
		return this.zkTools.delete(path.toString());
	}

	@Override
	public boolean deleteConfig(String product, String appName) {
		if (StringUtils.isEmpty(product) || StringUtils.isEmpty(appName)) {
			return false;
		}
		StringBuffer path = new StringBuffer(ConfigServerConstants.ZK_CONFIG_ROOT);
		path.append("/").append(product).append("/").append(appName);
		return this.zkTools.delete(path.toString());
	}

	@Override
	public boolean deleteConfig(String product, String appName, String key) {
		if (StringUtils.isEmpty(product) || StringUtils.isEmpty(appName) || StringUtils.isEmpty(key)) {
			return false;
		}
		StringBuffer path = new StringBuffer(ConfigServerConstants.ZK_CONFIG_ROOT);
		path.append("/").append(product).append("/").append(appName);
		if (key.contains(configSplitStr)) {
			String[] keys = key.split("\\.");
			for (String tmp : keys) {
				path.append("/").append(tmp);
			}
		} else {
			path.append("/").append(key);
		}
		return this.zkTools.delete(path.toString());
	}

	@Override
	public boolean setCryptoConfig(String product, String appName, String cryptType, String cryptKey) {
		this.setConfigValue(product, appName, ConfigServerConstants.CRYPTO_TYPE_TYPE, cryptType);
		this.setConfigValue(product, appName, ConfigServerConstants.CRYPTO_TYPE_KEY, cryptKey);
		return false;
	}

}
