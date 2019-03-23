package com.coulee.aicw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.coulee.aicw.foundations.utils.common.UUIDHexGenerator;
import com.coulee.aicw.service.ISMSService;
import com.coulee.aicw.sms.entity.SMSReceiveContentEntity;
import com.coulee.aicw.sms.service.ISMSReceiverService;
@Service
public class SMSReceiverServiceImpl implements ISMSReceiverService {
	@Autowired
	public ISMSService SMSService;
	@Override
	public void receive(SMSReceiveContentEntity content) {
		if(content!=null){
			if(StringUtils.isEmpty(content.getBusinessId())){
				this.SMSService.approvalReceptionSMS(content.getPhone(), content.getContent(), content.getBusinessId());
			}else{
				String businessId = UUIDHexGenerator.getInstance().generate();
				this.SMSService.applyReceptionSMS(content.getPhone(), content.getContent(), businessId);
			}
		}
	}

}
