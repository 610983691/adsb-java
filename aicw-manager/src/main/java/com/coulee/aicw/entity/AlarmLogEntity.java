package com.coulee.aicw.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="AlarmLogEntity")
public class AlarmLogEntity extends BaseEntity {
    /**
     * 防火墙告警ID
     */
    @ApiModelProperty(value = "防火墙告警ID")
    private String id;

    /**
     * 来源号码
     */
    @ApiModelProperty(value = "来源号码")
    private String mobileNumber;

    /**
     * 手机号归属地市
     */
    @ApiModelProperty(value = "手机号归属地市")
    private String mobileCity;
    
    /**
     * 手机号归属省份
     */
    @ApiModelProperty(value = "手机号归属省份")
    private String mobileProvince;
    
    /**
     * 手机号归属运营商
     */
    @ApiModelProperty(value = "手机号归属运营商")
    private String carrier;

    /**
     * 拦截次数
     */
    @ApiModelProperty(value = "拦截次数")
    private Integer reciveCount;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    
    /**
     * 告警详细信息
     */
    private AlarmLogMsgEntity alarmLogMsg;

    

	/**
	 * 查询条件
	 */
	@ApiModelProperty(value = "查询条件开始时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date beginTime;

	/**
	 * 查询条件
	 */
	@ApiModelProperty(value = "查询条件开始时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
    /**
     * fw_alarm_log
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

    /**
     * 来源号码
     * @return mobile_number 来源号码
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * 来源号码
     * @param mobileNumber 来源号码
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }



    /**
     * 拦截次数
     * @return recive_count 拦截次数
     */
    public Integer getReciveCount() {
        return reciveCount;
    }

    /**
     * 拦截次数
     * @param reciveCount 拦截次数
     */
    public void setReciveCount(Integer reciveCount) {
        this.reciveCount = reciveCount;
    }

    /**
     * 
     * @return create_date 
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 
     * @param createDate 
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 
     * @return update_date 
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 
     * @param updateDate 
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mobileNumber=").append(mobileNumber);
        sb.append(", mobileCity=").append(mobileCity);
        sb.append(", reciveCount=").append(reciveCount);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append("]");
        return sb.toString();
    }

	public AlarmLogMsgEntity getAlarmLogMsg() {
		return alarmLogMsg;
	}

	public void setAlarmLogMsg(AlarmLogMsgEntity alarmLogMsg) {
		this.alarmLogMsg = alarmLogMsg;
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

	public String getMobileCity() {
		return mobileCity;
	}

	public void setMobileCity(String mobileCity) {
		this.mobileCity = mobileCity;
	}

	public String getMobileProvince() {
		return mobileProvince;
	}

	public void setMobileProvince(String mobileProvince) {
		this.mobileProvince = mobileProvince;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
}