package com.coulee.aicw.collectanalyze.collect.common;

import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Description:读取配置文件工具类
 * Copyright (C) 2016 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2016-12-5
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class ReadSpecilConfig {
	
	/**
	 * Definition:读取xml，返回根节点
	 * @param xml
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-6
	 */
	private static Element getXmlRoot(String xml) {
		SAXReader saxReader = new SAXReader();
		try {
			Document doc = saxReader.read(new StringReader(xml.trim()));
			return doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Definition:读取定义的特殊关键字及对应的操作
	 * @return
	 * @Author: LanChao
	 * @Created date: 2016-12-5
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> readKeywordCommand() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("specil-definition.xml");
			String xml = CommonUtils.readTxtFileContent(in, "UTF-8");
			Element rootEle = getXmlRoot(xml);
			Iterator<Element> it = rootEle.element("keyword-command").elementIterator("object");
			while (it.hasNext()) {
				Element objEle = it.next();
				map.put(objEle.elementTextTrim("keyword"), objEle.elementTextTrim("command"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
