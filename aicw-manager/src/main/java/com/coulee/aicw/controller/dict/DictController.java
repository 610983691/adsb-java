package com.coulee.aicw.controller.dict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.entity.DictionaryEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.dict.IDictService;

/***
 * 数据字典增删改查
 * @author tongjie
 *
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {

	@Autowired
	private IDictService dictService;
	 
	@RequestMapping("/list")
	public PageEntity<DictionaryEntity> list(@RequestBody DictionaryEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<DictionaryEntity> pl = this.dictService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryDicList")
	public List queryDicList(@RequestBody DictionaryEntity entity) {
		List dicList = this.dictService.findByEntity(entity, null);
		return dicList;
	}
	@RequestMapping("/add")
	public Message addDict(@RequestBody DictionaryEntity entity) {
		return dictService.add(entity);
	}
	
	@RequestMapping("/delete")
	public Message deleteDict(@RequestBody DictionaryEntity entity) {
		return dictService.delete(entity.getId());
	}
	
	@RequestMapping("/update")
	public Message updateDict(@RequestBody DictionaryEntity entity) {
		return dictService.update(entity);
	}
}
