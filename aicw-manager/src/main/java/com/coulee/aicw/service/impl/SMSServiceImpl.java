package com.coulee.aicw.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coulee.aicw.client.InvokeTaskServiceClient;
import com.coulee.aicw.dao.FirewallAssetEntityMapper;
import com.coulee.aicw.entity.AlarmLogEntity;
import com.coulee.aicw.entity.ApplicationManageEntity;
import com.coulee.aicw.entity.ControlLogEntity;
import com.coulee.aicw.entity.FirewallAssetEntity;
import com.coulee.aicw.entity.TaskInfoEntity;
import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.Constants;
import com.coulee.aicw.foundations.entity.BaseEntity;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PhoneModel;
import com.coulee.aicw.foundations.utils.common.JWTUtils;
import com.coulee.aicw.foundations.utils.common.PhoneUtils;
import com.coulee.aicw.foundations.utils.json.JsonTools;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.IApplicationManageService;
import com.coulee.aicw.service.IOperateFirewallService;
import com.coulee.aicw.service.ISMSService;
import com.coulee.aicw.service.alarm.IAlarmLogService;
import com.coulee.aicw.service.log.IControlLogService;
import com.coulee.aicw.service.user.IUserService;
import com.coulee.aicw.sms.SMSContentUtils;
import com.coulee.aicw.sms.entity.SMSContentEntity;
import com.coulee.aicw.sms.service.ISMSSenderService;
import com.coulee.aicw.vo.OperateFirewallParam;
@Service
public class SMSServiceImpl implements ISMSService {
	private static Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);
	@Autowired
	private IUserService userService;
	@Autowired
	public IAlarmLogService alarmLogService;
	@Autowired
	private IControlLogService controlLogService;
	@Autowired
	public IApplicationManageService applicationManageService;
	@Autowired
	public FirewallAssetEntityMapper firewallAssetEntityMapper;
	@Autowired
	public IOperateFirewallService operateFirewallService;
	@Autowired
	public InvokeTaskServiceClient taskService;
	@Autowired(required=false)
	public ISMSSenderService ISMSSenderService;
	@Override
	public <T extends BaseEntity> Message add(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message delete(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseEntity> Message update(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseEntity> T findById(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseEntity> List<T> findByIds(List<Object> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseEntity> PageList<T> findByEntity(T entity, PageArg pageArg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseEntity> int countByEntity(T entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void applyReceptionSMS(String telphone, String msg,String smsnumber) {
		try{
			//根据用户手机号码，检查用户是否存在
			UserEntity ue = this.userService.findByUserTelphone(telphone);
			if(ue!=null){
				//解析短信内容，然后根据应用URL查询应用，判断应用是否需要审批，不审批直接调用防火墙策略下发策略
				SMSContentEntity se = SMSContentUtils.parseApplyContent(msg);
				//根据应用别名查询应用
				String appAlias = se.getAppAlias();
				ApplicationManageEntity ae = this.applicationManageService.findByAppAlias(appAlias);
				//调用下发防火墙策略接口
				FirewallAssetEntity fa = this.firewallAssetEntityMapper.findById(ae.getFwId());
				if(ae!=null){
					//值为1，需要审批，否则不需要审批
					if("1".equalsIgnoreCase(ae.getIsApprove())){
						//获取审批人信息
						UserEntity approveUser = this.userService.findById(ae.getApproveUser());
						
						ControlLogEntity ce = new ControlLogEntity();//记录策略下发日志
						ce.setFwIp(fa.getFwIp());
						ce.setFwName(fa.getFwName());
						ce.setMobileMsg(msg);
						ce.setApplyUserId(ue.getUserId());
						ce.setApplyMobileNumber(telphone);
						ce.setStatus(Constants.FW_LOG_STATUS_WAIT_APPROVA);
						ce.setValidTime(new Date());
						long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
						ce.setInvalidTime(new Date(currentTime));
						ce.setSmsnumber(smsnumber);
						ce.setApproveUserId(approveUser.getUserId());
						this.controlLogService.add(ce);
						
						//短信内容
						String sendmsg = SMSContentUtils.makeApprovalContent(se);
						//发送短信通知
						this.ISMSSenderService.sendSms(approveUser.getTelphone(), sendmsg, smsnumber);
						
					}else{
						ControlLogEntity ce = new ControlLogEntity();//记录策略下发日志
						ce.setFwIp(fa.getFwIp());
						ce.setFwName(fa.getFwName());
						ce.setMobileMsg(msg);
						ce.setApplyUserId(ue.getUserId());
						ce.setApplyMobileNumber(telphone);
						ce.setValidTime(new Date());
						long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
						ce.setInvalidTime(new Date(currentTime));
						ce.setSmsnumber(smsnumber);
//						ce.setSendOrder((String) mes.getObj());
						ce.setStatus(Constants.FW_LOG_STATUS_WATING);
						this.controlLogService.add(ce);
						Message mes = this.fwACLsend(ae, fa, se,ce);
						if(mes != null && mes.isSuccess()){//判断策略下发结果
							ce.setSendOrder((String) mes.getObj());
							ce.setValidTime(new Date());
							long currentTime1 = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
							ce.setInvalidTime(new Date(currentTime1));
							ce.setStatus(Constants.FW_LOG_STATUS_SUC);
							this.controlLogService.update(ce);
							//发送通知消息
							//短信内容
							String sendmsg = SMSContentUtils.makePassContent(se);
							this.ISMSSenderService.sendSms(telphone, sendmsg, smsnumber);
						}else{
							/*
							 * 调用计划或者检查防火墙命令失败 
							 */
							ce.setSendOrder((String) mes.getObj());
							ce.setStatus(Constants.FW_LOG_STATUS_FAIL);
							this.controlLogService.update(ce);
							//短信通知
							String  sendmsg = SMSContentUtils.makePolicyErrorContent(se);
							//发送短信通知
							this.ISMSSenderService.sendSms(telphone, sendmsg, smsnumber);
						}
					}
				}else{
					//短信通知
					String  sendmsg = SMSContentUtils.makeWrongfulContent(SMSContentUtils.WRONG_TYPE_APPALIAS);
					//发送短信通知
					this.ISMSSenderService.sendSms(telphone, sendmsg, smsnumber);
				}
			}else{
				AlarmLogEntity alarmLogEntity = new AlarmLogEntity();
				alarmLogEntity.setMobileNumber(telphone);
				alarmLogEntity.setReciveCount(0);
				//alarmLogEntity.setReciveMsg(msg);
				PhoneModel phoneModel = PhoneUtils.getPhoneModel(telphone);
				if(phoneModel != null){
					alarmLogEntity.setMobileProvince(phoneModel.getProvinceName());
					alarmLogEntity.setMobileCity(phoneModel.getCityName());
					alarmLogEntity.setCarrier(phoneModel.getCarrier());
		        }else{
		        	alarmLogEntity.setMobileProvince("该号码无效");
		        }
				
				alarmLogEntity.setCreateDate(new Date(0));
				this.alarmLogService.addAlarmLog(alarmLogEntity,msg);
				//发送消息通知
				String sendmsg = SMSContentUtils.makeWrongfulContent(SMSContentUtils.WRONG_TYPE_PHONE);
				this.ISMSSenderService.sendSms(telphone, sendmsg, smsnumber);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * 
	* Definition: 
	* author: HongZhang
	* Created date: 2018年10月29日下午3:57:17
	* @param ae 需要修改防火墙的应用
	*     netip  外网访问的ip
	* @return
	 */
	private Message fwACLsend(ApplicationManageEntity ae,FirewallAssetEntity fa,SMSContentEntity se,ControlLogEntity ce){
		try{
			String netIp = se.getNetIp();
			Message operFwMes = null;
			OperateFirewallParam paramVo = new OperateFirewallParam();
			paramVo.setFwId(fa.getId());
			paramVo.setOperateType(Constants.FW_ACL_TYPE_ADD);
			paramVo.setNetIP(se.getNetIp());
			paramVo.setAppliationId(ae.getId());
			paramVo.setInIp(netIp);
			paramVo.setOpenPort(ae.getConPort());
			Message checkMes = paramVo.checkParam();
			if(!checkMes.isSuccess()) {
				return checkMes;
			}
			/*
			 * 立即执行
			 */
			operFwMes = this.operateFirewallService.operateFw(paramVo);
			if(!operFwMes.isSuccess()) {
				return operFwMes;
			}
			/*
			 * 调用计划  控制防火墙
			 * 删除策略计划
			 */
			long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
			Date invalidTime = new Date(currentTime);
			/*
			 * 下发防火墙成功 
			 * 设置策略有效时间 范围
			 */
			ce.setValidTime(new Date());
			ce.setInvalidTime(invalidTime); 
			
			TaskInfoEntity removeFwTaskInfo = new TaskInfoEntity();
			removeFwTaskInfo.setApplicationHost("aicw-manager");
			removeFwTaskInfo.setJobId(ce.getId());
			removeFwTaskInfo.setJobType("FW_ACL_TYPE_REMOVE");
			// removeFwTaskInfo.setJobDescription("remove firewall order ");
			removeFwTaskInfo.setRequestPath("/fwInfo/sendFWOrder");
			removeFwTaskInfo.setCronExpression("");
			removeFwTaskInfo.setStartTime(invalidTime);
			Map<String,String> headersMap = new HashMap<String,String>();
			/*
			 * 延迟十秒关闭授权
			 */
			long expirationMill = currentTime + 10*1000;
			String token = JWTUtils.create("FW_ACL_TYPE_REMOVE_TASK", expirationMill);
			headersMap.put("Authorization", "JWT "+token);
			removeFwTaskInfo.setHeaders(headersMap);
			OperateFirewallParam removeFwOrderParam = new OperateFirewallParam();
			removeFwOrderParam.setFwId(fa.getId());
			removeFwOrderParam.setOperateType(Constants.FW_ACL_TYPE_REMOVE);
			removeFwOrderParam.setNetIP(netIp);
			removeFwOrderParam.setAppliationId(ae.getId());
			removeFwOrderParam.setInIp(ae.getConIp());
			removeFwOrderParam.setOpenPort(ae.getConPort());
			String removeJsonStr = JsonTools.obj2json(removeFwOrderParam);
			
			Map<String,Object> contentsMap = new HashMap<String,Object>();
			contentsMap.put("jsonParams", removeJsonStr);
			removeFwTaskInfo.setContents(contentsMap);
			Message addTaskMes = taskService.add(removeFwTaskInfo);
			if(addTaskMes.isSuccess()) {
				
			}else {
				operFwMes.setMsg(addTaskMes.getMsg());
				operFwMes.setSuccess(false);
			}
			return operFwMes;
		}catch(Exception e){
			e.printStackTrace();
			return Message.newFailureMessage("系统错误");
		}
	}
 
	@Override
	public void approvalReceptionSMS(String telphone, String msg, String smsnumber) {
		try{
			//根据短信号，查询申请信息
			ControlLogEntity ce = this.controlLogService.findBySmsNum(smsnumber);
			if(ce!=null){
				SMSContentEntity se = SMSContentUtils.parseApplyContent(ce.getMobileMsg());
				if("Y".equalsIgnoreCase(msg)){					
					//根据应用别名查询应用
					String appAlias = se.getAppAlias();
					ApplicationManageEntity ae = this.applicationManageService.findByAppAlias(appAlias);
					//调用下发防火墙策略接口
					FirewallAssetEntity fa = this.firewallAssetEntityMapper.findById(ae.getFwId());
					Message mes = this.fwACLsend(ae, fa, se,ce);
					if(mes.isSuccess()){//判断策略下发结果
						ce.setSendOrder((String) mes.getObj());
						ce.setStatus(Constants.FW_LOG_STATUS_SUC);
						/*
						 * fwACLsend 方法设置 时间范围
						 */
//						ce.setValidTime(new Date());
//						long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
//						ce.setInvalidTime(new Date(currentTime));
						ce.setApproveMsg(msg);
						this.controlLogService.update(ce);
						//发送通知消息
						//短信内容
						String sendmsg = SMSContentUtils.makePassContent(se);
						this.ISMSSenderService.sendSms(ce.getApplyMobileNumber(), sendmsg, smsnumber);
					}else{
						ce.setSendOrder((String) mes.getObj());
						ce.setStatus(Constants.FW_LOG_STATUS_FAIL);
						/*
						 * fwACLsend 方法设置 时间范围
						 */
//						ce.setValidTime(new Date());
//						long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
//						ce.setInvalidTime(new Date(currentTime));
						ce.setApproveMsg(msg);
						this.controlLogService.update(ce);
						
						//短信通知
						String  sendmsg = SMSContentUtils.makePolicyErrorContent(se);
						//发送短信通知
						this.ISMSSenderService.sendSms(ce.getApplyMobileNumber(), sendmsg, smsnumber);
					}
				}else{
					ce.setStatus(Constants.FW_LOG_STATUS_WAIT_FAIL);
					ce.setValidTime(new Date());
					long currentTime = System.currentTimeMillis() + Long.parseLong(se.getApplyDuration()) * 60 * 1000;
					ce.setInvalidTime(new Date(currentTime));
					ce.setApproveMsg(msg);
					this.controlLogService.update(ce);
					//发送通知消息
					//短信内容
					String sendmsg = SMSContentUtils.makeNotPassContent(se);
					this.ISMSSenderService.sendSms(ce.getApplyMobileNumber(), sendmsg, smsnumber);
				}
				
			}else{
				logger.info("无法查询到申请信息");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
