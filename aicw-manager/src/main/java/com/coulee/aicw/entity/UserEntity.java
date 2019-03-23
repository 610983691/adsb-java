package com.coulee.aicw.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.coulee.aicw.foundations.entity.BaseEntity;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value ="UserEntity")
public class UserEntity extends BaseEntity {
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String userId;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String userAccount;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String userName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String userGender;

    /**
     * 民族
     */
    @ApiModelProperty(value = "民族")
    private String nation;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String email;

    /**
     * 办公手机号
     */
    @ApiModelProperty(value = "办公手机号")
    private String telphone;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "登录密码")
    private String loginPassword;

    /**
     * 省份字典值
     */
    @ApiModelProperty(value = "省份字典值")
    private String provinceDic;

    /**
     * 地市字典值
     */
    @ApiModelProperty(value = "地市字典值")
    private String regionDic;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date validDate;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date invalidDate;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String remark;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String createUser;

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
    private String updateUser;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    private String userStatus;

    /**
     * 1表示超级管理员；0表示普通用户
     */
    @ApiModelProperty(value = "1表示超级管理员；0表示普通用户")
    private String isAdmin;

    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String uuid;

    /**
     * 是否接受短信
     */
    @ApiModelProperty(value = "是否接受短信")
    private String isReviceMsg;

    /**
     * 登录状态
     */
    @ApiModelProperty(value = "登录状态")
    private String loginStatus;

    /**
     * fw_users
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return user_id 
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId 
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 账号
     * @return user_account 账号
     */
    public String getUserAccount() {
        return userAccount;
    }

    /**
     * 账号
     * @param userAccount 账号
     */
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * 名称
     * @return user_name 名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 名称
     * @param userName 名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 性别
     * @return user_gender 性别
     */
    public String getUserGender() {
        return userGender;
    }

    /**
     * 性别
     * @param userGender 性别
     */
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    /**
     * 民族
     * @return nation 民族
     */
    public String getNation() {
        return nation;
    }

    /**
     * 民族
     * @param nation 民族
     */
    public void setNation(String nation) {
        this.nation = nation;
    }

    /**
     * 身份证号
     * @return id_number 身份证号
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 身份证号
     * @param idNumber 身份证号
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * 
     * @return email 
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 办公手机号
     * @return telphone 办公手机号
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * 办公手机号
     * @param telphone 办公手机号
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    /**
     * 登录密码
     * @return login_password 登录密码
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * 登录密码
     * @param loginPassword 登录密码
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * 省份字典值
     * @return province_dic 省份字典值
     */
    public String getProvinceDic() {
        return provinceDic;
    }

    /**
     * 省份字典值
     * @param provinceDic 省份字典值
     */
    public void setProvinceDic(String provinceDic) {
        this.provinceDic = provinceDic;
    }

    /**
     * 地市字典值
     * @return region_dic 地市字典值
     */
    public String getRegionDic() {
        return regionDic;
    }

    /**
     * 地市字典值
     * @param regionDic 地市字典值
     */
    public void setRegionDic(String regionDic) {
        this.regionDic = regionDic;
    }

    /**
     * 生效时间
     * @return valid_date 生效时间
     */
    public Date getValidDate() {
        return validDate;
    }

    /**
     * 生效时间
     * @param validDate 生效时间
     */
    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    /**
     * 失效时间
     * @return invalid_date 失效时间
     */
    public Date getInvalidDate() {
        return invalidDate;
    }

    /**
     * 失效时间
     * @param invalidDate 失效时间
     */
    public void setInvalidDate(Date invalidDate) {
        this.invalidDate = invalidDate;
    }

    /**
     * 
     * @return remark 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * @param remark 
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * 用户状态
     * @return user_status 用户状态
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * 用户状态
     * @param userStatus 用户状态
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 1表示超级管理员；0表示普通用户
     * @return is_admin 1表示超级管理员；0表示普通用户
     */
    public String getIsAdmin() {
        return isAdmin;
    }

    /**
     * 1表示超级管理员；0表示普通用户
     * @param isAdmin 1表示超级管理员；0表示普通用户
     */
    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 
     * @return uuid 
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 
     * @param uuid 
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 是否接受短信
     * @return is_revice_msg 是否接受短信
     */
    public String getIsReviceMsg() {
        return isReviceMsg;
    }

    /**
     * 是否接受短信
     * @param isReviceMsg 是否接受短信
     */
    public void setIsReviceMsg(String isReviceMsg) {
        this.isReviceMsg = isReviceMsg;
    }

    /**
     * 登录状态
     * @return login_status 登录状态
     */
    public String getLoginStatus() {
        return loginStatus;
    }

    /**
     * 登录状态
     * @param loginStatus 登录状态
     */
    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userAccount=").append(userAccount);
        sb.append(", userName=").append(userName);
        sb.append(", userGender=").append(userGender);
        sb.append(", nation=").append(nation);
        sb.append(", idNumber=").append(idNumber);
        sb.append(", email=").append(email);
        sb.append(", telphone=").append(telphone);
        sb.append(", loginPassword=").append(loginPassword);
        sb.append(", provinceDic=").append(provinceDic);
        sb.append(", regionDic=").append(regionDic);
        sb.append(", validDate=").append(validDate);
        sb.append(", invalidDate=").append(invalidDate);
        sb.append(", remark=").append(remark);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", userStatus=").append(userStatus);
        sb.append(", isAdmin=").append(isAdmin);
        sb.append(", uuid=").append(uuid);
        sb.append(", isReviceMsg=").append(isReviceMsg);
        sb.append(", loginStatus=").append(loginStatus);
        sb.append("]");
        return sb.toString();
    }
}