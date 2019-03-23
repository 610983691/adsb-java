package com.coulee.aicw.sms;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.coulee.aicw.sms.entity.CMPPConfig;
import com.coulee.aicw.sms.entity.SMSReceiveContentEntity;
import com.coulee.aicw.sms.service.ISMSReceiverService;
import com.coulee.aicw.sms.service.ISMSSenderService;
import com.coulee.aicw.sms.service.impl.cmpp.SMSReceiverSCMPP2Impl;
import com.coulee.aicw.sms.service.impl.cmpp.SMSSenderCMPP2Impl;
import com.coulee.aicw.sms.service.impl.cmpp.SMSSenderCMPP3Impl;

public class TestMain {
	
	private String protocol;
	
	private <T> T map2entity(Map<String, Object> map, Class<T> clazz) {
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
	
	private CMPPConfig getConfig() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(new File("./cmpp.properties")));
			Set<Entry<Object, Object>> entrySet = prop.entrySet();
			Map<String, Object> map = new HashMap<>();
            for (Entry<Object, Object> entry : entrySet) {
                if (!entry.getKey().toString().startsWith("#")) {
                	String key = ((String) entry.getKey()).replace("ismg.", "").trim();
                	String value = (String) entry.getValue();
                	if ("protocol".equals(key)) {
                		this.protocol = value;
                		continue;
                	}
                	map.put(key, value);
                }
            }
            CMPPConfig config = this.map2entity(map, CMPPConfig.class);
            System.out.println(config);
            return config;
		} catch (FileNotFoundException e) {
			System.err.println("cmpp.properties not found !");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class ISMSReceiverServiceImpl implements ISMSReceiverService {

		@Override
		public void receive(SMSReceiveContentEntity content) {
			System.out.println(content.toString());
		}
		
	}
	
	/**
	 * Description: 测试发送短信<br> 
	 * Created date: 2019年1月3日
	 * @param phone 电话号
	 * @param msg 消息
	 * @param bus 业务ID
	 * @return
	 * @throws Exception
	 * @author oblivion
	 */
	private boolean testSend(String phone, String msg, String bus) throws Exception {
		CMPPConfig config = this.getConfig();
		ISMSSenderService sender = null;
		if ("cmpp2".equals(this.protocol)) {
			sender = new SMSSenderCMPP2Impl(config);
		} else if ("cmpp3".equals(this.protocol)) {
			sender = new SMSSenderCMPP3Impl(config);
		} else {
			System.err.println("cmpp.properties ismg.protocol error, must be cmpp2 or cmpp3");
			System.exit(1);
		}
		System.out.println("SENDER == phone : " + phone + ", msg : " + msg + ", businessId : " + bus);
		return sender.sendSms(phone, msg, bus);
	}
	
	private void testReceiver() throws Exception {
		CMPPConfig config = this.getConfig();
		if ("cmpp2".equals(this.protocol)) {
			SMSReceiverSCMPP2Impl re = new SMSReceiverSCMPP2Impl(config);
			re.setReveivedTodo(new ISMSReceiverServiceImpl());
		} else if ("cmpp3".equals(this.protocol)) {
			SMSReceiverSCMPP2Impl re = new SMSReceiverSCMPP2Impl(config);
			re.setReveivedTodo(new ISMSReceiverServiceImpl());
		} else {
			System.err.println("cmpp.properties ismg.protocol error, must be cmpp2 or cmpp3");
			System.exit(1);
		}
	}

	private static void usage() {
		System.err.println("sms-tester version:1.0");
		System.err.println("Usage: java -jar aicw-sms-1.0.jar COMMAND");
		System.err.println("commands:");
		System.err.println("	sender \"phone\" \"msg\" \"businessId\"");
		System.err.println("	receiver");
        System.err.println("Sender Example: java -jar aicw-sms-1.0.jar sender \"13888888888\" \"hello,this is a test message.\" \"bus-001\"");
        System.err.println("Receiver Example: java -jar aicw-sms-1.0.jar receiver");
        System.exit(1);
	}
	
	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 1) {
			usage();
			System.exit(1);
		}
		String type = args[0];
		if ("sender".equals(type)) {
			if (args.length != 4) {
				usage();
				System.exit(1);
			}
			String phone = args[1];
			String msg = args[2];
			String bus = args[3];
			new TestMain().testSend(phone, msg, bus);
		} else if ("receiver".equals(type)) {
			new TestMain().testReceiver();
		} else {
			usage();
			System.exit(1);
		}
	}

}
