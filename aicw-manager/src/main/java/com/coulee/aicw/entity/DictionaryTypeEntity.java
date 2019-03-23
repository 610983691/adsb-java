package com.coulee.aicw.entity;

import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="DictionaryTypeEntity")
public class DictionaryTypeEntity extends BaseEntity {
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String id;

    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
    private String typeCode;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String typeName;

    /**
     * 类型描述
     */
    @ApiModelProperty(value = "类型描述")
    private String typeDes;

    /**
     * 类型状态
     */
    @ApiModelProperty(value = "类型状态")
    private String typeStatus;

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
     * fw_dictionary_type
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
     * 类型编码
     * @return type_code 类型编码
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 类型编码
     * @param typeCode 类型编码
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 类型名称
     * @return type_name 类型名称
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 类型名称
     * @param typeName 类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 类型描述
     * @return type_des 类型描述
     */
    public String getTypeDes() {
        return typeDes;
    }

    /**
     * 类型描述
     * @param typeDes 类型描述
     */
    public void setTypeDes(String typeDes) {
        this.typeDes = typeDes;
    }

    /**
     * 类型状态
     * @return type_status 类型状态
     */
    public String getTypeStatus() {
        return typeStatus;
    }

    /**
     * 类型状态
     * @param typeStatus 类型状态
     */
    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", typeCode=").append(typeCode);
        sb.append(", typeName=").append(typeName);
        sb.append(", typeDes=").append(typeDes);
        sb.append(", typeStatus=").append(typeStatus);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append("]");
        return sb.toString();
    }
}