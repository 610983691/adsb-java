package com.coulee.foundations.configserver.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.foundations.configserver.entity.ConfigUser;
import com.coulee.foundations.configserver.service.IConfigUserService;

/**
 * Description: 系统登录接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@RestController
public class LoginController extends BaseController {
	
	@Autowired
	private IConfigUserService configUserService;

	/**
	 * Description: 系统登录<br> 
	 * Created date: 2017年12月14日
	 * @param session
	 * @param uuid
	 * @param password
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Message login(HttpSession session,@RequestBody Map<String, String>  paramMap) {
		Message message = this.configUserService.checkUser(paramMap.get("username"), paramMap.get("password"));
		if (message.isSuccess()) {
			ConfigUser user = (ConfigUser) message.getObj();
			Map<String, String> map = new HashMap<String, String>(5);
			map.put("uuid", user.getUuid());
    		map.put("username", user.getUserName());
    		map.put("isLogin", "true");
    		map.put("isAdmin", String.valueOf(user.getIsAdmin()));
    		session.setAttribute("user", map);
			return Message.newSuccessMessage(message.getMsg());
		} else {
			return message;
		}
	}
	
	/**
	 * Description: 系统退出<br> 
	 * Created date: 2017年12月19日
	 * @param session
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
}
