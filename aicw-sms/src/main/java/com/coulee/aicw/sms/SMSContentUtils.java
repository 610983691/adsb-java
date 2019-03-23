package com.coulee.aicw.sms;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.coulee.aicw.sms.entity.SMSContentEntity;

/**
 * Description: 短信内容工具类<br>
 * Create Date: 2018年10月29日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class SMSContentUtils {
	
	/**
	 * 手机号不存在于系统中
	 */
	public static final int WRONG_TYPE_PHONE = 1;
	
	/**
	 * 应用别名不存在
	 */
	public static final int WRONG_TYPE_APPALIAS = 2;
	
	/**
	 * 审批申请短信内容模板
	 */
	private static final String TEMPLATE_APPROVAL = "姓名：{0}，手机号：{1}，申请由外网{2}访问资产{3}，访问时长{4}分钟，回复y同意，回复n不同意";
	
	/**
	 * 审批通过短信内容模板
	 */
	private static final String TEMPLATE_PASS = "您由外网{0}访问资产{1}、访问时长为{2}分钟的申请已通过";
	
	/**
	 * 审批未通过短信内容模板
	 */
	private static final String TEMPLATE_NOT_PASS = "您由外网{0}访问资产{1}、访问时长{2}分钟的申请未通过，请联系资产管理员";
	
	/**
	 * 策略超时短信内容模板
	 */
	private static final String TEMPLATE_EXPIRE = "您由外网{0}访问资产{1}、访问时长{2}分钟的申请已到期，如需继续访问请重新申请";
	
	/**
	 * 策略下发失败短信内容模板
	 */
	private static final String TEMPLATE_POLICY_ERROR = "您由外网{0}访问资产{1}、访问时长{2}分钟的申请策略执行失败，请联系管理员";
	
	/**
	 * 手机号不合法短信内容模板
	 */
	private static final String WRONGFUL_PHONE = "您的手机号码不合法，请联系管理员";
	
	/**
	 * 应用别名不合法短信内容模板
	 */
	private static final String WRONGFUL_APPALIAS = "您申请访问的应用别名不合法，请联系管理员";
	
	/**
	 * 申请内容不合法回复短信内容模板
	 */
	private static final String APPLY_CONTENT_ERROR = "您的申请内容不合法，请按\"应用别名	外网IP	申请时长（分钟）\"格式重新申请";
	
	/**
	 * 申请短信解析正则解析器
	 */
	private static final Pattern APPLY_CONTENT_REGEX_PATTERN = Pattern.compile("(?<appAlias>\\S+)\\s+(?<netIp>\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s+(?<applyDuration>\\d+)");
	
	/**
	 * Description: 解析申请短信内容<br> 
	 * 应用别名	外网IP	申请时长（分钟）
	 * Created date: 2018年10月29日
	 * @param msg
	 * @return
	 * @author oblivion
	 */
	public static SMSContentEntity parseApplyContent(String msg) {
		SMSContentEntity entity = new SMSContentEntity();
		if (StringUtils.isEmpty(msg)) {
			entity.setErrorMsg(APPLY_CONTENT_ERROR);
		} else {
			Matcher matcher = APPLY_CONTENT_REGEX_PATTERN.matcher(msg.trim());
			if (matcher.find()) {
				entity.setAppAlias(matcher.group("appAlias"));
				entity.setNetIp(matcher.group("netIp"));
				entity.setApplyDuration(matcher.group("applyDuration"));
			} else {
				entity.setErrorMsg(APPLY_CONTENT_ERROR);
			}
		}
		return entity;
	}
	
	/**
	 * Description: 构造审批申请短信内容<br> 
	 * Created date: 2018年10月29日
	 * @return
	 * @author oblivion
	 */
	public static String makeApprovalContent(SMSContentEntity entity) {
		MessageFormat mf = new MessageFormat(TEMPLATE_APPROVAL);
		String context = mf.format(new Object[] { entity.getApplyUser(), entity.getApplyPhone(), entity.getNetIp(),
				entity.getAppAlias(), entity.getApplyDuration() });
		return context;
	}
	
	/**
	 * Description: 构造申请通过短信通知内容<br> 
	 * Created date: 2018年10月29日
	 * @param entity
	 * @return
	 * @author oblivion
	 */
	public static String makePassContent(SMSContentEntity entity) {
		MessageFormat mf = new MessageFormat(TEMPLATE_PASS);
		String context = mf.format(new Object[] { entity.getNetIp(), entity.getAppAlias(), entity.getApplyDuration() });
		return context;
	}
	
	/**
	 * Description: 构造申请未通过短信通知内容<br> 
	 * Created date: 2018年10月29日
	 * @param entity
	 * @return
	 * @author oblivion
	 */
	public static String makeNotPassContent(SMSContentEntity entity) {
		MessageFormat mf = new MessageFormat(TEMPLATE_NOT_PASS);
		String context = mf.format(new Object[] { entity.getNetIp(), entity.getAppAlias(), entity.getApplyDuration() });
		return context;
	}
	
	/**
	 * Description: 构造申请时长到期短信通知内容<br> 
	 * Created date: 2018年10月29日
	 * @param entity
	 * @return
	 * @author oblivion
	 */
	public static String makeExpireContent(SMSContentEntity entity) {
		MessageFormat mf = new MessageFormat(TEMPLATE_EXPIRE);
		String context = mf.format(new Object[] { entity.getNetIp(), entity.getAppAlias(), entity.getApplyDuration() });
		return context;
	}
	
	/**
	 * Description: 构造策略下发失败短信通知内容<br> 
	 * Created date: 2018年10月29日
	 * @param entity
	 * @return
	 * @author oblivion
	 */
	public static String makePolicyErrorContent(SMSContentEntity entity) {
		MessageFormat mf = new MessageFormat(TEMPLATE_POLICY_ERROR);
		String context = mf.format(new Object[] { entity.getNetIp(), entity.getAppAlias(), entity.getApplyDuration() });
		return context;
	}
	
	/**
	 * Description: 生成不合法短信通知内容<br> 
	 * Created date: 2018年10月29日
	 * @param type 1:手机号不合法；2:应用别名不合法
	 * @return
	 * @author oblivion
	 */
	public static String makeWrongfulContent(int type) {
		switch (type) {
		case WRONG_TYPE_PHONE:
			return WRONGFUL_PHONE;
		case WRONG_TYPE_APPALIAS:
			return WRONGFUL_APPALIAS;
		}
		return "";
	}
}
