package com.coulee.aicw.service.alarm.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.dao.AlarmLogEntityMapper;
import com.coulee.aicw.dao.AlarmLogEntityMsgMapper;
import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.foundations.config.exception.CustomRuntimeException;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.alarm.IAlarmLogService;

@Service
@Transactional(rollbackFor=Exception.class)
public class AlarmLogServiceImpl extends AbstractBaseService implements IAlarmLogService {

	private static final Logger LOG = Logger.getLogger(AlarmLogServiceImpl.class);
	@Autowired
	private AlarmLogEntityMapper alarmLogEntityMapper;
	@Autowired
	private AlarmLogEntityMsgMapper alarmLogEntityMsgMapper;
	/***
	 * 更新主表，默认查询都写在主表里边去。
	 */
	@Override
	protected IBaseDao getBaseDao() {
		return this.alarmLogEntityMapper;
	}

	/***
	 * 新增告警信息，会同时新增alarm_Log和alarm_log_msg表
	 */
	@Override
	public Message addAlarmLog(AlarmLogEntity alarmLogEntity, String msg) throws CustomRuntimeException{
		try {
			AlarmLogEntity _alarmLogEntity = this.alarmLogEntityMapper.findByTel(alarmLogEntity.getMobileNumber());
			if (_alarmLogEntity != null) {

				this.addAlarmLogEntityMsg(msg, _alarmLogEntity);

				int reciveCount = _alarmLogEntity.getReciveCount();
				_alarmLogEntity.setReciveCount(reciveCount++);
				_alarmLogEntity.setUpdateDate(new Date());

				return this.update(_alarmLogEntity);
			} else {
				Message ms = this.add(alarmLogEntity);
				if (ms.isSuccess()) {
					AlarmLogEntity alarmLogEntity_ = (AlarmLogEntity) ms.getObj();
					this.addAlarmLogEntityMsg(msg, alarmLogEntity_);
				}
				return ms;
			}
		} catch (Exception e) {
			LOG.error("添加失败", e);
			throw new CustomRuntimeException("添加失败");
		}
	}

	/***
	 * 新增log_msg值
	 * @param msg
	 * @param alarmid
	 */
	private void addAlarmLogEntityMsg(String msg, AlarmLogEntity alarmLogEntity) {
		AlarmLogMsgEntity am = new AlarmLogMsgEntity();
		am.setReciveMsg(msg);
		am.setAlarmLogId(alarmLogEntity.getId());
		am.setMobileNumber(alarmLogEntity.getMobileNumber());
		am.setReceiveDate(new Date());
		this.alarmLogEntityMsgMapper.add(am);
	}

	@Override
	public PageList<AlarmLogMsgEntity> queryAlarmLogMsgDetail(AlarmLogEntity entity, PageArg pageArg) {
	
		return this.alarmLogEntityMsgMapper.queryAlarmLogMsgDetail(entity, pageArg);
	}

	
}
