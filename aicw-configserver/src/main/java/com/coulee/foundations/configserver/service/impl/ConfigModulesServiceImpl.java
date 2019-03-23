package com.coulee.foundations.configserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.dao.ConfigCommonMapper;
import com.coulee.foundations.configserver.dao.ConfigModulesMapper;
import com.coulee.foundations.configserver.dao.ConfigProductMapper;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigItemsService;
import com.coulee.foundations.configserver.service.IConfigModulesService;
import com.coulee.foundations.configserver.service.IConfigService;

/**
 * Description: 产品模块管理service<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigModulesServiceImpl extends AbstractBaseService implements IConfigModulesService {
	
	@Autowired
	private IConfigItemsService configItemsService;
	
	@Autowired
	private ConfigModulesMapper configModulesMapper;

	@Autowired
	private ConfigProductMapper configProductMapper;
	
	@Autowired
	private ConfigCommonMapper configCommonMapper;

	@Autowired
	private IConfigService configService;
	
	@Override
	protected IBaseDao getBaseDao() {
		return configModulesMapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public <T extends BaseEntity> Message add(T entity) {
		ConfigModules module = (ConfigModules) entity;
		if (module == null || StringUtils.isEmpty(module.getModuleMark()) || module.getProductId() == null) {
			return Message.newFailureMessage("增加失败！参数不完整！");
		}
		ConfigProduct product = this.configProductMapper.findById(module.getProductId());
		if (product == null) {
			return Message.newFailureMessage("增加失败！该模块所属产品不存在！");
		}
		if (this.isExist(module)) {
			return Message.newFailureMessage("增加失败！该模块标识已存在！");
		}
		int i = this.configModulesMapper.add(module);
		if (i > 0) {
			module.setId(this.configCommonMapper.getLastId());
			this.addExtendsConfig(product, module);
			return Message.newSuccessMessage("增加成功！");
		} else {
			return Message.newFailureMessage("增加失败！");
		}
	}
	
	/**
	 * Description: 判断该模块标识是否存在<br> 
	 * Created date: 2017年12月28日
	 * @param module
	 * @return
	 * @author oblivion
	 */
	private boolean isExist(ConfigModules module) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("productId", module.getProductId());
		params.put("moduleMark", module.getModuleMark());
		PageList<ConfigModules> pl = this.configModulesMapper.findByParams(params, null);
		if (pl != null && !pl.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Description: 处理继承自产品级别的配置<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @param module
	 * @author oblivion
	 */
	private void addExtendsConfig(ConfigProduct product, ConfigModules module) {
		//向zk上增加全局的加解密方式及密钥配置
		this.configService.setCryptoConfig(product.getProductMark(), module.getModuleMark(), product.getCryptType(),
				product.getCryptKey());
		if (module.getIsExtends().intValue() == 1) {
			//获取产品级配置信息，并增加到该模块配置
			this.configItemsService.processExtendsConfigItems(product.getId(), module.getId(), product.getProductMark(),
					module.getModuleMark());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public <T extends BaseEntity> Message update(T entity) {
		ConfigModules module = (ConfigModules) entity;
		ConfigModules old = super.findById(module.getId());
		//修改了模块标识，需同步修改zk上的配置
		if (!module.getModuleMark().equals(old.getModuleMark())) {
			if (this.isExist(module)) {
				return Message.newFailureMessage("修改失败！该模块标识已存在！");
			}
			ConfigProduct product = this.configProductMapper.findById(module.getProductId());
			List<ConfigItems> items = this.configItemsService.findConfigItems(product.getId(), module.getId());
			if (items != null && !items.isEmpty()) {
				//向zk上增加新模块的全局加解密方式及密钥配置
				this.configService.setCryptoConfig(product.getProductMark(), module.getModuleMark(), product.getCryptType(),
						product.getCryptKey());
				for (ConfigItems item : items) {
					//按新产品标识将原有配置信息写入zk
					this.configService.setConfigValue(product.getProductMark(), module.getModuleMark(), item.getItemKey(),
							item.getItemValue());
				}
				//删除zk上的旧配置信息
				this.configService.deleteConfig(product.getProductMark(), old.getModuleMark());
			}
		}
		return super.update(entity);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message batchDelete(List<ConfigModules> modules) {
		if (modules == null || modules.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要删除的产品！");
		}
		ConfigProduct product = this.configProductMapper.findById(modules.get(0).getProductId());
		for (ConfigModules module : modules) {
			this.delete(module.getId());
			this.configItemsService.deleteConfigItems(module.getProductId(), module.getId());
			this.configService.deleteConfig(product.getProductMark(), module.getModuleMark());
		}
		return Message.newSuccessMessage("删除成功！");
	}
}
