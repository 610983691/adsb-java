package com.coulee.aicw.service;

import java.util.List;

import com.coulee.aicw.entity.ApplicationManageEntity;
import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
/**
 * 应用管理
 * @author zyj
 *
 */
public interface IApplicationManageService extends IBaseService{
	/**
	 * 根据应用别名查询应用
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午3:26:06
	* @param 
	* @return
	 */
	public ApplicationManageEntity findByAppAlias(String appAlias);
	/**
	 * Description: 批量删除应用<br> 
	 * @param  
	 * @return
	 * @author zyj
	 */
	public Message batchDelete(List<ApplicationManageEntity> entityList);
}
