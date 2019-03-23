package com.coulee.foundations.configserver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.coulee.foundations.configserver.entity.Location;

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
		return "line1";
	}

	/**
	 * Description: 欢迎页<br> 
	 * Created date: 2017年12月19日
	 * @param model
	 * @return
	 * @author oblivion
	 */
	@RequestMapping("/plane1")
	public String plane1(Map<String, Object> model) {
		return "line1";
	}
	
	
	/**
	 * Description: 欢迎页<br> 
	 * Created date: 2017年12月19日
	 * @param model
	 * @return
	 * @author oblivion
	 */
	@RequestMapping("/plane2")
	public String plane2(Map<String, Object> model) {
		return "line2";
	}
	
	/**
	 * Description: 欢迎页<br> 
	 * Created date: 2017年12月19日
	 * @param model
	 * @return
	 * @author oblivion
	 */
	@RequestMapping("/plane3")
	public String plane3(Map<String, Object> model) {
		return "line3";
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
	
	@RequestMapping("/locations")
	@ResponseBody
	public String loadLocationData() throws IOException{
		Set<Location> result =  getRealLocations();
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/flydata1")
	@ResponseBody
	public String data1() throws IOException{
		Set<Location> result =  getRealLocations();
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/flydata2")
	@ResponseBody
	public String flydata2() throws IOException{
		Set<Location> result =  getRealLocations();
		return JSONObject.toJSONString(result);
	}
	
	@RequestMapping("/flydata3")
	@ResponseBody
	public String flydata3() throws IOException{
		Set<Location> result =  getRealLocations();
		return JSONObject.toJSONString(result);
	}
	
	private static final int times=100;
	
	/***
	 * 这个更真实
	 * @return
	 * @throws IOException 
	 */
	private Set<Location> getRealLocations() throws IOException{
		List<Location> list = ReadExcel.read();
		Set<Location> set = new HashSet<>();
		set.addAll(list);
		return set;
	}
	
	private List<Location> generateLocations(){
		List<Location> result = new ArrayList<>(times);
		Location begin = randomLocation(-180d,180d,-90d,90d);
		Location end = randomLocation(-180d,180d,-90d,90d);
		result.add(begin);
		
		/*計算中間的點*/
		for (int i = 1; i < times-1; i++) {
			Location lo = randomLocation(Double.valueOf(begin.getLon()),
					Double.valueOf(end.getLon()),Double.valueOf(begin.getLat()),
					Double.valueOf(end.getLat()));
			result.add(lo);
		}
		result.add(end);
		System.out.println(result);
		return result;
		
		
		
	}
	
	private Location randomLocation(Double minlonRange,Double maxLonRange,Double minlatRange,Double maxlatRange){
		Random ran = new Random();
		double rangeLon = maxLonRange-minlonRange;
		double rangeLat = maxlatRange-minlatRange;
		double lon = ran.nextDouble()*rangeLon-rangeLon/2;
		double lat = ran.nextDouble()*rangeLat-rangeLat/2;
		Location location = new Location();
		location.setLat(String.valueOf(lat));
		location.setLon(String.valueOf(lon));
		return location;
	}
	
	public static void main(String[] a){
		EntranceController con = new EntranceController();
		con.generateLocations();
	}
}
