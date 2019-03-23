package com.coulee.aicw.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

public interface AlarmLogEntityMsgMapper extends IBaseDao {
	
	/**
	 * 根据告警id，查询告警内容
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午2:58:03
	* @param 
	* @return
	 */
	public <T extends BaseEntity> List<T> findByAlarmLogId(String alarmid);
	
	PageList<AlarmLogMsgEntity> queryAlarmLogMsgDetail(@Param("params")AlarmLogEntity param ,@Param("pageArg")PageArg pageArg); 
	
	public Integer queryAlarmLogMsgCount(AlarmLogMsgEntity entity);
	
	
	List<AlarmLogMsgEntity> queryLatestAlarm10(); 
}