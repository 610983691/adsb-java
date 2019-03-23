package com.coulee.aicw.service.dict.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.dao.DictionaryEntityMapper;
import com.coulee.aicw.entity.DictionaryEntity;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.common.DateTools;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.dict.IDictService;

import oracle.sql.DATE;

@Service
public class IDictServiceImpl extends AbstractBaseService implements IDictService{
	@Autowired
	private DictionaryEntityMapper dictMapper;
	
	protected IBaseDao getBaseDao() {
		return dictMapper;
	}

	@Override
	public Map<String, Object> getDicMapByType(String type) {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		Map paraMap = new HashMap();
		paraMap.put("dicTypeCode", type);
		PageList<DictionaryEntity> pl = this.dictMapper.findByParams(paraMap, null);
		if(!BaseTools.isNull(pl)) {
			for(DictionaryEntity item : pl) {
				
				rsMap.put(item.getDicValue(), item.getDicName());
				
			}
		}
		return rsMap;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public <T extends BaseEntity> Message add(T entity) {
		DictionaryEntity t =(DictionaryEntity) entity;
		Date now =new Date();
		t.setUpdateDate(now);
		t.setCreateDate(now);
		int i = this.dictMapper.add(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@Override
	public String[] getDicMapByTypeForExl(String type) {
		String[] rs = null;
		Map paraMap = new HashMap();
		paraMap.put("dicTypeCode", type);
		PageList<DictionaryEntity> pl = this.dictMapper.findByParams(paraMap, null);
		if(!BaseTools.isNull(pl)) {
			rs = new String[pl.size()] ;
			int count = 0;
			for(DictionaryEntity item : pl) {
//				rs[count] =  item.getDicValue()+ "," + item.getDicName();
				rs[count] =  item.getDicName()+ "," + item.getDicValue();
				count = count + 1;
			}
		}
		return rs;
	}
}
