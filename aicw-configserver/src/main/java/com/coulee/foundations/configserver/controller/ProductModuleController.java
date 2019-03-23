package com.coulee.foundations.configserver.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigModulesService;
import com.coulee.foundations.configserver.service.IConfigProductService;

/**
 * Description: 产品模块管理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/product")
public class ProductModuleController extends BaseController {

	@Autowired
	private IConfigProductService configProductService;
	
	@Autowired
	private IConfigModulesService configModulesService;
	
	/**
	 * Description: 查询产品列表<br> 
	 * Created date: 2017年12月25日
	 * @param session
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listproducts", method = RequestMethod.POST)
	public List<ConfigProduct> listProducts(HttpSession session) {
		ConfigProduct product = new ConfigProduct();
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		String isAdmin = map.get("isAdmin");
		if (!"1".equals(isAdmin)) {
			String uuid = map.get("uuid");
			product.setCreateUuid(uuid);
		}
		return this.configProductService.findByEntity(product, null);
	}
	
	/**
	 * Description: 查询模块列表<br> 
	 * Created date: 2017年12月25日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listmodules", method = RequestMethod.POST)
	public List<ConfigModules> listModules(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		int productId = jo.getIntValue("productId");
		if (productId == 0) {
			return null;
		}
		ConfigModules modules = new ConfigModules();
		modules.setProductId(productId);
		return this.configModulesService.findByEntity(modules, null);
	}
	
	
	/**
	 * Description: 增加产品<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add/product", method = RequestMethod.POST)
	public Message addProduct(HttpSession session, @RequestBody String json) {
		ConfigProduct product = JsonTools.json2obj(json, ConfigProduct.class);
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		product.setCreateUuid(map.get("uuid"));
		return this.configProductService.add(product);
	}
	
	/**
	 * Description: 修改产品<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/update/product", method = RequestMethod.POST)
	public Message updateProduct(@RequestBody String json) {
		ConfigProduct product = JsonTools.json2obj(json, ConfigProduct.class);
		return this.configProductService.update(product);
	}
	
	/**
	 * Description: 删除产品<br> 
	 * Created date: 2017年12月26日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/delete/product", method = RequestMethod.POST)
	public Message deleteProduct(@RequestBody String json) {
		List<ConfigProduct> products = JsonTools.jsonarray2list(json, ConfigProduct.class);
		return this.configProductService.batchDelete(products);
	}
	
	
	/**
	 * Description: 增加模块<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/add/module", method = RequestMethod.POST)
	public Message addModule(@RequestBody String json) {
		ConfigModules module = JsonTools.json2obj(json, ConfigModules.class);
		return this.configModulesService.add(module);
	}
	
	/**
	 * Description: 修改模块<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/update/module", method = RequestMethod.POST)
	public Message updateModule(@RequestBody String json) {
		ConfigModules module = JsonTools.json2obj(json, ConfigModules.class);
		return this.configModulesService.update(module);
	}
	
	/**
	 * Description: 删除模块<br> 
	 * Created date: 2017年12月26日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/delete/module", method = RequestMethod.POST)
	public Message deleteModule(@RequestBody String json) {
		List<ConfigModules> products = JsonTools.jsonarray2list(json, ConfigModules.class);
		return this.configModulesService.batchDelete(products);
	}
}
