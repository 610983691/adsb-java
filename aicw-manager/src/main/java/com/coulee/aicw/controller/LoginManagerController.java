package com.coulee.aicw.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.service.user.IUserService;
/**
 * 系统登录与退出
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月25日上午10:53:42
 * author：HongZhang
 * @version 1.0
 */
@RestController
public class LoginManagerController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(LoginManagerController.class);
	@Autowired
	private IUserService userService;
	/**
	 * 系统登录入口
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月25日上午10:54:46
	* @param 
	* @return
	 */
	@RequestMapping(value = "/loginManager", method = RequestMethod.POST)
	public Message login(HttpSession session, @RequestParam("username") String userAccount,
			@RequestParam("password") String password) {
		Message message = this.userService.checkUser(userAccount, password);
		logger.info(message.getMsg());
		if(message.isSuccess()){
			UserEntity ue = (UserEntity)message.getObj();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", ue.getUserId());
    		map.put("userName", ue.getUserName());
    		map.put("isLogin", "true");
    		map.put("isAdmin", String.valueOf(ue.getIsAdmin()));
    		session.setAttribute("user", map);
			return Message.newSuccessMessage(message.getMsg());
		}
		return message;
	}
	/**
	 * 系统退出
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月25日上午10:55:20
	* @param 
	* @return
	 */
	@RequestMapping(value = "/logoutManager")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
}
