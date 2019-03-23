package com.coulee.aicw.foundations.entity;
/**
 * 手机号码归属地实体类
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月30日上午10:38:30
 * author：HongZhang
 * @version 1.0
 */
public class PhoneModel {
	/** 省份名称 */
    private String provinceName;

    /** 城市名称 */
    private String cityName;

    /** 运营商：移动/电信/联通 */
    private String carrier;

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

}
