package com.coulee.aicw.service.log;

import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;

public interface IControlLogService extends IBaseService {
	/**
	 * 根据短信码查询日志信息
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午12:35:31
	* @param 
	* @return
	 */
    public ControlLogEntity findBySmsNum(String smsnum);
    
    /**
     * 根据条件对各状态进行分类统计。
     * @param entity
     * @return
     */
    public Message statisticByStatus(ControlLogEntity entity);
    
    /**
     * 根据条件对各防火墙进行统计。
     * @param entity
     * @return
     */
    public Message statisticByIP(ControlLogEntity entity);
}
