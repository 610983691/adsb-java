package com.coulee.aicw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coulee.aicw.dao.ApplicationManageEntityMapper;
import com.coulee.aicw.entity.ApplicationManageEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.service.IApplicationManageService;
@Service
public class ApplicationManageServiceImpl extends AbstractBaseService  implements IApplicationManageService {

	@Autowired
	public ApplicationManageEntityMapper applicationManageEntityMapper;
	 

	@Override
	public ApplicationManageEntity findByAppAlias(String appAlias) {
		try{
			return this.applicationManageEntityMapper.findByAppAlias(appAlias);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected IBaseDao getBaseDao() {
		return applicationManageEntityMapper;
	}

	@Override
	public Message batchDelete(List<ApplicationManageEntity> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要删除的应用！");
		}
		for (ApplicationManageEntity item : entityList) {
			this.delete(item.getId());
		}
		return Message.newSuccessMessage("删除成功！");
	}

}
