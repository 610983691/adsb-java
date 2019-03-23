package com.coulee.aicw.foundations.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;

/**
 * Description: service层抽象类<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public abstract class AbstractBaseService implements IBaseService {

	/**
	 * Description: 获取该service操作数据层的dao<br>
	 * Created date: 2017年12月4日
	 * 
	 * @return
	 * @author LanChao
	 */
	protected abstract IBaseDao getBaseDao();

	@Transactional(rollbackFor=Exception.class)
	@Override
	public <T extends BaseEntity> Message add(T entity) {
		int i = this.getBaseDao().add(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Message delete(Object id) {
		BaseEntity entity = this.findById(id);
		int i = this.getBaseDao().delete(id);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public <T extends BaseEntity> Message update(T entity) {
		int i = this.getBaseDao().update(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@Override
	public <T extends BaseEntity> T findById(Object id) {
		return this.getBaseDao().findById(id);
	}
	
	@Override
	public <T extends BaseEntity> List<T> findByIds(List<Object> ids) {
		return this.getBaseDao().findByIds(ids);
	}

	@Override
	public <T extends BaseEntity> PageList<T> findByEntity(T entity, PageArg pageArg) {
		Map<String, Object> params = ObjTransTools.entity2map(entity);
		return this.getBaseDao().findByParams(params, pageArg);
	}

	@Override
	public <T extends BaseEntity> int countByEntity(T entity) {
		Map<String, Object> params = ObjTransTools.entity2map(entity);
		return this.getBaseDao().countByParams(params);
	}
}
