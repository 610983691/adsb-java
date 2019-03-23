package com.coulee.aicw.service.statistic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coulee.aicw.dao.AlarmLogEntityMapper;
import com.coulee.aicw.dao.AlarmLogEntityMsgMapper;
import com.coulee.aicw.dao.ControlLogEntityMapper;
import com.coulee.aicw.dao.StatisticMapper;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.statistic.ChinaMapEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.entity.statistic.UserLogTop10Entity;
import com.coulee.aicw.foundations.utils.common.DateTools;
import com.coulee.aicw.service.statistic.IStatisticService;

@Service
public class StatisticServiceImpl implements IStatisticService {

	@Autowired
	private StatisticMapper statisticMapper;

	@Autowired
	private AlarmLogEntityMapper alarmLogMapper;

	@Autowired
	private AlarmLogEntityMsgMapper alarmLogMsgMapper;

	@Autowired
	private ControlLogEntityMapper controlLogMapper;

	@Override
	public List<ChinaMapEntity> queryMapData(String param) {
		List<ChinaMapEntity> list = statisticMapper.queryChinaMapData(null);
		HashMap<String, ChinaMapEntity> chinaMap = getDefaultChinaMapData();
		for (ChinaMapEntity chinaMapEntity : list) {
			chinaMap.put(chinaMapEntity.getName(), chinaMapEntity);
		}
		List<ChinaMapEntity> res = new ArrayList<>();
		res.addAll(chinaMap.values());
		return res;
	}

	
	private static  HashMap<String, ChinaMapEntity> getDefaultChinaMapData() {
		HashMap<String, ChinaMapEntity> map = new HashMap<>();
		map.put("北京", new ChinaMapEntity("北京"));
		map.put("天津", new ChinaMapEntity("天津"));
		map.put("上海", new ChinaMapEntity("上海"));
		map.put("重庆", new ChinaMapEntity("重庆"));
		map.put("河北", new ChinaMapEntity("河北"));
		map.put("河南", new ChinaMapEntity("河南"));
		map.put("云南", new ChinaMapEntity("云南"));
		map.put("辽宁", new ChinaMapEntity("辽宁"));
		map.put("黑龙江", new ChinaMapEntity("黑龙江"));
		map.put("湖南", new ChinaMapEntity("湖南"));
		map.put("安徽", new ChinaMapEntity("安徽"));
		map.put("山东", new ChinaMapEntity("山东"));
		map.put("新疆", new ChinaMapEntity("新疆"));
		map.put("江苏", new ChinaMapEntity("江苏"));
		map.put("浙江", new ChinaMapEntity("浙江"));
		map.put("江西", new ChinaMapEntity("江西"));
		map.put("湖北", new ChinaMapEntity("湖北"));
		map.put("广西", new ChinaMapEntity("广西"));
		map.put("甘肃", new ChinaMapEntity("甘肃"));
		map.put("山西", new ChinaMapEntity("山西"));
		map.put("内蒙古", new ChinaMapEntity("内蒙古"));
		map.put("陕西", new ChinaMapEntity("陕西"));
		map.put("吉林", new ChinaMapEntity("吉林"));
		map.put("福建", new ChinaMapEntity("福建"));
		map.put("贵州", new ChinaMapEntity("贵州"));
		map.put("青海", new ChinaMapEntity("青海"));
		map.put("广东", new ChinaMapEntity("广东"));
		map.put("西藏", new ChinaMapEntity("西藏"));
		map.put("四川", new ChinaMapEntity("四川"));
		map.put("宁夏", new ChinaMapEntity("宁夏"));
		map.put("海南", new ChinaMapEntity("海南"));
		map.put("台湾", new ChinaMapEntity("台湾"));
		map.put("香港", new ChinaMapEntity("香港"));
		map.put("澳门", new ChinaMapEntity("澳门"));
		return map;
	}

	@Override
	public List<UserLogTop10Entity> queryUserStatusTop10(UserLogTop10Entity entity) {
		List<UserLogTop10Entity> list = statisticMapper.queryUserStatusTop10(entity);
		return list;
	}

	@Override
	public Map<String, List<StatisticEntity>> loadTop10Phone() {
		Map<String, List<StatisticEntity>> result = new HashMap<>();
		StatisticEntity param = new StatisticEntity();
		Date now = new Date();
		/* 本月 */
		param.setBeginTime(DateTools.getSomeMonthStartTime(now));
		param.setEndTime(now);

		List<StatisticEntity> top10Numbers = alarmLogMapper.queryTop10Phone(param);
		result.put("month", top10Numbers);
		if(top10Numbers.isEmpty()) {
			result.put("month1", top10Numbers);
			result.put("month6", top10Numbers);
			result.put("history", top10Numbers);
			return result;
		}
		List<String> list = new ArrayList<>();
		for (StatisticEntity statisticEntity : top10Numbers) {
			list.add(statisticEntity.getName());
		}
		param.setMobileNumbers(list);
		/* 上月 */
		param.setBeginTime(DateTools.getPreviousMonthStart());
		param.setEndTime(DateTools.getPreviousMonthEnd());
		result.put("month1", alarmLogMapper.queryCountByPhoneNumber(param));
		/* 半年 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, -6);
		param.setBeginTime(calendar.getTime());
		param.setEndTime(now);
		result.put("month6", alarmLogMapper.queryCountByPhoneNumber(param));

		/* 历史所有 */
		calendar.setTimeInMillis(0);
		param.setBeginTime(calendar.getTime());
		param.setEndTime(now);
		result.put("history", alarmLogMapper.queryCountByPhoneNumber(param));

		return result;
	}

	@Override
	public List<StatisticEntity> loadRadarDatas(AlarmLogMsgEntity param) {
		Integer alarmCounts = alarmLogMsgMapper.queryAlarmLogMsgCount(param);
		StatisticEntity alarm = new StatisticEntity();
		alarm.setName("告警数量");
		alarm.setValue(alarmCounts);
		ControlLogEntity queryParam = new ControlLogEntity();
		queryParam.setBeginTime(param == null ? null : param.getBeginTime());
		queryParam.setEndTime(param == null ? null : param.getEndTime());
		List<StatisticEntity> result = controlLogMapper.statisticByStatus(queryParam);
		result.add(alarm);
		return result;
	}

	@Override
	public List<AlarmLogMsgEntity> queryLatestAlarm10() {
		return alarmLogMsgMapper.queryLatestAlarm10();
	}

	@Override
	public List<ControlLogEntity> queryLatest10ControlLogs(ControlLogEntity entity) {
		return controlLogMapper.queryLatest10ControlLogs(entity);
	}

}
