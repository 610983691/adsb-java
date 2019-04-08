package com.coulee.foundations.configserver.entity;

import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.entity.BaseEntity;

public class PointInfo extends BaseEntity {
	

    private String lat;

    private String lng;
    
    private String icao;
    
    private String id;

    public PointInfo(){
    	
    }
    
    /***
     * 
     * @param lat
     * @param lon
     * @param icao
     * @param id
     */
    public PointInfo(String lon,String lat,String icao,String id){
    	this.lat=lat;
    	this.lng=lon;
    	this.id=id;
    	this.icao=icao;
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
		if(o instanceof PointInfo){
			PointInfo that = (PointInfo) o;
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

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}