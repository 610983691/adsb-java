package com.coulee.aicw.service.dict.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coulee.aicw.dao.DictionaryTypeEntityMapper;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.service.dict.IDictTypeService;

@Service
public class IDictTypeServiceImpl extends AbstractBaseService implements IDictTypeService{
	@Autowired
	private DictionaryTypeEntityMapper dictTypeMapper;
	
	protected IBaseDao getBaseDao() {
		return dictTypeMapper;
	}
	
}
