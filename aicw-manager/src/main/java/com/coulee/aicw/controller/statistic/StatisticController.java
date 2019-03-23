package com.coulee.aicw.controller.statistic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.statistic.ChinaMapEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.entity.statistic.UserLogTop10Entity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IApplicationManageService;
import com.coulee.aicw.service.IFwInfoService;
import com.coulee.aicw.service.statistic.IStatisticService;
import com.coulee.aicw.service.user.IUserService;

@RestController
@RequestMapping("/statistic")
public class StatisticController extends BaseController {

	@Autowired
	private IStatisticService statisticService;

	@Autowired
	private IFwInfoService fwInfoService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IApplicationManageService appService;

	/***
	 * 查询地图数据
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadMapData")
	public Message loadChinaMapData(String s) {
		List<ChinaMapEntity> res = statisticService.queryMapData(s);
		return Message.newSuccessMessage(JSONObject.toJSONString(res));
	}

	/***
	 * 获取用户日志top10
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadUserLogTop10")
	public Message loadUserLogTop10(@RequestBody(required = false) UserLogTop10Entity entity) {
		List<UserLogTop10Entity> res = statisticService.queryUserStatusTop10(entity);
		return Message.newSuccessMessage(JSONObject.toJSONString(res));
	}

	/***
	 * 获取top10的告警号码(本月的前10号码，然后根据号码统计这10个号码上月，半年，历史的告警数)
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadTop10Phone")
	public Message loadTop10Phone() {
		Map<String, List<StatisticEntity>> res = statisticService.loadTop10Phone();
		return Message.newSuccessMessage(JSONObject.toJSONString(res));
	}

	/***
	 * 获取防火墙状态
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadfwTestStatus")
	public Message loadfwTestStatus() {
		List<StatisticEntity> res = fwInfoService.queryTestStatusCount();
		return Message.newSuccessMessage(JSONObject.toJSONString(res));
	}

	/***
	 * 查询雷达图的数据
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadRadarDatas")
	public Message loadRadarDatas(@RequestBody(required = false) AlarmLogMsgEntity s) {
		List<StatisticEntity> res = statisticService.loadRadarDatas(s);
		return Message.newSuccessMessage(JSONObject.toJSONString(res));
	}

	/***
	 * 查询最近的10条告警
	 * 
	 * @return
	 */
	@RequestMapping("/LoadLatestAlarm10")
	public PageEntity<AlarmLogMsgEntity> LoadLatestAlarm10() {
		List<AlarmLogMsgEntity> res = statisticService.queryLatestAlarm10();
		PageList<AlarmLogMsgEntity> page = new PageList<AlarmLogMsgEntity>();
		page.addAll(res);
		return this.makePageEntity(page);
	}

	/***
	 * 查询最近的10条失败日志
	 * 
	 * @return
	 */
	@RequestMapping("/loadLatestLog10")
	public PageEntity<ControlLogEntity> loadLatest10ControlLogs(@RequestBody(required = false) ControlLogEntity entity) {
		List<ControlLogEntity> res = statisticService.queryLatest10ControlLogs(entity);
		PageList<ControlLogEntity> page = new PageList<ControlLogEntity>();
		page.addAll(res);
		return  this.makePageEntity(page);
	}

	/***
	 * 查询应用数量
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadAppCount")
	public Message loadAppCount() {
		Integer res = appService.countByEntity(null);
		return Message.newSuccessMessage(res.toString());
	}

	/***
	 * 查询用户数量
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadUserCount")
	public Message loadUserCount() {
		Integer res = userService.countByEntity(null);
		return Message.newSuccessMessage(res.toString());
	}

	/***
	 * 查询防火墙数量
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadFwCount")
	public Message loadFwCount() {
		int res = fwInfoService.countByEntity(null);
		return Message.newSuccessMessage(String.valueOf(res));
	}
	
	/***
	 * 查询统计视图页面的告警详情。
	 * 
	 * @param s
	 * @return
	 */
	@RequestMapping("/loadAlarmDetails")
	public Message loadAlarmDetails(@RequestBody(required = false) AlarmLogMsgEntity s) {
		int res = fwInfoService.countByEntity(null);
		return Message.newSuccessMessage(String.valueOf(res));
	}
}
