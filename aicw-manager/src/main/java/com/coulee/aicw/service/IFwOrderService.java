package com.coulee.aicw.service;

import java.util.List;

import com.coulee.aicw.entity.FwOrderEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;

public interface IFwOrderService extends IBaseService {
	/**
	 * Description: 批量删除<br> 
	 * @param  
	 * @return
	 * @author zyj
	 */
	public Message batchDelete(List<FwOrderEntity> entityList);
}
