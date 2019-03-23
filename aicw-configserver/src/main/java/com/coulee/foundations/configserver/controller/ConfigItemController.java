package com.coulee.foundations.configserver.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;
import com.coulee.foundations.configserver.service.IConfigItemsService;

/**
 * Description: 配置项管理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/configitem")
public class ConfigItemController extends BaseController {
	
	@Autowired
	private IConfigItemsService configItemsService;

	/**
	 * Description: 保存配置临时方法<br> 
	 * Created date: 2017年12月25日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/savetemp", method = RequestMethod.POST)
	public Message saveConfigTemp(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer moduleId = jo.getInteger("moduleId");
		String configInfo = jo.getString("configInfo");
		return this.configItemsService.saveConfigTemp(productId, moduleId, configInfo);
	}
	
	/**
	 * Description: 根据产品ID、模块ID获取配置类别列表<br> 
	 * Created date: 2018年1月2日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listtype", method = RequestMethod.POST)
	public List<ConfigMain> findConfigType(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer moduleId = jo.getInteger("moduleId");
		return this.configItemsService.findConfigType(productId, moduleId);
	}
	
	/**
	 * Description: 根据配置项类型ID查询配置项列表<br> 
	 * Created date: 2018年1月2日
	 * @param configTypeId
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listitems", method = RequestMethod.POST)
	public List<ConfigItems> findConfigItems(@RequestBody Map<String,Integer> map) {
		return this.configItemsService.findConfigItemsByType(map.get("configTypeId"));
	}
	
	/**
	 * Description: 保存配置项<br> 
	 * Created date: 2018年1月2日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/saveitems", method = RequestMethod.POST)
	public Message saveConfigItems(@RequestBody String json) {
		if (StringUtils.isEmpty(json)) {
			return Message.newFailureMessage("保存失败，配置信息为空！");
		}
		JSONObject jo = JSON.parseObject(json);
		Integer configMainId = jo.getInteger("configMainId");
		List<ConfigItems> configItems = JsonTools.jsonarray2list(jo.getJSONArray("records").toJSONString(), ConfigItems.class);
		return this.configItemsService.saveConfigItems(configMainId, configItems);
	}
	
	/**
	 * Description: 删除某配置分类及其配置项<br> 
	 * Created date: 2018年1月3日
	 * @param configTypeId
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody Map<String,Integer> map) {
		return this.configItemsService.deleteConfig(map.get("configTypeId"));
	}
	
	/**
	 * Description: 检验新增的配置项分类是否存在<br> 
	 * Created date: 2018年1月8日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/checkconfigtype", method = RequestMethod.POST)
	public Message checkConfigType(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer moduleId = jo.getInteger("moduleId");
		String configType = jo.getString("configType");
		return this.configItemsService.checkConfigType(productId, moduleId, configType);
	}
	
	/**
	 * Description: 为产品模块新增配置类别及配置项<br> 
	 * Created date: 2018年1月17日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/additems", method = RequestMethod.POST)
	public Message addConfigItems(HttpSession session, @RequestBody String json) {
		if (StringUtils.isEmpty(json)) {
			return Message.newFailureMessage("保存失败，配置信息为空！");
		}
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		JSONObject jo = JSON.parseObject(json);
		ConfigMain configMain = JsonTools.json2obj(json, ConfigMain.class);
		List<ConfigItems> configItems = JsonTools.jsonarray2list(jo.getJSONArray("items").toJSONString(), ConfigItems.class);
		configMain.setCreateUuid(map.get("uuid"));
		return this.configItemsService.addConfigTypeAndItems(configMain, configItems);
	}
	
	/**
	 * Description: 重写zk内的配置信息<br> 
	 * Created date: 2018年1月17日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/overwrite", method = RequestMethod.POST)
	public Message overWriteConfigItems(@RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer moduleId = jo.getInteger("moduleId");
		return this.configItemsService.overWriteConfigItems(productId, moduleId);
	}
}
