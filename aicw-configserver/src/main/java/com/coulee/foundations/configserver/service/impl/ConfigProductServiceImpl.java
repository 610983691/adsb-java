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
import com.coulee.aicw.foundations.utils.crypt.SecetKeyUtils;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.dao.ConfigModulesMapper;
import com.coulee.foundations.configserver.dao.ConfigProductMapper;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigItemsService;
import com.coulee.foundations.configserver.service.IConfigProductService;
import com.coulee.foundations.configserver.service.IConfigService;

/**
 * Description: 产品管理service<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigProductServiceImpl extends AbstractBaseService implements IConfigProductService {
	
	@Autowired
	private IConfigItemsService configItemsService;

	@Autowired
	private ConfigProductMapper configProductMapper;
	
	@Autowired
	private ConfigModulesMapper configModulesMapper;
	
	@Autowired
	private IConfigService configService;
	
	@Override
	protected IBaseDao getBaseDao() {
		return configProductMapper;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public <T extends BaseEntity> Message add(T entity) {
		ConfigProduct product = (ConfigProduct) entity;
		if (product == null || StringUtils.isEmpty(product.getCryptKey()) || StringUtils.isEmpty(product.getProductMark())) {
			return Message.newFailureMessage("增加失败！参数不完整！");
		}
		if (this.isExist(product)) {
			return Message.newFailureMessage("增加失败！该产品标识已存在！");
		}
		product.setCryptKey(SecetKeyUtils.encodeKey(product.getCryptKey()));
		return super.add(product);
	}
	
	/**
	 * Description: 判断该产品标识是否已存在<br> 
	 * Created date: 2017年12月28日
	 * @param product
	 * @return
	 * @author oblivion
	 */
	private boolean isExist(ConfigProduct product) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("productMark", product.getProductMark());
		PageList<ConfigProduct> pl = this.configProductMapper.findByParams(params, null);
		if (pl != null && !pl.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public <T extends BaseEntity> Message update(T entity) {
		ConfigProduct product = (ConfigProduct) entity;
		ConfigProduct old = super.findById(product.getId());
		product.setCryptType(old.getCryptType());
		product.setCryptKey(old.getCryptKey());
		//修改了产品标识，需同步修改zk上的配置
		if (!old.getProductMark().equals(product.getProductMark())) {
			if (this.isExist(product)) {
				return Message.newFailureMessage("修改失败！该产品标识已存在！");
			}
			List<ConfigItems> items = this.configItemsService.findConfigItems(product.getId());
			if (items != null && !items.isEmpty()) {
				for (ConfigItems item : items) {
					//按新产品标识将原有配置信息写入zk
					this.configService.setConfigValue(product.getProductMark(), item.getModuleMark(), item.getItemKey(),
							item.getItemValue());
				}
				//删除zk上的旧配置信息
				this.configService.deleteConfig(old.getProductMark());
			}
		}
		return super.update(product);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message batchDelete(List<ConfigProduct> products) {
		if (products == null || products.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要删除的产品！");
		}
		for (ConfigProduct product : products) {
			this.delete(product.getId());
			this.configModulesMapper.deleteByProductId(product.getId());
			this.configItemsService.deleteConfigItems(product.getId());
		}
		return Message.newSuccessMessage("删除成功！");
	}
}
