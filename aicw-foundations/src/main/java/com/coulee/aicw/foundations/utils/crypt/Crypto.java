package com.coulee.aicw.foundations.utils.crypt;

import java.lang.reflect.Method;

import com.coulee.aicw.foundations.utils.crypt.types.Aes;
import com.coulee.aicw.foundations.utils.crypt.types.Des;
import com.coulee.aicw.foundations.utils.crypt.types.TripleDes;

/**
 * Description: 加解密工具<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public abstract class Crypto {
	
	/**
	 * 秘钥混淆后缀
	 */
	protected final String KEY_SUFFIX = "_aicw";
	
	/**
	 * Description: 获取加解密工具实例<br> 
	 * Created date: 2018年9月17日
	 * @param type
	 * @return
	 * @author oblivion
	 */
	public static Crypto getInstance(String type) {
		if (Aes.TYPE.equals(type)) {
			return new Aes();
		} else if (TripleDes.TYPE.equals(type)) {
			return new TripleDes();
		} else if (Des.TYPE.equals(type)) {
			return new Des();
		}
		return null;
	}
	
	/**
	 * Description: 判断是否为空字符串<br> 
	 * Created date: 2018年9月17日
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	protected boolean isEmpty(String ori) {
		if (ori == null || "".equals(ori) || "".equals(ori.trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Description: base64编码<br> 
	 * Created date: 2018年10月11日
	 * @param input
	 * @return
	 * @throws Exception
	 * @author oblivion
	 */
	protected String encodeBase64(byte[] input) throws Exception {
		Class<?> clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method method = clazz.getMethod("encode", byte[].class);
		method.setAccessible(true);
		Object retObj = method.invoke(null, new Object[] { input });
		return (String) retObj;
	}

	/**
	 * Description: base64解码<br> 
	 * Created date: 2018年10月11日
	 * @param input
	 * @return
	 * @throws Exception
	 * @author oblivion
	 */
	protected byte[] decodeBase64(String input) throws Exception {
		Class<?> clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method method = clazz.getMethod("decode", String.class);
		method.setAccessible(true);
		Object retObj = method.invoke(null, input);
		if(retObj == null) {
			return null;
		}
		return (byte[]) retObj;
	}

	/**
	 * Description: 将字符串加密<br> 
	 * Created date: 2018年9月17日
	 * @param ori 待加密字符串
	 * @param key 秘钥
	 * @return 加密后的字符串
	 * @author oblivion
	 */
	public abstract String encrypt(String ori, String key);
	
	/**
	 * Description: 将字符串解密<br> 
	 * Created date: 2018年9月17日
	 * @param ori 待解密字符串
	 * @param key 秘钥
	 * @return 解密后的字符串
	 * @author oblivion
	 */
	public abstract String decrypt(String ori, String key);
	
}
