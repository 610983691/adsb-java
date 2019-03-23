package com.coulee.aicw.foundations.utils.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description: MD5加密工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class MD5 {
	
	/**
	 * Definition:将密文字节数组转成hex文
	 * @param bytes
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年7月2日
	 */
	private static String tohex(byte[] bytes) {
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		// 把密文转换成十六进制的字符串形式
        int j = bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
	}
	
	/**
	 * Definition:对字符串进行MD5加密
	 * @param src 要加密的明文
	 * @return 加密后的MD5值
	 * @Author: oblivion
	 * @Created date: 2015年6月4日
	 */
	public static String md5(String src) {
        try {
            byte[] btInput = src.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            return tohex(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	
	/**
	 * Definition:对文件进行MD5加密
	 * @param file 要加密的文件
	 * @return
	 * @Author: oblivion
	 * @Created date: 2015年7月2日
	 */
	public static String md5(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
	        FileChannel ch = in.getChannel();  
	        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length()); 
	        MessageDigest mdInst = MessageDigest.getInstance("MD5");
	        mdInst.update(byteBuffer);
	        byte[] md = mdInst.digest();
	        return tohex(md);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
    }  

}
