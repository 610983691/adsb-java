package com.coulee.aicw.foundations.utils.ldap;

/**
 * 
 * @author coulee
 *
 */
public class LDAPEnv {
	
    // 无论用什么LDAP服务器的固定写法，指定了JNDI服务提供者中工厂类
    private String factory="com.sun.jndi.ldap.LdapCtxFactory";

    // 服务连接地址
    private String url="ldap://192.168.0.23:389/";

    // 登录LDAP的用户名和密码
    private String adminUID="cn=Directory Manager";
    
    // 登录LDAP用户密码
    private String adminPWD="12345678";
    
    // 安全访问需要的证书库
    private String sslTrustStore;
    
    // 安全访问需要的证书库密码
    private String sslTrustStorePassword;

    // 安全通道访问
    private String securityProtocol;

    // 连接TimeOut
    private String timeOut = "3000";

    //是否使用连接池
    private boolean isPool = true;

    //根节点
    private String baseDn = "";

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdminUID() {
		return adminUID;
	}

	public void setAdminUID(String adminUID) {
		this.adminUID = adminUID;
	}

	public String getAdminPWD() {
		return adminPWD;
	}

	public void setAdminPWD(String adminPWD) {
		this.adminPWD = adminPWD;
	}

	public String getSslTrustStore() {
		return sslTrustStore;
	}

	public void setSslTrustStore(String sslTrustStore) {
		this.sslTrustStore = sslTrustStore;
	}

	public String getSslTrustStorePassword() {
		return sslTrustStorePassword;
	}

	public void setSslTrustStorePassword(String sslTrustStorePassword) {
		this.sslTrustStorePassword = sslTrustStorePassword;
	}

	public String getSecurityProtocol() {
		return securityProtocol;
	}

	public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isPool() {
		return isPool;
	}

	public void setPool(boolean isPool) {
		this.isPool = isPool;
	}

	public String getBaseDn() {
		return baseDn;
	}

	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}
    
}
