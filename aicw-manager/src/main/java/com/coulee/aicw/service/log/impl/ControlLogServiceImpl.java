package com.coulee.aicw.service.log.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.dao.ControlLogEntityMapper;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.service.log.IControlLogService;

@Service
public class ControlLogServiceImpl extends AbstractBaseService implements IControlLogService {
	@Autowired
	private ControlLogEntityMapper controlLogEntityMapper;

	protected IBaseDao getBaseDao() {
		return controlLogEntityMapper;
	}

	@Override
	public ControlLogEntity findBySmsNum(String smsnum) {
		try {
			ControlLogEntity ce = this.controlLogEntityMapper.findBySmsNum(smsnum);
			return ce;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Message statisticByStatus(ControlLogEntity entity) {
		List<StatisticEntity> result = this.controlLogEntityMapper.statisticByStatus(entity);
		String response = JSONObject.toJSONString(result);
		return result == null ? Message.newFailureMessage(response) : Message.newSuccessMessage(response);
	}
	
	@Override
	public Message statisticByIP(ControlLogEntity entity) {
		List<StatisticEntity> result = this.controlLogEntityMapper.statisticByIP(entity);
		String response = JSONObject.toJSONString(result);
		return result == null ? Message.newFailureMessage(response) : Message.newSuccessMessage(response);
	}

}
