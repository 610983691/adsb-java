package com.coulee.aicw.foundations.utils.crypt.types;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.log4j.Logger;

import com.coulee.aicw.foundations.utils.crypt.Crypto;

/**
 * Description: AES<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class Aes extends Crypto {
	private static final Logger logger = Logger.getLogger(Aes.class);
	public static final String TYPE = "AES";

	@Override
	public String encrypt(String ori, String key) {
		if (isEmpty(ori)) {
			return null;
		}
		try {
			String dest = encode(ori, key);
			return this.encodeBase64(dest.getBytes("ISO-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String decrypt(String ori, String key) {
		if (isEmpty(ori)) {
			return null;
		}
		try {
			byte[] dBase64Rs = this.decodeBase64(ori);
			if(dBase64Rs == null) {
				return null;
			}
			String src = new String(dBase64Rs, "ISO-8859-1");
			return decode(src, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: 解密<br> 
	 * Created date: 2018年9月17日
	 * @param str
	 * @param key
	 * @return
	 * @author oblivion
	 * @throws Exception 
	 */
	private String decode(String str, String key) throws Exception {
		// base64解码
		byte[] b = this.decodeBase64(str);
		if(b == null) {
			System.out.println("decode fail");
			logger.error("decode fail--Please check key-"+key);
			return null;
		}
		Cipher cipher = Cipher.getInstance(TYPE);
		cipher.init(Cipher.DECRYPT_MODE, this.getKey(key));
		byte[] byteFina = cipher.doFinal(b);
		return new String(byteFina);
	}
	public static void main(String[] args) {
		Aes aes = new Aes();
		try {
			String ss = aes.encrypt("coulee2018", "couleeaciw");
			System.out.println(ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	/**
	 * Description: 加密<br> 
	 * Created date: 2018年9月17日
	 * @param str
	 * @param key
	 * @return
	 * @author oblivion
	 * @throws Exception 
	 */
	private String encode(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(TYPE);
		cipher.init(Cipher.ENCRYPT_MODE, this.getKey(key));
		byte[] byteFina = cipher.doFinal(str.getBytes());
		// base64编码
		return this.encodeBase64(byteFina);
	}

	/**
	 * Description: 生成密钥<br> 
	 * Created date: 2018年9月17日
	 * @param strKey
	 * @return
	 * @author oblivion
	 * @throws Exception 
	 */
	private Key getKey(String strKey) throws Exception {
		strKey = strKey + KEY_SUFFIX;
		KeyGenerator keyGenerator = KeyGenerator.getInstance(TYPE);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG"); // 追加防止跨操作系统的密钥对象不同问题
		secureRandom.setSeed(strKey.getBytes());
		keyGenerator.init(128, secureRandom);
		Key key = keyGenerator.generateKey();
		return key;
	}
}
