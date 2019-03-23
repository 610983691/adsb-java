package com.coulee.aicw.service.user;

import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
/**
 * 用户的接口类
 * Description:
 * Copyright (C) 2013 Coulee Right Reserved.
 * createDate：2018年10月25日下午2:27:57
 * author：HongZhang
 * @version 1.0
 */
public interface IUserService extends IBaseService {
	
	/**
	 * 根据用户账号，验证用户密码是否正确
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月25日下午2:36:42
	* @param 
	* @return
	 */
	public Message checkUser(String userAccount, String password);
	
	public UserEntity findByUserTelphone(String telphone);
	public Message add(UserEntity entity) ;
	
	/***
	 * 修改用户信息
	 * @param entity
	 * @return
	 */
	public Message updateUser(UserEntity entity) ;
	
	/***
	 * 判断用户账号是否存在
	 * @param account 用户的账号
	 * @return true--已存在,false--不存在
	 */
	public boolean isExistAccount(String account);
}
