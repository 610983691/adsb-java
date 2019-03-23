package com.coulee.aicw.controller.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.entity.DictionaryTypeEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.dict.IDictTypeService;

@RestController
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {

	@Autowired
	private IDictTypeService dictTypeService;
	/***
	 * 查询字典类型
	 * @param entity 要查询的类型实体，为空则查询所有类型。
	 * @return 字典类型列表
	 */
	@RequestMapping("/list")
	public PageEntity<DictionaryTypeEntity> list(@RequestBody DictionaryTypeEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<DictionaryTypeEntity> pl = this.dictTypeService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}
	
}
