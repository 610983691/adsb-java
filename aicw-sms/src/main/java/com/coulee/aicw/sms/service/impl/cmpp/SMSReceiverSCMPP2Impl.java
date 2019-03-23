package com.coulee.aicw.sms.service.impl.cmpp;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.coulee.aicw.sms.entity.CMPPConfig;
import com.coulee.aicw.sms.entity.SMSReceiveContentEntity;
import com.coulee.aicw.sms.service.ISMSReceiverService;
import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.smproxy.SMProxy;

/**
 * Description: CMPP2.0协议短信接收<br>
 * Create Date: 2018年10月30日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Profile("cmpp2")
@Service
public class SMSReceiverSCMPP2Impl extends SMProxy {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ISMSReceiverService receivedTodo;

	@Autowired
	public void setReveivedTodo(ISMSReceiverService receivedTodo) {
		this.receivedTodo = receivedTodo;
	}

	@Autowired
	public SMSReceiverSCMPP2Impl(CMPPConfig config) {
		super(CMPPUtils.getCMPPArgs(config));
	}

	@Override
	public CMPPMessage onDeliver(CMPPDeliverMessage deliverMsg) {
		SMSReceiveContentEntity entity = new SMSReceiveContentEntity();
		if (deliverMsg.getRegisteredDeliver() == 0) {
			try {
				byte[] reveivedBytes = deliverMsg.getBytes();
				String content = null;
				if (deliverMsg.getMsgFmt() == 8) { //UTF-8
					content = new String(reveivedBytes, "UTF-8");
				} else { //GBK
					content = new String(reveivedBytes, "GBK");
				}
				entity.setContent(content);
				entity.setBusinessId(deliverMsg.getServiceId());
				entity.setPhone(deliverMsg.getSrcterminalId());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("收到状态报告消息： stat=").append(deliverMsg.getStat()).append("dest_termID=")
					.append(deliverMsg.getDestTerminalId()).append(";destterm=").append(deliverMsg.getDestnationId())
					.append(";serviceid=").append(deliverMsg.getServiceId()).append(";tppid=")
					.append(deliverMsg.getTpPid()).append(";tpudhi=").append(deliverMsg.getTpUdhi()).append(";msgfmt")
					.append(deliverMsg.getMsgFmt()).append(";内容").append(new String(deliverMsg.getMsgContent()))
					.append(";srctermid=").append(deliverMsg.getSrcterminalId()).append(";deliver=")
					.append(deliverMsg.getRegisteredDeliver());
			entity.setError(sb.toString());
		}
		new Thread() {
			@Override
			public void run() {
				logger.info("Reveived SMS : ", entity.toString());
				receivedTodo.receive(entity);
			}
		}.start();
		return super.onDeliver(deliverMsg);
	}

}
