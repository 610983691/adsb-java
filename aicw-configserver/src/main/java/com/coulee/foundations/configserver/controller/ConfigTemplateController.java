package com.coulee.foundations.configserver.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.entity.ConfigTemplate;
import com.coulee.foundations.configserver.entity.ConfigTemplateItems;
import com.coulee.foundations.configserver.service.IConfigTemplateService;

/**
 * Description: 模板管理controller<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/template")
public class ConfigTemplateController extends BaseController {

	@Autowired
	private IConfigTemplateService configTemplateService;

	/**
	 * Description: 查询模板列表<br> 
	 * Created date: 2018年1月16日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageEntity<ConfigTemplate> list(HttpSession session, @RequestBody ConfigTemplate configTemplate) {
		PageArg pageArg = this.getPageArg(configTemplate);
		Map sessionMap = (Map)session.getAttribute("user");
		if (Integer.valueOf(sessionMap.get("isAdmin").toString()) != 1) {
			configTemplate.setCreateUuid(sessionMap.get("uuid").toString());
			configTemplate.setIsInner(1);
		} 
		PageList<ConfigTemplate> pageList = this.configTemplateService.findByParamsWithInner(configTemplate, pageArg);
		return this.makePageEntity(pageList);
	}

	/**
	 * Description: 增加模板<br> 
	 * Created date: 2018年1月16日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(HttpSession session, @RequestBody String json) {
		ConfigTemplate configTemplate = JsonTools.json2obj(json, ConfigTemplate.class);
		Map sessionMap = (Map)session.getAttribute("user");
		configTemplate.setCreateUuid(sessionMap.get("uuid").toString());
		JSONObject jo = JSON.parseObject(json);
		List<ConfigTemplateItems> items = JsonTools.jsonarray2list(jo.getJSONArray("items").toJSONString(), ConfigTemplateItems.class);
		return this.configTemplateService.addConfigTemplate(configTemplate, items);
	}
	
	/**
	 * Description: 删除模板<br> 
	 * Created date: 2018年1月16日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody String json) {
		List<ConfigTemplate> configTemplates = JsonTools.jsonarray2list(json, ConfigTemplate.class);
		return this.configTemplateService.batchDelete(configTemplates);
	}

	/**
	 * Description: 模板配置项详情<br> 
	 * Created date: 2018年1月16日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public List<ConfigTemplateItems> detail(@RequestBody String json) {
		ConfigTemplate configTemplate = JsonTools.json2obj(json, ConfigTemplate.class);
		return this.configTemplateService.detailConfigTemplate(configTemplate);
	}

	/**
	 * Description: 修改模板<br> 
	 * Created date: 2018年1月16日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody String json) {
		ConfigTemplate configTemplate = JsonTools.json2obj(json, ConfigTemplate.class);
		JSONObject jo = JSON.parseObject(json);
		List<ConfigTemplateItems> items = JsonTools.jsonarray2list(jo.getJSONArray("items").toJSONString(), ConfigTemplateItems.class);
		return this.configTemplateService.updateConfigTemplate(configTemplate, items);
	}

	/**
	 * Description: 模板下拉框数据获取<br> 
	 * Created date: 2018年1月8日
	 * @param session
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listcombo", method = RequestMethod.POST)
	public List<ConfigTemplate> findConfigTemplatesForCombo(HttpSession session) {
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		String isAdmin = map.get("isAdmin");
		ConfigTemplate entity = new ConfigTemplate();
		if (!"1".equals(isAdmin)) {
			String uuid = map.get("uuid");
			entity.setCreateUuid(uuid);
			entity.setIsInner(1);
		}
		return this.configTemplateService.findByCreater(entity, null);
	}
	
	/**
	 * Description: 查询模板配置项<br> 
	 * Created date: 2018年1月8日
	 * @param templateid
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listitems", method = RequestMethod.POST)
	public List<ConfigTemplateItems> findTemplateItems(@RequestBody  Map<String,Integer> map ) {
		return this.configTemplateService.findByTemplateId(map.get("templateId"));
	}
}
