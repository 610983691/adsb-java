package com.coulee.aicw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.dao.FwOrderEntityMapper;
import com.coulee.aicw.entity.FwOrderEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.service.IFwOrderService;
@Service
public class FwOrderServiceImpl extends AbstractBaseService implements IFwOrderService {
	@Autowired
	private FwOrderEntityMapper fwOrderMapper;
	@Override
	protected IBaseDao getBaseDao() {
		return fwOrderMapper;
	}
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Message batchDelete(List<FwOrderEntity> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return Message.newFailureMessage("删除失败，不存在要信息！");
		}
		for (FwOrderEntity item : entityList) {
			this.delete(item.getId());
		}
		return Message.newSuccessMessage("删除成功！");
	}
}
