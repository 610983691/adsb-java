package com.coulee.aicw.foundations.service;

import java.util.List;

import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: 逻辑层接口，所有Service组件接口需继承该接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IBaseService {
	
	/**
	 * Definition: 增加操作
	 * @param <T>
	 * @param entity 要增加的数据对象
	 * @return Message 操作结果对象，包括增加成功包含主键ID的entity对象
	 * @Author: oblivion
	 * @Created date: 2014年12月4日
	 */
	public <T extends BaseEntity> Message add(T entity);
	
	/**
	 * Definition: 根据主键ID删除数据
	 * @param id 要删除的数据ID
	 * @return Message 操作结果对象
	 * @Author: oblivion
	 * @Created date: 2014年12月4日
	 */
	public Message delete(Object id);
	
	/**
	 * Definition: 修改操作
	 * @param <T>
	 * @param entity 要修改的数据对象
	 * @return Message 操作结果对象
	 * @Author: oblivion
	 * @Created date: 2014年12月4日
	 */
	public <T extends BaseEntity> Message update (T entity);
	
	/**
	 * Definition: 根据主键ID查询数据
	 * @param id 主键ID
	 * @return 符合条件的结果对象
	 * @Author: oblivion
	 * @Created date: 2014年12月11日
	 */
	public <T extends BaseEntity> T findById(Object id);
	
	/**
	 * Description: 根据主键ID集合查询数据<br> 
	 * Created date: 2018年3月6日
	 * @param ids 主键ID集合
	 * @return 符合条件的结果对象集合
	 * @author oblivion
	 */
	public <T extends BaseEntity> List<T> findByIds(List<Object> ids);
	
	/**
	 * Definition:根据条件查询
	 * @param entity 条件对象，如不传递则查询所有
	 * @param pageArg 分页参数
	 * @return 符合条件分页后的数据对象集合
	 * @Author: oblivion
	 * @Created date: 2014年12月11日
	 */
	public <T extends BaseEntity> PageList<T> findByEntity(T entity, PageArg pageArg);
	
	/**
	 * Description: 根据条件查询条数<br> 
	 * Created date: 2018年3月6日
	 * @param entity
	 * @return
	 * @author oblivion
	 */
	public <T extends BaseEntity> int countByEntity(T entity);
	
}

	