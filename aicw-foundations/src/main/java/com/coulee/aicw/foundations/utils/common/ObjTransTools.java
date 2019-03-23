package com.coulee.aicw.foundations.utils.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: 对象转换工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class ObjTransTools {
	
	static {
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
	}

	/**
	 * Definition:将Map转换为entity实体
	 * @param <T>
	 * @param map
	 * @param entity
	 * @Author: oblivion
	 * @Created date: 2014年12月9日
	 */
	public static <T> T map2entity(Map<String, Object> map, Class<T> clazz) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		try {
			T t = clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					descriptor.getWriteMethod().invoke(t, new Object[] { value });
				}
			}
			return t;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Definition:将LDAP返回的MAP转换成entity实体
	 * @param map Map属性及值
	 * @param clazz 要转换成的对象Class
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年6月3日
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T map2ldapEntity(Map<String, List> map, Class<T> clazz) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		try {
			T t = clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					List<Object> value = map.get(propertyName);
					if (descriptor.getPropertyType() == String.class && value != null) {
						descriptor.getWriteMethod().invoke(t, new Object[] { value.get(0) });
					} else if (descriptor.getPropertyType() == List.class || descriptor.getPropertyType() == ArrayList.class) {
						descriptor.getWriteMethod().invoke(t, new Object[] { value });
					} else {
						descriptor.getWriteMethod().invoke(t, new Object[] { value });
					}
				}
			}
			return t;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Definition:将entity对象转换为Map
	 * @param entity
	 * @param map
	 * @Author: oblivion
	 * @Created date: 2014年12月9日
	 */
	public static Map<String, Object> entity2map(Object entity) {
		if (entity == null) {
			return null;
		}
		Class<?> clazz = entity.getClass();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			Map<String, Object> map = new HashMap<String, Object>(propertyDescriptors.length);
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (!"class".equals(propertyName)) {
					Object result = descriptor.getReadMethod().invoke(entity, new Object[0]);
					map.put(propertyName, result);
				}
			}
			return map;
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Definition:将元素为Map<String,Object>的PageList转换为entity实体的PageList
	 * @param pageList 元素为Map<String,Object>的PageList
	 * @param entityClass 要转换成的entity实体类
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月9日
	 */
	public static <T> PageList<T> mapPageList2entityPageList(PageList<Map<String, Object>> pageList, Class<T> entityClass) {
		if (pageList == null || pageList.isEmpty()) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> map : pageList) {
			T t = map2entity(map, entityClass);
			list.add(t);
		}
		PageList<T> newList = new PageList<T>(list, pageList.getCurPage(), pageList.getPageSize());
		newList.setTotalRow(pageList.getTotalRow());
		newList.setTotalPage(pageList.getTotalPage());
		return newList;
	}
	
	
}

	