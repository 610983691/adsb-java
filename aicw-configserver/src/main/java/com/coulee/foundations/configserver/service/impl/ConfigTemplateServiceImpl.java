package com.coulee.foundations.configserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.dao.ConfigCommonMapper;
import com.coulee.foundations.configserver.dao.ConfigTemplateItemsMapper;
import com.coulee.foundations.configserver.dao.ConfigTemplateMapper;
import com.coulee.foundations.configserver.entity.ConfigTemplate;
import com.coulee.foundations.configserver.entity.ConfigTemplateItems;
import com.coulee.foundations.configserver.service.IConfigTemplateService;

/**
 * Description: 模板管理service<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigTemplateServiceImpl extends AbstractBaseService implements IConfigTemplateService {

	@Autowired
	private ConfigTemplateItemsMapper configTemplateItemsMapper;

	@Autowired
	private ConfigTemplateMapper configTemplateMapper;
	
	@Autowired
	private ConfigCommonMapper configCommonMapper;

	@Override
	protected IBaseDao getBaseDao() {
		return configTemplateMapper;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message addConfigTemplate(ConfigTemplate configTemplate, List<ConfigTemplateItems> items) {
		if (configTemplate == null || items == null || items.isEmpty()) {
			return Message.newFailureMessage("增加失败，信息不完整！");
		}
		Map<String, Object> params = ObjTransTools.entity2map(configTemplate);
		List<ConfigTemplate> pl = this.configTemplateMapper.checkExist(params);
		if (pl != null && !pl.isEmpty()) {
			return Message.newFailureMessage("增加失败，该模板名称已存在！");
		}
		int length = this.configTemplateMapper.add(configTemplate);
		if (length > 0) {
			Integer templateId = configCommonMapper.getLastId();
			for (int i = 0; i < items.size(); i++) {
				ConfigTemplateItems item = items.get(i);
				item.setTemplateId(templateId);
				item.setItemSeq(i);
				this.configTemplateItemsMapper.add(item);
			}
		}
		return Message.newSuccessMessage("增加成功！");
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message batchDelete(List<ConfigTemplate> configTemplates) {
		if (configTemplates == null || configTemplates.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要删除的模板！");
		}
		for (ConfigTemplate configTemplate : configTemplates) {
			this.delete(configTemplate.getId());
			this.configTemplateItemsMapper.deleteByTemplateId(configTemplate.getId());
		}
		return Message.newSuccessMessage("删除成功！");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public PageList<ConfigTemplateItems> detailConfigTemplate(ConfigTemplate configTemplate) {
		ConfigTemplateItems configTemplateItems = new ConfigTemplateItems();
		configTemplateItems.setTemplateId(configTemplate.getId());
		Map<String, Object> params = ObjTransTools.entity2map(configTemplateItems);
		return this.configTemplateItemsMapper.findByParamsOrderBySEQ(params, null);

	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message updateConfigTemplate(ConfigTemplate configTemplate, List<ConfigTemplateItems> items) {
		if (configTemplate == null || items == null || items.isEmpty()) {
			return Message.newFailureMessage("修改失败，信息不完整！");
		}
		Map<String, Object> params = ObjTransTools.entity2map(configTemplate);
		List<ConfigTemplate> pl = this.configTemplateMapper.checkExist(params);
		if (pl != null && !pl.isEmpty()) {
			return Message.newFailureMessage("修改失败，该模板名称已存在！");
		}
		int i = this.configTemplateMapper.update(configTemplate);
		if (i > 0) {
			this.configTemplateItemsMapper.deleteByTemplateId(configTemplate.getId());
			Integer templateId = configTemplate.getId();
			for (ConfigTemplateItems item : items) {
				item.setTemplateId(templateId);
				this.configTemplateItemsMapper.add(item);
			}
		}
		return Message.newSuccessMessage("修改成功！");
	}
	
	@Override
	public <T extends BaseEntity> PageList<T> findByParamsWithInner(T entity, PageArg pageArg) {
		Map<String, Object> params = ObjTransTools.entity2map(entity);
		return this.configTemplateMapper.findByParamsWithInner(params, pageArg);
	}


	@Override
	public List<ConfigTemplate> findByCreater(ConfigTemplate entity, PageArg pageArg) {
		return this.configTemplateMapper.findByCreater(ObjTransTools.entity2map(entity), pageArg);
	}

	@Override
	public List<ConfigTemplateItems> findByTemplateId(Integer templateId) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("templateId", templateId);
		return this.configTemplateItemsMapper.findByParams(params, null);
	}


}
