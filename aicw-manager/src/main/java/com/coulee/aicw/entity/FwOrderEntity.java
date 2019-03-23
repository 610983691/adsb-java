package com.coulee.aicw.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="FwOrderEntity")
public class FwOrderEntity extends BaseEntity {
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String id;

    /**
     * 防火墙类型
     */
    @ApiModelProperty(value = "防火墙类型")
    private String fwType;
    private String fwTypeDes;
    /**
     * 防火墙命令
     */
    @ApiModelProperty(value = "防火墙命令")
    private String fwOrder;

    /**
     * 命令执行顺序
     */
    @ApiModelProperty(value = "命令执行顺序")
    private Integer fwOrderNum;

    /**
     * 指令类型
     */
    @ApiModelProperty(value = "指令类型")
    private String orderType;
    private String orderTypeDes;
    /**
     * 期待回显信息
     */
    @ApiModelProperty(value = "期待回显信息")
    private String sucPrompt;

    /**
     * 错误回显信息
     */
    @ApiModelProperty(value = "错误回显信息")
    private String errorPrompt;

    /**
     * fw_order
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return id 
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 防火墙类型
     * @return fw_type 防火墙类型
     */
    public String getFwType() {
        return fwType;
    }

    /**
     * 防火墙类型
     * @param fwType 防火墙类型
     */
    public void setFwType(String fwType) {
        this.fwType = fwType;
    }

    /**
     * 防火墙命令
     * @return fw_order 防火墙命令
     */
    public String getFwOrder() {
        return fwOrder;
    }

    /**
     * 防火墙命令
     * @param fwOrder 防火墙命令
     */
    public void setFwOrder(String fwOrder) {
        this.fwOrder = fwOrder;
    }

    /**
     * 命令执行顺序
     * @return fw_order_num 命令执行顺序
     */
    public Integer getFwOrderNum() {
        return fwOrderNum;
    }

    /**
     * 命令执行顺序
     * @param fwOrderNum 命令执行顺序
     */
    public void setFwOrderNum(Integer fwOrderNum) {
        this.fwOrderNum = fwOrderNum;
    }

    /**
     * 指令类型
     * @return order_type 指令类型
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 指令类型
     * @param orderType 指令类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 期待回显信息
     * @return suc_prompt 期待回显信息
     */
    public String getSucPrompt() {
        return sucPrompt;
    }

    /**
     * 期待回显信息
     * @param sucPrompt 期待回显信息
     */
    public void setSucPrompt(String sucPrompt) {
        this.sucPrompt = sucPrompt;
    }

    /**
     * 错误回显信息
     * @return error_prompt 错误回显信息
     */
    public String getErrorPrompt() {
        return errorPrompt;
    }

    /**
     * 错误回显信息
     * @param errorPrompt 错误回显信息
     */
    public void setErrorPrompt(String errorPrompt) {
        this.errorPrompt = errorPrompt;
    }

    public String getFwTypeDes() {
		return fwTypeDes;
	}

	public void setFwTypeDes(String fwTypeDes) {
		this.fwTypeDes = fwTypeDes;
	}

	public String getOrderTypeDes() {
		return orderTypeDes;
	}

	public void setOrderTypeDes(String orderTypeDes) {
		this.orderTypeDes = orderTypeDes;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fwType=").append(fwType);
        sb.append(", fwOrder=").append(fwOrder);
        sb.append(", fwOrderNum=").append(fwOrderNum);
        sb.append(", orderType=").append(orderType);
        sb.append(", sucPrompt=").append(sucPrompt);
        sb.append(", errorPrompt=").append(errorPrompt);
        sb.append("]");
        return sb.toString();
    }
}