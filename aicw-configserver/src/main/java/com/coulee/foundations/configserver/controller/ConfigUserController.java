package com.coulee.foundations.configserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.foundations.configserver.entity.ConfigUser;
import com.coulee.foundations.configserver.service.IConfigUserService;

/**
 * Description: 用户管理controller接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/user")
public class ConfigUserController extends BaseController {
	
	@Autowired
	private IConfigUserService configUserService;

	/**
	 * Description: 用户信息自维护保存<br> 
	 * Created date: 2017年12月19日
	 * @param user
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/selfupdate", method = RequestMethod.POST)
	public Message selfUpdate(@RequestBody ConfigUser user) {
		return this.configUserService.selfUpdate(user);
	}
	
	/**
	 * Description: 查询用户列表<br> 
	 * Created date: 2017年12月20日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageEntity<ConfigUser> list(@RequestBody ConfigUser entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<ConfigUser> pl = this.configUserService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}
	
	/**
	 * Description: 删除用户<br> 
	 * Created date: 2017年12月20日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody String json) {
		List<ConfigUser> users = JsonTools.jsonarray2list(json, ConfigUser.class);
		return this.configUserService.batchDelete(users);
	}
	
	/**
	 * Description: 增加用户<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(@RequestBody String json) {
		ConfigUser user = JsonTools.json2obj(json, ConfigUser.class);
		return this.configUserService.addUser(user);
	}
	
	/**
	 * Description: 修改用户<br> 
	 * Created date: 2017年12月22日
	 * @param json
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody String json) {
		ConfigUser user = JsonTools.json2obj(json, ConfigUser.class);
		return this.configUserService.updateUser(user);
	}
}
