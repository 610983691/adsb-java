package com.coulee.aicw.controller.fwInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.util.ParameterMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.TaskInfoEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.common.BaseTools;
import com.coulee.aicw.foundations.utils.common.FilePathTools;
import com.coulee.aicw.foundations.utils.excel.ImportExcel;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IFwInfoService;
import com.coulee.aicw.service.IOperateFirewallService;
import com.coulee.aicw.vo.OperateFirewallParam;

/**
 * 防火墙资产管理
 * 
 * @author zyj
 *
 */
@RestController
@RequestMapping("/fwInfo")
public class fwInfoController extends BaseController {
	private static final Logger logger = Logger.getLogger(fwInfoController.class);
	@Autowired
	private IFwInfoService assetInfoService;
	@Autowired
	private IOperateFirewallService operateFirewall;
//	public static final String TEMP_FW_INFO_FILE = "TEMP_FW_INFO_FILE";
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public PageEntity<FirewallAssetEntity> list(@RequestBody FirewallAssetEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<FirewallAssetEntity> pl = this.assetInfoService.queryFwList(entity, pageArg);
		return this.makePageEntity(pl);
	}
	@RequestMapping(value = "/testFWLogin", method = RequestMethod.POST)
	public Message testFWLogin() {
		return this.operateFirewall.testFwStatus();
	}
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "/sendFWOrder", method = RequestMethod.POST)
	public Message sendFWOrder(@RequestBody TaskInfoEntity taskInfo) {
//	public Message sendFWOrder(ServletRequest request) {
//		ParameterMap<String, String[]> requestParams=(ParameterMap<String, String[]>)request.getParameterMap();
//		String[] taskInfoJson = (String[]) requestParams.get("taskInfoJson");
//		if(taskInfoJson == null || taskInfoJson.length == 0) {
//			return Message.newFailureMessage("请求参数为空");
//		}
//		String taskInfoStr = taskInfoJson[0];
//		long time1 = System.currentTimeMillis();
//		TaskInfoEntity taskInfo = JsonTools.json2obj(taskInfoStr, TaskInfoEntity.class);
		Map<String,Object> contentsMap = taskInfo.getContents();
		if(BaseTools.isNull(contentsMap)) {
			return Message.newFailureMessage("未传入参数");
		}
		String jsonParams = BaseTools.getDefStr(contentsMap.get("jsonParams"));
		logger.info("jsonParams--"+jsonParams);
		if(BaseTools.isNull(jsonParams)) {
			return Message.newFailureMessage("未传入参数");
		}
		OperateFirewallParam fwOrderParam = JsonTools.json2obj(jsonParams, OperateFirewallParam.class);
		operateFirewall.operateFw(fwOrderParam);
		long time2 = System.currentTimeMillis();
//		logger.info("end sendFWOrder use time--"+(time2-time1)/1000);
		return Message.newSuccessMessage("防火墙设备下发策略调用成功");
	}
	/**
	 * Description: 增加防火墙<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Message add(@RequestBody String json) {
		FirewallAssetEntity entity = JsonTools.json2obj(json, FirewallAssetEntity.class);
		return assetInfoService.add(entity);
	}
	
	/**
	 * Description: 修改防火墙<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Message update(@RequestBody String json) {
		FirewallAssetEntity entity = JsonTools.json2obj(json, FirewallAssetEntity.class);
		return assetInfoService.update(entity);
	}
	
	/**
	 * Description: 删除防火墙<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Message delete(@RequestBody String json) {
		List<FirewallAssetEntity> delList = JsonTools.jsonarray2list(json, FirewallAssetEntity.class);
		return this.assetInfoService.batchDelete(delList);
	}
	
	/**
	 * Description: 上传临时数据库文件<br> 
	 * Created date: 2018年1月10日
	 * @param dbFile
	 * @param session
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @author oblivion
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public Message uploadFile(@RequestParam("exlFile") MultipartFile exlFile, HttpSession session)
			throws IllegalStateException, IOException {
		if (exlFile == null || exlFile.isEmpty()) {
			return Message.newFailureMessage("上传失败");
		}
		String filePath = exlFile.getOriginalFilename();
		boolean exl2003 = ImportExcel.isExcel2003(filePath);
		boolean exl2007 = ImportExcel.isExcel2007(filePath);
		boolean isExl = exl2003 || exl2007;
		if (!isExl) {
			return Message.newFailureMessage("文件格式不正确");
		}
		ImportExcel importTools = new ImportExcel();
		List<Map> dataList = importTools.read(exlFile.getInputStream(),exl2003);
		System.out.println(dataList.toString());
		return this.assetInfoService.uploadFile(dataList);
//		Map<String, String> map = (Map<String, String>) session.getAttribute("user");
//		String uid = map.get("uuid");
//		String tempFileName = DateTools.getDateStr("yyyyMMddHHmmssSSS") + "_" + uid + ".db";
//		File tmpDbFile = getTempFilePath(tempFileName);
//		dbFile.transferTo(tmpDbFile.getAbsoluteFile());
//		String oldDbFileName = map.get(TEMP_FW_INFO_FILE);
//		if (!StringUtils.isEmpty(oldDbFileName)) {
//			//删除旧文件
//			File oldDbFile = getTempDbFilePath(oldDbFileName);
//			if (oldDbFile.exists()) {
//				oldDbFile.delete();
//			}
//		}
//		map.put(TEMP_FW_INFO_FILE, tempDbFileName);
//		return Message.newSuccessMessage("上传成功");
	}
	
	public static File getTempFilePath(String tempDbFileName) {
		if (FilePathTools.isDeployModel()) {
			File dbFolder = new File("fwInfo");
			if (!dbFolder.exists()) {
				dbFolder.mkdir();
			}
			File tempDbFolder = new File(dbFolder, "temp");
			if (!tempDbFolder.exists()) {
				tempDbFolder.mkdir();
			}
			return new File(tempDbFolder, tempDbFileName);
		} else {
			String resourcesPath = FilePathTools.getResourcePathForJar();
			return new File(new File(resourcesPath).getParentFile(), tempDbFileName);
		}
	}
//	@Autowired
//	private A4DataSourceConfig  ldapConfig;
	/**
	 * Description: 同步防火墙<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
//	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
//	@RequestMapping(value = "/syn", method = RequestMethod.POST)
//	public Message synFw(@RequestBody String json) {
//		LDAPEnv ldapEnv = ldapConfig.getLDAPEnv();
//		try {
//			DirContext context = LdapOperUtils.getDirContext(ldapEnv);
//			
//			String baseDn = ldapEnv.getBaseDn();
//			String dn = "ou=netDevice, ou=device, ou=resource," + baseDn;
//			long time1 = System.currentTimeMillis();
//			logger.info("start search ladp ");
//			PageList<HashMap<String, List>> pl = LdapOperUtils.searchContextOne(context, dn, null, null);
//			if(BaseTools.isNull(pl)) {
//				return Message.newFailureMessage("未同步到信息");
//			}
//			long time2 = System.currentTimeMillis();
//			logger.info("start search ladp  end use time-"+((time2-time1)/1000));
//			logger.info("pl size-- "+pl.size());
//			logger.info("start ladp to entity ");
//			String driverType = null;
//			List<FirewallAssetEntity> fwList = new ArrayList<FirewallAssetEntity>();
//			for(HashMap<String, List> item : pl) {
//				driverType = BaseTools.getDefStr(item.get("driverType"));
//				if(!this.isFWType(driverType)) {
//					continue;
//				}
//				NetDeviceLdapVo vo = ObjTransTools.map2ldapEntity(item, NetDeviceLdapVo.class);
//				if(BaseTools.isNull(vo.getCn())) {
//					logger.info("ip "+vo.getIp()+" uuid is null ");
//					continue;
//				}
//				fwList.add(this.getFwEntity(vo));
//			}
//			long time3 = System.currentTimeMillis();
//			logger.info("end ladp to entity end use time-"+((time3-time2)/1000));
//			logger.info("fwList size--"+fwList.size());
//			logger.info("fwList  --"+fwList.toString());
//			return assetInfoService.synFw(fwList);
//		} catch (LDAPException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	/**
	 * 
	 * @param vo
	 * @return
	 */
//	private FirewallAssetEntity getFwEntity(NetDeviceLdapVo vo ) {
//		FirewallAssetEntity fwE = new FirewallAssetEntity();
//		fwE.setFwIp(vo.getIp());
//		fwE.setFwName(vo.getName());
//		fwE.setUuid(vo.getCn());
//		fwE.setFwPort(BaseTools.getIntDef0(vo.getAdminPort()));
//		fwE.setFwProtocol(vo.getConType());
//		fwE.setFwStatus("DIC_RES_STATUS_USE");
//		fwE.setFwType(vo.getDriverType());
//		fwE.setAdminAcount(vo.getAdminAccount());
//		if(!BaseTools.isNull(vo.getAdminPwd())) {
//			String adminPwd = ldapConfig.decode(vo.getAdminPwd());
//			fwE.setAdminPwd(adminPwd);
//		}
////		fwE.setAdminPwd(vo.getAdminPwd());
//		fwE.setAdminPrompt(vo.getAdminPrompt());
//		if(!BaseTools.isNull(vo.getEnablePwd())) {
//			String rootPwd = ldapConfig.decode(vo.getEnablePwd());
//			fwE.setRootPwd(rootPwd);
//		}
//		fwE.setRootPrompt(vo.getConPrompt());
//		return fwE;
//	}
//	private static final String[] fwTypeIndex = new String[] {"FIREWALL"};
//	/**
//	 * 是否防火墙
//	 * @param assetType
//	 * @return
//	 */
//	private boolean isFWType(String driverType) {
//		if(BaseTools.isNull(driverType)) {
//			return false;
//		}
//		for(String item : fwTypeIndex) {
//			if(driverType.indexOf(item) > -1) {
//				return true;
//			}
//			
//		}
//		return false;
//	}
	
	/**
	 * Description: 导出模板<br> 
	 * @param json
	 * @return
	 * @author zyj
	 */
	@RequestMapping(value="/downDemo", method=RequestMethod.POST)
	@ResponseBody
	public void downDemo(@RequestParam(value="fileFormat", required=false) String fileFormat,HttpServletResponse response) {
		this.assetInfoService.downDemo(fileFormat,response);
	}
}
