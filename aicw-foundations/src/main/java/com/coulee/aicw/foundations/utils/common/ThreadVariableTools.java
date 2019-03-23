package com.coulee.aicw.foundations.utils.common;

import java.util.Hashtable;
import java.util.Map;

/**
 * Description: 线程变量工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ThreadVariableTools {
	
	private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	/**
	 * Definition:设置线程变量
	 * @param key 变量KEY
	 * @param object 变量值
	 * @Author: oblivion
	 * @Created date: 2014年12月3日
	 */
	public static void setVariable(String key, Object object) {
		if (object == null) {
			return;
		}
		Map<String, Object> map = threadLocal.get();
		if (map != null) {
			map.put(key, object);
		} else {
			Map<String, Object> initMap = new Hashtable<String, Object>(3);
			initMap.put(key, object);
			threadLocal.set(initMap);
		}
	}

	/**
	 * Definition:取出线程变量,取出后原值还存在。如果想要取出后该值被删除，应该使用remove(key)方法
	 * @param key 变量KEY
	 * @return 变量值
	 * @Author: oblivion
	 * @Created date: 2014年12月3日
	 * @modify tongjie
	 */
	public static Object getVariable(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map != null) {
			return map.get(key);
		} else {
			return null;
		}
	}
	
	
	/***
	 *  Removes the key (and its corresponding value) from this
     * hashtable. This method does nothing if the key is not in the hashtable.
	 * @param key
	 * @return the value to which the key had been mapped in this hashtable, or null if the key did not have a mappingThrows:NullPointerException - if the key is null
	 */
	public static Object remove(String key) {
		Map<String, Object> map = threadLocal.get();
		if (map != null) {
			return map.remove(key);
		} else {
			return null;
		}
	}


	/***
	 * 整个线程变量的map都被移除。所有值都被删除，慎重使用，除非你知道你在干嘛。
	 */
	public static void removeVariable() {
		threadLocal.remove();
	}
}

