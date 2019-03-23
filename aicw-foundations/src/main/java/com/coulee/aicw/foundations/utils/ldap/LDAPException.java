package com.coulee.aicw.foundations.utils.ldap;

/**
 * LDAP
 * 异常处理
 * @author zyj
 *
 */
public class LDAPException extends Exception {
	
    private static final long serialVersionUID = 8520729784912471315L;

	public LDAPException(Throwable cause) {
        super(cause);
    }

    public LDAPException(String message) {
        super(message);
    }

    public LDAPException() {
        super();
    }
    
    public LDAPException(String message, Throwable cause) {
		super(message, cause);
	}
}
