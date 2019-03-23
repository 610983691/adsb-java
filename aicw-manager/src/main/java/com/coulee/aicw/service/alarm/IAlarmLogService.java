package com.coulee.aicw.service.alarm;

import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.AlarmLogMsgEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * 异常申请拦截预警日志 Description: Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月29日上午11:13:25 author：HongZhang
 * 
 * @version 1.0
 */
public interface IAlarmLogService extends IBaseService {
	/**
	 * 新增告警日志 Definition: author: HongZhang Created date: 2018年10月29日上午11:32:08
	 * 
	 * @param
	 * @return 成功/失败返回标准的Message信息。发生异常会回滚，并往上层抛出Runtimeexception
	 */
	public Message addAlarmLog(AlarmLogEntity alarmLogEntity, String msg);

	/***
	 * 告警信息详情查询
	 * @param entity
	 * @param pageArg
	 */
	PageList<AlarmLogMsgEntity> queryAlarmLogMsgDetail(AlarmLogEntity entity, PageArg pageArg);
}
