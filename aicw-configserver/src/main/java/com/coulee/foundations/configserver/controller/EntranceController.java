package com.coulee.foundations.configserver.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 页面入口控制器，主要做页面跳转<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Controller
public class EntranceController {

	/**
	 * Description: 欢迎页<br> 
	 * Created date: 2017年12月19日
	 * @param model
	 * @return
	 * @author oblivion
	 */
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "login";
	}

	/**
	 * Description: 登录成功后的首页<br> 
	 * Created date: 2017年12月19日
	 * @return
	 * @author oblivion
	 */
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	/**
	 * Description: 统一处理页面跳转<br> 
	 * Created date: 2017年12月22日
	 * @param request
	 * @return
	 * @author oblivion
	 */
	@RequestMapping(value = {"/*.html", "/**/*.html"})
	public String forwardPage(HttpServletRequest request) {
		String htmlUrl = request.getRequestURI();
		htmlUrl = htmlUrl.substring(0, htmlUrl.lastIndexOf(".html"));
		if (htmlUrl.startsWith("/")) {
			htmlUrl = htmlUrl.replaceFirst("/", "");
		}
		return htmlUrl;
	}
	
}
