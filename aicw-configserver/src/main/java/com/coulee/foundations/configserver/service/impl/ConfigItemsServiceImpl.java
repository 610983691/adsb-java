package com.coulee.foundations.configserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.crypt.Crypto;
import com.coulee.aicw.foundations.utils.crypt.SecetKeyUtils;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.ConfigServerConstants;
import com.coulee.foundations.configserver.dao.ConfigCommonMapper;
import com.coulee.foundations.configserver.dao.ConfigItemsMapper;
import com.coulee.foundations.configserver.dao.ConfigMainMapper;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigItemsService;
import com.coulee.foundations.configserver.service.IConfigModulesService;
import com.coulee.foundations.configserver.service.IConfigProductService;
import com.coulee.foundations.configserver.service.IConfigService;

/**
 * Description: 配置项管理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigItemsServiceImpl implements IConfigItemsService {

	@Autowired
	private ConfigItemsMapper configItemsMapper;
	
	@Autowired
	private ConfigMainMapper configMainMapper;
	
	@Autowired
	private ConfigCommonMapper configCommonMapper;
	
	@Autowired
	private IConfigService configService;
	
	@Autowired
	private IConfigProductService configProductService;
	
	@Autowired
	private IConfigModulesService configModulesService;

	@Override
	public Message saveConfigTemp(Integer productId, Integer moduleId, String configInfo) {
		if (StringUtils.isEmpty(configInfo) || StringUtils.isEmpty(configInfo.trim())) {
			return Message.newFailureMessage("配置信息不存在！");
		}
		ConfigProduct product = this.configProductService.findById(productId);
		if (product == null) {
			return Message.newFailureMessage("该产品信息不存在！");
		}
		String productMark = product.getProductMark();
		if (moduleId == null || moduleId.intValue() == 0) {
			ConfigModules module = new ConfigModules();
			module.setProductId(productId);
			PageList<ConfigModules> pl = this.configModulesService.findByEntity(module, null);
			if (pl == null || pl.isEmpty()) {
				return Message.newFailureMessage("该产品模块信息不存在！");
			}
			Map<String, String> configMap = this.formatConfigInfo(configInfo, product);
			for (ConfigModules configModules : pl) {
				this.writeConfigToZk(productMark, configModules.getModuleMark(), configMap);
			}
		} else {
			ConfigModules module = this.configModulesService.findById(moduleId);
			if (module == null) {
				return Message.newFailureMessage("该产品模块信息不存在！");
			}
			this.writeConfigToZk(productMark, module.getModuleMark(), this.formatConfigInfo(configInfo, product));
		}
		return Message.newSuccessMessage("配置成功！");
	}
	
	/**
	 * Description: 格式化配置信息<br> 
	 * Created date: 2017年12月25日
	 * @param configInfo
	 * @param product
	 * @return
	 * @author oblivion
	 */
	private Map<String, String> formatConfigInfo(String configInfo, ConfigProduct product) {
		String[] configLines = configInfo.split("\r\n");
		Map<String, String> configMap = new HashMap<>(configLines.length + 3);
		for (String line : configLines) {
			if (StringUtils.isEmpty(line.trim())) {
				continue;
			}
			String key = line.substring(0, line.indexOf("="));
			String value = line.substring(line.indexOf("=") + 1, line.length());
			configMap.put(key.trim(), value.trim());
		}
		configMap.put(ConfigServerConstants.CRYPTO_TYPE_TYPE, product.getCryptType());
		configMap.put(ConfigServerConstants.CRYPTO_TYPE_KEY, product.getCryptKey());
		return configMap;
	}
	
	/**
	 * Description: 将配置信息写入zk<br> 
	 * Created date: 2017年12月25日
	 * @param productMark
	 * @param moduleMark
	 * @param configMap
	 * @author oblivion
	 */
	private void writeConfigToZk(String productMark, String moduleMark, Map<String, String> configMap) {
		for(Map.Entry<String, String> entry : configMap.entrySet()) {
			this.configService.setConfigValue(productMark, moduleMark, entry.getKey(), entry.getValue());
		}
	}

	@Override
	public List<ConfigItems> findConfigItems(Integer productId) {
		return this.findConfigItems(productId, null);
	}

	@Override
	public List<ConfigItems> findConfigItems(Integer productId, Integer moduleId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("productId", productId);
		if (moduleId != null) {
			params.put("moduleId", moduleId);
		}
		return this.configItemsMapper.findConfigItems(params);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message deleteConfigItems(Integer productId) {
		return this.deleteConfigItems(productId, null);
	}

	@Override
	public Message deleteConfigItems(Integer productId, Integer moduleId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("productId", productId);
		if (moduleId != null) {
			params.put("moduleId", moduleId);
		}
		int i = this.configItemsMapper.deleteByProductOrMoudle(params);
		i = this.configMainMapper.deleteByProductOrMoudle(params);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！");
		}
		return Message.newFailureMessage("操作失败！");
	}

	@Override
	public void processExtendsConfigItems(Integer productId, Integer moduleId, String productMark, String moduleMark) {
		List<ConfigMain> configMains = this.configMainMapper.findProductLevelConfig(productId);
		if (configMains == null || configMains.isEmpty()) {
			return;
		}
		Map<String, Object> params = new HashMap<>(configMains.size());
		for (ConfigMain configMain : configMains) {
			int parentMainId = configMain.getId();
			configMain.setModuleId(moduleId);
			configMain.setExtendsId(parentMainId);
			configMain.setId(null);
			//将CONFIG_MAIN信息入库
			int i = this.configMainMapper.add(configMain);
			if (i > 0) {
				int newMainId = this.configCommonMapper.getLastId();
				params.put("configMainId", parentMainId);
				PageList<ConfigItems> pl = this.configItemsMapper.findByParams(params, null);
				if (pl == null || pl.isEmpty()) {
					continue;
				}
				for (ConfigItems item : pl) {
					item.setIsExtends(1);
					item.setExtendsId(item.getId());
					item.setConfigMainId(newMainId);
					item.setId(null);
					if (this.configItemsMapper.add(item) > 0) {
						this.configService.setConfigValue(productMark, moduleMark, item.getItemKey(), item.getItemValue());
					}
				}
			}
		}
	}

	@Override
	public List<ConfigMain> findConfigType(Integer productId, Integer moduleId) {
		if (productId == null || productId == 0) {
			return null;
		}
		//查询产品级配置
		if (moduleId == null || moduleId == 0) {
			return this.configMainMapper.findProductLevelConfig(productId);
		} else {
			//查询模块级配置
			Map<String, Object> params = new HashMap<String, Object>(2);
			params.put("productId", productId);
			params.put("moduleId", moduleId);
			return this.configMainMapper.findByParams(params, null);
		}
	}

	@Override
	public List<ConfigItems> findConfigItemsByType(Integer configMainId) {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("configMainId", configMainId);
		List<ConfigItems> list = this.configItemsMapper.findConfigItems(params);
		if (list == null || list.isEmpty()) {
			return list;
		}
		for (int i = 0; i < list.size(); i++) {
			ConfigItems item = list.get(i);
			if (StringUtils.isEmpty(item.getModuleMark())) {
				//产品级配置
				continue;
			}
			String zkValue = this.configService.getConfigValue(item.getProductMark(), item.getModuleMark(), item.getItemKey());
			String localValue = item.getItemValue();
			//如果zk上存在该属性值，且与数据库内存储值不同，则以zk为主
			if (StringUtils.isEmpty(zkValue)) {
				item.setDataVersion("unsyn");
			} else {
				if (!zkValue.equals(localValue)) {
					item.setItemValue(zkValue);
					list.set(i, item);
					this.configItemsMapper.update(item);
				}
			}
		}
		return list;
	}

	@Override
	public Message saveConfigItems(Integer configMainId, List<ConfigItems> configItems) {
		ConfigMain configMain = this.configMainMapper.findById(configMainId);
		ConfigProduct product = this.configProductService.findById(configMain.getProductId());
		ConfigModules module = this.configModulesService.findById(configMain.getModuleId());
		Map<Integer, ConfigItems> oldItemsMap = this.findConfigItemsMapByConfigType(configMainId);
		if (module == null) {
			this.saveConfigItemsLevelProduct(product, oldItemsMap, configItems);
		} else {
			this.saveConfigItemsLevelModule(product, module, oldItemsMap, configItems);
		}
		return Message.newSuccessMessage("保存配置信息成功！");
	}
	
	/**
	 * Description: 对配置值进行加密<br> 
	 * Created date: 2018年1月2日
	 * @param product
	 * @param itemValue
	 * @return
	 * @author oblivion
	 */
	private String cryptItemValue(ConfigProduct product, String itemValue) {
		if (StringUtils.isEmpty(itemValue)) {
			return "";
		}
		Crypto c = Crypto.getInstance(product.getCryptType());
		return c.encrypt(itemValue, SecetKeyUtils.decodeKey(product.getCryptKey()));
	}
	
	/**
	 * Description: 保存产品级配置，保存继承产品的模块配置<br> 
	 * Created date: 2018年1月3日
	 * @param product
	 * @param oldItemsMap
	 * @param configItems
	 * @author oblivion
	 */
	private void saveConfigItemsLevelProduct(ConfigProduct product, Map<Integer, ConfigItems> oldItemsMap, List<ConfigItems> configItems) {
		List<ConfigItems> newItems = new ArrayList<>(configItems.size());
		ConfigModules entity = new ConfigModules();
		entity.setProductId(product.getId());
		PageList<ConfigModules> modules = this.configModulesService.findByEntity(entity, null);
		for (ConfigItems item : configItems) {
			ConfigItems oldItem = oldItemsMap.get(item.getId());
			if (oldItem != null) { //修改配置项
				oldItemsMap.remove(item.getId());
				this.saveConfigItemsForUpdate(oldItem, item, product, modules, true);
			} else { //新增配置项
				this.saveConfigItemsForAdd(item, product, null);
				newItems.add(item);
			}
		}
		if (!oldItemsMap.isEmpty()) {
			//删除配置项
			for (Integer id : oldItemsMap.keySet()) {
				if (this.configItemsMapper.delete(id) > 0) { //删除产品级配置
					List<ConfigItems> items = this.findExtendsItemsByParendId(id);
					if (items == null || items.isEmpty()) {
						continue;
					}
					//修改继承配置项为非继承，目前不级联删除继承该产品的模块配置
					for (ConfigItems it : items) {
						it.setExtendsId(null);
						it.setIsExtends(0);
						this.configItemsMapper.update(it);
					}
				}
			}
		}
		if (!newItems.isEmpty()) {
			//为模块继承配置增加配置项
			this.saveConfigItemsForAddExtends(newItems, product);
		}
	}
	
	/**
	 * Description: 保存模块级别配置<br> 
	 * Created date: 2018年1月3日
	 * @param product
	 * @param module
	 * @param oldItemsMap
	 * @param configItems
	 * @author oblivion
	 */
	private void saveConfigItemsLevelModule(ConfigProduct product, ConfigModules module,
			Map<Integer, ConfigItems> oldItemsMap, List<ConfigItems> configItems) {
		for (ConfigItems item : configItems) {
			ConfigItems oldItem = oldItemsMap.get(item.getId());
			if (oldItem != null) { //修改配置项
				oldItemsMap.remove(item.getId());
				this.saveConfigItemsForUpdate(oldItem, item, product, null, false);
			} else { //新增配置项
				this.saveConfigItemsForAdd(item, product, module);
			}
		}
		if (!oldItemsMap.isEmpty()) {
			//删除配置项
			for (Map.Entry<Integer, ConfigItems> entry : oldItemsMap.entrySet()) {
				Integer id = entry.getKey();
				ConfigItems item = entry.getValue();
				if (this.configItemsMapper.delete(id) > 0) {
					this.configService.deleteConfig(item.getProductMark(), item.getModuleMark(), item.getItemKey());
				}
			}
		}
	}
	
	/**
	 * Description: 保存配置-修改配置项<br> 
	 * Created date: 2018年1月5日
	 * @param oldItem
	 * @param item
	 * @param product
	 * @param modules
	 * @param isProductLevel
	 * @author oblivion
	 */
	private void saveConfigItemsForUpdate(ConfigItems oldItem, ConfigItems item, ConfigProduct product, List<ConfigModules> modules, boolean isProductLevel) {
		String oldValue = oldItem.getItemValue() == null ? "" : oldItem.getItemValue();
		String newValue = item.getItemValue() == null ? "" : item.getItemValue();
		//配置项值未修改，只修改zk内的配置项值
		if (oldValue.equals(newValue)) {
			if (isProductLevel) {
				if (modules == null || modules.isEmpty()) {
					return;
				}
				for (ConfigModules module : modules) {
					this.configService.setConfigValue(oldItem.getProductMark(), module.getModuleMark(), oldItem.getItemKey(), newValue);
				}
			} else {
				this.configService.setConfigValue(oldItem.getProductMark(), oldItem.getModuleMark(), oldItem.getItemKey(), newValue);
			}
			return;
		}
		if (oldItem.getItemIsCrypt() == 1) {
			newValue = this.cryptItemValue(product, newValue);
		}
		oldItem.setItemValue(newValue);
		oldItem.setExtendsId(null);
		oldItem.setIsExtends(0);
		if (this.configItemsMapper.update(oldItem) > 0) {
			if (isProductLevel) { //产品级配置，需修改模块继承配置
				List<ConfigItems> items = this.findExtendsItemsByParendId(oldItem.getId());
				if (items == null || items.isEmpty()) {
					return;
				}
				//修改继承配置项
				for (ConfigItems it : items) {
					it.setItemValue(newValue);
					if (this.configItemsMapper.update(it) > 0) {
						this.configService.setConfigValue(it.getProductMark(), it.getModuleMark(), it.getItemKey(), newValue);
					}
				}
			} else { //模块级配置
				this.configService.setConfigValue(oldItem.getProductMark(), oldItem.getModuleMark(), oldItem.getItemKey(), newValue);
			}
			
		}
	}
	
	/**
	 * Description: 保存配置-新增配置项<br> 
	 * Created date: 2018年1月5日
	 * @param item
	 * @param product
	 * @param module
	 * @author oblivion
	 */
	private void saveConfigItemsForAdd(ConfigItems item, ConfigProduct product, ConfigModules module) {
		item.setId(null);
		item.setExtendsId(null);
		item.setIsExtends(0);
		item.setItemSeq(this.configItemsMapper.findMaxSeqByMainId(item.getConfigMainId()));
		if (item.getItemIsCrypt() == 1) {
			item.setItemValue(this.cryptItemValue(product, item.getItemValue()));
		}
		if (this.configItemsMapper.add(item) > 0) {
			item.setId(this.configCommonMapper.getLastId());
			if (module != null) { //模块级配置
				this.configService.setConfigValue(product.getProductMark(), module.getModuleMark(), item.getItemKey(), item.getItemValue());
			}
		}
	}
	
	/**
	 * Description: 保存配置-新增继承配置项<br> 
	 * Created date: 2018年1月5日
	 * @param newItems
	 * @param product
	 * @author oblivion
	 */
	private void saveConfigItemsForAddExtends(List<ConfigItems> newItems, ConfigProduct product) {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("extendsId", newItems.get(0).getConfigMainId());
		List<ConfigMain> configMains = this.configMainMapper.findByParams(params, null);
		if (configMains == null || configMains.isEmpty()) { //没有继承该产品级配置的模块
			return;
		}
		Map<Integer, ConfigModules> modulesMap = new HashMap<>(configMains.size());
		//处理新增的继承的模块级别配置项
		for (ConfigItems item : newItems) {
			item.setExtendsId(item.getId());
			item.setId(null);
			item.setIsExtends(1);
			//为所有继承自改产品的模块增加配置项
			for (ConfigMain configMain : configMains) {
				item.setConfigMainId(configMain.getId());
				item.setItemSeq(this.configItemsMapper.findMaxSeqByMainId(configMain.getId()));
				if (this.configItemsMapper.add(item) > 0) {
					ConfigModules module = modulesMap.get(configMain.getModuleId());
					if (module == null) {
						module = this.configModulesService.findById(configMain.getModuleId());
						if (module == null) {
							continue;
						}
						modulesMap.put(configMain.getModuleId(), module);
					}
					this.configService.setConfigValue(product.getProductMark(), module.getModuleMark(), item.getItemKey(), item.getItemValue());
				}
			}
		}
	}
	
	/**
	 * Description: 根据configMainId查询配置信息，返回Map数据类型集合<br> 
	 * Created date: 2018年1月3日
	 * @param configMainId
	 * @return
	 * @author oblivion
	 */
	private Map<Integer, ConfigItems> findConfigItemsMapByConfigType(Integer configMainId) {
		Map<String, Object> params = new HashMap<>(1);
		params.put("configMainId", configMainId);
		List<ConfigItems> list = this.configItemsMapper.findConfigItems(params);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Integer, ConfigItems> map = new HashMap<>(list.size());
		for (ConfigItems item : list) {
			map.put(item.getId(), item);
		}
		return map;
	}
	
	/**
	 * Description: 根据父ID查询继承配置项<br> 
	 * Created date: 2018年1月3日
	 * @param parentId
	 * @return
	 * @author oblivion
	 */
	private List<ConfigItems> findExtendsItemsByParendId(Integer parentId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("isExtends", 1);
		params.put("extendsId", parentId);
		return this.configItemsMapper.findConfigItems(params);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message deleteConfig(Integer configMainId) {
		ConfigMain configMain = this.configMainMapper.findById(configMainId);
		Map<String, Object> params = new HashMap<>(4);
		params.put("configMainId", configMainId);
		List<ConfigItems> list = this.configItemsMapper.findConfigItems(params);
		int i = this.configMainMapper.delete(configMainId);//删除main表数据
		if (i > 0) {
			if (list == null || list.isEmpty()) {
				return Message.newSuccessMessage("删除成功！");
			}
			//产品级配置，目前不级联删除继承该产品的模块配置
			if (configMain.getModuleId() == null || configMain.getModuleId() == 0) {
				List<Integer> ids = new ArrayList<>(list.size());
				for (ConfigItems item : list) {
					ids.add(item.getId());
					this.configItemsMapper.delete(item.getId());
				}
				params.clear();
				params.put("ids", ids);
				params.put("isExtends", 0);
				params.put("extendsId", null);
				this.configItemsMapper.updateExtendsItemsByParentIds(params);
			} else {
				for (ConfigItems item : list) {
					this.configItemsMapper.delete(item.getId());
					this.configService.deleteConfig(item.getProductMark(), item.getModuleMark(), item.getItemKey());
				}
			}
			return Message.newSuccessMessage("删除成功！");
		} else {
			return Message.newFailureMessage("删除失败！");
		}
	}

	@Override
	public Message checkConfigType(Integer productId, Integer moduleId, String configType) {
		Map<String, Object> params = new HashMap<>(3);
		params.put("productId", productId);
		params.put("moduleId", moduleId);
		params.put("configType", configType);
		PageList<ConfigMain> pl = this.configMainMapper.findByParams(params, null);
		if (pl != null && !pl.isEmpty()) {
			if (moduleId != null && moduleId != 0) {
				return Message.newFailureMessage("该配置类别已存在，请重新输入！");
			} else {
				return Message.newFailureMessage("该配置类别存在于该产品或该产品下的模块内，请重新输入！");
			}
		}
		return Message.newSuccessMessage("");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message addConfigTypeAndItems(ConfigMain configMain, List<ConfigItems> configItems) {
		if (configMain == null || configItems == null || configItems.isEmpty()) {
			return Message.newFailureMessage("增加失败，信息不完整！");
		}
		Message ms = this.checkConfigType(configMain.getProductId(), configMain.getModuleId(), configMain.getConfigType());
		if (!ms.isSuccess()) {
			return ms;
		}
		this.configMainMapper.add(configMain);
		configMain.setId(this.configCommonMapper.getLastId());
		ConfigProduct product = this.configProductService.findById(configMain.getProductId());
		ConfigModules module = this.configModulesService.findById(configMain.getModuleId());
		for (int i = 0; i < configItems.size(); i++) {
			ConfigItems item = configItems.get(i);
			item.setConfigMainId(configMain.getId());
			item.setIsExtends(0);
			item.setItemSeq(i);
			if (item.getItemIsCrypt() == 1) {
				item.setItemValue(this.cryptItemValue(product, item.getItemValue()));
			}
			this.configItemsMapper.add(item);
			item.setId(this.configCommonMapper.getLastId());
			configItems.set(i, item);
			if (module != null) { //增加的是模块级配置
				this.configService.setConfigValue(product.getProductMark(), module.getModuleMark(), item.getItemKey(), item.getItemValue());
			}
		}
		if (configMain.getModuleId() == null) { //增加产品级配置，需处理继承数据
			this.addConfigTypeAndItemsForExtends(product, configMain, configItems);
		}
		return Message.newSuccessMessage("增加配置分类及配置项成功！");
	}
	
	/**
	 * Description: 增加配置类别及配置项-处理产品模块继承<br> 
	 * Created date: 2018年1月8日
	 * @param product
	 * @param configMain
	 * @param configItems
	 * @author oblivion
	 */
	private void addConfigTypeAndItemsForExtends(ConfigProduct product, ConfigMain configMain, List<ConfigItems> configItems) {
		ConfigModules module = new ConfigModules();
		module.setProductId(configMain.getProductId());
		module.setIsExtends(1);
		PageList<ConfigModules> pl = this.configModulesService.findByEntity(module, null);
		if (pl == null || pl.isEmpty()) {
			return;
		}
		ConfigItems newItem = new ConfigItems();
		Integer extendsId = configMain.getId();
		for (ConfigModules configModules : pl) {
			configMain.setExtendsId(extendsId);
			configMain.setId(null);
			configMain.setModuleId(configModules.getId());
			this.configMainMapper.add(configMain);
			configMain.setId(this.configCommonMapper.getLastId());
			for (ConfigItems item : configItems) {
				BeanUtils.copyProperties(item, newItem);
				newItem.setId(null);
				newItem.setExtendsId(item.getId());
				newItem.setIsExtends(1);
				newItem.setConfigMainId(configMain.getId());
				this.configItemsMapper.add(newItem);
				this.configService.setConfigValue(product.getProductMark(), configModules.getModuleMark(), newItem.getItemKey(), newItem.getItemValue());
			}
		}
	}

	@Override
	public Message overWriteConfigItems(Integer productId, Integer moduleId) {
		if (productId == null || productId == 0) {
			return Message.newFailureMessage("写入失败，所选产品信息不存在！");
		}
		ConfigProduct product = this.configProductService.findById(productId);
		if (product == null) {
			return Message.newFailureMessage("写入失败，所选产品信息不存在！");
		}
		Map<String, Object> params = new HashMap<>(2);
		params.put("productId", productId);
		if (moduleId != null && moduleId != 0) {
			//选择了模块，查询该产品模块信息，以及配置信息，写入ZK
			ConfigModules module = this.configModulesService.findById(moduleId);
			if (module == null) {
				return Message.newFailureMessage("写入失败，所选模块信息不存在！");
			}
			//写入产品级全局加解密配置
			this.configService.setCryptoConfig(product.getProductMark(), module.getModuleMark(), product.getCryptType(),
					product.getCryptKey());
			params.put("moduleId", moduleId);
		} else {
			//未选择模块，查询该产品下所有的模块信息、配置信息，写入ZK
			ConfigModules entity = new ConfigModules();
			entity.setProductId(productId);
			PageList<ConfigModules> modules = this.configModulesService.findByEntity(entity, null);
			if (modules == null || modules.isEmpty()) {
				return Message.newFailureMessage("写入失败，所选产品下不存在模块信息！");
			}
			for (ConfigModules module : modules) {
				//写入产品级全局加解密配置
				this.configService.setCryptoConfig(product.getProductMark(), module.getModuleMark(), product.getCryptType(),
						product.getCryptKey());
			}
		}
		List<ConfigItems> items = this.configItemsMapper.findConfigItems(params);
		if (items == null || items.isEmpty()) {
			return Message.newFailureMessage("写入失败，所选产品下不存在配置信息！");
		}
		for (ConfigItems item : items) {
			if (StringUtils.isEmpty(item.getModuleMark())) {
				continue;
			}
			//将配置信息写入ZK
			this.configService.setConfigValue(item.getProductMark(), item.getModuleMark(), item.getItemKey(),
					item.getItemValue());
		}
		return Message.newSuccessMessage("写入成功！");
	}
}
