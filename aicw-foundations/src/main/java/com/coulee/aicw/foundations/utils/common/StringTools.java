package com.coulee.aicw.foundations.utils.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description:字符串操作工具类
 * Copyright (C) 2014 Coulee All Right Reserved.
 * Author：LanChao
 * Create Date: 2014年12月2日
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class StringTools {
	
	private static Logger logger = LoggerFactory.getLogger(StringTools.class);


	/**
	 * Definition:iso字符集转换成GBK
	 * @param strIso
	 * @return
	 * @throws FrameworkException
	 * @Author: LanChao
	 * @Created date: 2014年12月2日
	 */
	public static String iso2gbk(String strIso) throws Exception {
		// 判断给定参数是否为空，如果空则返回参数错误消息
		if (strIso == null) {
			throw new Exception("error.app.util.stringtools.IncorrectPara", null);
		}
		byte[] tmpbyte = null;
		try {
			// 得到给定字符串按iso字符集字节化
			tmpbyte = strIso.getBytes("ISO8859_1");
			// 转化为GBK字符集
			strIso = new String(tmpbyte, "GBK");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error: Method: StringTools.iso2gbk :"
					+ e.getMessage());
			throw new Exception(
					"error.app.util.stringtools.UnsupportedEncodingException",
					null);
		}
		return strIso;
	}

	/**
	 * 判断一个字符串是否不为空
	 * 
	 * @param str
	 *            要处理判断的字符串
	 * @return boolean - 如果字符串不是null而且不是" "型，则返回true
	 */
	public static boolean isNotNull(String str) {
		if (str != null && str.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Description:判断一个字符串是否为空或者null
	 * @param str
	 * @return
	 * @Author： yan chong 
	 * @Created ：2016年5月30日 下午2:29:27
	 */
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.equals("")){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断字符串是否为能够转换为int型的数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		if (str == null || "".equals(str)) {
			return false;
		} else {
			Pattern pattern = Pattern.compile("[0-9]+[0-9]*");
			return pattern.matcher(str).matches();
		}
	}


	/**
	 * 将UNICODE 字符串解码
	 * 
	 * @param src
	 *            String UNICODE 编码的字符串
	 * @return String 解码后的字符串
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}


	/**
	 * 字符串去空
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String toTrim(String str) {
		if (str == null) {
			return "";
		}
		if (str.trim().equalsIgnoreCase("null")) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 判断字符串是否为非空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return "".equals(toTrim(string));
	}

	/**
	 * 字符串替换，将 str 中的 substr 全部换成 restr
	 * 
	 * @param str
	 *            源字符串
	 * @param substr
	 *            被替换的字符串
	 * @param restr
	 *            替换为的字符串
	 * @return 替换后的字符串
	 * @exception 无
	 */
	public static String replaceChar(String str, String substr, String restr) {
		String[] tmp = tokenizerString2Array(str, substr);
		String returnstr = null;
		if (tmp.length != 0) {
			returnstr = tmp[0];
			for (int i = 0; i < tmp.length - 1; i++) {
				returnstr = null2String(returnstr) + restr + tmp[i + 1];
			}
		}
		return null2String(returnstr);
	}

	/**
	 * 如果字符串为null,返回为空,否则返回该字符串
	 * 
	 * @param s
	 *            字符串
	 * @return String
	 */
	public static String null2String(String s) {
		return s == null ? "" : s;
	}

	/**
	 * 将输入的字符串str按照分割符dim分割成字符串数组并返回ArrayList字符串数组
	 * 
	 * @param str
	 *            给定字符串
	 * @param dim
	 *            分割符
	 * @param returndim
	 *            返回值中是否包含分割符号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List tokenizerString(String str, String dim,
			boolean returndim) {
		str = null2String(str);
		dim = null2String(dim);
		List strlist = new ArrayList();
		// 字符串str按照分割符dim分割成字符串数组并返回ArrayList字符串数组
		StringTokenizer strtoken = new StringTokenizer(str, dim, returndim);

		while (strtoken.hasMoreElements()) {
			strlist.add(strtoken.nextElement());
		}
		return strlist;
	}

	/**
	 * 类似TokenizerString,将输入的字符串str按照分割符dim分割成字符串数组 并返回定长字符串数组
	 * 
	 * @param str
	 * @param dim
	 * @return String[]
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String[] tokenizerString2Array(String str, String dim,
			boolean returndim) {
		List strlist = tokenizerString(str, dim, returndim);
		// 得到Arraylist的长度
		int strcount = strlist.size();
		String[] strarray = new String[strcount];
		// 把Arraylist压入数组
		for (int i = 0; i < strcount; i++) {
			strarray[i] = (String) strlist.get(i);
		}
		return strarray;
	}

	public static String[] tokenizerString2Array(String str, String dim) {
		return tokenizerString2Array(str, dim, false);
	}

	/**
	 * 转换正则表达式
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String transRegEx(String str) {
		if (str == null || "".equals(str)) {
			return str;
		}
		String[] crary = { "\\\\", "\\.", "\\$", "\\^", "\\{", "\\[", "\\(", "\\|", "\\)", "\\*", "\\+", "\\?" };
		for (int i = 0; i < crary.length; i++) {
			if (i != 0) {
				str = str.replaceAll(crary[i], "\\" + crary[i]);
			} else {
				str = str.replaceAll(crary[i], "\\\\\\\\");
			}
		}
		return str;
	}

	/**
	 * 生成8位的小写字母和数字组成的passcode
	 * 
	 * @param SourceData
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String getPasscodeRandomChar() {
		Random random = new Random();
		String _returnString = "";
		for (int i = 0; i < 8; i++) {
			StringBuffer ch = new StringBuffer(2);
			int no = random.nextInt(10 + 26); // ASCII值
			ch.append((no < 10) ? (char) (no + 48) : (char) (no + 55));
			_returnString += ch.toString().toLowerCase();
		}
		return _returnString;
	}

	/**
	 * 
	 * 首字母变大写
	 * 
	 * @param key
	 * @return
	 */
	public static String firstCharactorUpper(String str) {
		if (str == null || "".equals(str)) {
			return null;
		}
		char[] strchar = str.toCharArray();
		if (strchar.length > 0) {
			char a = strchar[0];
			if (a >= 'a' && a <= 'z') {
				strchar[0] = (char) (a - 32);
			}
		}
		str = new String(strchar);
		return str;
	}

	/**
	 * 
	 * 首字母变小写
	 * 
	 * @param key
	 * @return
	 */
	public static String firstCharactorLower(String str) {
		if (str == null || "".equals(str)) {
			return null;
		}
		char[] strchar = str.toCharArray();
		if (strchar.length > 0) {
			char a = strchar[0];
			if (a >= 'A' && a <= 'Z') {
				strchar[0] = (char) (a + 32);
			}
		}
		str = new String(strchar);
		return str;
	}


	/*
	 * 检测是否包含特殊字符
	 */
	public static boolean getIsContainSpecialChar(String pwd) {
		Pattern p = Pattern.compile(".*\\W.*");
		Matcher m = p.matcher(pwd);
		boolean flag = m.matches();
		return flag;
	}

	/*
	 * 检测是否包含数字字符
	 */
	public static boolean getIsContainNumberChar(String pwd) {
		Pattern p = Pattern.compile(".*[0-9].*");
		Matcher m = p.matcher(pwd);
		boolean flag = m.matches();
		return flag;
	}

	/*
	 * 检测是否包含小写字符
	 */
	public static boolean getIsContainLowercaseChar(String pwd) {
		Pattern p = Pattern.compile(".*[a-z].*");
		Matcher m = p.matcher(pwd);
		boolean flag = m.matches();
		return flag;
	}

	/*
	 * 检测是否包含大写字符
	 */
	public static boolean getIsContainUpperCharChar(String pwd) {
		Pattern p = Pattern.compile(".*[A-Z].*");
		Matcher m = p.matcher(pwd);
		boolean flag = m.matches();
		return flag;
	}

	public static String getCDataString(String code) {
		if (null != code && !"".equals(code)) {
			if (code.indexOf("]]>") > -1) {
				code = code.replaceAll("]]>", "]]]><![CDATA[]>");
			}
			code = "<![CDATA[" + code + "]]>";
		}
		return code;
	}

	public static String getUTF8StrByISO8859(String operStr) {
		if (operStr != null && !"".equals(operStr)) {
			try {
				operStr = new String(operStr.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return operStr;
	}

}
