package com.coulee.aicw.service.dict;

import java.util.Map;

import com.coulee.aicw.foundations.service.IBaseService;

/***
 * 数据字典
 * @author tongjie
 *
 */
public interface IDictService extends IBaseService{
	/**
	 * 根据字典类型获取 map集合
	 * key  dicValue
	 * value  dicName
	 * @param type
	 * @return
	 */
	public Map<String,Object> getDicMapByType(String type);
	/**
	 * 获取exl导出文件的下拉框格式
	 * @param type
	 * @return
	 */
	public String[] getDicMapByTypeForExl(String type);
}
