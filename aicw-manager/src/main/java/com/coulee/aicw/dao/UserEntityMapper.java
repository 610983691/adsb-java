package com.coulee.aicw.dao;

import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;

public interface UserEntityMapper extends IBaseDao {
	/**
	 * 根据用户id逻辑删除用户
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月24日下午3:48:14
	* @param 
	* @return
	 */
	public int deleteByUserId(String userId);
	/**
	 * 根据用户账号，查询用户信息
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月25日下午2:39:54
	* @param 
	* @return
	 */
	public UserEntity findByUserAccount(String userAccount);
	
	
	/**
	 * 跟进用户手机号码查询用户信息
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日上午10:10:23
	* @param 
	* @return
	 */
	public UserEntity findByUserTel(String tel);
	
}