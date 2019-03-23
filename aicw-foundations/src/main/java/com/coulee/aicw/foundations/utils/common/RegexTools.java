package com.coulee.aicw.foundations.utils.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * Description: 正则表达式工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class RegexTools {
	
	/**
	 * 匹配IPV4正则
	 */
	public static final String REGEX_IPV4 = "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])";
	
	/**
	 * 匹配IPV6正则
	 */
	public static final String REGEX_IPV6 = "\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*";
	
	/**
	 * 网址
	 */
	public static final String NET_ADDRESS = "http[s]{0,1}://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
	
	/**
	 * 匹配Email正则
	 */
	public static final String REGEX_MAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	/**
	 * 汉字
	 */
	public static final String CHINESE_CHART = "[\u4e00-\u9fa5]+";
	
	/**
	 * 英文
	 */
	public static final String ENGLISH_CHART = "[A-Za-z]+";
	
	
	/**
	 * Definition: 根据正则表达式匹配字符串，并将所有匹配到的结果返回
	 * @param inputStr 要进行匹配的字符串
	 * @param regex 正则表达式
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月4日
	 */
	public static List<String> getAllMatching(String inputStr, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr.trim());
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(0).trim());
		}
		return list;
	}
	
	
	/**
	 * Definition:根据正则表达式匹配字符串，并将所有匹配到的某分组结果返回
	 * @param inputStr 要进行匹配的字符串
	 * @param regex 正则表达式
	 * @param group 要返回的组值
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月4日
	 */
	public static List<String> getAllMatchingGroup(String inputStr, String regex, int group){
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr.trim());
		while (matcher.find()) {
			list.add(matcher.group(group).trim());
		}
		return list;
	}
	public static String getMatching(String inputStr, String regex, int group){
		List<String> matcherList = getAllMatchingGroup(inputStr, regex, group);
		String xx = "";
		if (matcherList != null && !matcherList.isEmpty()) {
			xx = matcherList.get(0);
		}
		return xx;
	}
	
	/**
	 * Definition:判断字符串是否被正则表达式匹配到
	 * @param str 字符串
	 * @param regex 正则表达式
	 * @return
	 * @Author: oblivion
	 * @Created date: 2014年12月10日
	 */
	public static boolean isMatched(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Definition:正则匹配工具，使用别名
	 * @param str 待匹配的字符串
	 * @param regex 带别名的正则表达式
	 * @return List集合，每个元素为Map，key为别名，value为匹配到的值
	 * @Author: oblivion
	 * @Created date: 2015年12月24日
	 */
	public static List<Map<String, String>> getMatchingByAlias(String str, String regex) {
//		带别名的正则 (?i)(?<HotFixID>KB\\S{1,100})\\s{1,10}(?<InstalledOn>\\S{1,20})
		String aliasRegex = "(?i)\\?\\<(\\w+)\\>";
		//匹配别名部分的正则
		Pattern pattern = Pattern.compile(aliasRegex); 
		Matcher matcher = pattern.matcher(regex.trim());
		List<String> alias = new ArrayList<String>();
		while (matcher.find()) {
			//取出别名
			alias.add(matcher.group(1).trim()); 
		}
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(str);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		while (matcher.find()) {
			Map<String, String> map = new HashMap<String, String>(alias.size());
			String temp = null;
			String aliaS = null;
			for (int i = 0 ,sz = alias.size(); i < sz; i++) {
				/*
				 * add zyj 201603019 增加字符串去掉空格
				 */
				aliaS = alias.get(i);
				temp = matcher.group(aliaS);
				if(temp != null)
				{
					temp = temp.trim();
				}
//				map.put(alias.get(i), matcher.group(alias.get(i)));
				map.put(aliaS, temp);
			}
			result.add(map);
		}
		return result;
	}
	private static final int SPLIT_NUM = 100;
	/**
	 * add zyj
	 * 20160513
	 * 拆分大的指令内容，分块执行
	 * @param startIndex
	 * @param str
	 * @param regex
	 * @return
	 */
	public static List<Map<String, String>> getMatchingByAlias(String startIndex,String str, String regex) {
		List<Map<String, String>> rsList = new ArrayList<>();
		if(str == null || "".equals(str.trim()))
		{
			return rsList;
		}
		if(startIndex == null || "".equals(startIndex.trim()))
		{
			return rsList;
		}
		if(regex == null || "".equals(regex.trim()))
		{
			return rsList;
		}
		String[] echoS = str.split(startIndex); 
		List<String> splitList = new ArrayList<>();
		StringBuffer split = new StringBuffer();
		int count = 1;
		for(int i = 0,sz = echoS.length ; i < sz ; i ++)
		{
			String item = echoS[i];
			item = startIndex + item;
			split.append(item);
			split.append("\n");
			count ++;
			//结尾
			if(i == sz-1)
			{
				splitList.add(split.toString());
				split.setLength(0);
				break;
			}
			if(count == SPLIT_NUM)
			{
				splitList.add(split.toString());
				split.setLength(0);
				count = 1;
			}
		}

		List<Map<String, String>> matchList = null;
		for(String item : splitList)
		{
			matchList = getMatchingByAlias(item,regex);
			if(matchList != null && !matchList.isEmpty())
			{
				rsList.addAll(matchList);
			}
		}
//		long time3 =  System.currentTimeMillis();
//		System.out.println("耗时-regex==" + (time3 - time2));
//		System.out.println(" -countSum==" +countSum);
		return rsList;
	}
	
	/**
	 * Definition:获取括号内的内容
	 * @param left 左括号
	 * @param right 右括号
	 * @param str 待匹配的内容
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016-12-9
	 */
	public static List<String> getBracketsContent(char left, char right, String str) {
		if (StringUtils.isEmpty(str) || !str.contains(String.valueOf(left)) || !str.contains(String.valueOf(right))) {
			return null;
		}
		List<String> ret = new ArrayList<String>();
		int l = 0;
		int r = 0;
		int count = 0;
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (str.charAt(i) == left) {
				if (count == 0) {
					l = i;
				}
				count++;
			}
			if (str.charAt(i) == right) {
				count--;
				if (count == 0) {
					r = i;
					ret.add(str.substring(l + 1, r));
					List<String> tmp = getBracketsContent(left, right, str.substring(l + 1, r));
					if (tmp != null && !tmp.isEmpty()) {
						ret.addAll(tmp);
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Definition:判断正则表达式是否为命名分组表达式
	 * @param regex
	 * @return
	 * @Author: oblivion
	 * @Created date: 2016-12-9
	 */
	public static boolean isGroupRegex(String regex) {
		if (regex == null || "".equals(regex)) {
			return false;
		}
		List<String> list = getBracketsContent('(', ')', regex);
		if (list == null || list.isEmpty()) {
			return false;
		}
		String groupRegex = "\\?<\\S+>.+";
		Pattern pattern = Pattern.compile(groupRegex);
		Matcher matcher = null;
		for (String str : list) {
			matcher = pattern.matcher(str);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		String regex = "(?i)(?<HotFixID>KB(\\S){1,100})\\s{1,10}(?<InstalledOn>\\S{1,20})";
		String tmp = regex.replaceFirst("(?i)\\(\\?i\\)", "");
		System.out.println(tmp);
	}
	 
}

	