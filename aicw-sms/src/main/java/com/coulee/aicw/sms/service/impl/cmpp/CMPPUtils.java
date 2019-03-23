package com.coulee.aicw.sms.service.impl.cmpp;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.coulee.aicw.sms.entity.CMPPConfig;
import com.huawei.insa2.util.Args;

/**
 * Description: CMPPUtils<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class CMPPUtils {

	/**
	 * Description: 构造消息数据<br> 
	 * Created date: 2018年10月30日
	 * @param message
	 * @return
	 * @author oblivion
	 */
	public static List<byte[]> getLongByte(String message) {
		List<byte[]> list = new ArrayList<byte[]>();
		try {
			byte[] messageUCS2 = message.getBytes("UnicodeBigUnmarked");// 转换为byte[]
			int messageUCS2Len = messageUCS2.length;// 长短信长度
			int maxMessageLen = 140;
			if (messageUCS2Len > maxMessageLen) {// 长短信发送
				// int tpUdhi = 1;
				// 长消息是1.短消息是0
				// int msgFmt = 0x08;//长消息不能用GBK
				int messageUCS2Count = messageUCS2Len / (maxMessageLen - 6) + 1;// 长短信分为多少条发送
				byte[] tp_udhiHead = new byte[6];
				Random random = new Random();
				random.nextBytes(tp_udhiHead);// 随机填充tp_udhiHead[3] 标识这批短信
				tp_udhiHead[0] = 0x05;
				tp_udhiHead[1] = 0x00;
				tp_udhiHead[2] = 0x03;
				// tp_udhiHead[3] = 0x0A;
				tp_udhiHead[4] = (byte) messageUCS2Count;
				tp_udhiHead[5] = 0x01;// 默认为第一条
				for (int i = 0; i < messageUCS2Count; i++) {
					tp_udhiHead[5] = (byte) (i + 1);
					byte[] msgContent;
					if (i != messageUCS2Count - 1) {// 不为最后一条
						msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (maxMessageLen - 6),
								(i + 1) * (maxMessageLen - 6));
						list.add(msgContent);
					} else {
						msgContent = byteAdd(tp_udhiHead, messageUCS2, i * (maxMessageLen - 6), messageUCS2Len);
						list.add(msgContent);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static byte[] byteAdd(byte[] tpUdhiHead, byte[] messageUCS2, int i, int j) {
		byte[] msgb = new byte[j - i + 6];
		System.arraycopy(tpUdhiHead, 0, msgb, 0, 6);
		System.arraycopy(messageUCS2, i, msgb, 6, j - i);
		return msgb;
	}
	
	/**
	 * Description: 将实体类对象转化为map数据格式<br> 
	 * Created date: 2018年10月30日
	 * @param entity
	 * @return
	 * @author oblivion
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
	 * Description: 获取CMPP配置<br> 
	 * Created date: 2018年10月30日
	 * @param config
	 * @return
	 * @author oblivion
	 */
	public static Args getCMPPArgs(CMPPConfig config) {
		Args args = new Args();
		Map<String, Object> configMap = entity2map(config);
		for (Map.Entry<String, Object> entry : configMap.entrySet()) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			key = key.replaceAll("_", "-");
			args.set(key, value);
		}
		return args;
	}
}
