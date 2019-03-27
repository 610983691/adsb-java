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
public class WxLocation extends BaseEntity {
	

    private String lat;

    private String lng;
    
    private Double range;

    public WxLocation(){
    	
    }
    
    /**
     * 維度、經度
     * @param lat
     * @param lon
     */
    public WxLocation(String lat,String lon){
    	this.lat=lat;
    	this.lng=lon;
    }
    
    /**
     * 維度、經度
     * @param lat
     * @param lon
     */
    public WxLocation(String lat,String lon,Double range){
    	this.lat=lat;
    	this.lng=lon;
    	this.range =range;
    }
    

	public Double getRange() {
		return range;
	}

	public void setRange(Double range) {
		this.range = range;
	}


	private static final long serialVersionUID = 1L;


	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lon) {
		this.lng = lon;
	}
	
	public boolean equals(Object o){
		if(o ==null){
			return false;
		}
		if(o instanceof WxLocation){
			WxLocation that = (WxLocation) o;
			if(o==this){
				return true;
			}else if(this.hashCode() == that.hashCode()){
				return true;
			}else if(this.lat.equals(that.getLat())&&this.lng.equals(that.getLng())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		return this.lat.hashCode()+this.lng.hashCode()*31;
	}
	
	public String toString(){
		return JSONObject.toJSONString(this);
	}

}