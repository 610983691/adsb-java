package com.coulee.aicw.foundations.utils.crypt.types;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.coulee.aicw.foundations.utils.crypt.Crypto;

/**
 * Description: DES<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class Des extends Crypto {
	
	public static final String TYPE = "DES";

	@Override
	public String encrypt(String ori, String key) {
		if (isEmpty(ori)) {
			return null;
		}
		try {
			byte[] bytes = encode(ori.getBytes("UTF8"), key);
			return this.encodeBase64(bytes);
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
			byte[] bytes = this.decodeBase64(ori);
			return new String(decode(bytes, key), "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Description: 加密<br> 
	 * Created date: 2018年9月17日
	 * @param bytePub
	 * @param strKey
	 * @return
	 * @author oblivion
	 * @throws Exception 
	 */
	private byte[] encode(byte[] bytePub, String strKey) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		Key key = getKey(strKey);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(bytePub);
	}

	/**
	 * Description: 解密<br> 
	 * Created date: 2018年9月17日
	 * @param bytePri
	 * @param strKey
	 * @return
	 * @author oblivion
	 * @throws Exception 
	 */
	private byte[] decode(byte[] bytePri, String strKey) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		Key key = getKey(strKey);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(bytePri);
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
		KeyGenerator generator = KeyGenerator.getInstance("DES");
		SecureRandom secure = SecureRandom.getInstance("SHA1PRNG");
		secure.setSeed(strKey.getBytes());
		generator.init(56, secure);
		Key key = generator.generateKey();
		return key;
	}

}
