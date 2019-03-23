package com.coulee.aicw.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coulee.aicw.dao.FirewallAssetEntityMapper;
import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.statistic.StatisticEntity;
import com.coulee.aicw.foundations.config.CryptoTools;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.common.IPHelper;
import com.coulee.aicw.foundations.utils.common.ObjTransTools;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IFwInfoService;
import com.coulee.aicw.service.dict.IDictService;
import com.coulee.aicw.util.HSSFTools;

/**
 * 防火墙资产信息管理
 * 
 * @author zyj
 *
 */
@Service
public class FwInfoServiceImpl extends AbstractBaseService implements IFwInfoService {
	@Autowired
	private IDictService dictService;

	@Autowired
	public FirewallAssetEntityMapper fwAssetEntityMapper;
	/**
	 * 加解密工具类
	 */
	@Autowired
	private CryptoTools cryptoTools;

	@Override
	protected IBaseDao getBaseDao() {
		return fwAssetEntityMapper;
	}

	private static final String ipType_v6 = "1";
	private static final String ipType_v4 = "0";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends BaseEntity> Message add(T entity) {
		FirewallAssetEntity fwEntity = (FirewallAssetEntity) entity;
		Map queryPara = new HashMap();
		if (ipType_v6.equals(fwEntity.getIpType())) {
			queryPara.put("fwIpv6", fwEntity.getFwIpv6());
		} else {
			queryPara.put("fwIp", fwEntity.getFwIp());
		}

		// queryPara.setFwIp(entity.getFwIp());
		PageList<FirewallAssetEntity> pl = getBaseDao().findByParams(queryPara, null);
		if (!BaseTools.isNull(pl)) {
			return Message.newFailureMessage("IP已经存在，请重新维护");
		}
		this.encodePwdByType(fwEntity, true);
		fwEntity.setCreateUser(fwEntity.get_username());
		fwEntity.setCreateDate(new Date());
		int i = this.getBaseDao().add(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends BaseEntity> Message update(T entity) {
		FirewallAssetEntity fwEntity = (FirewallAssetEntity) entity;
		Map queryPara = new HashMap();
		if (ipType_v6.equals(fwEntity.getIpType())) {
			queryPara.put("fwIpv6", fwEntity.getFwIpv6());
		} else {
			queryPara.put("fwIp", fwEntity.getFwIp());
		}
		PageList<FirewallAssetEntity> pl = getBaseDao().findByParams(queryPara, null);
		if (!BaseTools.isNull(pl)) {
			FirewallAssetEntity rsEntity = pl.get(0);
			if (!rsEntity.getId().equals(fwEntity.getId())) {
				return Message.newFailureMessage("IP已经存在，请重新维护");
			}
		}
		this.encodePwdByType(fwEntity, true);
		fwEntity.setUpdateUser(fwEntity.get_username());
		fwEntity.setUpdateDate(new Date());
		int i = this.getBaseDao().update(entity);
		if (i > 0) {
			return Message.newSuccessMessage("操作成功！", entity);
		} else {
			return Message.newFailureMessage("操作失败！", entity);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PageList queryFwList(FirewallAssetEntity entity, PageArg pageArg) {
		Map<String, Object> params = ObjTransTools.entity2map(entity);

		PageList<FirewallAssetEntity> pl = this.getBaseDao().findByParams(params, pageArg);
		Map assetStatusMap = dictService.getDicMapByType("DIC_ASSET_STATUS");
		// DIC_FW_TYPE
		Map assetTypeMap = dictService.getDicMapByType("DIC_FW_TYPE");
		for (FirewallAssetEntity item : pl) {
			item.setFwStatusDes(BaseTools.getDefStr(assetStatusMap.get(item.getFwStatus())));
			item.setFwTypeDes(BaseTools.getDefStr(assetTypeMap.get(item.getFwType())));
			this.encodePwdByType(item, false);
		}
		return pl;
	}

	/**
	 * 资产状态修改为删除
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public Message batchDelete(List<FirewallAssetEntity> entityList) {
		if (BaseTools.isNull(entityList)) {
			return Message.newFailureMessage("删除失败，没有选择删除信息！");
		}
		for (FirewallAssetEntity item : entityList) {
//			item.setFwStatus("DIC_RES_STATUS_DEL");
//			this.encodePwdByType(item, true);
			FirewallAssetEntity delItem = new FirewallAssetEntity();
			delItem.setFwStatus("DIC_RES_STATUS_DEL");
			delItem.setId(item.getId());
			fwAssetEntityMapper.update(delItem);
		}
		return Message.newSuccessMessage("删除成功");
	}

	/**
	 * 处理密码，加密，解密
	 * 
	 * @param entity
	 * @param isEncode
	 */
	private void encodePwdByType(FirewallAssetEntity entity, boolean isEncode) {
		if (!BaseTools.isNull(entity.getAdminPwd())) {
			if (isEncode) {
				entity.setAdminPwd(cryptoTools.encode(entity.getAdminPwd()));
			} else {
				entity.setAdminPwd(cryptoTools.decode(entity.getAdminPwd()));
			}

		}
		if (!BaseTools.isNull(entity.getRootPwd())) {
			if (isEncode) {
				entity.setRootPwd(cryptoTools.encode(entity.getRootPwd()));
			} else {
				entity.setRootPwd(cryptoTools.decode(entity.getRootPwd()));
			}

		}
	}

	@Override
	public List<StatisticEntity> queryTestStatusCount() {
		List<StatisticEntity> list = fwAssetEntityMapper.queryTestStatusCount();
		if(list == null) {
			return new ArrayList();
		}
		String name = null;
		for (StatisticEntity statisticEntity : list) {
			name = statisticEntity.getName();
			if ("1".equals(name)) {
				statisticEntity.setName("成功");
			} else {
				statisticEntity.setName("失败");
			}
		}
		return list;
	}

	private List<Map<String, String>> batchImportErrorList = new ArrayList<Map<String, String>>();

	@Override
	public void downDemo(String fileFormat,HttpServletResponse response) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("防火墙");
		String dictionarySheetName = "字典值";
		HSSFSheet dictionarySheet = hwb.createSheet(dictionarySheetName);
		// 样式
		HSSFCellStyle titleStyle = hwb.createCellStyle();
		HSSFCellStyle style = hwb.createCellStyle();

		/**
		 * 设置字体
		 */
		HSSFFont titleFont = HSSFTools.setHSSFont(hwb, "宋体", (short) 12, "黑色", true, false, false);
		HSSFFont font = HSSFTools.setHSSFont(hwb, "宋体", (short) 12, "黑色", false, false, false);

		// 将字体对象赋值给单元格样式对象
		titleStyle.setFont(titleFont);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 设置缺省列宽
		sheet.setDefaultColumnWidth(16);
		// 设置缺省列高
		sheet.setDefaultRowHeightInPoints(13);
		List<String[]> titleList = this.getTitleList();
		HSSFRow rowT = sheet.createRow(0);
		HSSFPatriarch patr = sheet.createDrawingPatriarch();
		for (int j = 0; j < titleList.size(); j++) {
			HSSFCell cell = rowT.createCell(j);
			String[] titleTemp =  titleList.get(j);
			String titleName = titleTemp[0];
			String titleComment = titleTemp[1];
			short x1 = 0, y1 = 0, x2 = 0, y2 = 0, c1 = 0, r1 = 0, c2 = 1, r2 = 2;
			if ("系统管理员".equals(titleName) || "安全管理员".equals(titleName) || "责任人".equals(titleName)) {
				c2 = 2;
				r2 = 3;
			}
			HSSFComment comment = HSSFTools.setHSSFComment(patr, x1, y1, x2, y2, c1, r1, c2, r2, titleComment,
					"coulee");

			int width = 45;
			String[] array = null;
			if ("设备状态".equals(titleName)) {
				array = this.getDictionaryMap(titleName);
			} else if ("设备类型".equals(titleName)) {
				array = this.getDictionaryMap(titleName);
				width = 160;
			} else if ("设备编码".equals(titleName)) {
				array = this.getDictionaryMap(titleName);
			} else if ("连接协议".equals(titleName)) {
				array = this.getDictionaryMap(titleName);
			}
			if (array != null) {
				HSSFTools.createSelect(j, hwb, sheet, dictionarySheet, dictionarySheetName, array);
			}
			sheet.setColumnWidth(cell.getColumnIndex(), 100 * width);

			cell.setCellValue(titleName);
			cell.setCellStyle(titleStyle);
			cell.setCellComment(comment);
		}
		try{
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			String fileName = "防火墙"+new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) ;
			response.addHeader("Content-Disposition", "attachment;   filename=\"" + (new String(fileName.getBytes("GB2312"), "ISO8859-1")) + ".xls\"");
			hwb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param dicType
	 * @return
	 */
	private String[] getDictionaryMap(String dicType) {
		String dicCode = null;
		if ("设备状态".equals(dicType)) {
			dicCode = "DIC_ASSET_STATUS";
		} else if ("设备类型".equals(dicType)) {
			dicCode = "DIC_FW_TYPE";
		} else if ("设备编码".equals(dicType)) {
			dicCode = "DIC_ASSET_ENCODE";
		} else if ("连接协议".equals(dicType)) {
			dicCode = "DIC_CON_PROTOCOL";
		}
		String[] resultArr = dictService.getDicMapByTypeForExl(dicCode);
		return resultArr;
	}
 
	/**
	 * 导出模型标题头
	 * 
	 * @return
	 */
	private List<String[]> getTitleList() {
		List<String[]> titleList = new ArrayList<String[]>();
		titleList.add(new String[] {"设备IP","必填项","fwIp"});
		titleList.add(new String[] {"设备名称","必填项","fwName"});
		titleList.add(new String[] {"设备编号","非必填","fwCode"});
		titleList.add(new String[] {"设备别名","非必填","fwAlias"});
		titleList.add(new String[] {"设备状态","必填项","fwStatus"});
		titleList.add(new String[] {"设备类型","必填项","fwType"});
		titleList.add(new String[] {"设备编码","必填项","encode"});
		titleList.add(new String[] {"连接协议","必填项","fwProtocol"});
		titleList.add(new String[] {"端口","必填项","fwPort"}); 
		titleList.add(new String[] {"管理员账号","必填项","adminAcount"}); 
		titleList.add(new String[] {"管理员密码","必填项","adminPwd"}); 
		titleList.add(new String[] {"管理员提示符","必填项","adminPrompt"}); 
		titleList.add(new String[] {"特权密码","必填项","rootPwd"}); 
		titleList.add(new String[] {"特权提示符","必填项","rootPrompt"}); 
//		titleList.add(new String[] {"操作结果"," "}); 
//		titleList.add(new String[] {"失败原因"," "}); 
		return titleList;
	}
	// @Override
	// public Message synFw(List<FirewallAssetEntity> fwList) {
	// if(BaseTools.isNull(fwList)) {
	// return Message.newFailureMessage("没有同步信息！");
	// }
	// /*
	// * 根据同步过来的防火墙IP再从 本系统数据库获取防火墙信息
	// */
	// Map<String, FirewallAssetEntity> fwMap = this.getFwMapForSyn(fwList);
	// String userName = "syn";
	// Date nowD = new Date();
	// String devIp = null;
	// for(FirewallAssetEntity entity : fwList) {
	// this.encodePwdByType(entity, true);
	// devIp = entity.getFwIp();
	// if(IPHelper.isIpv4(devIp)) {
	// entity.setIpType("0");
	// }else {
	//// entity.setFwIp("");
	// entity.setIpType("1");
	// entity.setFwIpv6(devIp);
	// }
	// if(fwMap.containsKey(devIp)) {
	// entity.setUpdateUser(userName);
	// entity.setUpdateDate(nowD);
	// int i = this.getBaseDao().update(entity);
	// }else {
	// entity.setCreateUser(userName);
	// entity.setCreateDate(nowD);
	// int i = this.getBaseDao().add(entity);
	// }
	// }
	// /*
	// * 解决同步过来的字典
	// */
	// this.resolveDic(fwList);
	// return Message.newSuccessMessage("同步成功");
	// }
	// @Autowired
	// private DictionaryEntityMapper dictMapper;
	// private static final String[] dicMul = new String[]
	// {"DIC_CON_PROTOCOL","DIC_FW_TYPE","DIC_ASSET_ENCODE"};
	// private static final String[] dicColMul = new String[]
	// {"fwProtocol","fwType","encode"};
	// /**
	// * 解决同步过来的字典
	// * @param fwList
	// */
	// private void resolveDic(List<FirewallAssetEntity> fwList) {
	// if(BaseTools.isNull(fwList)) {
	// return ;
	// }
	// /*
	// * DIC_CON_PROTOCOL 连接协议 fwProtocol
	// * DIC_FW_TYPE 防火墙类型 fwType
	// * DIC_ASSET_ENCODE 设备编码 encode
	// */
	// Map<String, String> colMap = new HashMap<String, String>();
	// colMap.put("fwProtocol", "DIC_CON_PROTOCOL");
	// colMap.put("fwType", "DIC_FW_TYPE");
	// colMap.put("encode", "DIC_ASSET_ENCODE");
	//
	// Map<String, Map<String, DictionaryEntity>> dicMap = new HashMap<String,
	// Map<String, DictionaryEntity>>();
	// Map<String, Object> queryMap = new HashMap<String, Object>();
	// for(String dicTypeCode : dicMul) {
	// queryMap.put("dicTypeCode", dicTypeCode);
	// List<DictionaryEntity> dicList = dictMapper.findByParams(queryMap, null);
	// Map<String, DictionaryEntity> dicTempMap = new HashMap<String,
	// DictionaryEntity>();
	// if(!BaseTools.isNull(dicList)) {
	// for(DictionaryEntity item : dicList) {
	// dicTempMap.put(item.getDicValue(), item);
	// }
	// }
	// dicMap.put(dicTypeCode, dicTempMap);
	// queryMap.clear();
	// }
	// List<DictionaryEntity> addDicList = new ArrayList<DictionaryEntity>();
	// for(FirewallAssetEntity item : fwList) {
	// Map<String, Object> tempMap = ObjTransTools.entity2map(item);
	// String dicTypeCode = null;
	// for(String column : dicColMul) {
	// dicTypeCode = colMap.get(column);
	// String value = BaseTools.getDefStr(tempMap.get(column));
	// if(BaseTools.isNull(value)) {
	// continue;
	// }
	// Map dicTempMap = dicMap.get(dicTypeCode);
	// if(dicTempMap.containsKey(value)) {
	//
	// }else {
	// dicTempMap.put(value, value);
	// DictionaryEntity dicEntity = new DictionaryEntity();
	// dicEntity.setDicName(value);
	// dicEntity.setDicValue(value);
	// dicEntity.setDicStatus("1");
	// dicEntity.setDicTypeCode(dicTypeCode);
	// addDicList.add(dicEntity);
	// }
	// }
	// }
	// if(!BaseTools.isNull(addDicList)) {
	// for(DictionaryEntity item : addDicList) {
	// dictMapper.add(item);
	// }
	//
	// }
	//
	//
	// }
	//
	// /**
	// * 得到属性值
	// * @param obj
	// */
	// public static String readAttributeValue(Object obj,String columnName) {
	// try {
	// String columnValue = "";
	// // 得到class
	// Class cls = obj.getClass();
	// // 得到所有属性
	// Field[] fields = cls.getDeclaredFields();
	// /*
	// * 遍历
	// */
	// for (int i = 0; i < fields.length; i++) {
	// // 得到属性
	// Field field = fields[i];
	// // 打开私有访问
	// field.setAccessible(true);
	// // 获取属性
	// String name = field.getName();
	// if(columnName.equals(name)) {
	// // 获取属性值
	// Object value = field.get(obj);
	// columnValue = BaseTools.getDefStr(value);
	// return columnValue;
	// }
	//
	// }
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// return columnName;
	// }
	// /**
	// * 根据同步过来的防火墙IP再从 本系统数据库获取防火墙信息
	// * @param fwList
	// * @return
	// */
	// private Map<String, FirewallAssetEntity>
	// getFwMapForSyn(List<FirewallAssetEntity> fwList) {
	// Map<String, FirewallAssetEntity> fwMap = new HashMap<String,
	// FirewallAssetEntity>();
	// List<String> ipSynList = new ArrayList<String>();
	// Map<String, FirewallAssetEntity> ipSynMap = new HashMap<String,
	// FirewallAssetEntity>();
	// String devIp = null;
	// for(FirewallAssetEntity item : fwList) {
	// devIp = item.getFwIp();
	// if(ipSynMap.containsKey(devIp)) {
	//
	// }else {
	// ipSynList.add(devIp);
	// ipSynMap.put(devIp, item);
	// }
	//
	// }
	// List<List> ipGroupList = BaseTools.getListGroup(ipSynList, 1000);
	// for(List item : ipGroupList) {
	// //fwIpMul
	// Map paraMap = new HashMap();
	// paraMap.put("fwIpMul", item);
	// List<FirewallAssetEntity> fwRsList = this.getBaseDao().findByParams(paraMap,
	// null);
	// if(!BaseTools.isNull(fwRsList)) {
	// for(FirewallAssetEntity entity : fwRsList) {
	// fwMap.put(entity.getFwIp(), entity);
	// }
	// }
	// }
	// return fwMap;
	// }
	/**
	 * 解析xls的 下拉框  数据
	 * @param title
	 * @param value
	 * @return
	 */
	private String getDicValue(String title,String value) {
		if(BaseTools.isNull(value)) {
			return null;
		}
		if ("设备状态".equals(title) || "设备类型".equals(title) || "设备编码".equals(title) || "连接协议".equals(title)) {
			 
		} else {
			return value;
		}
		
		String rs = null;
		int valueIndex = value.indexOf(",");
		rs = value.substring(valueIndex+1, value.length());
		return rs;
	}
	public static void main(String[] args) {
		String ss = "123,adb";
		int valueIndex = ss.indexOf(",");
		String rs = ss.substring(valueIndex+1, ss.length());
		System.out.println(rs);
	}
	private static final String checkRequireTrue = "必填项";
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Message uploadFile(List<Map> dataList) {
		try {
			List<FirewallAssetEntity> saveList = new ArrayList();
			if (BaseTools.isNull(dataList)) {
				return Message.newFailureMessage("传入参数为空");
			}
			List<String[]> titleList = this.getTitleList();
			String exlColumnName = null;
			String exlColumnValue = null;
			String fwIp = null;
			List<String> errorList = new ArrayList<String>();
			int index = 1;
			for (Map item : dataList) {
				Map tempMap = new HashMap();
				/*
				 * titleList.add(new String[] {"设备名称","必填项","fwName"});
				 */

				StringBuffer errorSb = new StringBuffer();
				for (String[] title : titleList) {
					exlColumnName = title[0];
					exlColumnValue = BaseTools.getDefStr(item.get(exlColumnName));
					exlColumnValue = this.getDicValue(exlColumnName, exlColumnValue);
					if (BaseTools.isNull(exlColumnValue)) {
						if (checkRequireTrue.equals(title[1])) {
							if (errorSb.length() > 0) {
								errorSb.append(",");
							}
							errorSb.append(exlColumnName);
						}
					}
					tempMap.put(title[2], exlColumnValue);
				}
				fwIp = BaseTools.getDefStr(tempMap.get("fwIp"));
				if (BaseTools.isNull(fwIp)) {
					errorList.add("存在未输入防火墙IP的行项" + index);
					index = index + 1;
					continue;
				}
				if (errorSb.length() > 0) {
					String errorItem = "防火墙IP:" + fwIp + "的" + errorSb.toString() + "必填项为空";
					errorList.add(errorItem);
					index = index + 1;
					continue;
				} else {
					String jsonStr = JsonTools.obj2json(tempMap);
					FirewallAssetEntity entity = JsonTools.json2obj(jsonStr,  FirewallAssetEntity.class);
					entity.setFwIp(null);
					if (IPHelper.isIpv4(fwIp)) {
						entity.setFwIp(fwIp);
						entity.setIpType(ipType_v4);
					} else {
						entity.setFwIpv6(fwIp);
						entity.setIpType(ipType_v6);
					}
					saveList.add(entity);
				}
				index = index + 1;
			}
			if (BaseTools.isNull(saveList)) {
				return Message.newFailureMessage(this.getErrorMsg(errorList));
			}
			Date nowD = new Date();
			for (FirewallAssetEntity item : saveList) {
				Map params = new HashMap();
				if (ipType_v6.equals(item.getIpType())) {
					params.put("fwIpv6", item.getFwIpv6());
				} else {
					params.put("fwIp", item.getFwIp());
				}

				List<FirewallAssetEntity> rsList = this.fwAssetEntityMapper.findByParams(params, null);
				if (BaseTools.isNull(rsList)) {
					item.setCreateDate(nowD);
					this.encodePwdByType(item, true);
					fwAssetEntityMapper.add(item);
				} else {
					FirewallAssetEntity dbEntity = rsList.get(0);
					item.setUpdateDate(nowD);
					item.setId(dbEntity.getId());
					this.encodePwdByType(item, true);
					fwAssetEntityMapper.update(item);
				}

			}
		} catch (Exception e) {
			return Message.newFailureMessage("导入出错:"+e.getMessage());
		}
		return Message.newSuccessMessage("导出完成");
	}
	/**
	 * 
	 * @param errorList
	 * @return
	 */
	private String getErrorMsg(List<String> errorList) {
		if(BaseTools.isNull(errorList)) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		for(String item : errorList) {
			if(rs.length() > 0) {
				rs.append("/r/n;");
			}
			rs.append(item);
		}
		return rs.toString();
	}
}
