package com.coulee.foundations.configserver.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.DateTools;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.foundations.ConfigServerConstants;
import com.coulee.foundations.configserver.commons.ConfigServerUtils;
import com.coulee.foundations.configserver.entity.ConfigItems;
import com.coulee.foundations.configserver.entity.ConfigMain;
import com.coulee.foundations.configserver.entity.ConfigModules;
import com.coulee.foundations.configserver.entity.ConfigProduct;
import com.coulee.foundations.configserver.service.IConfigDataService;

/**
 * Description: 配置数据管理<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/configdata")
public class ConfigDataController extends BaseController {
	
	@Autowired
	private IConfigDataService configDataService;
	
	/**
	 * Description: 获取已上传的数据库文件路径<br> 
	 * Created date: 2018年1月10日
	 * @param session
	 * @return
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	private String getDbFilePath(HttpSession session) {
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		File dbFile = ConfigServerUtils.getTempDbFilePath(map.get(ConfigServerConstants.TEMP_DB_FILE));
		return dbFile.getAbsolutePath();
	}

	/**
	 * Description: 上传临时数据库文件<br> 
	 * Created date: 2018年1月10日
	 * @param dbFile
	 * @param session
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public Message uploadFile(@RequestParam("dbFile") MultipartFile dbFile, HttpSession session)
			throws IllegalStateException, IOException {
		if (dbFile == null || dbFile.isEmpty() || !dbFile.getOriginalFilename().endsWith(".db")) {
			return Message.newFailureMessage("上传失败");
		}
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		String uid = map.get("uuid");
		String tempDbFileName = DateTools.getDateStr("yyyyMMddHHmmssSSS") + "_" + uid + ".db";
		File tmpDbFile = ConfigServerUtils.getTempDbFilePath(tempDbFileName);
		dbFile.transferTo(tmpDbFile.getAbsoluteFile());
		String oldDbFileName = map.get(ConfigServerConstants.TEMP_DB_FILE);
		if (!StringUtils.isEmpty(oldDbFileName)) {
			//删除旧文件
			File oldDbFile = ConfigServerUtils.getTempDbFilePath(oldDbFileName);
			if (oldDbFile.exists()) {
				oldDbFile.delete();
			}
		}
		map.put(ConfigServerConstants.TEMP_DB_FILE, tempDbFileName);
		return Message.newSuccessMessage("上传成功");
	}
	
	/**
	 * Description: 根据已上传的数据库文件，查询产品列表<br> 
	 * Created date: 2018年1月10日
	 * @param session
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listproducts", method = RequestMethod.POST)
	public List<ConfigProduct> listProduct(HttpSession session) {
		return this.configDataService.findProduct(this.getDbFilePath(session));
	}
	
	/**
	 * Description: 查询模块列表<br> 
	 * Created date: 2017年12月25日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listmodules", method = RequestMethod.POST)
	public List<ConfigModules> listModules(HttpSession session, @RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer realId = jo.getInteger("realId");
		if (productId == 0) {
			return null;
		}
		return this.configDataService.findModules(this.getDbFilePath(session), productId, realId);
	}
	
	/**
	 * Description: 根据产品ID、模块ID获取配置类别列表<br> 
	 * Created date: 2018年1月2日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listtype", method = RequestMethod.POST)
	public List<ConfigMain> findConfigType(HttpSession session, @RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer productId = jo.getInteger("productId");
		Integer moduleId = jo.getInteger("moduleId");
		Integer realProductId = jo.getInteger("realProductId");
		Integer realModuleId = jo.getInteger("realModuleId");
		return this.configDataService.findConfigType(this.getDbFilePath(session), productId, moduleId, realProductId, realModuleId);
	}
	
	/**
	 * Description: 根据配置项类型ID查询配置项列表<br> 
	 * Created date: 2018年1月10日
	 * @param session
	 * @param configTypeId
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/listitems", method = RequestMethod.POST)
	public List<ConfigItems> findConfigItems(HttpSession session, @RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer configTypeId = jo.getInteger("configTypeId");
		Integer realConfigTypeId = jo.getInteger("realConfigTypeId");
		return this.configDataService.findConfigItemsByType(this.getDbFilePath(session), configTypeId, realConfigTypeId);
	}
	
	/**
	 * Description: 查看某产品及其模块下是否存在配置项<br> 
	 * Created date: 2018年1月10日
	 * @param session
	 * @param productId
	 * @param moduleId
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/checkhaveitems", method = RequestMethod.POST)
	public Message checkHaveConfigItems(HttpSession session, @RequestBody Map<String,Integer> map) {
		return this.configDataService.checkHaveConfigItems(this.getDbFilePath(session), map.get("productId"), map.get("moduleId"));
	}
	
	/**
	 * Description: 保存临时配置项信息<br> 
	 * Created date: 2018年1月11日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/saveitems", method = RequestMethod.POST)
	public Message saveConfigItems(HttpSession session, @RequestBody String json) {
		JSONObject jo = JSON.parseObject(json);
		Integer configTypeId = jo.getInteger("configTypeId");
		List<ConfigItems> items = JsonTools.jsonarray2list(jo.getJSONArray("items").toJSONString(), ConfigItems.class);
		return this.configDataService.saveConfigItems(this.getDbFilePath(session), configTypeId, items);
	}
	
	/**
	 * Description: 导入配置类别<br> 
	 * Created date: 2018年1月13日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/importtype", method = RequestMethod.POST)
	public Message importType(HttpSession session, @RequestBody String json) {
		return this.configDataService.importType(this.getDbFilePath(session), json);
	}
	
	/**
	 * Description: 导入全部配置<br> 
	 * Created date: 2018年1月13日
	 * @param session
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/importall", method = RequestMethod.POST)
	public Message importAll(HttpSession session, @RequestBody String json) {
		return this.configDataService.importAll(this.getDbFilePath(session), json);
	}
}
