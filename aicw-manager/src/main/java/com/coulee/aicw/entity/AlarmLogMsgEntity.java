package com.coulee.aicw.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="AlarmLogEntityMsg")
public class AlarmLogMsgEntity extends BaseEntity {
    /**
     * 防火墙告警ID
     */
    @ApiModelProperty(value = "防火墙预警信息id")
    private String id;

    /**
     * 接收信息
     */
    @ApiModelProperty(value = "接收信息")
    private String reciveMsg;
    /**
     * 防火墙告警ID
     */
    @ApiModelProperty(value = "防火墙告警ID")
    private String alarmLogId;
    
    
    @ApiModelProperty(value = "接收信息")
    private String mobileNumber;
    
    @ApiModelProperty(value = "省份标识")
    private String mobileProvince;
    
    @ApiModelProperty(value = "地市标识")
    private String mobileCity;
    
    /**
     * 接收告警时间
     */
    @ApiModelProperty(value = "接收告警时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date receiveDate;
    
    
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
     * fw_alarm_log_msg
     */
    private static final long serialVersionUID = 1L;

    /**
     * 防火墙告警ID
     * @return id 防火墙告警ID
     */
    public String getId() {
        return id;
    }

    /**
     * 防火墙告警ID
     * @param id 防火墙告警ID
     */
    public void setId(String id) {
        this.id = id;
    }

   

    public String getReciveMsg() {
		return reciveMsg;
	}

	public void setReciveMsg(String reciveMsg) {
		this.reciveMsg = reciveMsg;
	}

	public String getAlarmLogId() {
		return alarmLogId;
	}

	public void setAlarmLogId(String alarmLogId) {
		this.alarmLogId = alarmLogId;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", reciveMsg=").append(reciveMsg);
        sb.append(", alarmLogId=").append(alarmLogId);
        sb.append("]");
        return sb.toString();
    }

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public String getMobileProvince() {
		return mobileProvince;
	}

	public void setMobileProvince(String mobileProvince) {
		this.mobileProvince = mobileProvince;
	}

	public String getMobileCity() {
		return mobileCity;
	}

	public void setMobileCity(String mobileCity) {
		this.mobileCity = mobileCity;
	}
}