package com.coulee.aicw.controller.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.log.IControlLogService;

@RestController
@RequestMapping("/controlLog")
public class ControlLogController extends BaseController {

	@Autowired
	private IControlLogService controlLogService;

	@RequestMapping("/list")
	public PageEntity<ControlLogEntity> list(@RequestBody ControlLogEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<ControlLogEntity> pl = this.controlLogService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}

	/***
	 * 按照条件统计各个状态的数量()
	 * 
	 * @return
	 */
	@RequestMapping("/statisticByStatus")
	public Message statisticByStatus(@RequestBody(required=false) ControlLogEntity entity) {
		return this.controlLogService.statisticByStatus(entity);
	}
	

	/***
	 * 按照条件统计各个状态的数量()
	 * 
	 * @return
	 */
	@RequestMapping("/statisticByIP")
	public Message statisticByIP(@RequestBody(required=false) ControlLogEntity entity) {
		return this.controlLogService.statisticByIP(entity);
	}
}
