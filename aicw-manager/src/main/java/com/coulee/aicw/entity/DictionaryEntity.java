package com.coulee.aicw.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="DictionaryEntity")
public class DictionaryEntity extends BaseEntity {
    /**
     * 字典ID
     */
    @ApiModelProperty(value = "字典ID")
    private String id;

    /**
     * 字典类型编码
     */
    @ApiModelProperty(value = "字典类型编码")
    private String dicTypeCode;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dicName;

    /**
     * 字典状态
     */
    @ApiModelProperty(value = "字典状态")
    private String dicStatus;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String createUser;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Date createDate;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String updateUser;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private Date updateDate;

    /**
     * 值类型
     */
    @ApiModelProperty(value = "值类型")
    private String valueType;

    /**
     * 字典父ID
     */
    @ApiModelProperty(value = "字典父ID")
    private String parentDicId;

    /**
     * 字典值
     */
    @ApiModelProperty(value = "字典值")
    private String dicValue;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String dicDes;

    /**
     * fw_dictionary
     */
    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     * @return id 字典ID
     */
    public String getId() {
        return id;
    }

    /**
     * 字典ID
     * @param id 字典ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 字典类型编码
     * @return dic_type_code 字典类型编码
     */
    public String getDicTypeCode() {
        return dicTypeCode;
    }

    /**
     * 字典类型编码
     * @param dicTypeCode 字典类型编码
     */
    public void setDicTypeCode(String dicTypeCode) {
        this.dicTypeCode = dicTypeCode;
    }

    /**
     * 字典名称
     * @return dic_name 字典名称
     */
    public String getDicName() {
        return dicName;
    }

    /**
     * 字典名称
     * @param dicName 字典名称
     */
    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    /**
     * 字典状态
     * @return dic_status 字典状态
     */
    public String getDicStatus() {
        return dicStatus;
    }

    /**
     * 字典状态
     * @param dicStatus 字典状态
     */
    public void setDicStatus(String dicStatus) {
        this.dicStatus = dicStatus;
    }

    /**
     * 
     * @return create_user 
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 
     * @param createUser 
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
     * @return update_user 
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
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

    /**
     * 值类型
     * @return value_type 值类型
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * 值类型
     * @param valueType 值类型
     */
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    /**
     * 字典父ID
     * @return parent_dic_id 字典父ID
     */
    public String getParentDicId() {
        return parentDicId;
    }

    /**
     * 字典父ID
     * @param parentDicId 字典父ID
     */
    public void setParentDicId(String parentDicId) {
        this.parentDicId = parentDicId;
    }

    /**
     * 字典值
     * @return dic_value 字典值
     */
    public String getDicValue() {
        return dicValue;
    }

    /**
     * 字典值
     * @param dicValue 字典值
     */
    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    /**
     * 
     * @return dic_des 
     */
    public String getDicDes() {
        return dicDes;
    }

    /**
     * 
     * @param dicDes 
     */
    public void setDicDes(String dicDes) {
        this.dicDes = dicDes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dicTypeCode=").append(dicTypeCode);
        sb.append(", dicName=").append(dicName);
        sb.append(", dicStatus=").append(dicStatus);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", valueType=").append(valueType);
        sb.append(", parentDicId=").append(parentDicId);
        sb.append(", dicValue=").append(dicValue);
        sb.append(", dicDes=").append(dicDes);
        sb.append("]");
        return sb.toString();
    }
}