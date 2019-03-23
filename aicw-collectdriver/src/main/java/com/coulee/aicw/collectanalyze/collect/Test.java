package com.coulee.aicw.collectanalyze.collect;

import java.util.ArrayList;
import java.util.List;

import com.coulee.aicw.collectanalyze.collect.common.CollectConstants;
import com.coulee.aicw.collectanalyze.collect.common.ErrorCode;
import com.coulee.aicw.collectanalyze.collect.driver.Driver;
import com.coulee.aicw.collectanalyze.collect.driver.NetdeviceDriver;
import com.coulee.aicw.collectanalyze.collect.driver.UnixHostDriver;
import com.coulee.aicw.collectanalyze.collect.driver.WinHostDriver;
import com.coulee.aicw.collectanalyze.collect.exception.DriverException;
import com.coulee.aicw.collectanalyze.collect.valueobject.CommandArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ConnectionArg;
import com.coulee.aicw.collectanalyze.collect.valueobject.ExecuteResult;

public class Test {
	
	public static Driver testLogin() { 
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("LINUX", "id") //设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setOpenArg("192.168.1.16", 22, "SSH2") //连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
				.setEncode("UTF-8") //设备编码，默认为UTF-8
				.setTimeout(10, 40) //设备超时时间，分别为连接超时、读取回显超时，单位为秒
				.setLoginArg("lanchao", "123456", "$") //设备登录参数，分别为用户名、密码、提示符
				.setAdminLoginArg("root", "123456", "#") //切换特权参数，分别为特权用户名（如部分网络设备无则传空）、特权密码、特权提示符，如LoginArg处封装的即为特权信息，则不需封装AdminLoginArg信息，如windows则无此设置
				.setDatabaseOpenArg("192.168.1.16", 1521, "oracle.jdbc.driver.OracleDriver", "orcl") //数据库连接参数，分别为数据库IP、端口、驱动类、数据库名
				.setDatabaseLoginArg("vuln", "1234567890") //数据库登录参数，分别为数据库用户名、密码
				.setDatabaseSpecialArg("UTF-8", "ifxserver") //特殊数据库配置，分别为数据库编码（默认为UTF-8）、informix数据库servername
				.setUnixSudoMode(true);
		try {
			String switchCommand = "su -";
			String moreExecuter = " ";
			String morePrompt = "--More--";
			conArg.setDriverClass(UnixHostDriver.class.getName());
			CollectionUtils utils = CollectionUtils.instance();
			return utils.login(conArg, switchCommand, moreExecuter, morePrompt);
		} catch (DriverException e) {
			e.printStackTrace();
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
		return null;
	}
	
	public static void testExecuteCommonScript() {
		Driver driver = testLogin();
		String cmd = "ls\r\npwd\r\ncat /etc/passwd\r\ncat /etc/shadow\r\n";
		CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_COMMON, cmd);
		ExecuteResult result;
		try {
			result = driver.executeCmd(commandArg);
			System.out.println(result.getResult());
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	public static void testExecuteShellScript(){
		Driver driver = testLogin();
		String cmd = "#!/bin/bash\n" + 
				"FTPSTATUS=`netstat -antp|grep -i \"listen\"|grep \":21\\>\"|wc -l`\n" + 
				"function Check_vsftpd\n" + 
				"{\n" + 
				"if [ -f /etc/vsftpd.conf ];\n" + 
				"    then FTPCONF=\"/etc/vsftpd.conf\";\n" + 
				"        elif [ -f /etc/vsftpd/vsftpd.conf ];\n" + 
				"        then FTPCONF=\"/etc/vsftpd/vsftpd.conf\";\n" + 
				"fi;\n" + 
				"echo +++++++++vsftpd.conf+++++++++++++++++\n" + 
				"cat $FTPCONF|egrep -v \"^#|^$\";\n" + 
				"echo +++++++++vsftpd.conf end+++++++++++++\n" + 
				"ls_recurse_enable=`cat $FTPCONF|grep -i \"ls_recurse_enable=YES\"|wc -l`;\n" + 
				"local_umask=`cat $FTPCONF|grep -i \"local_umask=022\"|wc -l`;\n" + 
				"anon_umask=`cat $FTPCONF|grep -i \"anon_umask=022\"|wc -l`;\n" + 
				"VSFTPD_NO=$(expr $ls_recurse_enable + $local_umask + $anon_umask);\n" + 
				"if [ $VSFTPD_NO -eq 3 ];\n" + 
				"   then  echo \"vsftpd is running.$FTPCONF is recommended.FTP check result:true.\";\n" + 
				"   else  echo \"vsftpd is running.$FTPCONF is not recommended.FTP check result:false.\";\n" + 
				"fi;\n" + 
				"unset FTPCONF VSFTPD_NO ls_recurse_enable local_umask anon_umask;\n" + 
				"}\n" + 
				"function Check_pureftpd\n" + 
				"{\n" + 
				"echo +++++++++pure-ftpd.conf+++++++++++++++++;\n" + 
				"cat /etc/pure-ftpd/pure-ftpd.conf|egrep -v \"^#|^$\";\n" + 
				"echo +++++++++pure-ftpd.conf end+++++++++++++;\n" + 
				"if [ `cat /etc/pure-ftpd/pure-ftpd.conf|grep -v \"^#\"|grep -i \"Umask\"|grep -i \"177:077\"|wc -l` -eq 1 ];\n" + 
				"   then echo \"pure-ftpd is running.pure-ftpd.conf is recommended.FTP check result:true.\";\n" + 
				"   else echo \"pure-ftpd is running.pure-ftpd.conf is not recommended.FTP check result:false.\";\n" + 
				"fi;\n" + 
				"}\n" + 
				"if [ $FTPSTATUS = 0 ];\n" + 
				"     then  echo \"FTP is not running.FTP check result:true.\";\n" + 
				"     else  if ([ `ps -ef|grep vsftpd|grep -v \"grep\"|wc -l` -ne 0 ] || [ `chkconfig --list |grep vsftp|grep -v \"0:\"|grep -w on|wc -l` -ne 0 ]);\n" + 
				"              then Check_vsftpd;\n" + 
				"              else Check_pureftpd;\n" + 
				"           fi;\n" + 
				"fi\n" + 
				"unset FTPSTATUS;";
		CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_SHELL, cmd);
		ExecuteResult result;
		try {
			result = driver.executeCmd(commandArg);
			System.out.println(result.getResult());
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	
	public static void testJumpLogin() {
		//目标机
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("LINUX", "id")
				.setOpenArg("192.168.1.16", 22, "SSH2")
				.setEncode("UTF-8")
				.setTimeout(10, 40)
				.setLoginArg("lanchao", "123456", "$")
				.setAdminLoginArg("root", "123456", "#");
		conArg.setDriverClass(UnixHostDriver.class.getName());
		//跳转机1
		ConnectionArg jumpArg1 = new ConnectionArg().setDeviceInfo("LINUX", "id")
				.setOpenArg("192.168.1.10", 22, "SSH2")
				.setEncode("UTF-8")
				.setTimeout(10, 40)
				.setLoginArg("root", "aicw_720", "#");
		jumpArg1.setDriverClass(UnixHostDriver.class.getName());
		//跳转机2
//		ConnectionArg jumpArg2 = new ConnectionArg().setDeviceInfo("LINUX", "id")
//				.setOpenArg("192.168.1.21", 22, "SSH2")
//				.setEncode("UTF-8")
//				.setTimeout(10, 40)
//				.setLoginArg("lanchao", "123456", "$");
//		jumpArg2.setDriverClass(UnixHostDriver.class.getName());
		
		List<ConnectionArg> jumps = new ArrayList<ConnectionArg>();
		jumps.add(jumpArg1);
//		jumps.add(jumpArg2);
		conArg.setJumps(jumps);
		
		try {
			String switchCommand = "su -";
			String moreExecuter = " ";
			String morePrompt = "--More--";
			CollectionUtils utils = CollectionUtils.instance();
			Driver driver = utils.login(conArg, switchCommand, moreExecuter, morePrompt);
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	
	public static void testJumpLoginWindows() {
		//目标机
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("LINUX", "id")
				.setOpenArg("192.168.1.16", 22, "SSH2")
				.setEncode("UTF-8")
				.setTimeout(10, 40)
				.setLoginArg("lanchao", "123456", "$")
				.setAdminLoginArg("root", "123456", "#");
		conArg.setDriverClass(UnixHostDriver.class.getName());
		
		//跳转机1
		ConnectionArg jumpArg1 = new ConnectionArg().setDeviceInfo("LINUX", "id")
				.setOpenArg("192.168.1.21", 22, "SSH2")
				.setEncode("UTF-8")
				.setTimeout(10, 40)
				.setLoginArg("lanchao", "123456", "$");
		jumpArg1.setDriverClass(UnixHostDriver.class.getName());
		
		//跳转机2
		ConnectionArg jumpArg2 = new ConnectionArg().setDeviceInfo("WINDOWS", "id") //设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setOpenArg("192.168.1.123", 22, "WIN_SSH2") //连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
				.setEncode("GBK") //设备编码，默认为GBK
				.setTimeout(10, 10) //设备超时时间，分别为连接超时、读取回显超时，单位为秒
				.setLoginArg("Administrator", "123456", ">"); //设备登录参数，分别为用户名、密码、提示符
		jumpArg2.setDriverClass(WinHostDriver.class.getName());
		
		List<ConnectionArg> jumps = new ArrayList<ConnectionArg>();
		jumps.add(jumpArg1);
		jumps.add(jumpArg2);
		conArg.setJumps(jumps);
		
		try {
			String switchCommand = "su -";
			String moreExecuter = " ";
			String morePrompt = "--More--";
			CollectionUtils utils = CollectionUtils.instance();
			Driver driver = utils.login(conArg, switchCommand, moreExecuter, morePrompt);
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
			System.out.println("solution : " + ErrorCode.solution(e.getMessage()));
		}
	}
	
	public static Driver testLoginWindows() {
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("WINDOWS", "id") //设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setOpenArg("192.168.1.108", 23, "TELNET") //连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
				.setEncode("GBK") //设备编码，默认为GBK
				.setTimeout(30, 60) //设备超时时间，分别为连接超时、读取回显超时，单位为秒
				.setLoginArg("Administrator", "Ab123456", ">"); //设备登录参数，分别为用户名、密码、提示符
		try {
			String switchCommand = "";
			String moreExecuter = "";
			String morePrompt = "";
			conArg.setDriverClass(WinHostDriver.class.getName());
			CollectionUtils utils = CollectionUtils.instance();
			return utils.login(conArg, switchCommand, moreExecuter, morePrompt);
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
		return null;
	}
	
	
	public static void testExecuteWindowsCmd() {
		Driver driver = testLoginWindows();
		ExecuteResult result;
		try {
			result = driver.executeCmd(new CommandArg(CollectConstants.SCRIPT_COMMON, "ipconfig"));
			result = driver.executeCmd(new CommandArg(CollectConstants.SCRIPT_COMMON, "wmic nteventlog get Caption,FileSize,MaxFileSize,OverWritePolicy"));
			result = driver.executeCmd(new CommandArg(CollectConstants.SCRIPT_COMMON, "secedit /export /cfg sec.inf /quiet\r\ntype sec.inf\r\ndel sec.inf"));
			result = driver.executeCmd(new CommandArg(CollectConstants.SCRIPT_COMMON, "cd /d c:\\\r\ndir\r\n"));
			System.out.println(result.getResult());
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	
	public static void testLoginNetdevice() {
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("HUAWEI", "id") //设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setOpenArg("192.168.1.16", 22, "SSH2") //连接信息，分别为IP、端口、协议（SSH1、SSH2、WIN_SSH2、TELNET、JDBC）
				.setEncode("UTF-8") //设备编码
				.setTimeout(10, 10) //设备超时时间，分别为连接超时、读取回显超时，单位为秒
				.setLoginArg("lanchao", "123456", "$") //设备登录参数，分别为用户名、密码、提示符
				.setAdminLoginArg("", "123456", "#");
		try {
			String switchCommand = "su -";
			String moreExecuter = "";
			String morePrompt = "";
			conArg.setDriverClass(NetdeviceDriver.class.getName());
			CollectionUtils utils = CollectionUtils.instance();
			utils.login(conArg, switchCommand, moreExecuter, morePrompt);
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	
	public static Driver testLoginDatabase() {
		ConnectionArg conArg = new ConnectionArg().setDeviceInfo("ORACLE", "id") //设备信息，LINUX为调用侧设备类型字典值，id为该设备的ID
				.setDatabaseLoginArg("root", "123456")
				.setDatabaseOpenArg("192.168.1.125", 3306, "com.mysql.jdbc.Driver", "mysql");
		CollectionUtils utils = CollectionUtils.instance();
		try {
			return utils.loginDB(conArg);
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
		return null;
	}
	
	public static void testExecuteSql() {
		Driver driver = testLoginDatabase();
		String sql = "show variables like 'version';\r\n"
				+"select user,password from mysql.user;\r\n"
				+"show variables like 'ssl_key';";
		CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_SQL, sql);
		ExecuteResult result;
		try {
			result = driver.executeCmd(commandArg);
			System.out.println(result.getResult());
			System.out.println(result.getSelectSqlResult());
			driver.logout();
		} catch (DriverException e) {
			System.out.println("error msg : " + ErrorCode.msg(e.getMessage()));
		}
	}
	
	public static void testExecuteInteract() {
		Driver driver = testLogin();
		CommandArg commandArg = new CommandArg(CollectConstants.SCRIPT_INTERACT);
		commandArg.setInteractiveType(CollectConstants.SCRIPT_COMMON);
		commandArg.addInteractiveCmd("useradd -s {0} {1}", CollectConstants.DEFAULT_PROMPT, "already exists|已存在");
		commandArg.addInteractiveCmd("passwd {1}", ":|：", null);
		commandArg.addInteractiveCmd("{2}", ":|：", null);
		commandArg.addInteractiveCmd("{2}", CollectConstants.DEFAULT_PROMPT, null);
		commandArg.addInteractiveCmd("echo $?", CollectConstants.DEFAULT_PROMPT, null);
		commandArg.addCmdArgs("/bin/bash", "lantest", "123456");
		try {
			ExecuteResult result = driver.executeCmd(commandArg);
			System.out.println("=================================" + result.getResult());
		} catch (DriverException e) {
			e.printStackTrace();
			System.out.println(ErrorCode.msg(e.getMessage()));
		} finally {
			driver.logout();
		}
		driver.logout();
	}

	public static void main(String[] args) {
//		testExecuteShellScript();
		testJumpLogin();
	}
	
	
}
