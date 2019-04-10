package com.coulee.foundations.configserver.entity;

import com.alibaba.fastjson.JSONObject;
import com.coulee.aicw.foundations.entity.BaseEntity;

public class PointInfo extends BaseEntity {

	private String lat;

	private String lng;

	private String icao;

	private String id;

	private String nbspeed;// 南北速度

	private String dxspeed;// 东西速度

	private String hspeed;// 垂直速度
	private String showlon;// 垂直速度
	private String showlat;// 垂直速度

	public PointInfo() {

	}

	/***
	 * 
	 * @param lat
	 * @param lon
	 * @param icao
	 * @param id
	 */
	public PointInfo(String lon, String lat, String icao, String id, String nbspeed, String dxspeed, String hspeed,
			String showlon, String showlat) {
		this.lat = lat;
		this.lng = lon;
		this.icao = icao;
		this.id = id;
		this.nbspeed = nbspeed;
		this.dxspeed = dxspeed;
		this.hspeed = hspeed;
		this.showlon = showlon;
		this.showlat = showlat;
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

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof PointInfo) {
			PointInfo that = (PointInfo) o;
			if (o == this) {
				return true;
			} else if (this.hashCode() == that.hashCode()) {
				return true;
			} else if (this.lat.equals(that.getLat()) && this.lng.equals(that.getLng())) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return this.lat.hashCode() + this.lng.hashCode() * 31;
	}

	public String toString() {
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

	public String getNbspeed() {
		return nbspeed;
	}

	public void setNbspeed(String nbspeed) {
		this.nbspeed = nbspeed;
	}

	public String getDxspeed() {
		return dxspeed;
	}

	public void setDxspeed(String dxspeed) {
		this.dxspeed = dxspeed;
	}

	public String getHspeed() {
		return hspeed;
	}

	public void setHspeed(String hspeed) {
		this.hspeed = hspeed;
	}

	public String getShowlon() {
		return showlon;
	}

	public void setShowlon(String showlon) {
		this.showlon = showlon;
	}

	public String getShowlat() {
		return showlat;
	}

	public void setShowlat(String showlat) {
		this.showlat = showlat;
	}

}