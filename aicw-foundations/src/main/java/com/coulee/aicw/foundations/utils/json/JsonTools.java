package com.coulee.aicw.foundations.utils.json;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.coulee.aicw.foundations.utils.common.DateTools;

/**
 * Description: JSON与JAVA对象相互转换工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class JsonTools {
	
	private static SerializeConfig sconfig = new SerializeConfig();

	private static SerializerFeature[] feature = new SerializerFeature[6];
	
	
	static {
		sconfig.put(java.util.Date.class, new SimpleDateFormatSerializer(DateTools.COMMON_DATE_FORMAT));
		sconfig.put(java.sql.Date.class, new SimpleDateFormatSerializer(DateTools.COMMON_DATE_FORMAT));
		sconfig.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer(DateTools.COMMON_DATE_FORMAT));
		
		feature[0] = SerializerFeature.WriteNullStringAsEmpty;
		feature[1] = SerializerFeature.WriteMapNullValue;
		feature[2] = SerializerFeature.WriteNullListAsEmpty;
		feature[3] = SerializerFeature.WriteNullNumberAsZero;
		feature[4] = SerializerFeature.WriteNullBooleanAsFalse;
		feature[5] = SerializerFeature.DisableCircularReferenceDetect;
	}

	/**
	 * Definition:将对象转换为Json字符串
	 * @param obj 要转换的对象
	 * @return Json字符串
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static String obj2json (Object obj) {
		return JSON.toJSONString(obj, sconfig, feature);
	}
	
	/**
	 * Definition:将Json字符串转换为对象
	 * @param json 要转换的Json字符串
	 * @param clazz 要转换成的类
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static <T> T json2obj(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
	
	/**
	 * Definition:将集合转换为Json数组
	 * @param list 要进行转换的集合
	 * @return 转换后的Json数组
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static String list2jsonarray(List<?> list) {
		return JSON.toJSONString(list, sconfig, feature);
	}
	
	
	/**
	 * Definition:Json数组字符串转换为List集合
	 * @param jsonarray Json数组字符串
	 * @param clazz 集合内每个元素类
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static <T> List<T> jsonarray2list(String jsonarray, Class<T> clazz) {
		return JSON.parseArray(jsonarray, clazz);
	}
	
	/**
	 * Definition:对象数组转换为Json数组
	 * @param t 对象数组
	 * @return Json数组
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static <T> String array2jsonarray(T[] t) {
		return JSON.toJSONString(t, sconfig, feature);
	}
	/**
	 * 判断对象是否json格式
	 * @param content
	 * @return
	 */
	public static boolean isJson(String content) {
        try {
        	JSON.parseObject(content);
//            JSONObject.fromObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

	