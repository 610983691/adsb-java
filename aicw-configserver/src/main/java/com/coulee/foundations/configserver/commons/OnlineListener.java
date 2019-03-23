package com.coulee.foundations.configserver.commons;

import java.io.File;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.util.StringUtils;

import com.coulee.foundations.ConfigServerConstants;

/**
 * Description: session在线监听<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@WebListener
public class OnlineListener implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
		String oldDbFileName = map.get(ConfigServerConstants.TEMP_DB_FILE);
		if (!StringUtils.isEmpty(oldDbFileName)) {
			//删除旧文件
			File oldDbFile = ConfigServerUtils.getTempDbFilePath(oldDbFileName);
			if (oldDbFile.exists()) {
				oldDbFile.delete();
			}
		}
	}

}
