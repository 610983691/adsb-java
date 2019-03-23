package com.coulee.aicw.entity.statistic;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import io.swagger.annotations.ApiModel;

/***
 * 首页统计视图所需数据实体类
 * 需要特定的字段时，可以自行添加。
 * 特殊的统计视图，使用专门的实体类
 * @author tongjie
 *
 */
@ApiModel(value ="statisticEntity")
public class StatisticEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***
	 * 表主键
	 */
	private String id;
	
	/***
	 * 名称
	 */
	private String name;
	
	/**
	 * 数量
	 */
	private Integer value;
	/**
	 * 查询条件
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	/**
	 * 查询条件
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 查询条件
	 */
	private List<String> mobileNumbers;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(List<String> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

}