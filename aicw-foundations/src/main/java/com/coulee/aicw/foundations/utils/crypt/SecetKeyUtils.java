package com.coulee.aicw.foundations.utils.crypt;

import java.util.Random;

import com.coulee.aicw.foundations.utils.crypt.types.Aes;

/**
 * Description: 密钥操作类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class SecetKeyUtils {
	
	private static final String KEY = "f28JxE*2!b,=";

	private static Crypto crypto = Crypto.getInstance(Aes.TYPE);
	
	/**
	 * Description: 生成随机密钥<br>
	 * Created date: 2017年12月15日
	 * 
	 * @param length 密钥长度
	 * @return
	 * @author oblivion
	 */
	public static String generateKey(int length) {
		String val = "";
		char charOrNum = 'c';
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			charOrNum = random.nextInt(2) % 2 == 0 ? 'c' : 'n';
			if ('c' == charOrNum) {
				int upperOrletter = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + upperOrletter);
			} else {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * Description: 对密钥进行加密<br>
	 * Created date: 2017年12月15日
	 * 
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public static String encodeKey(String ori) {
		return crypto.encrypt(ori, KEY);
	}

	/**
	 * Description: 对密钥解密<br>
	 * Created date: 2017年12月15日
	 * 
	 * @param ori
	 * @return
	 * @author oblivion
	 */
	public static String decodeKey(String ori) {
		return crypto.decrypt(ori, KEY);
	}

}
