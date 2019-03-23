package com.coulee.aicw.foundations.utils.common;


import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

/**
 * Description: jwt操作工具类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Configuration
@ConditionalOnWebApplication
public class JWTUtils {
	
	@Autowired
	private Environment env;
	
	private static Logger logger = LoggerFactory.getLogger(JWTUtils.class);

	/**
	 * 密钥
	 */
	private static String SECURITY_KEY = "f3lsdjkl42*0324j2()_&*123fhsdayp91389&*(^3==";
	
	@Bean
	public JWTUtils initKey() {
		String cryptoKey = env.getProperty("crypto.key");
		if (!StringUtils.isEmpty(cryptoKey)) {
			SECURITY_KEY = cryptoKey;
		}
		return this;
	}
	
	/**
	 * Definition:使用配置中心配置的秘钥创建token
	 * @param subject subject
	 * @param expirationMill token过期时间，0永不过期
	 * @return
	 * @Author: oblivion
	 * @Created date: 2017-5-31
	 */
	public static String create(String subject, long expirationMill) {
		long now = System.currentTimeMillis();
		Date nowDate = new Date(now);
		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(nowDate).setIssuer("aicw-security")
				.setSubject(subject).signWith(SignatureAlgorithm.HS256, SECURITY_KEY);
		if (expirationMill > 0) {
			long expMillis = now + expirationMill;
		    builder.setExpiration(new Date(expMillis));
		}
		return builder.compact();
	}
	
	/**
	 * Definition:使用配置中心秘钥解密token<br> 
	 * @param token
	 * @return
	 * @throws Exception
	 * @Author: oblivion
	 * @Created date: 2017-5-31
	 */
	public static Claims parse(String token) throws Exception {
		return Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
	}
	
	/**
	 * Description: 验证token是否合法<br> 
	 * Created date: 2018年7月31日
	 * @param token
	 * @return
	 * @author oblivion
	 */
	public static boolean isValidate(String token) {
		boolean isValidate = true;
		try {
			parse(token);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
				isValidate = true;
			} else {
				isValidate = false;
			}
		}
		return isValidate;
	}
	
	/**
	 * Description: 根据旧token刷新获取新token<br> 
	 * Created date: 2018年7月31日
	 * @param oldToken
	 * @return
	 * @author oblivion
	 */
	public static String refresh(String oldToken) {
		if (!isValidate(oldToken)) {
			logger.error("oldToken is not validated !!!");
			return null;
		}
		String[] jwts = oldToken.split("\\.");
		if (jwts.length == 3) {
			String payload = Base64Codec.BASE64URL.decodeToString(jwts[1]);
			JSONObject jo = JSON.parseObject(payload);
			String subject = jo.getString("sub");
			long expirationMill = 0;
			if (jo.containsKey("exp")) {
				long exp = jo.getLongValue("exp");
				long iat = jo.getLongValue("iat");
				expirationMill = (exp - iat) * 1000;
			}
			return create(subject, expirationMill);
		}
		return null;
	}
	
}
