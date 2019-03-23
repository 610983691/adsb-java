package com.coulee.aicw.scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.scheduler.entity.TaskInfoEntity;
import com.coulee.aicw.scheduler.quartz.SchedulerManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 调度管理RESTful服务<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
@Api(tags = "调度管理RESTful服务")
@RestController
@RequestMapping(value = "/scheduler")
public class SchedulerController extends BaseController {

	@Autowired
	private SchedulerManager schedulerManager;

	/**
	 * Description: 查询指定任务类型的调度列表<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "查询指定任务类型的调度列表数据", notes = "查询指定任务类型的调度列表数据")
	@ApiImplicitParams({ @ApiImplicitParam(name = "jobType", value = "任务类型", required = true, paramType = "path") })
	@RequestMapping(value = "/list/{jobType}", method = RequestMethod.POST)
	public List<TaskInfoEntity> list(@PathVariable("jobType") String jobType) {
		return schedulerManager.listTask(jobType);
	}

	/**
	 * Description: 增加调度<br> 
	 * Created date: 2018年9月27日
	 * @param taskInfo
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "增加调度任务", notes = "增加调度任务，返回操作结果")
	@ApiImplicitParams({ @ApiImplicitParam(name = "taskInfo", value = "调度数据", required = true, dataType = "TaskInfoEntity", paramType = "body") })
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(@RequestBody TaskInfoEntity taskInfo) {
		return this.schedulerManager.addTask(taskInfo);
	}

	/**
	 * Description: 删除调度<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @param jobId
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "删除调度任务", notes = "根据任务ID删除调度任务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "jobType", value = "任务类型", required = true, paramType = "path"),
			@ApiImplicitParam(name = "jobId", value = "任务ID", required = true, paramType = "path") })
	@RequestMapping(value = "/delete/{jobType}/{jobId}", method = RequestMethod.POST)
	public Message delete(@PathVariable("jobType") String jobType, @PathVariable("jobId") String jobId) {
		return this.schedulerManager.deleteTask(jobType, jobId);
	}

	/**
	 * Description: 修改调度<br> 
	 * Created date: 2018年9月27日
	 * @param taskInfo
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "修改调度任务", notes = "修改调度任务，返回操作结果")
	@ApiImplicitParams({ @ApiImplicitParam(name = "taskInfo", value = "调度数据", required = true, dataType = "TaskInfoEntity", paramType = "body") })
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody TaskInfoEntity taskInfo) {
		return this.schedulerManager.updateTask(taskInfo);
	}

	/**
	 * Description: 暂停调度<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @param jobId
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "暂停调度任务", notes = "根据任务ID暂停调度任务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "jobType", value = "任务类型", required = true, paramType = "path"),
		@ApiImplicitParam(name = "jobId", value = "任务ID", required = true, paramType = "path") })
	@RequestMapping(value = "/pause/{jobId}", method = RequestMethod.POST)
	public Message pause(@PathVariable("jobType") String jobType, @PathVariable("jobId") String jobId) {
		return this.schedulerManager.pauseTask(jobType, jobId);
	}

	/**
	 * Description: 恢复调度<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @param jobId
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "恢复调度任务", notes = "根据任务ID恢复调度任务")
	@ApiImplicitParams({ @ApiImplicitParam(name = "jobType", value = "任务类型", required = true, paramType = "path"),
		@ApiImplicitParam(name = "jobId", value = "任务ID", required = true, paramType = "path") })
	@RequestMapping(value = "/resume/{jobId}", method = RequestMethod.POST)
	public Message resume(@PathVariable("jobType") String jobType, @PathVariable("jobId") String jobId) {
		return this.schedulerManager.resumeTask(jobType, jobId);
	}

	/**
	 * Description: 调度状态<br> 
	 * Created date: 2018年9月28日
	 * @param jobType
	 * @param jobId
	 * @return
	 * @author oblivion
	 */
	@ApiOperation(value = "调度任务状态查询", notes = "根据任务ID查询调度任务状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "jobType", value = "任务类型", required = true, paramType = "path"),
		@ApiImplicitParam(name = "jobId", value = "任务ID", required = true, paramType = "path") })
	@RequestMapping(value = "/status/{jobId}", method = RequestMethod.POST)
	public String status(@PathVariable("jobType") String jobType, @PathVariable("jobId") String jobId) {
		return this.schedulerManager.getTaskStatus(jobType, jobId);
	}
}
