package com.coulee.aicw.entity.statistic;

import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import io.swagger.annotations.ApiModel;

/***
 * 首页统计视图所需数据实体类
 * 
 * @author tongjie
 *
 */
@ApiModel(value = "chinaMapEntity")
public class ChinaMapEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChinaMapEntity() {
		
	}
	public ChinaMapEntity(String name) {
		this.name = name;
		this.value = 0;
	}

	/***
	 * 省份
	 */
	private String province;
	/***
	 * 省份名称(中文汉字)
	 */
	private String name;

	/**
	 * 数量
	 */
	private Integer value;
	/**
	 * 查询条件
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	/**
	 * 查询条件
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

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

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof ChinaMapEntity) {
			if (((ChinaMapEntity) o).getName().equals(this.getName())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}