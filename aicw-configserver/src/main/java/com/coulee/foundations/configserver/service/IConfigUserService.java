package com.coulee.foundations.configserver.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.foundations.configserver.entity.ConfigUser;

/**
 * Description: 用户管理service接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IConfigUserService extends IBaseService {

	/**
	 * Description: 验证用户名密码是否正确<br> 
	 * Created date: 2017年12月14日
	 * @param uuid
	 * @param password
	 * @return
	 * @author LanChao
	 */
	public Message checkUser(String uuid, String password);
	
	/**
	 * Description: 用户信息自维护<br> 
	 * Created date: 2017年12月19日
	 * @param user
	 * @return
	 * @author LanChao
	 */
	public Message selfUpdate(ConfigUser user);
	
	/**
	 * Description: 批量删除用户<br> 
	 * Created date: 2017年12月20日
	 * @param users
	 * @return
	 * @author LanChao
	 */
	public Message batchDelete(List<ConfigUser> users);
	
	/**
	 * Description: 增加用户<br> 
	 * Created date: 2017年12月22日
	 * @param user
	 * @return
	 * @author LanChao
	 */
	public Message addUser(ConfigUser user);
	
	/**
	 * Description: 修改用户<br> 
	 * Created date: 2017年12月22日
	 * @param user
	 * @return
	 * @author LanChao
	 */
	public Message updateUser(ConfigUser user);
}
