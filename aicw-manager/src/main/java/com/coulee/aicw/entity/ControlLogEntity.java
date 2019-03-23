package com.coulee.aicw.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ControlLogEntity")
public class ControlLogEntity extends BaseEntity {
	/**
	 * 防火墙日志ID
	 */
	@ApiModelProperty(value = "防火墙日志ID")
	private String id;

	/**
	 * 防火墙IP
	 */
	@ApiModelProperty(value = "防火墙IP")
	private String fwIp;

	/**
	 * 防火墙名称
	 */
	@ApiModelProperty(value = "防火墙名称")
	private String fwName;

	/**
	 * 下发策略
	 */
	@ApiModelProperty(value = "下发策略")
	private String sendOrder;

	/**
	 * 短信原始信息
	 */
	@ApiModelProperty(value = "短信原始信息")
	private String mobileMsg;

	/**
	 * 申请人
	 */
	@ApiModelProperty(value = "申请人")
	private String applyUserId;

	/**
	 * 申请人电话
	 */
	@ApiModelProperty(value = "申请人电话")
	private String applyMobileNumber;

	/**
	 * 审批人
	 */
	@ApiModelProperty(value = "审批人")
	private String approveUserId;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private String status;

	/**
	 * 生效时间
	 */
	@ApiModelProperty(value = "生效时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date validTime;

	/**
	 * 失效时间
	 */
	@ApiModelProperty(value = "失效时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date invalidTime;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 短信码
	 */
	@ApiModelProperty(value = "短信码")
	private String smsnumber;
	/**
	 * 审批回复信息
	 */
	@ApiModelProperty(value = "审批回复信息")
	private String approveMsg;

	/**
	 * 审批回复信息
	 */
	@ApiModelProperty(value = "审批回复信息")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date beginTime;

	/**
	 * 审批回复信息
	 */
	@ApiModelProperty(value = "审批回复信息")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * fw_control_log
	 */
	private static final long serialVersionUID = 1L;

	public String getSmsnumber() {
		return smsnumber;
	}

	public void setSmsnumber(String smsnumber) {
		this.smsnumber = smsnumber;
	}

	public String getApproveMsg() {
		return approveMsg;
	}

	public void setApproveMsg(String approveMsg) {
		this.approveMsg = approveMsg;
	}

	/**
	 * 防火墙日志ID
	 * 
	 * @return id 防火墙日志ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 防火墙日志ID
	 * 
	 * @param id 防火墙日志ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 防火墙IP
	 * 
	 * @return fw_ip 防火墙IP
	 */
	public String getFwIp() {
		return fwIp;
	}

	/**
	 * 防火墙IP
	 * 
	 * @param fwIp 防火墙IP
	 */
	public void setFwIp(String fwIp) {
		this.fwIp = fwIp;
	}

	/**
	 * 防火墙名称
	 * 
	 * @return fw_name 防火墙名称
	 */
	public String getFwName() {
		return fwName;
	}

	/**
	 * 防火墙名称
	 * 
	 * @param fwName 防火墙名称
	 */
	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	/**
	 * 下发策略
	 * 
	 * @return send_order 下发策略
	 */
	public String getSendOrder() {
		return sendOrder;
	}

	/**
	 * 下发策略
	 * 
	 * @param sendOrder 下发策略
	 */
	public void setSendOrder(String sendOrder) {
		this.sendOrder = sendOrder;
	}

	/**
	 * 短信原始信息
	 * 
	 * @return mobile_msg 短信原始信息
	 */
	public String getMobileMsg() {
		return mobileMsg;
	}

	/**
	 * 短信原始信息
	 * 
	 * @param mobileMsg 短信原始信息
	 */
	public void setMobileMsg(String mobileMsg) {
		this.mobileMsg = mobileMsg;
	}

	/**
	 * 申请人
	 * 
	 * @return apply_user_id 申请人
	 */
	public String getApplyUserId() {
		return applyUserId;
	}

	/**
	 * 申请人
	 * 
	 * @param applyUserId 申请人
	 */
	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	/**
	 * 申请人电话
	 * 
	 * @return apply_mobile_number 申请人电话
	 */
	public String getApplyMobileNumber() {
		return applyMobileNumber;
	}

	/**
	 * 申请人电话
	 * 
	 * @param applyMobileNumber 申请人电话
	 */
	public void setApplyMobileNumber(String applyMobileNumber) {
		this.applyMobileNumber = applyMobileNumber;
	}

	/**
	 * 审批人
	 * 
	 * @return approve_user_id 审批人
	 */
	public String getApproveUserId() {
		return approveUserId;
	}

	/**
	 * 审批人
	 * 
	 * @param approveUserId 审批人
	 */
	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}

	/**
	 * 状态
	 * 
	 * @return status 状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 状态
	 * 
	 * @param status 状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 生效时间
	 * 
	 * @return valid_time 生效时间
	 */
	public Date getValidTime() {
		return validTime;
	}

	/**
	 * 生效时间
	 * 
	 * @param validTime 生效时间
	 */
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	/**
	 * 失效时间
	 * 
	 * @return invalid_time 失效时间
	 */
	public Date getInvalidTime() {
		return invalidTime;
	}

	/**
	 * 失效时间
	 * 
	 * @param invalidTime 失效时间
	 */
	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	/**
	 * 备注
	 * 
	 * @return remark 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 备注
	 * 
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", fwIp=").append(fwIp);
		sb.append(", fwName=").append(fwName);
		sb.append(", sendOrder=").append(sendOrder);
		sb.append(", mobileMsg=").append(mobileMsg);
		sb.append(", applyUserId=").append(applyUserId);
		sb.append(", applyMobileNumber=").append(applyMobileNumber);
		sb.append(", approveUserId=").append(approveUserId);
		sb.append(", status=").append(status);
		sb.append(", validTime=").append(validTime);
		sb.append(", invalidTime=").append(invalidTime);
		sb.append(", remark=").append(remark);
		sb.append("]");
		return sb.toString();
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
}