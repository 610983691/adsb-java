package com.coulee.aicw.foundations.utils.ldap;

import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.GreaterThanOrEqualsFilter;
import org.springframework.ldap.filter.LessThanOrEqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.NotPresentFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.filter.PresentFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;

/**
 * Description: 
 * <br>LDAP过滤条件生成工具<br>支持特殊字符过滤<br>
 * Author：LanChao
 * Create Date: 2017-3-16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public class LdapFilter {
	
	/**
	 * Definition: <br>= 如(sn=李四\2a)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter eq(String property, String value) {
		return new EqualsFilter(property, value);
	}
	
	/**
	 * Definition:<br><= 如(age<=10)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter leq(String property, String value) {
		return new LessThanOrEqualsFilter(property, value);
	}
	
	/**
	 * Definition:<br>>= 如(age>=10)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter geq(String property, String value) {
		return new GreaterThanOrEqualsFilter(property, value);
	}
	
	/**
	 * Definition:<br>like 如(sn=*张三*)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter like(String property, String value) {
		return new WhitespaceWildcardsFilter(property, value);
	}
	
	/**
	 * Definition:<br>left like 如(sn=*张三)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter llike(String property, String value) {
		return new LikeFilter(property, "*" + value);
	}
	
	/**
	 * Definition:<br>right like 如(sn=张三*)
	 * @param property
	 * @param value
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter rlike(String property, String value) {
		return new LikeFilter(property, value + "*");
	}
	
	/**
	 * Definition:包含某属性
	 * @param property
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter have(String property) {
		return new PresentFilter(property);
	}
	
	/**
	 * Definition:不包含某属性
	 * @param property
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter nohave(String property) {
		return new NotPresentFilter(property);
	}
	
	/**
	 * Definition:非
	 * @param filter
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter not(Filter filter) {
		return new NotFilter(filter);
	}
	
	/**
	 * Definition:并且
	 * @param filters
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter and(Filter... filters) {
		if (filters == null || filters.length == 0)
			return null;
		AndFilter and = new AndFilter();
		for (Filter filter : filters) {
			and.and(filter);
		}
		return and;
	}
	
	/**
	 * Definition:或者
	 * @param filters
	 * @return
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static Filter or(Filter... filters) {
		if (filters == null || filters.length == 0)
			return null;
		OrFilter or = new OrFilter();
		for (Filter filter : filters) {
			or.or(filter);
		}
		return or;
	}
	

	/**
	 * Definition:
	 * 
	 * @param args
	 * @Author: LanChao
	 * @Created date: 2017-3-16
	 */
	public static void main(String[] args) {
		Filter eq = LdapFilter.eq("objectclass", "person");
		System.out.println("等于	" + eq.encode());
		Filter like = LdapFilter.like("sn", "张三");
		System.out.println("模糊	" + like.encode());
		Filter geq = LdapFilter.geq("age", "20");
		System.out.println("大于等于	" + geq.encode());
		Filter not = LdapFilter.not(LdapFilter.eq("sex", "woman"));
		System.out.println("非	" + not.encode());
		Filter and = LdapFilter.and(eq, like, geq, not);
		System.out.println("并且	" + and.encode());
		Filter or = LdapFilter.or(eq, like, geq, not);
		System.out.println("或者	" + or.encode());
	}

}
