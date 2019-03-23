package com.coulee.foundations.configserver.commons;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.coulee.aicw.foundations.config.exception.ExceptionCode;
import com.coulee.aicw.foundations.utils.common.JWTUtils;

/**
 * Description: 安全过滤器<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = "/*")
public class SecurityFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${filter-white-list}")
	private String whiteListStr;

	/**
	 * 白名单
	 */
	private Set<String> whiteList = new HashSet<String>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		whiteList.add("/");
		whiteList.add("/login");
		whiteList.add("/locations");
		if (!StringUtils.isEmpty(whiteListStr)) {
			String[] split = whiteListStr.split(",");
			for (String s : split) {
				whiteList.add(s);
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String uri = httpRequest.getRequestURI().replaceAll("//", "/").replace(httpRequest.getContextPath(), "");
		if (this.checkWhiteList(uri)) {
			chain.doFilter(request, response);
			return;
		}
		logger.info("request url {}", uri);
		String token = httpRequest.getHeader("Authorization");
		// restful接口基于jwt形式的token认证
		if (!StringUtils.isEmpty(token) && token.startsWith("JWT ")) {
			try {
				JWTUtils.parse(token.substring(4));
				chain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				httpResponse.setCharacterEncoding("UTF-8");
				httpResponse.setContentType("application/json; charset=utf-8");
				httpResponse.setStatus(ExceptionCode.UNAUTHORIZED.getCode());
				httpResponse.getWriter().write(ExceptionCode.UNAUTHORIZED.getErrorMsg());
			}
		} else {
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>) httpRequest.getSession().getAttribute("user");
			if (map != null && "true".equals(map.get("isLogin"))) {
				chain.doFilter(request, response);
			} else {
				httpResponse.sendRedirect("/");
			}
		}
		return;
	}

	@Override
	public void destroy() {
	}

	/**
	 * Description: 白名单检查<br>
	 * Created date: 2017年12月14日
	 * 
	 * @param url
	 * @return
	 * @author oblivion
	 */
	private boolean checkWhiteList(String url) {
		if (whiteList.contains(url)) {
			return true;
		} else {
			for (String s : whiteList) {
				if (s.endsWith("*") && url.startsWith(s.replace("*", ""))) {
					return true;
				}
			}
		}
		return false;
	}
}
