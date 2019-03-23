package com.coulee.foundations.configserver.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.JdbcTools;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.crypt.Crypto;
import com.coulee.aicw.foundations.utils.crypt.SecetKeyUtils;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.ConfigServerConstants;
import com.coulee.foundations.configserver.dao.ConfigCommonMapper;
import com.coulee.foundations.configserver.dao.ConfigItemsMapper;
import com.coulee.foundations.configserver.dao.ConfigMainMapper;
import com.coulee.foundations.configserver.dao.ConfigModulesMapper;
import com.coulee.foundations.configserver.dao.ConfigProductMapper;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigDataService;
import com.coulee.foundations.configserver.service.IConfigService;

/**
 * Description: 配置中心-配置导入service接口实现类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Service
public class ConfigDataServiceImpl implements IConfigDataService {
	
	@Autowired
	private ConfigProductMapper configProductMapper;

	@Autowired
	private ConfigModulesMapper configModulesMapper;

	@Autowired
	private ConfigMainMapper configMainMapper;

	@Autowired
	private ConfigItemsMapper configItemsMapper;

	@Autowired
	private ConfigCommonMapper configCommonMapper;

	@Autowired
	private IConfigService configService;
	
	/**
	 * SQLITE数据库驱动
	 */
	private final String SQLITE_DRIVER = "org.sqlite.JDBC";
	
	/**
	 * Description: 获取操作数据文件的jdbc连接<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @return
	 * @author oblivion
	 */
	private JdbcTools getJdbcTools(String dbFilePath) {
		return new JdbcTools(SQLITE_DRIVER, "jdbc:sqlite:" + dbFilePath, "", "");
	}
	
	/**
	 * Description: 将查询结果集元素由map转为实体对象<br> 
	 * Created date: 2018年1月10日
	 * @param list
	 * @param clazz
	 * @return
	 * @author oblivion
	 */
	private <T> List<T> mapConverToList(List<Map<String, Object>> list, Class<T> clazz) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<T> ret = new ArrayList<>(list.size());
		for (Map<String, Object> map : list) {
			ret.add(ObjTransTools.map2entity(map, clazz));
		}
		return ret;
	}
	
	/**
	 * Description: 多条数据查询<br> 
	 * Created date: 2018年1月10日
	 * @param dbFilePath
	 * @param sql
	 * @param param
	 * @param retClass
	 * @return
	 * @author oblivion
	 */
	private <T> List<T> queryPlural(String dbFilePath, String sql, JdbcTools.Parameters param, Class<T> retClass) {
		JdbcTools tools = this.getJdbcTools(dbFilePath);
		try {
			List<Map<String, Object>> list = tools.queryPlural(param, sql);
			List<T> ret = this.mapConverToList(list, retClass);
			return ret == null ? new ArrayList<>(0) : ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<T>(0);
	}

	@Override
	public List<ConfigProduct> findProduct(String dbFilePath) {
		String sql = "SELECT ID AS id, PRODUCT_NAME AS productName, PRODUCT_MARK AS productMark, CRYPT_TYPE AS cryptType, CRYPT_KEY AS cryptKey, CREATE_UUID AS createUuid FROM PT_CONFIG_PRODUCT";
		List<ConfigProduct> tempProducts = this.queryPlural(dbFilePath, sql, null, ConfigProduct.class);
		if (tempProducts == null || tempProducts.isEmpty()) {
			return tempProducts;
		}
		Map<String, ConfigProduct> realProductsMap = this.findRealProduct();
		for (ConfigProduct product : tempProducts) {
			//真实数据
			ConfigProduct realProduct = realProductsMap.get(product.getProductMark());
			if (realProduct != null) {
				product.setRealId(realProduct.getId());
				product.setDataVersion(ConfigServerConstants.DATA_VERSION_OLD);
			} else {
				product.setDataVersion(ConfigServerConstants.DATA_VERSION_NEW);
			}
		}
		return tempProducts;
	}
	
	/**
	 * Description: 查询真实的产品数据，返回产品标识为key，产品对象为value的链表<br> 
	 * Created date: 2018年1月11日
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigProduct> findRealProduct() {
		PageList<ConfigProduct> realList = this.configProductMapper.findByParams(null, null);
		if (realList == null || realList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigProduct> map = new HashMap<>(realList.size());
		for (ConfigProduct product : realList) {
			map.put(product.getProductMark(), product);
		}
		return map;
	}

	@Override
	public List<ConfigModules> findModules(String dbFilePath, Integer productId, Integer realProductId) {
		String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_NAME AS moduleName, MODULE_MARK AS moduleMark, IS_EXTENDS AS isExtends FROM PT_CONFIG_MODULES WHERE PRODUCT_ID = ?";
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, productId);
		List<ConfigModules> tempModules = this.queryPlural(dbFilePath, sql, param, ConfigModules.class);
		if (tempModules == null || tempModules.isEmpty()) {
			return tempModules;
		}
		Map<String, ConfigModules> realModulesMap = this.findRealModules(realProductId);
		for (ConfigModules module : tempModules) {
			//真实数据
			ConfigModules realModule = realModulesMap.get(module.getModuleMark());
			if (realModule != null) {
				module.setIsExtends(realModule.getIsExtends());
				module.setRealId(realModule.getId());
				module.setDataVersion(ConfigServerConstants.DATA_VERSION_OLD);
			} else {
				module.setDataVersion(ConfigServerConstants.DATA_VERSION_NEW);
			}
		}
		return tempModules;
	}
	
	/**
	 * Description: 根据真实的产品ID查询其下的真实模块数据<br> 
	 * Created date: 2018年1月11日
	 * @param realId
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigModules> findRealModules(Integer realId) {
		if (realId == null || realId == 0) {
			return new HashMap<>(0);
		}
		Map<String, Object> params = new HashMap<>(1);
		params.put("productId", realId);
		PageList<ConfigModules> realList = this.configModulesMapper.findByParams(params, null);
		if (realList == null || realList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigModules> map = new HashMap<>(realList.size());
		for (ConfigModules module : realList) {
			map.put(module.getModuleMark(), module);
		}
		return map;
	}
	

	@Override
	public List<ConfigMain> findConfigType(String dbFilePath, Integer productId, Integer moduleId, Integer realProductId, Integer realModuleId) {
		if (productId == null || productId == 0) {
			return null;
		}
		StringBuffer sql = new StringBuffer("SELECT ID AS id, PRODUCT_ID AS productId, MODULE_ID AS moduleId, CONFIG_TYPE AS configType, CREATE_UUID AS createUuid, EXTENDS_ID AS extendsId FROM PT_CONFIG_MAIN WHERE 1=1 ");
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		//查询产品级配置
		if (moduleId == null || moduleId == 0) {
			sql.append("AND PRODUCT_ID = ? AND MODULE_ID IS NULL ");
			param.setInt(1, productId);
		} else {
			sql.append("AND PRODUCT_ID = ? AND MODULE_ID = ?");
			param.setInt(1, productId);
			param.setInt(2, moduleId);
		}
		List<ConfigMain> newConfigMainList = this.queryPlural(dbFilePath, sql.toString(), param, ConfigMain.class);
		if (newConfigMainList == null || newConfigMainList.isEmpty()) {
			return newConfigMainList;
		}
		//临时数据模块ID不为空，真实数据模块ID为空，则为新增模块，模块下所有分类也为新增
		boolean isNewConfigMain = (moduleId != null && moduleId != 0) && (realModuleId == null || realModuleId == 0);
		if (isNewConfigMain) { 
			for (ConfigMain main : newConfigMainList) {
				main.setDataVersion(ConfigServerConstants.DATA_VERSION_NEW);
			}
		} else {
			Map<String, ConfigMain> realConfigMainMap = this.findRealConfigType(realProductId, realModuleId, (moduleId == null || moduleId == 0));
			for (ConfigMain main : newConfigMainList) {
				//真实数据
				ConfigMain realMain = realConfigMainMap.get(main.getConfigType());
				if (realMain != null) {
					main.setRealId(realMain.getId());
					main.setDataVersion(ConfigServerConstants.DATA_VERSION_OLD);
				} else {
					main.setDataVersion(ConfigServerConstants.DATA_VERSION_NEW);
				}
			}
		}
		return newConfigMainList;
	}
	
	/**
	 * Description: 根据真实的产品ID和模块ID查询配置类别<br> 
	 * Created date: 2018年1月11日
	 * @param realProductId
	 * @param realModuleId
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigMain> findRealConfigType(Integer realProductId, Integer realModuleId, boolean isProductLevel) {
		if (realProductId == null || realProductId == 0) {
			return new HashMap<>(0);
		}
		List<ConfigMain> realList = null;
		if (isProductLevel) {
			realList = this.configMainMapper.findProductLevelConfig(realProductId);
		} else {
			Map<String, Object> params = new HashMap<>(1);
			params.put("productId", realProductId);
			params.put("moduleId", realModuleId);
			realList = this.configMainMapper.findByParams(params, null);
		}
		if (realList == null || realList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigMain> map = new HashMap<>(realList.size());
		for (ConfigMain main : realList) {
			map.put(main.getConfigType(), main);
		}
		return map;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<ConfigItems> findConfigItemsByType(String dbFilePath, Integer configTypeId, Integer realConfigTypeId) {
		List<ConfigItems> newConfigItemList = this.findNewConfigItemsByType(dbFilePath, configTypeId);
		if (newConfigItemList == null || newConfigItemList.isEmpty()) {
			return newConfigItemList;
		}
		Map<String, ConfigItems> realConfigMainMap = this.findRealConfigItemsMapByType(realConfigTypeId);
		TreeSet<Integer> seqs = new TreeSet<>();
		for (ConfigItems item : newConfigItemList) {
			//真实数据
			ConfigItems realItem = realConfigMainMap.get(item.getItemKey());
			if (realItem != null && realItem.getItemIsCrypt().intValue() == item.getItemIsCrypt().intValue()) {
				this.updateNewItemIfNecessary(item, realItem, dbFilePath);
				item.setRealId(realItem.getId());
				item.setDataVersion(ConfigServerConstants.DATA_VERSION_OLD);
				seqs.add(realItem.getItemSeq());
			} else {
				item.setDataVersion(ConfigServerConstants.DATA_VERSION_NEW);
			}
		}
		//重置新数据的排序号码
		int lastSeq = seqs.isEmpty() ? 0 : seqs.last();
		for (ConfigItems item : newConfigItemList) {
			if (ConfigServerConstants.DATA_VERSION_NEW.equals(item.getDataVersion())) {
				item.setItemSeq(++lastSeq);
				this.updateNewItem(item, dbFilePath);
			}
		}
		Collections.sort(newConfigItemList, new Comparator<ConfigItems>() {
			@Override
			public int compare(ConfigItems o1, ConfigItems o2) {
				return o1.getItemSeq() > o2.getItemSeq() ? 1 : -1;
			}
		});
		return newConfigItemList;
	}
	
	/**
	 * Description: 根据新的类别ID查询配置项<br> 
	 * Created date: 2018年1月11日
	 * @param dbFilePath
	 * @param configTypeId
	 * @return
	 * @author oblivion
	 */
	private List<ConfigItems> findNewConfigItemsByType(String dbFilePath, Integer configTypeId) {
		String sql = "SELECT ID AS id, CONFIG_MAIN_ID AS configMainId, ITEM_DESC AS itemDesc, ITEM_IS_CRYPT AS itemIsCrypt, ITEM_KEY AS itemKey, " + 
				"ITEM_VALUE AS itemValue, ITEM_SEQ AS itemSeq, IS_EXTENDS AS isExtends, EXTENDS_ID AS extendsId FROM PT_CONFIG_ITEMS WHERE CONFIG_MAIN_ID = ? ORDER BY ITEM_SEQ";
		if (configTypeId == null || configTypeId == 0) {
			return new ArrayList<ConfigItems>(0);
		}
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, configTypeId);
		return this.queryPlural(dbFilePath, sql, param, ConfigItems.class);
	}
	
	/**
	 * Description: 根据新的类别ID查询配置项，返回Key为配置项id，value为配置项对象的map<br> 
	 * Created date: 2018年1月11日
	 * @param dbFilePath
	 * @param configTypeId
	 * @return
	 * @author oblivion
	 */
	private Map<Integer, ConfigItems> findNewConfigItemsMapByType(String dbFilePath, Integer configTypeId) {
		List<ConfigItems> newConfigItemList = this.findNewConfigItemsByType(dbFilePath, configTypeId);
		if (newConfigItemList == null || newConfigItemList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<Integer, ConfigItems> map = new HashMap<>(newConfigItemList.size());
		for (ConfigItems item : newConfigItemList) {
			map.put(item.getId(), item);
		}
		return map;
	}
	
	/**
	 * Description: 根据真实的类别ID查询配置项，返回Key为配置项key，value为配置项对象的map<br> 
	 * Created date: 2018年1月11日
	 * @param realConfigTypeId
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigItems> findRealConfigItemsMapByType(Integer realConfigTypeId) {
		if (realConfigTypeId == null || realConfigTypeId == 0) {
			return new HashMap<>(0);
		}
		Map<String, Object> params = new HashMap<>(1);
		params.put("configMainId", realConfigTypeId);
		List<ConfigItems> realList = this.configItemsMapper.findConfigItems(params);
		if (realList == null || realList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigItems> map = new HashMap<>(realList.size());
		for (ConfigItems item : realList) {
			String zkItemValue = this.configService.getConfigValue(item.getProductMark(), item.getModuleMark(), item.getItemKey());
			if (!StringUtils.isEmpty(zkItemValue)) {
				item.setItemValue(zkItemValue);
			}
			map.put(item.getItemKey(), item);
		}
		return map;
	}
	
	/**
	 * Description: 比较临时配置与已存在的真实配置，如不同则将真实配置更新至临时配置<br> 
	 * Created date: 2018年1月11日
	 * @param newItem
	 * @param realItem
	 * @param dbFilePath
	 * @author oblivion
	 */
	private void updateNewItemIfNecessary(ConfigItems newItem, ConfigItems realItem, String dbFilePath) {
		boolean needUpdate = false;
		if (this.checkIsDifferent(newItem.getItemValue(), realItem.getItemValue())) {
			newItem.setItemValue(realItem.getItemValue());
			needUpdate = true;
		}
		if (this.checkIsDifferent(newItem.getItemSeq(), realItem.getItemSeq())) {
			newItem.setItemSeq(realItem.getItemSeq());
			needUpdate = true;
		}
		if (needUpdate) {
			this.updateNewItem(newItem, dbFilePath);
		}
	}
	
	/**
	 * Description: 更新临时配置项数据<br> 
	 * Created date: 2018年1月11日
	 * @param item
	 * @author oblivion
	 */
	private void updateNewItem(ConfigItems item, String dbFilePath) {
		String sql = "UPDATE PT_CONFIG_ITEMS SET ITEM_VALUE = ?, ITEM_SEQ =?, IS_EXTENDS = ?, EXTENDS_ID = ? WHERE ID = ?";
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setString(1, item.getItemValue());
		param.setObject(2, item.getItemSeq());
		param.setObject(3, item.getIsExtends());
		param.setObject(4, item.getExtendsId());
		param.setObject(5, item.getId());
		JdbcTools tools = this.getJdbcTools(dbFilePath);
		try {
			tools.update(param, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
	/**
	 * Description: 判断两对象是否一致<br> 
	 * Created date: 2018年1月11日
	 * @param newValue
	 * @param realValue
	 * @return
	 * @author oblivion
	 */
	private boolean checkIsDifferent(Object newValue, Object realValue) {
		if (newValue == null && realValue == null) {
			return false;
		}
		if (newValue == null || realValue == null) {
			return true;
		} else {
			return !newValue.equals(realValue);
		}
	}

	@Override
	public Message checkHaveConfigItems(String dbFilePath, Integer productId, Integer moduleId) {
		StringBuffer sql = new StringBuffer("SELECT * FROM PT_CONFIG_ITEMS WHERE CONFIG_MAIN_ID IN (SELECT ID FROM PT_CONFIG_MAIN WHERE PRODUCT_ID = ?");
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, productId);
		if (moduleId != null && moduleId != 0) {
			sql.append(" AND MODULE_ID = ? ");
			param.setInt(2, moduleId);
		}
		sql.append(")");
		JdbcTools tools = this.getJdbcTools(dbFilePath);
		try {
			int i = tools.queryDataCount(param, sql.toString());
			if (i > 0) {
				return Message.newSuccessMessage("该产品及其模块下存在配置项");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Message.newFailureMessage("该产品及其模块下无配置项");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message saveConfigItems(String dbFilePath, Integer configTypeId, List<ConfigItems> items) {
		if (items == null || items.isEmpty()) {
			return Message.newFailureMessage("保存失败，不存在配置项！");
		}
		ConfigProduct product = this.findProductByConfigMainId(dbFilePath, configTypeId);
		if (product == null) {
			return Message.newFailureMessage("保存失败，不存在该配置项的产品信息！");
		}
		String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_ID AS moduleId, CONFIG_TYPE AS configType, CREATE_UUID AS createUuid, EXTENDS_ID AS extendsId FROM PT_CONFIG_MAIN WHERE ID = ?";
		JdbcTools tools = this.getJdbcTools(dbFilePath);
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setObject(1, configTypeId);
		boolean isProductLevel = false; //是否产品级配置
		try {
			Map<String, Object> configMain = tools.querySingular(param, sql);
			if (configMain == null) {
				return Message.newFailureMessage("保存失败，不存在配置类别信息！");
			}
			Object moduleId = configMain.get("moduleId");
			if (moduleId == null || (Integer)moduleId == 0) {
				isProductLevel = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<Integer, ConfigItems> itemsMap = this.findNewConfigItemsMapByType(dbFilePath, configTypeId);
		for (ConfigItems item : items) {
			ConfigItems tmp = itemsMap.get(item.getId());
			if (tmp == null) {
				continue;
			}
			String tmpValue = tmp.getItemValue() == null ? "" : tmp.getItemValue();
			String itemValue = item.getItemValue() == null ? "" : item.getItemValue();
			if (tmpValue.equals(itemValue)) { //无修改
				continue;
			}
			if (item.getItemIsCrypt() == 1) {
				item.setItemValue(this.cryptItemValue(product, item.getItemValue()));
			}
			this.updateNewItem(item, dbFilePath);
			if (isProductLevel) {
				this.saveConfigItemsForExtends(dbFilePath, item.getId(), item.getItemValue());
			}
		}
		return Message.newSuccessMessage("保存成功");
	}
	
	/**
	 * Description: 保存临时配置项-处理继承信息<br> 
	 * Created date: 2018年1月11日
	 * @param dbFilePath
	 * @param extendsId
	 * @param itemValue
	 * @author oblivion
	 */
	private void saveConfigItemsForExtends(String dbFilePath, Integer extendsId, String itemValue) {
		String sql = "SELECT ID AS id, CONFIG_MAIN_ID AS configMainId, ITEM_DESC AS itemDesc, ITEM_IS_CRYPT AS itemIsCrypt, ITEM_KEY AS itemKey, " + 
				"ITEM_VALUE AS itemValue, ITEM_SEQ AS itemSeq, IS_EXTENDS AS isExtends, EXTENDS_ID AS extendsId FROM PT_CONFIG_ITEMS WHERE IS_EXTENDS = 1 AND EXTENDS_ID = ?";
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, extendsId);
		List<ConfigItems> list = this.queryPlural(dbFilePath, sql, param, ConfigItems.class);
		if (list != null && !list.isEmpty()) {
			for (ConfigItems item : list) {
				item.setItemValue(itemValue);
				this.updateNewItem(item, dbFilePath);
			}
		}
	}
	
	
	/**
	 * Description: 根据配置类别ID获取其所属产品信息<br> 
	 * Created date: 2018年1月11日
	 * @param dbFilePath
	 * @param configMainId
	 * @return
	 * @author oblivion
	 */
	private ConfigProduct findProductByConfigMainId(String dbFilePath, Integer configMainId) {
		String sql = "SELECT ID AS id, PRODUCT_NAME AS productName, PRODUCT_MARK AS productMark, CRYPT_TYPE AS cryptType, CRYPT_KEY AS cryptKey, CREATE_UUID AS createUuid " + 
				"FROM PT_CONFIG_PRODUCT WHERE ID = (SELECT PRODUCT_ID FROM PT_CONFIG_MAIN WHERE ID = ?)";
		JdbcTools tools = this.getJdbcTools(dbFilePath);
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setObject(1, configMainId);
		try {
			Map<String, Object> productMap = tools.querySingular(param, sql);
			ConfigProduct product = ObjTransTools.map2entity(productMap, ConfigProduct.class);
			Map<String, Object> params = new HashMap<>(1);
			params.put("productMark", product.getProductMark());
			PageList<ConfigProduct> pl = this.configProductMapper.findByParams(params, null);
			if (pl != null && !pl.isEmpty()) { //如果存在真实产品信息，则返回真实数据，否则为新增产品
				product = pl.get(0);
			}
			return product;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: 对配置项值加密<br> 
	 * Created date: 2018年1月11日
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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message importType(String dbFilePath, String params) {
		JSONObject jo = JSON.parseObject(params);
		Integer productId = jo.getInteger("productId");
		Integer realProductId = jo.getInteger("realProductId");
		Integer moduleId = jo.getInteger("moduleId");
		Integer realModuleId = jo.getInteger("realModuleId");
		Integer typeId = jo.getInteger("typeId");
		Integer realTypeId = jo.getInteger("realTypeId");
		try {
			ConfigProduct product = this.importPorcessProduct(dbFilePath, productId, realProductId);
			Map<Integer, ConfigModules> modules = this.importProcessModule(dbFilePath, product, moduleId, realModuleId);
			Map<Integer, ConfigMain> configMains = this.importProcessConfigType(dbFilePath, product, moduleId, modules, typeId, realTypeId);
			this.importPorcessConfigItems(dbFilePath, typeId, product, modules, configMains);
		} catch (Exception e) {
			e.printStackTrace();
			return Message.newFailureMessage(e.getMessage());
		}
		return Message.newSuccessMessage("导入成功");
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Message importAll(String dbFilePath, String params) {
		JSONObject jo = JSON.parseObject(params);
		Integer productId = jo.getInteger("productId");
		Integer realProductId = jo.getInteger("realProductId");
		Integer moduleId = jo.getInteger("moduleId");
		Integer realModuleId = jo.getInteger("realModuleId");
		try {
			ConfigProduct product = this.importPorcessProduct(dbFilePath, productId, realProductId);
			Map<Integer, ConfigModules> modules = this.importProcessModule(dbFilePath, product, moduleId, realModuleId);
			Map<Integer, ConfigMain> configMains = this.importProcessConfigTypeForAll(dbFilePath, product, modules);
			this.importPorcessConfigItems(dbFilePath, null, product, modules, configMains);
		} catch (Exception e) {
			e.printStackTrace();
			return Message.newFailureMessage(e.getMessage());
		}
		return Message.newSuccessMessage("导入成功");
	}
	
	/**
	 * Description: 导入配置，处理产品信息<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param productId 产品临时数据ID
	 * @param realProductId 产品真实数据ID
	 * @return 包含真实数据ID的产品对象
	 * @throws Exception
	 * @author oblivion
	 */
	private ConfigProduct importPorcessProduct(String dbFilePath, Integer productId, Integer realProductId) throws Exception {
		ConfigProduct product = null;
		if (realProductId != null && realProductId != 0) { //真实数据内已存在的产品
			product = this.configProductMapper.findById(realProductId);
			product.setId(productId);
			product.setRealId(realProductId);
		} else { //真实数据内不存在，将临时数据内的产品信息存入真实数据内
			String sql = "SELECT ID AS id, PRODUCT_NAME AS productName, PRODUCT_MARK AS productMark, CRYPT_TYPE AS cryptType, CRYPT_KEY AS cryptKey, CREATE_UUID AS createUuid FROM PT_CONFIG_PRODUCT WHERE ID = ?";
			JdbcTools tools = this.getJdbcTools(dbFilePath);
			JdbcTools.Parameters param = new JdbcTools.Parameters();
			param.setInt(1, productId);
			try {
				Map<String, Object> productMap = tools.querySingular(param, sql);
				ConfigProduct tmpProduct = ObjTransTools.map2entity(productMap, ConfigProduct.class);
				Integer tmpDataId = tmpProduct.getId();
				tmpProduct.setId(null);
				this.configProductMapper.add(tmpProduct);
				tmpProduct.setId(tmpDataId);
				tmpProduct.setRealId(this.configCommonMapper.getLastId());
				product = tmpProduct;
			} catch (Exception e) {
				throw new Exception("产品数据不存在");
			}
		}
		return product;
	}
	
	/**
	 * Description: 导入配置，处理模块信息<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param product 包含真实产品ID的产品对象
	 * @param moduleId 模块临时数据ID
	 * @param realModuleId 模块真实数据ID
	 * @return key为模块临时数据ID，value为包含真实数据ID的模块对象
	 * @throws Exception
	 * @author oblivion
	 */
	private Map<Integer, ConfigModules> importProcessModule(String dbFilePath, ConfigProduct product, Integer moduleId, Integer realModuleId) throws Exception {
		Map<Integer, ConfigModules> map = new HashMap<>(10);
		if (moduleId != null && moduleId != 0) { //选择了模块，为模块级配置
			if (realModuleId != null && realModuleId != 0) { //选择的模块为真实数据存在的
				ConfigModules module = this.configModulesMapper.findById(realModuleId);
				module.setId(moduleId);
				module.setRealId(realModuleId);
				map.put(module.getId(), module);
			} else { //选择的模块不存在于真实数据内，将临时数据内的模块信息存入真实数据内
				String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_NAME AS moduleName, MODULE_MARK AS moduleMark, IS_EXTENDS AS isExtends FROM PT_CONFIG_MODULES WHERE ID = ?";
				JdbcTools tools = this.getJdbcTools(dbFilePath);
				JdbcTools.Parameters param = new JdbcTools.Parameters();
				param.setInt(1, moduleId);
				try {
					Map<String, Object> moduleMap = tools.querySingular(param, sql);
					ConfigModules tmpModule = ObjTransTools.map2entity(moduleMap, ConfigModules.class);
					this.importProcessModuleAdd(product, tmpModule);
					map.put(tmpModule.getId(), tmpModule);
				} catch (Exception e) {
					throw new Exception("模块数据不存在");
				}
			}
		} else { //未选择模块，为产品级配置
			List<ConfigModules> list = this.findModules(dbFilePath, product.getId(), product.getRealId());
			for (ConfigModules module : list) {
				//模块不存在于真实数据内
				if(module.getRealId() == null || module.getRealId() == 0) {
					this.importProcessModuleAdd(product, module);
				}
				map.put(module.getId(), module);
			}
		}
		return map;
	}
	
	/**
	 * Description: 导入配置，处理模块信息，增加新模块<br> 
	 * Created date: 2018年1月13日
	 * @param product 包含真实产品ID的产品对象
	 * @param newModule 要新增的模块对象
	 * @author oblivion
	 */
	private void importProcessModuleAdd(ConfigProduct product, ConfigModules newModule) {
		Integer tmpDataId = newModule.getId();
		newModule.setId(null);
		newModule.setProductId(product.getRealId());
		this.configModulesMapper.add(newModule);
		newModule.setId(tmpDataId);
		newModule.setRealId(this.configCommonMapper.getLastId());
		//新增模块，初始化其全局加解密配置信息
		this.configService.setCryptoConfig(product.getProductMark(), newModule.getModuleMark(), product.getCryptType(),
				product.getCryptKey());
	}
	
	/**
	 * Description: 导入全部配置，处理配置类别信息<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param product 包含真实产品ID的产品对象
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @return key为配置分类临时数据ID，value为包含真实数据ID的配置分类对象
	 * @throws Exception
	 * @author oblivion
	 */
	private Map<Integer, ConfigMain> importProcessConfigTypeForAll(String dbFilePath, ConfigProduct product, Map<Integer, ConfigModules> modules) throws Exception {
		//未选择分类，为全部导入，导入原则，先将产品级配置全部导入，如果选择模块则导入该模块配置，否则导入全部
		Map<Integer, ConfigMain> map = new HashMap<>(10);
		String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_ID AS moduleId, CONFIG_TYPE AS configType, CREATE_UUID AS createUuid, EXTENDS_ID AS extendsId FROM PT_CONFIG_MAIN WHERE PRODUCT_ID = ? ";
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, product.getId());
		List<ConfigMain> tempConfigMainList = this.queryPlural(dbFilePath, sql.toString(), param, ConfigMain.class);
		Map<String, ConfigMain> realConfigMainList = this.findExtendsRealConfigMain(product.getRealId(), null);
		List<ConfigMain> extendsMains = new ArrayList<>(tempConfigMainList.size());
		for (ConfigMain tempMain : tempConfigMainList) {
			if (tempMain.getModuleId() == null || tempMain.getModuleId() == 0) { //产品级配置
				ConfigMain realMain = realConfigMainList.get(tempMain.getConfigType() + "_" + tempMain.getModuleId());
				if (realMain != null) { // 存在该配置分类数据
					realMain.setRealId(realMain.getId());
					realMain.setId(tempMain.getId());
					map.put(tempMain.getId(), realMain);
				} else { //不存在，将临时数据内的配置分类信息存入真实数据内
					this.importProcessConfigMainAdd(product, null, tempMain, modules);
					map.put(tempMain.getId(), tempMain);
				}
			} else { //模块级配置
				ConfigModules realConfigModule = modules.get(tempMain.getModuleId());
				if (realConfigModule == null) { //该配置类别所属模块还未导入至真实数据内
					continue;
				}
				ConfigMain realMain = realConfigMainList.get(tempMain.getConfigType() + "_" + realConfigModule.getRealId());
				if (realMain != null) { // 存在该配置分类数据
					realMain.setRealId(realMain.getId());
					realMain.setId(tempMain.getId());
					map.put(tempMain.getId(), realMain);
				} else { //不存在，将临时数据内的配置分类信息存入真实数据内
					if (tempMain.getExtendsId() != null && tempMain.getExtendsId() != 0) {
						extendsMains.add(tempMain); //如果为继承类别则不处理，先处理父类别
					} else {
						this.importProcessConfigMainAdd(product, tempMain.getModuleId(), tempMain, modules);
						map.put(tempMain.getId(), tempMain);
					}
				}
			}
		}
		//处理继承类别
		for (ConfigMain extendsMain : extendsMains) {
			extendsMain.setExtendsId(map.get(extendsMain.getExtendsId()).getRealId());
			this.importProcessConfigMainAdd(product, extendsMain.getModuleId(), extendsMain, modules);
			map.put(extendsMain.getId(), extendsMain);
		}
		return map;
	}
	
	/**
	 * Description: 导入配置类别，处理配置类别信息<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param product 包含真实产品ID的产品对象
	 * @param moduleId 模块临时数据ID
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @param typeId 配置类别临时数据ID
	 * @param realTypeId 配置类别真实数据ID
	 * @return key为配置分类临时数据ID，value为包含真实数据ID的配置分类对象
	 * @throws Exception
	 * @author oblivion
	 */
	private Map<Integer, ConfigMain> importProcessConfigType(String dbFilePath, ConfigProduct product, Integer moduleId, Map<Integer, ConfigModules> modules, Integer typeId,
			Integer realTypeId) throws Exception {
		Map<Integer, ConfigMain> map = new HashMap<>(10);
		ConfigMain configMain = null;
		if (realTypeId != null && realTypeId != 0) { //选择的分类为真实数据存在的
			configMain = this.configMainMapper.findById(realTypeId);
			configMain.setId(typeId);
			configMain.setRealId(realTypeId);
			map.put(typeId, configMain);
			if (moduleId == null || moduleId == 0) { //未选择模块，为产品级配置，处理继承
				this.importProcessConfigMainExtends(dbFilePath, product, configMain.getId(), configMain.getRealId(), modules, map);
			}
		} else { //选择的分类不存在于真实数据内，将临时数据内的分类信息存入真实数据内
			String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_ID AS moduleId, CONFIG_TYPE AS configType, CREATE_UUID AS createUuid, EXTENDS_ID AS extendsId FROM PT_CONFIG_MAIN WHERE 1=1  AND ID = ? ";
			JdbcTools tools = this.getJdbcTools(dbFilePath);
			JdbcTools.Parameters param = new JdbcTools.Parameters();
			try {
				param.setInt(1, typeId);
				Map<String, Object> typeMap = tools.querySingular(param, sql);
				configMain = ObjTransTools.map2entity(typeMap, ConfigMain.class);
				this.importProcessConfigMainAdd(product, moduleId, configMain, modules);
				map.put(configMain.getId(), configMain);
			} catch (Exception e) {
				throw new Exception("模块数据不存在");
			}
			if (moduleId == null || moduleId == 0) { //未选择模块，为产品级配置，处理继承
				this.importProcessConfigMainExtends(dbFilePath, product, configMain.getId(), configMain.getRealId(), modules, map);
			}
		}
		return map;
	}
	
	/**
	 * Description: 导入配置，处理配置类别信息-继承情况<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath 
	 * @param product 包含真实产品ID的产品对象
	 * @param tempExtendsId 临时数据内的分类继承ID
	 * @param realExtendsId 真实数据内的分类继承ID
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @param retMap
	 * @author oblivion
	 */
	private void importProcessConfigMainExtends(String dbFilePath, ConfigProduct product, Integer tempExtendsId,
			Integer realExtendsId, Map<Integer, ConfigModules> modules, Map<Integer, ConfigMain> retMap) {
		//根据临时数据的分类继承ID查询继承自该ID的临时数据，根据真实数据的分类继承ID查询继承自该ID的真实数据，进行比较过滤，将不存在于的真实数据内的临时数据保存
		String sql = "SELECT ID AS id, PRODUCT_ID AS productId, MODULE_ID AS moduleId, CONFIG_TYPE AS configType, CREATE_UUID AS createUuid, EXTENDS_ID AS extendsId FROM PT_CONFIG_MAIN WHERE 1=1  AND EXTENDS_ID = ? ";
		JdbcTools.Parameters param = new JdbcTools.Parameters();
		param.setInt(1, tempExtendsId);
		List<ConfigMain> tempExtendsConfigMains = this.queryPlural(dbFilePath, sql, param, ConfigMain.class);
		Map<String, ConfigMain> realExtendsConfigMains = this.findExtendsRealConfigMain(product.getRealId(), realExtendsId);
		for (ConfigMain tempMain : tempExtendsConfigMains) {
			ConfigMain realMain = realExtendsConfigMains.get(tempMain.getConfigType() + "_" + modules.get(tempMain.getModuleId()).getRealId());
			if (realMain != null) {// 存在该配置分类数据
				realMain.setRealId(realMain.getId());
				realMain.setId(tempMain.getId());
				retMap.put(realMain.getId(), realMain);
			} else {//不存在该配置分类数据，增加至真实数据内
				if (tempMain.getModuleId() != null && tempMain.getModuleId() != 0 && modules.get(tempMain.getModuleId()) == null) {
					//此模块还未添加至真实数据内
					continue;
				}
				tempMain.setProductId(product.getRealId());
				tempMain.setExtendsId(realExtendsId);
				this.importProcessConfigMainAdd(product, tempMain.getModuleId(), tempMain, modules);
				retMap.put(tempMain.getId(), tempMain);
			}
		}
	}
	
	/**
	 * Description: 根据类别ID查询继承自该ID的真实类别信息<br> 
	 * Created date: 2018年1月13日
	 * @param productId
	 * @param extendsId
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigMain> findExtendsRealConfigMain(Integer productId, Integer extendsId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("productId", productId);
		if (extendsId != null && extendsId != 0) {
			params.put("orExtendsId", extendsId);
		}
		PageList<ConfigMain> mains = this.configMainMapper.findByParams(params, null);
		if (mains == null || mains.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigMain> map = new HashMap<>(mains.size());
		for (ConfigMain m : mains) {
			map.put(m.getConfigType() + "_" + m.getModuleId(), m);
		}
		return map;
	}
	
	
	/**
	 * Description: 导入配置，处理配置类别信息，增加配置类别<br> 
	 * Created date: 2018年1月13日
	 * @param product 包含真实产品ID的产品对象
	 * @param moduleId 模块临时数据ID
	 * @param configMain 要增加的配置类别对象
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @author oblivion
	 */
	private void importProcessConfigMainAdd(ConfigProduct product, Integer moduleId, ConfigMain configMain, Map<Integer, ConfigModules> modules) {
		Integer tmpDataId = configMain.getId();
		configMain.setId(null);
		configMain.setProductId(product.getRealId());
		if (moduleId != null && moduleId != 0 && modules.get(moduleId) != null) {
			configMain.setModuleId(modules.get(moduleId).getRealId());
		}
		this.configMainMapper.add(configMain);
		configMain.setId(tmpDataId);
		configMain.setRealId(this.configCommonMapper.getLastId());
	}
	
	/**
	 * Description: 导入配置，处理配置项信息<br> 
	 * Created date: 2018年1月13日
	 * @param dbFilePath
	 * @param newItemIds 新的配置项ID集合
	 * @param product 包含真实产品ID的产品对象
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @param configMains 配置分类Map，key为配置分类临时数据ID，value为包含真实数据ID的配置分类对象
	 * @author oblivion
	 */
	private void importPorcessConfigItems(String dbFilePath, Integer typeId, ConfigProduct product,
			Map<Integer, ConfigModules> modules, Map<Integer, ConfigMain> configMains) {
		if (typeId != null && typeId != 0) { //导入分类，根据前端传入的配置项ID查询临时数据，保存至真实数据内
			StringBuffer sql = new StringBuffer("SELECT ID AS id, CONFIG_MAIN_ID AS configMainId, ITEM_DESC AS itemDesc, ITEM_IS_CRYPT AS itemIsCrypt, ITEM_KEY AS itemKey, " + 
					"ITEM_VALUE AS itemValue, ITEM_SEQ AS itemSeq, IS_EXTENDS AS isExtends, EXTENDS_ID AS extendsId FROM PT_CONFIG_ITEMS WHERE 1=1 "
					+ " AND CONFIG_MAIN_ID IN (SELECT ID FROM PT_CONFIG_MAIN WHERE ID = ? OR EXTENDS_ID = ?)");
			JdbcTools.Parameters param = new JdbcTools.Parameters();
			param.setInt(1, typeId);
			param.setInt(2, typeId);
			List<ConfigItems> items = this.queryPlural(dbFilePath, sql.toString(), param, ConfigItems.class);
			this.importPorcessConfigItemsAddOne(items, product, modules, configMains);
		} else { //导入全部，根据临时数据配置分类查询所有临时配置项，结合已有配置项过滤出新增项，将新增项保存至真实数据内
			String sql = "SELECT ID AS id, CONFIG_MAIN_ID AS configMainId, ITEM_DESC AS itemDesc, ITEM_IS_CRYPT AS itemIsCrypt, ITEM_KEY AS itemKey, "
					+ "ITEM_VALUE AS itemValue, ITEM_SEQ AS itemSeq, IS_EXTENDS AS isExtends, EXTENDS_ID AS extendsId FROM PT_CONFIG_ITEMS WHERE "
					+ "CONFIG_MAIN_ID IN (SELECT ID FROM PT_CONFIG_MAIN WHERE PRODUCT_ID = ?) ";
			JdbcTools.Parameters param = new JdbcTools.Parameters();
			param.setInt(1, product.getId());
			List<ConfigItems> items = this.queryPlural(dbFilePath, sql, param, ConfigItems.class);
			this.importPorcessConfigItemsAddOne(items, product, modules, configMains);
		}
	}
	
	/**
	 * Description: 导入配置，处理配置项信息-增加配置项<br> 
	 * Created date: 2018年1月13日
	 * @param items 要增加的配置项集合
	 * @param product 包含真实产品ID的产品对象
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @param configMains 配置分类Map，key为配置分类临时数据ID，value为包含真实数据ID的配置分类对象
	 * @author oblivion
	 */
	private void importPorcessConfigItemsAddOne(List<ConfigItems> items, ConfigProduct product, Map<Integer, ConfigModules> modules, Map<Integer, ConfigMain> configMains) {
		Map<Integer, ConfigItems> map = new HashMap<>(10);
		Map<String, ConfigItems> realMap = this.findRealConfigItemsMapByProduct(product.getRealId());
		List<ConfigItems> extendsItems = new ArrayList<>(items.size());
		for (ConfigItems item : items) {
			ConfigMain configMain = configMains.get(item.getConfigMainId());
			if (configMain == null || realMap.get(item.getItemKey() + "_" + configMain.getRealId()) != null) {
				//如果分类不存在或配置项已存在，则忽略
				continue;
			}
			if (item.getIsExtends() == 0) { //先处理产品级配置，不处理继承
				this.importPorcessConfigItemsAddTwo(product, modules, configMains, item);
				map.put(item.getId(), item);
			} else {
				extendsItems.add(item);
			}
		}
		for (ConfigItems item : extendsItems) { //处理继承
			Integer extendsId = map.get(item.getExtendsId()) != null ? map.get(item.getExtendsId()).getRealId() : null;
			item.setExtendsId(extendsId);
			if (extendsId != null) {
				item.setIsExtends(1);
			} else {
				item.setIsExtends(0);
			}
			this.importPorcessConfigItemsAddTwo(product, modules, configMains, item);
		}
	}
	
	/**
	 * Description: 导入配置，处理配置项信息-增加配置项<br> 
	 * Created date: 2018年1月13日
	 * @param product 包含真实产品ID的产品对象
	 * @param modules 模块对象Map，key为临时数据ID，value为模块对象
	 * @param mains 配置分类Map，key为配置分类临时数据ID，value为包含真实数据ID的配置分类对象
	 * @param item 要增加的配置项
	 * @author oblivion
	 */
	private void importPorcessConfigItemsAddTwo(ConfigProduct product, Map<Integer, ConfigModules> modules, Map<Integer, ConfigMain> mains, ConfigItems item) {
		ConfigMain main = mains.get(item.getConfigMainId());
		if (main == null) { //此分类还未加入真实数据内
			return;
		}
		ConfigModules module = null;
		if (main.getModuleId() != null && main.getModuleId() != 0) {
			//根据ConfigMain里存储的真实模块ID查找真实模块信息
			for (Map.Entry<Integer, ConfigModules> entry : modules.entrySet()) {
				module = entry.getValue();
				if (module.getRealId().intValue() == main.getModuleId().intValue()) {
					break;
				}
			}
			if (module == null) {
				return; //此模块还未加入真实数据内
			}
		}
		Integer tmpDataId = item.getId();
		item.setConfigMainId(main.getRealId());
		item.setId(null);
		this.configItemsMapper.add(item);
		item.setId(tmpDataId);
		item.setRealId(this.configCommonMapper.getLastId());
		if (module != null) { //非产品级配置，写入zk
			String productMark = product.getProductMark();
			this.configService.setConfigValue(productMark, module.getModuleMark(), item.getItemKey(), item.getItemValue());
		}
	}
	
	
	/**
	 * Description: 根据真实的类别ID查询配置项，返回Key为配置项key+mainId，value为配置项对象的map<br> 
	 * Created date: 2018年1月11日
	 * @param productId
	 * @return
	 * @author oblivion
	 */
	private Map<String, ConfigItems> findRealConfigItemsMapByProduct(Integer productId) {
		if (productId == null || productId == 0) {
			return new HashMap<>(0);
		}
		Map<String, Object> params = new HashMap<>(1);
		params.put("productId", productId);
		PageList<ConfigItems> realList = this.configItemsMapper.findByParams(params, null);
		if (realList == null || realList.isEmpty()) {
			return new HashMap<>(0);
		}
		Map<String, ConfigItems> map = new HashMap<>(realList.size());
		for (ConfigItems item : realList) {
			map.put(item.getItemKey() + "_" + item.getConfigMainId(), item);
		}
		return map;
	}
}
