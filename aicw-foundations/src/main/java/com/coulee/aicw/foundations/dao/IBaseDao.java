package com.coulee.aicw.foundations.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: 数据库操作接口，所有DAO模块接口需继承此接口<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public interface IBaseDao {

	/**
	 * Definition: 增加一条数据，新产生的ID会加入到dto中
	 * @param dto 数据信息
	 * @return 影响条数
	 * @Author: oblivion
	 * @Created date: 2014年12月11日
	 */
	public <T extends BaseEntity> int add (T dto) throws DataAccessException;
	
	/**
	 * Definition:根据ID删除数据方法
	 * @param id 主键ID
	 * @return 影响条数
	 * @Author: oblivion
	 * @Created date: 2014年12月8日
	 */
	public int delete(Object id);
	
	/**
	 * Definition:更新数据方法
	 * @param <T>
	 * @param dto 数据信息
	 * @return 影响条数
	 * @Author: oblivion
	 * @Created date: 2014年12月11日
	 */
	public <T extends BaseEntity> int update(T dto);
	
	/**
	 * Definition:根据ID查询数据
	 * @param id 要查询的主键ID
	 * @return 符号条件的数据DTO对象
	 * @Author: oblivion
	 * @Created date: 2014年12月8日
	 */
	public <T extends BaseEntity> T findById(Object id);
	
	/**
	 * Description: 根据ID集合查询数据<br> 
	 * Created date: 2018年3月6日
	 * @param ids 要查询的主键ID集合
	 * @return 符号条件的数据集合
	 * @author oblivion
	 */
	public <T extends BaseEntity> List<T> findByIds(@Param("ids")List<Object> ids);
	
	/**
	 * Definition:根据参数查询数据
	 * @param <T>
	 * @param params 参数Map，如传空则查询所有
	 * @param pageArg 分页参数，不分页则传null
	 * @return 符号条件的分页后数据集合
	 * @Author: oblivion
	 * @Created date: 2014年12月8日
	 */
	public <T extends BaseEntity> PageList<T> findByParams(@Param("params")Map<String, Object> params, @Param("pageArg")PageArg pageArg);

	/**
	 * Description: 根据参数查询数据条数<br> 
	 * Created date: 2018年3月6日
	 * @param params 参数Map，如传空则查询所有
	 * @return
	 * @author oblivion
	 */
	public int countByParams(@Param("params")Map<String, Object> params);
}

	