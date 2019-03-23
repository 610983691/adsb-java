package com.coulee.aicw.sms.service.impl.cmpp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.coulee.aicw.sms.entity.CMPPConfig;
import com.coulee.aicw.sms.service.ISMSSenderService;
import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitMessage;
import com.huawei.insa2.comm.cmpp30.message.CMPP30SubmitRepMessage;
import com.huawei.smproxy.SMProxy30;

/**
 * Description: 基于华为SMProxy实现的CMPP3.0协议发送短信<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * 
 * @author oblivion
 * @version 1.0
 */
@Profile("cmpp3")
@Service
public class SMSSenderCMPP3Impl implements ISMSSenderService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private SMProxy30 smProxy = null;

	private CMPPConfig config;

	public SMSSenderCMPP3Impl(CMPPConfig config) {
		this.config = config;
	}
	
	/**
	 * Description: 初始化<br> 
	 * Created date: 2018年10月30日
	 * @author oblivion
	 */
	private void initProxy() {
		if (this.smProxy == null || !StringUtils.isEmpty(this.smProxy.getConnState())) {
			try {
				this.smProxy = new SMProxy30(CMPPUtils.getCMPPArgs(config));
				logger.info("init cmpp sms gateway success, ip {}, port {}", this.config.getHost(), this.config.getPort());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Description: 构造发送消息体<br> 
	 * Created date: 2018年10月30日
	 * @param pkTotal 总条数
	 * @param pkNumber 当前条
	 * @param msgFmt 消息格式
	 * @param destPhones 对端手机号
	 * @param content 消息体
	 * @param businessId 业务编码
	 * @return
	 * @author oblivion
	 */
	private CMPP30SubmitMessage getMessage(int pkTotal, int pkNumber, int msgFmt, String[] destPhones, byte[] content, String businessId) {
		CMPP30SubmitMessage submitMsg = new CMPP30SubmitMessage(
				pkTotal, // @pk_Total 相同msg_Id消息总条数,短短信这里是1
				pkNumber, // @pk_Number 相同msg_Id的消息序号
				1, // @registered_Delivery 是否要求返回状态报告
				1, // @msg_Level 信息级别
				StringUtils.isEmpty(businessId) ? "" : businessId, // @service_Id 业务类型 用户自定义 用来分类查询
				2, // @fee_UserType 0对目的终端计费；1对源终端计费；2对SP计费;
				"", // @fee_Terminal_Id 被计费用户的号码
				1, // @fee_Terminal_Type 被计费用户的类型 0：表示真实号码；为1：表示伪码
				0, // @tp_Pid GSM协议类型 一般文本的时候设0,铃声图片设1
				pkTotal > 1 ? 1 : 0, // @tp_Udhi GSM协议类型 0不包含头部信息 1包含头部信息
				msgFmt, // @msg_Fmt 消息格式
				this.config.getSource_addr(), // @msg_Src 消息内容来源 6位的企业代码,这里需修改
				"01", // @fee_Type 资费类别 一般为02：按条计信息费
				"10", // @fee_Code 资费代码(以分为单位)
				null, // @valid_Time 存活有效期
				null, // @at_Time 定时发送时间
				this.config.getService_id(), // @src_Terminal_Id 移动所提供的服务代码 此处需修改
				destPhones, // @dest_Terminal_Id 接收业务的MSISDN号码,就是接收短信的手机号,String数组
				0, // @dest_Terminal_Type 接收业务的类型 0：表示真实号码；为1：表示伪码
				content, // @msg_Content 消息内容 byte[],发送的消息内容,需要转化为byte[]
				"" // @LinkID
		);
		return submitMsg;
	}

	@Override
	public boolean sendSms(String phone, String msg, String businessId) {
		this.initProxy();
		String[] destPhones = new String[] { phone };
		try {
			byte[] bytes = msg.getBytes("UTF-8");
			if (bytes.length <= 140) {
				CMPP30SubmitMessage submitMsg = this.getMessage(1, 1, 15, destPhones, bytes, businessId);
				CMPP30SubmitRepMessage sub = (CMPP30SubmitRepMessage) this.smProxy.send(submitMsg);
				if (sub.getResult() != 0) {
					return false;
				}
			} else {
				List<byte[]> msgbytes = CMPPUtils.getLongByte(msg);
				for (int i = 0; i < msgbytes.size(); i++) {
					CMPP30SubmitMessage submitMsg = this.getMessage(msgbytes.size(), i + 1, 8, destPhones, msgbytes.get(i), businessId);
					CMPP30SubmitRepMessage sub = (CMPP30SubmitRepMessage) this.smProxy.send(submitMsg);
					if (sub.getResult() != 0) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
