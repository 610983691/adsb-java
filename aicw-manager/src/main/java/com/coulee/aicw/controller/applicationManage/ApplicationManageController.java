package com.coulee.aicw.controller.applicationManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.coulee.aicw.dao.FirewallAssetEntityMapper;
import com.coulee.aicw.dao.UserEntityMapper;
import com.coulee.aicw.entity.ApplicationManageEntity;
import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IApplicationManageService;
@RestController
@RequestMapping("/applicationManage")
public class ApplicationManageController extends BaseController {
	@Autowired
	private IApplicationManageService appManageService;
	@Autowired
	public FirewallAssetEntityMapper fwAssetEntityMapper;
	@Autowired
	private UserEntityMapper userMapper;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageEntity<ApplicationManageEntity> list(@RequestBody ApplicationManageEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<ApplicationManageEntity> pl = this.appManageService.findByEntity(entity, pageArg);
		/*
		 * 防火墙信息集合
		 */
		Map<String,FirewallAssetEntity> fwMap = new HashMap<String,FirewallAssetEntity>();
		Map<String,String> userMap = new HashMap<String,String>();
		if(!BaseTools.isNull(pl)) {
			String fwId = null;
			String approveUser = null;
			for(ApplicationManageEntity item : pl) {
				fwId = item.getFwId();
				if(!BaseTools.isNull(fwId)) {
					if(fwMap.containsKey(fwId) && fwMap.get(fwId) != null) {
						item.setFwName(fwMap.get(fwId).getFwName());
						item.setFwIp(fwMap.get(fwId).getFwIp());
					}else {
						FirewallAssetEntity fwEntity = fwAssetEntityMapper.findById(fwId);
						if(fwEntity != null) {
							item.setFwName(fwEntity.getFwName());
							item.setFwIp(fwEntity.getFwIp());
						}
						fwMap.put(fwId, fwEntity);
					}
				}

				approveUser = item.getApproveUser();
				if(!BaseTools.isNull(approveUser)) {
					if(userMap.containsKey(approveUser)) {
						item.setApproveUserName(userMap.get(approveUser));
					}else {
						UserEntity userEntity = userMapper.findByUserAccount(approveUser);
						if(userEntity != null) {
							item.setApproveUserName(userEntity.getUserName());
						}
						userMap.put(approveUser, item.getApproveUserName());
					}
					
				}
			}
		}
		return this.makePageEntity(pl);
	}
	
	/**
	 * Description: 增加应用<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(@RequestBody String json) {
		ApplicationManageEntity entity = JsonTools.json2obj(json, ApplicationManageEntity.class);
		Message checkMes = this.checkEntity(entity);
		if(checkMes != null) {
			return checkMes;
		}
		ApplicationManageEntity queryPara = new ApplicationManageEntity();
		queryPara.setAppAlias(entity.getAppAlias());
		PageList<ApplicationManageEntity> pl = this.appManageService.findByEntity(queryPara, null);
		if(!BaseTools.isNull(pl)) {
			return Message.newFailureMessage("应用简称重复，请重新维护");
		}
		return appManageService.add(entity);
	}
	/**
	 * 校验保存信息
	 * @param entity
	 * @return
	 */
	private Message checkEntity(ApplicationManageEntity entity) {
		if("1".equals(entity.getIsUseUrl())) {
			if(BaseTools.isNull(entity.getAppIpUrl())) {
				return Message.newFailureMessage("请维护域名!");
			}
		}else {
			if(BaseTools.isNull(entity.getConIp())) {
				return Message.newFailureMessage("请维护访问内网IP!");
			}
			if(entity.getConPort() == null || entity.getConPort().intValue() == 0) {
				return Message.newFailureMessage("请维护访问内网端口!");
			}
		}
		return null;
	}
	
	/**
	 * Description: 修改应用<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody String json) {
		ApplicationManageEntity entity = JsonTools.json2obj(json, ApplicationManageEntity.class);
		Message checkMes = this.checkEntity(entity);
		if(checkMes != null) {
			return checkMes;
		}
		ApplicationManageEntity queryPara = new ApplicationManageEntity();
		queryPara.setAppAlias(entity.getAppAlias());
		PageList<ApplicationManageEntity> pl = this.appManageService.findByEntity(queryPara, null);
		if(!BaseTools.isNull(pl)) {
			ApplicationManageEntity rsEntity = pl.get(0);
			if(!rsEntity.getId().equals(entity.getId())) {
				return Message.newFailureMessage("应用简称重复，请重新维护");
			}
		}
		return appManageService.update(entity);
	}
	
	/**
	 * Description: 删除应用<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody String json) {
		List<ApplicationManageEntity> delList = JsonTools.jsonarray2list(json, ApplicationManageEntity.class);
		return this.appManageService.batchDelete(delList);
	}
}
