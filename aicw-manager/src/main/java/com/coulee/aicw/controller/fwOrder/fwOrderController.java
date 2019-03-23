package com.coulee.aicw.controller.fwOrder;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.entity.FwOrderEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IFwOrderService;
import com.coulee.aicw.service.dict.IDictService;
/**
 * 防火墙命令管理
 * @author zyj
 *
 */
@RestController
@RequestMapping("/fwOrder")
public class fwOrderController extends BaseController {

	@Autowired
	private IFwOrderService fwOrderService;
	@Autowired
	private IDictService dictService;
	
	private static final Logger logger = Logger.getLogger(fwOrderController.class);
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageEntity<FwOrderEntity> list(@RequestBody FwOrderEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<FwOrderEntity> pl = this.fwOrderService.findByEntity(entity, pageArg);
		logger.info("query pl size --"+pl.size());
		Map fwTypeMap = dictService.getDicMapByType("DIC_FW_TYPE");
		Map fwOrderTypeMap = dictService.getDicMapByType("DIC_FW_ORDER_TYPE");
		if(!BaseTools.isNull(pl)) {
			 for(FwOrderEntity item : pl) {
				 item.setFwTypeDes(BaseTools.getDefStr(fwTypeMap.get(item.getFwType())));
				 item.setOrderTypeDes(BaseTools.getDefStr(fwOrderTypeMap.get(item.getOrderType())));
			 }
		}
		
		return this.makePageEntity(pl);
	}
	
	/**
	 * Description: 增加<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(@RequestBody String json) {
		FwOrderEntity entity = JsonTools.json2obj(json, FwOrderEntity.class);
		PageList<FwOrderEntity> pl = this.fwOrderService.findByEntity(entity, null);
		if(!BaseTools.isNull(pl)) {
			return Message.newFailureMessage("记录重复，请重新维护");
		}
		return fwOrderService.add(entity);
	}
	
	/**
	 * Description: 修改<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody String json) {
		FwOrderEntity entity = JsonTools.json2obj(json, FwOrderEntity.class);
		return fwOrderService.update(entity);
	}
	
	/**
	 * Description: 批量删除<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody String json) {
		List<FwOrderEntity> delList = JsonTools.jsonarray2list(json, FwOrderEntity.class);
		return this.fwOrderService.batchDelete(delList);
	}
}
