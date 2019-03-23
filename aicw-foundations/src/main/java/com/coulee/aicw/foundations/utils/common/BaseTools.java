package com.coulee.aicw.foundations.utils.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageList;

import oracle.sql.TIMESTAMP;

/**
 * Description: 字符串简单操作工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */

public class BaseTools {
	private static DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
	private static SimpleDateFormat formatterT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	private static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");;
	public static final int ascNum = 65;

	public static char num2Char(int num) {

		return (char) (num + ascNum);
	}

	 
 
	/**
	 * add zyj 2016-10-20 增加获取空判断
	 * 
	 * @param e
	 * @param key
	 * @return
	 */
	public static String getEleStr(Element e, String key) {
		String rs = null;
		if (e == null || e.element(key) == null) {
			return rs;
		}
		rs = e.element(key).getText();
		if (rs != null) {
			rs = rs.trim();
		}
		return rs;
	}

	/**
	 * add zyj 2017-6-16 增加获取空判断
	 * 
	 * @return
	 */
	public static String getStrByJson(JSONObject jo, String key) {
		String rs = null;
		if (jo == null || isNull(key)) {
			return rs;
		}
		try {
			rs = getDefStr(jo.get(key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static Integer getIntByJson(JSONObject jo, String key) {
		Integer rs = null;
		if (jo == null || isNull(key)) {
			return rs;
		}
		try {
			rs = getInt(jo.get(key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 去掉前后空格字符串
	 * 
	 * @param temp
	 * @return
	 */
	public static String getDefStr(Object temp) {
		if (temp == null) {
			return "";
		}
		String tempS = null;
		if (temp instanceof BigDecimal) {
			BigDecimal dbTemp = (BigDecimal) temp;
			tempS = dbTemp.toString();
		} else if (temp instanceof Date) {
			tempS = formatter.format(temp);
		} else {
			tempS = "" + temp;
		}

		if (tempS == null || "".equals(tempS.trim()) || "null".equals(tempS.trim().toLowerCase())) {
			return "";
		}
		return tempS.trim();
	}

	/**
	 * 
	 * @param temp
	 * @return
	 */
	public static Integer getInt(Object temp) {
		if (isNull(temp)) {
			return null;
		}
		if (temp instanceof BigDecimal) {
			return ((BigDecimal) temp).intValue();
		} else if (temp instanceof String) {
			String num = (String) temp;

			if (isNull(num)) {
				return null;
			}
			num = num.trim();
			return Integer.parseInt(num);
		}else if (temp instanceof Double) {
			Double db = Double.parseDouble("" + temp);
			int b = db.intValue();
			return Integer.valueOf(b);

		}else if(temp instanceof Float) {
			Float db =Float.parseFloat(""+temp);
			int b=db.intValue();
			return Integer.valueOf(b);
		}
		return (Integer) temp;

	}

	/**
	 * 强制获取数字类型，遇到字符串等不可转换信息，直接赋值0
	 * 
	 * @param temp
	 * @return
	 */
	public static Integer getIntDef0(Object temp) {
		int rs = 0;
		if (isNull(temp)) {
			return rs;
		}
		try {
			if (temp instanceof BigDecimal) {
				rs = ((BigDecimal) temp).intValue();
			} else if (temp instanceof String) {
				String num = (String) temp;
				if (isNull(num)) {
					return 0;
				}
				num = num.trim();
				return Integer.parseInt(num);
			} else if (temp instanceof Double) {
				Double db = Double.parseDouble("" + temp);
				int b = db.intValue();
				return Integer.valueOf(b);

			} else if (temp instanceof Float) {
				Float db = Float.parseFloat("" + temp);
				int b = db.intValue();
				rs = Integer.valueOf(b);
			}
			else {
				rs = (int) temp;
			}
		} catch (Exception e) {
			rs = 0;
		}
		return rs;

	}

	/**
	 * 
	 * @param temp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNull(Object temp) {
		if (temp == null) {
			return true;
		}
		if (temp instanceof String) {
			return isNull((String) temp);
		} else if (temp instanceof List) {
			return isNull((List) temp);
		} else if (temp instanceof Map) {
			return isNull((Map) temp);
		} else if (temp instanceof String[]) {
			return isNull((String[]) temp);
		} 
		else if (temp instanceof PageList) {
			PageList pList = (PageList) temp;

			return pList.size() == 0 ? true : false;
		}
		return false;
	}

	/**
	 * 
	 * @param temp
	 * @return
	 */
	public static boolean isNull(String[] temp) {
		if (temp == null) {
			return true;
		}
		if (temp.length == 0) {
			return true;
		}
		boolean isNullB = true;
		for (String item : temp) {
			if (!isNull(item)) {
				isNullB = false;
				break;
			}
		}
		return isNullB;
	}

	/**
	 * 判断空字符串
	 * 
	 * @param temp
	 * @return
	 */
	public static boolean isNull(String temp) {
		if (temp == null) {
			return true;
		}
		if ("null".equals(temp.toLowerCase())) {
			return true;
		}
		if ("".equals(temp.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param temp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNull(List temp) {
		if (temp == null) {
			return true;
		}
		if (temp instanceof PageList) {
			PageList pList = (PageList) temp;

			return pList.size() == 0 ? true : false;
		}
		return (temp == null || temp.isEmpty());
	}

	/**
	 * 
	 * @param temp
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNull(Map temp) {
		return (temp == null || temp.isEmpty());
	}

	/**
	 * list的map集合排除相同数据集,简单处理，复杂数据处理不了
	 * 
	 * @param saveList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map> removeSameData(List<Map> saveList) {
		List<Map> rs = new ArrayList<Map>();
		if (saveList == null || saveList.isEmpty()) {
			return rs;
		}
		Map tempMap = new HashMap();
		for (Map item : saveList) {
			if (item == null || item.isEmpty()) {
				continue;
			}
			int mapSize = item.size();
			Object[] keyObj = new Object[mapSize];
			Iterator ite = item.entrySet().iterator();
			int start = 0;
			while (ite.hasNext()) {
				Entry ent = (Entry) ite.next();
				keyObj[start] = ent.getValue();
				start++;
			}
			MapKey keyM = new MapKey(keyObj);
			if (!tempMap.containsKey(keyM)) {
				tempMap.put(keyM, null);
				rs.add(item);
			}
		}
		return rs;
	}

	/**
	 * 构造in语句
	 * 
	 * @param value
	 * @return
	 */
	public static String getInSql(String value) {
		StringBuffer sql = new StringBuffer();

		if (isNull(value)) {
			return null;
		}
		if (value.indexOf(",") >= 0) {
			String[] valueS = value.split(",");
			sql.append(" in ( ");
			for (int i = 0, sz = valueS.length; i < sz; i++) {
				if (i == 0) {

				} else {
					sql.append(",");
				}
				sql.append(" '").append(valueS[i]).append("' ");
			}
			sql.append(" ) ");
		} else {
			sql.append(" = '").append(value).append("'  ");
		}
		return sql.toString();
	}

	/**
	 * 把list 分组执行
	 * 
	 * @param tempList
	 * @param count
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<List> getListGroup(List tempList, int count) {
		List<List> rs = new ArrayList();
		if (tempList == null || tempList.isEmpty()) {
			return rs;
		}
		if(count == 0){
			rs.add(tempList);
			return rs;
		}
		Map<Integer, List> groupMap = new HashMap();
		int groupNum = 1;
		Object itemMap = null;
		int countN = count;
		for (int i = 0, sz = tempList.size(); i < sz; i++) {
			itemMap = tempList.get(i);
			if (i == countN) {
				groupNum++;
				countN = count * groupNum;
			}

			if (groupMap.containsKey(groupNum)) {
				List gList = groupMap.get(groupNum);
				gList.add(itemMap);

			} else {
				List gList = new ArrayList();
				gList.add(itemMap);
				groupMap.put(groupNum, gList);
				rs.add(gList);
			}

		}
		return rs;
	}

	/**
	 * 把list 分组执行 groupNum 参数 确定list分几组 多余行项放到最后list
	 * 
	 * @param tempList
	 * @param count
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<List> getListByGroupNum(List tempList, int groupCount) {
		List<List> rs = new ArrayList();
		if (tempList == null || tempList.isEmpty()) {
			return rs;
		}
		if(groupCount == 0 || groupCount == 1){
			rs.add(tempList);
			return rs;
		}
		Map<Integer, List> groupMap = new HashMap();
		int groupNum = 1;
		Object itemMap = null;
		int listSize = tempList.size();
		int rowCount = listSize / groupCount;
		int maxCount = rowCount * groupCount;
		int countN = rowCount;
		for (int i = 0; i < listSize; i++) {
			itemMap = tempList.get(i);
			/*
			 * 收尾工作
			 */
			if (i >= maxCount) {
				groupNum = groupCount;
			}
			/*
			 * 分摊
			 */
			else if (i == countN) {
				groupNum++;
				countN = rowCount * groupNum;
			}
			if (groupMap.containsKey(groupNum)) {
				List gList = groupMap.get(groupNum);
				gList.add(itemMap);

			} else {
				List gList = new ArrayList();
				gList.add(itemMap);
				groupMap.put(groupNum, gList);
				rs.add(gList);
			}

		}
		return rs;
	}

	/**
	 * 判断两个字符串是否相等
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isSameStr(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null) {
			if (str2 != null) {
				return false;
			}

		}
		if (str2 == null) {
			if (str1 != null) {
				return false;
			}
		}

		return str1.equals(str2);
	}

	/**
	 * 比较两个int对象的值 是否相等
	 * 
	 * @param int1
	 * @param int2
	 * @return
	 */
	public static boolean isSameInt(Integer int1, Integer int2) {
		int temp1 = int1 == null ? 0 : int1.intValue();
		int temp2 = int2 == null ? 0 : int2.intValue();
		return temp1 == temp2;
	}

	public static String filterCtrlChars(String source) {
		StringBuffer sf = new StringBuffer();
		for (char c : source.toCharArray()) {
			if (Character.isISOControl(c)) {
				sf.append("\\").append(Integer.toOctalString(c));
			} else {
				sf.append(c);
			}
		}
		return sf.toString();
	}

	/**
	 * mybatis升级后出现日期类型转换问题
	 * 
	 * @param temp
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static Date getDate(Object temp) throws ParseException, SQLException {
		if (temp == null) {
			return null;
		}
		if (temp instanceof Timestamp) {
			Timestamp tempD = (Timestamp) temp;
			return new Date(tempD.getTime());
		} 
		else if (temp instanceof TIMESTAMP) {
			TIMESTAMP tempD = (TIMESTAMP) temp;
			return new Date(tempD.timestampValue().getTime());
		} 
		else if (temp instanceof String) {
			String tempS = (String) temp;
			if (tempS.indexOf("T") >= 0) {
				Date dd = formatterT.parse(tempS);
				String ddStr = formatter.format(dd);
				return DateTools.formatDateString(ddStr);
			} else {
				return formatter.parse(tempS);
			}

		}
		return (Date) temp;
	}

	public static String formateTDateStr(String temp) throws ParseException, SQLException {
		if (temp == null) {
			return null;
		}
		String rs = temp;
		if (temp.indexOf("T") >= 0) {
			Date dd = formatterT.parse(temp);
			rs = formatter.format(dd);
		}
		return rs;
	}
	public static String fTDateStr(String temp) throws ParseException, SQLException {
		if (temp == null) {
			return null;
		}
		String rs = temp;
		if (temp.indexOf("T") >= 0) {
			Date dd = formatterT.parse(temp);
			rs = sdFormat.format(dd);
		}
		return rs;
	}
	public static void testBigSet() {
		// 4,7,2,5,3
		BitSet bs = new BitSet(8);
		int size = bs.size();
		int[] ia = { 4, 2, 7, 2, 5, 3 };
		for (int i : ia) {
			bs.set(i, true);
		}
		size = bs.size();
		for (int j = 0; j < size; j++) {
			System.out.println("j==" + j);
			if (bs.get(j)) {
				System.out.println(j + "");
			}

		}

		System.out.println(bs.toString());

	}

	/**
	 * 拆分字符串
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<String> splistStr(String str, String splitIndex, boolean isRemoveSame) {
		List<String> rs = new ArrayList<String>();
		if (isNull(str) || isNull(splitIndex)) {
			return rs;
		}
		if (str.indexOf(splitIndex) >= 0) {
			String[] splitMul = str.split(splitIndex);
			if (isRemoveSame) {
				Map<String, String> keyMap = new HashMap<String, String>();
				for (String item : splitMul) {
					if (keyMap.containsKey(item)) {
						continue;
					}
					keyMap.put(item, null);
					rs.add(item);
				}
			} else {
				for (String item : splitMul) {
					rs.add(item);
				}
			}
		} else {
			rs.add(str);
		}

		return rs;
	}

	/**
	 * 构造ID的list
	 * 
	 * @param str
	 * @param splitIndex
	 * @return
	 */
	public static List<Integer> splistStrToInt(String str, String splitIndex) {
		List<Integer> rs = new ArrayList<Integer>();
		if (isNull(str) || isNull(splitIndex)) {
			return rs;
		}
		if (str.indexOf(splitIndex) >= 0) {
			String[] splitMul = str.split(splitIndex);

			Map<String, String> keyMap = new HashMap<String, String>();
			for (String item : splitMul) {
				if (BaseTools.isNull(item)) {
					continue;
				}
				if (keyMap.containsKey(item)) {
					continue;
				}
				keyMap.put(item, null);
				rs.add(BaseTools.getInt(item));
			}

		} else {
			rs.add(BaseTools.getInt(str));
		}

		return rs;
	}

	public static Map<String, String> splistStr(String str, String splitIndex) {
		Map<String, String> rsMap = new HashMap<String, String>();
		if (isNull(str) || isNull(splitIndex)) {
			return rsMap;
		}
		if (str.indexOf(splitIndex) >= 0) {
			String[] splitMul = str.split(splitIndex);
			for (String item : splitMul) {
				if (BaseTools.isNull(item)) {
					continue;
				}
				rsMap.put(item, null);
			}

		} else {
			rsMap.put(str, null);
		}

		return rsMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getIDSList(String temp) {
		List idsList = new ArrayList();
		String tempN = temp.substring(1, temp.length() - 1);
		String[] list = tempN.split(",");
		for (String item : list) {
			if (BaseTools.isNull(item)) {
				continue;
			}
			idsList.add(item);
		}

		return idsList;
	}

	/**
	 * 删除ArrayList中重复元素，保持顺序
	 * 
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void removeDuplicateWithOrder(List list) {
		if (BaseTools.isNull(list)) {
			return;
		}
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		list.clear();
		list.addAll(newList);
	}

	/**
	 * 超过list的size，取list最后一个值
	 * 
	 * @param objList
	 * @param index
	 * @param objName
	 * @return
	 */
	public static Object getListObjByIndex(List<Map<String, Object>> objList, int index, String objName) {
		Object obj = null;
		if (index > objList.size() - 1) {
			obj = "";
		} else {
			obj = objList.get(index).get(objName);
		}
		return obj;
	}

	/**
	 * 克隆对象
	 * @param obj
	 * @return
	 */
	public static Object cloneObj(Object obj) {
		Object rsObj = null;
		if (obj == null) {
			return rsObj;
		}
		// Map tempMap = ObjTransTools.vo2map(obj);
		// rsObj = ObjTransTools.map2vo(tempMap, obj.getClass());
		try {
			Class<?> clazz = obj.getClass();
			rsObj= clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			Object value = null;
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (!"class".equals(propertyName)) {
					value = descriptor.getReadMethod().invoke(obj, new Object[0]);
					descriptor.getWriteMethod().invoke(rsObj, new Object[] { value });
				}
			}
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

		return rsObj;
	}
	public static void main(String[] args) {
		List<String> ipList = new ArrayList<String>();
//		Map m1 = new HashMap();
//		m1.put("ip", "2.2.3.4");
//		ipList.add(m1);
//		Map m2 = new HashMap();
//		m2.put("ip", "1.2.3.2");
//		ipList.add(m2);
//		Map m3 = new HashMap();
//		m3.put("ip", "1.2.3.4");
//		ipList.add(m3);
//		Map m4 = new HashMap();
//		m4.put("ip", "1.2.3.3");
//		ipList.add(m4);
		ipList.add("2.2.3.4");
		ipList.add("1.2.3.2");
		ipList.add("1.2.3.4");
		ipList.add("1.2.3.3");
//		List rs = BaseTools.sortIPV4List(ipList, "ip");
//		System.out.println(rs.toString());
	}
	/**
	 * ipv4 地址排序
	 * @param ipv4List 可以是 dto，vo，map，json的集合 必须传 获取ip字段的ipkey关键字；如果是ip字符串计划，ipkey可以忽略
	 * @return
	 */
//	public static List sortIPV4List(List ipv4List,String ipKey){
//		List rs = new ArrayList();
//		Collections.sort(ipv4List, new ComparatorIPV4(ipKey));//排序
//		rs.addAll(ipv4List);
//		return rs;
//	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sortIPV4List(List ipv4List,String ipKey){
		Collections.sort(ipv4List, new ComparatorIPV4(ipKey));//排序
	}
	/**
	 * list.add(map)
	 * @author zyj
	 *
	 */
	@SuppressWarnings("rawtypes")
	private static class ComparatorIPV4 implements Comparator {
		private String ipKey;
		public ComparatorIPV4(String ipKey) {
			this.ipKey = ipKey;
		}
		 
		public int compare(Object o1, Object o2) {
			Map m1 = null;
			Map m2 = null;
			String ip1 = null;
			String ip2 = null;
			if(o1 instanceof Map){
				m1 = (Map) o1;
				m2 = (Map) o2;
			}
			else if(o1 instanceof BaseEntity ){
				m1 = ObjTransTools.entity2map(o1);
				m2 = ObjTransTools.entity2map(o2);
			}
			
			else if(o1 instanceof String){
				String str1 = BaseTools.getDefStr(o1);
				String str2 = BaseTools.getDefStr(o2);
				if(JsonTools.isJson(str1)){
					m1 = JsonTools.json2obj(str1, Map.class);
					m2 = JsonTools.json2obj(str2, Map.class);
					
				}else{
					ip1 = str1;
					ip2 = str2;
				}
			}
			if(m1 != null){
				ip1 = BaseTools.getDefStr(m1.get(ipKey));
				ip2 = BaseTools.getDefStr(m2.get(ipKey));
			}
			 
			int ip1Int = IPHelper.ip2int(ip1);
			int ip2Int = IPHelper.ip2int(ip2);
			return ip1Int - ip2Int;
		}

	}
}
