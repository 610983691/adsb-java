package com.coulee.aicw.controller.alram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.alarm.IAlarmLogService;

@RestController
@RequestMapping("/alarm")
public class AlarmController extends BaseController {

	@Autowired
	private IAlarmLogService alarmLogService;

	/***
	 * @param 查询参数
	 * @return
	 */
	@RequestMapping("/list")
	public PageEntity<AlarmLogEntity> list(@RequestBody AlarmLogEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<AlarmLogEntity> pl = this.alarmLogService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}

	
	/***
	 * @param 查询
	 * @return
	 */
	@RequestMapping("/listDetail")
	public PageEntity<AlarmLogMsgEntity> listDetail(@RequestBody AlarmLogEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<AlarmLogMsgEntity> pl = this.alarmLogService.queryAlarmLogMsgDetail(entity, pageArg);
		return this.makePageEntity(pl);
	}
}
