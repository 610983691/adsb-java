package com.coulee.foundations.configserver.entity;

import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.entity.BaseEntity;

/**
 * Description: 配置项实体类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class Location extends BaseEntity {
	

    private String lat;

    private String lon;


    private static final long serialVersionUID = 1L;


	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
	
	public boolean equals(Object o){
		if(o ==null){
			return false;
		}
		if(o instanceof Location){
			Location that = (Location) o;
			if(o==this){
				return true;
			}else if(this.hashCode() == that.hashCode()){
				return true;
			}else if(this.lat.equals(that.getLat())&&this.lon.equals(that.getLon())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return this.lat.hashCode()+this.lon.hashCode()*31;
	}
	
	public String toString(){
		return JSONObject.toJSONString(this);
	}

}