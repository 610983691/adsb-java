package com.coulee.aicw.service.statistic;

import java.util.List;
import java.util.Map;

import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.statistic.ChinaMapEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.entity.statistic.UserLogTop10Entity;
public interface IStatisticService {
	
	List<ChinaMapEntity> queryMapData(String param);
	
	
	List<UserLogTop10Entity> queryUserStatusTop10(UserLogTop10Entity entity);
	
	/***
	 * 获取top10的告警号码(本月的前10号码，然后根据号码统计这10个号码上月，半年，历史的告警数)
	 * @param s
	 * @return
	 */
	Map<String,List<StatisticEntity>>  loadTop10Phone();
	
	
	List<StatisticEntity> loadRadarDatas(AlarmLogMsgEntity param);
	
	
	/***
	 * 查询最近的10条告警信息
	 * @return
	 */
	List<AlarmLogMsgEntity> queryLatestAlarm10();
	
	/***
	 * 查询最近的10条告警日志
	 * @param entity 查询参数
	 * @return
	 */
	List<ControlLogEntity> queryLatest10ControlLogs(ControlLogEntity entity);
	
}
