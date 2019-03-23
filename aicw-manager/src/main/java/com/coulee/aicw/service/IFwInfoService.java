package com.coulee.aicw.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.springframework.web.bind.annotation.RequestBody;

import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.aicw.foundations.utils.page.PageArg;
/**
 * 防火墙资产信息管理
 * @author zyj
 *
 */
import com.coulee.aicw.foundations.utils.page.PageList;
public interface IFwInfoService extends IBaseService{
	/**
	 * Description: 批量删除用户<br> 
	 * @param  
	 * @return
	 * @author zyj
	 */
	public Message batchDelete(List<FirewallAssetEntity> entityList);
	
	/**
	 * 新增防火墙
	 * @param entity
	 * @return
	 */
//	public Message addFw(FirewallAssetEntity entity);
	/**
	 * 修改防火墙
	 * @param entity
	 * @return
	 */
//	public Message updateFw(FirewallAssetEntity entity);
	/**
	 * 查询防火墙
	 * @param entity
	 * @param pageArg
	 * @return
	 */
	public PageList<FirewallAssetEntity> queryFwList(FirewallAssetEntity entity, PageArg pageArg);
	
	/**
	 * 查询防火墙个状态的数量
	 * @return
	 */
	public List<StatisticEntity> queryTestStatusCount();
	/**
	 * 导出模板
	 * @param json
	 * @return
	 */
	public void downDemo(String fileFormat,HttpServletResponse response);
	
	/**
	 * 导入
	 * @param json
	 * @return
	 */
	public Message uploadFile(List<Map> dataList);
}
