package com.coulee.aicw.foundations.utils.ldap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPSearchConstraints;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.util.ConnectionPool;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.foundations.utils.page.PageUtils;

 

/**
 * Description: LDAP操作工具类
 * Author：LanChao
 * Create Date: 2015年5月22日
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LdapOperUtils {

	private static Logger log = LoggerFactory.getLogger(LdapOperUtils.class);

	private static ConnectionPool connPool;

	private static List<String> cryptPropList = new ArrayList<String>();
	
	static {
		cryptPropList.add("iamUserPassword");
		cryptPropList.add("adminPwd");
		cryptPropList.add("rootPwd"); 
		cryptPropList.add("conWebServicesUserPwd");
		cryptPropList.add("conDBAdminPwd");
		cryptPropList.add("conLDAPAdminPwd");
		cryptPropList.add("conLDAPCertificateStorePwd");
		cryptPropList.add("password");
		cryptPropList.add("historyPassword");
		cryptPropList.add("iamadpassword");
		cryptPropList.add("enablePwd");
		cryptPropList.add("loginPassword");
		cryptPropList.add("accPassword");
		cryptPropList.add("iamConLDAPAdminPwd");
		cryptPropList.add("oldPassword");
		cryptPropList.add("iamAppAdminUserPassword");
		cryptPropList.add("iamVirtualAdPassword");
	}
	
	
	private LdapOperUtils() {
	}

	/**
	 * Definition:LDAP初始化属性验证
	 * @param context
	 * @param dn
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	private static void checkArg(DirContext context, String dn) throws LDAPException {
		if (context == null) {
			log.error("Ldap context is null");
			throw new LDAPException("Ldap context is null");
		}
		if (StringUtils.isEmpty(dn)) {
			log.error("Ldap base dn is null");
			throw new LDAPException("Ldap base dn is null");
		}
	}

	/**
	 * Definition:检查LDAP属性MAP
	 * @param attMap
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	private static void checkAttrArg(Map attMap) throws LDAPException {
		if (attMap == null || attMap.isEmpty()) {
			log.error("Ldap parameter  is null");
			throw new LDAPException("Ldap parameter  is null");
		}
	}

	/**
	 * Definition:增加一个节点数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 参数Map
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void addContext(DirContext context, String dn, Map attMap) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = new BasicAttributes();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Attribute att = null;
			Object valueObj = attMap.get(key); 
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			} else if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List valueList = (List) valueObj;
				if(valueList!=null&&!valueList.isEmpty()){
					for (int i = 0; i < valueList.size(); i++) { 
						String _valueObj = (String) valueList.get(i);
						_valueObj = writeAttBefore(key, _valueObj);
						att.add(_valueObj);
					}
				}
			} else {
				att = new BasicAttribute(key, valueObj);
			}
			attrs.put(att);
		}
		try {
			context.createSubcontext(dn, attrs);
		} catch (NamingException ex) {
			 ex.printStackTrace();
			throw new LDAPException(ex);
		}
	}
	
	/**
	 * Definition:增加一个节点数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 参数Map
	 * @param isIgnoreCase 是否忽略大小写
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void addContext(DirContext context, String dn, Map attMap, String isIgnoreCase) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = null; 
		if("0".equals(isIgnoreCase)){
			attrs = new BasicAttributes(true);
		}
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Attribute att = null;
			Object valueObj = attMap.get(key);
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			} else if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					String _valueObj = (String) valueList.get(i);
					_valueObj = writeAttBefore(key, _valueObj);
					att.add(_valueObj);
				}
			} else {
				att = new BasicAttribute(key, valueObj);
			}
			attrs.put(att);
		}
		try {
			context.createSubcontext(dn, attrs);
		} catch (NamingException ex) {
			 ex.printStackTrace();
			throw new LDAPException(ex);
		}
	}
	
	/**
	 * Definition:删除节点
	 * @param context LDAP连接
	 * @param dn 要删除的节点DN
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void deleteContext(DirContext context, String dn) throws LDAPException {
		checkArg(context, dn);
		try {
			context.destroySubcontext(dn);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}

	/**
	 * Definition:递归删除某节点及其以下的数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param childIDAttrName
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	private static void deleteChildrenContext(DirContext context, String dn, String childIDAttrName) throws LDAPException {
		PageList pageList = searchContextOne(context, dn, null, new String[] { childIDAttrName }, null);
		if (pageList != null && pageList.size() > 0) {
			for (int i = 0; i < pageList.size(); i++) {
				Map attMap = (Map) pageList.get(i);
				if (attMap != null) {
					List childIDAttrNameList = (List) attMap.get(childIDAttrName);
					if (childIDAttrNameList != null && childIDAttrNameList.size() > 0) {
						String childIDAttrNameVal = (String) childIDAttrNameList.get(0);
						StringBuffer childDNBuf = new StringBuffer();
						childDNBuf.append(childIDAttrName).append("=").append(childIDAttrNameVal).append(",").append(dn);
						deleteChildrenContext(context, childDNBuf.toString(), childIDAttrName);
					}
				}
			}
		}
		LdapOperUtils.deleteContext(context, dn);
	}

	/**
	 * Definition: 递归删除某指定节点下的数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param childIDAttrName
	 * @param isDelDn 是否删除所指定的节点
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void deleteChildrenContext(DirContext context, String dn, String childIDAttrName, boolean isDelDn) throws LDAPException {
		if (StringUtils.isEmpty(childIDAttrName)) {
			childIDAttrName = "entrydn";
		}
		PageList pageList = searchContextOne(context, dn, null, new String[] { childIDAttrName }, null);
		if (pageList != null && pageList.size() > 0) {
			for (int i = 0; i < pageList.size(); i++) {
				Map attMap = (Map) pageList.get(i);
				if (attMap != null) {
					List childIDAttrNameList = (List) attMap.get(childIDAttrName);
					if (childIDAttrNameList != null && childIDAttrNameList.size() > 0) {
						String childIDAttrNameVal = (String) childIDAttrNameList.get(0);
						StringBuffer childDNBuf = new StringBuffer();
						childDNBuf.append(childIDAttrName).append("=").append(childIDAttrNameVal).append(",").append(dn);
						deleteChildrenContext(context, childDNBuf.toString(), childIDAttrName);
					}
				}
			}
		}
		if (isDelDn) {
			LdapOperUtils.deleteContext(context, dn);
		}
	}

	/**
	 * Definition:节点重命名
	 * @param context LDAP连接
	 * @param dn 旧节点
	 * @param newdn 新节点
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void reNameContext(DirContext context, String dn, String newdn) throws LDAPException {
		checkArg(context, dn);
		if (StringUtils.isEmpty(newdn)) {
			log.error("new dn is null");
			throw new LDAPException("new dn is null");
		}
		try {
			context.rename(dn, newdn);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}

	/**
	 * Definition:为指定节点增加属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 属性MAP
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void addAttributes(DirContext context, String dn, Map attMap) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = new BasicAttributes();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Attribute att = null;
			Object valueObj = attMap.get(key);
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			} else if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List<String> valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					String _valueObj = (String) valueList.get(i);
					_valueObj = writeAttBefore(key, _valueObj);
					att.add(_valueObj);
				}
			}
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.ADD_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}
	
	/**
	 * Definition:为指定节点增加属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 属性MAP
	 * @param isIgnoreCase 是否忽略大小写
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void addAttributes(DirContext context, String dn, Map attMap,String isIgnoreCase) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs  = null;
		if("0".equals(isIgnoreCase)){
			attrs = new BasicAttributes(true);//20110620 add ���Դ�Сд
		}
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Attribute att = null;
			Object valueObj = attMap.get(key);
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			} else if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List<String> valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					String _valueObj = (String) valueList.get(i);
					_valueObj = writeAttBefore(key, _valueObj);
					att.add(_valueObj);
				}
			}
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.ADD_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}
	
	/**
	 * Definition: 删除指定节点某些属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 属性MAP
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void deleteAttributes(DirContext context, String dn, Map attMap) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = new BasicAttributes();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Attribute att = null;
			Object valueObj = attMap.get(key);
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof String) {
				att = new BasicAttribute(key, valueObj);
			} else if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					att.add(valueList.get(i));
				}
			}
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.REMOVE_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}

	/**
	 * Definition:删除指定节点属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attList 属性集合
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void deleteAttributes(DirContext context, String dn, List attList) throws LDAPException {
		checkArg(context, dn);
		if (attList == null || attList.isEmpty()) {
			log.error("params is null");
			throw new LDAPException("params is null");
		}
		Attributes attrs = new BasicAttributes();
		for (int i = 0; i < attList.size(); i++) {
			Attribute att = null;
			att = new BasicAttribute((String) attList.get(i));
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.REMOVE_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}

	/**
	 * Definition:修改属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 属性MAP集合
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void modifyAttributes(DirContext context, String dn, Map attMap) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = new BasicAttributes(true);
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next(); 
			Attribute att = null;
			Object valueObj = attMap.get(key); 
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					if (!key.equals("photo")) {
						String _valueObj = (String) valueList.get(i);
						_valueObj = writeAttBefore(key, _valueObj); 
						att.add(_valueObj);
					} else {
						att.add(valueList.get(i));
					}
				}
			} else if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			}
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.REPLACE_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}
	
	/**
	* Definition:修改属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attMap 属性MAP集合
	 * @param isIgnoreCase 是否忽略大小写 0忽略
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void modifyAttributes(DirContext context, String dn, Map attMap,String isIgnoreCase) throws LDAPException {
		checkArg(context, dn);
		checkAttrArg(attMap);
		Set keySet = attMap.keySet();
		Iterator keyIterator = keySet.iterator();
		Attributes attrs = null;
		if("0".equals(isIgnoreCase)){
			attrs = new BasicAttributes(true);
		}
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next(); 
			Attribute att = null;
			Object valueObj = attMap.get(key); 
			if (valueObj == null) {
				continue;
			}
			if (valueObj instanceof List) {
				att = new BasicAttribute(key);
				List valueList = (List) valueObj;
				for (int i = 0; i < valueList.size(); i++) {
					if (!key.equals("photo")) {
						String _valueObj = (String) valueList.get(i);
						_valueObj = writeAttBefore(key, _valueObj); 
						att.add(_valueObj);
					} else {
						att.add(valueList.get(i));
					}
				}
			} else if (valueObj instanceof String) {
				String _valueObj = (String) valueObj;
				_valueObj = writeAttBefore(key, _valueObj);
				att = new BasicAttribute(key, _valueObj);
			}
			attrs.put(att);
		}
		try {
			context.modifyAttributes(dn, DirContext.REPLACE_ATTRIBUTE, attrs);
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}
	
	/**
	 * Definition:查询某些指定属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attNameList 要查询的属性集合
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static Map getAttributes(DirContext context, String dn, List attNameList) throws LDAPException {
		checkArg(context, dn);
		Map attsMap = new HashMap();
		Attributes results = null;
		List attValList = null;
		String attrId = null;
		try {
			if (attNameList == null) {
				results = context.getAttributes(dn);
			} else {
				if (!attNameList.isEmpty()) {
					String[] stTemp = new String[attNameList.size()];
					results = context.getAttributes(dn, (String[]) (attNameList.toArray(stTemp)));
				}
			}
			for (int i = 0; i < attNameList.size(); i++) {
				Attribute attr = results.get((String) attNameList.get(i));
				attrId = (String) attNameList.get(i);
				if (attr != null) {
					if (attr.size() > 0) {
						NamingEnumeration vals = attr.getAll();
						if (vals == null) {
							continue;
						}
						Object obj1 = vals.nextElement();
						if (obj1 == null) {
							continue;
						}
						while (vals.hasMoreElements()) {
							if (attValList == null) {
								attValList = new ArrayList();
								attValList.add(obj1);
							}
							attValList.add(vals.nextElement());
						}
						if (attValList != null) {
							attsMap.put(attrId, attValList);
							attValList = null;
						} else {
							attsMap.put(attrId, obj1);
						}
					}
				}
			}
			return attsMap;
		} catch (NamingException ex) {
			throw new LDAPException(ex);
		}
	}

	/**
	 * Definition: 查询指定属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param attName 指定属性名
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static List getAttributeValues(DirContext context, String dn, String attName) throws LDAPException {
		checkArg(context, dn);
		List attValList = new ArrayList();
		List attNameList = new ArrayList();
		attNameList.add(attName);
		Map attMap = null;
		attMap = getAttributes(context, dn, attNameList);
		if (attMap != null) {
			Object attValObj = attMap.get(attName);
			if (attValObj instanceof String) {
				attValList.add((String) attValObj);
			} else if (attValObj instanceof List) {
				attValList = ((List) attValObj);
			}
		}
		return attValList;
	}

	/**
	 * Definition:查询数据集合
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤条件
	 * @param returnedAtts 要查询返回的属性数组
	 * @param searchControl
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	private static PageList searchContext(DirContext context, String dn,
			String filter, String[] returnedAtts, int searchControl,
			PageArg pageArg) throws LDAPException {
		List resultList = new ArrayList();
		PageList pageList = null;
		LDAPConnection conn = null;
		LDAPSearchResults results = null;
		try {
			conn = getConnection(context);
			LDAPSearchConstraints constraints = new LDAPSearchConstraints(0, 0,	0, false, 0, null, 10);
			constraints.setMaxResults(0);
			if (filter != null && filter.trim().equals("")) {
				filter = null;
			}
			results = conn.search(dn, searchControl, filter, returnedAtts,false, constraints);
			int totalRow = results.getCount();
			int pos = 0;
			int pageSize = Integer.MAX_VALUE;
			if (pageArg != null) {
				pos = ((pageArg.getCurPage() <= 0 ? 1 : pageArg.getCurPage()) - 1) * pageArg.getPageSize();
			}
			int i = -1;
			while (results.hasMoreElements()) {
				i++;
				if(i < pos){
					results.nextElement();
					continue;
				}
				if(i >= pos + pageSize){
					break;
				}
				Object obj = results.nextElement();
				LDAPEntry entry = (LDAPEntry) obj;
				LDAPAttributeSet findAttrs = entry.getAttributeSet();
				Enumeration enumAttrs = findAttrs.getAttributes();
				Map resultRowMap = new HashMap();
				while (enumAttrs.hasMoreElements()) {
					LDAPAttribute anAttr = (LDAPAttribute) enumAttrs.nextElement();
					Enumeration enumVals = anAttr.getStringValues();
					ArrayList valList = new ArrayList();
					while (enumVals.hasMoreElements()) {
						Object objs = enumVals.nextElement();
						if (objs instanceof String) {
							String _value = (String) objs;
							_value = readAttAfter(anAttr.getName(), _value);
							valList.add(_value);
						} else {
							valList.add(obj);
						}
						resultRowMap.put(anAttr.getName(), valList);
					}
				}
				resultList.add(resultRowMap);
			}
			if (pageArg != null) {
				pageList = PageUtils.makeListPage(pageArg, resultList);
				pageList.setTotalRow(totalRow);
			} else {
				pageArg = new PageArg();
				pageArg.setCurPage(1);
				pageList = new PageList(resultList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return pageList;
	}
	
	
	/**
	 * Definition:查询某节点下所有层数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤器
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextSub(DirContext context, String dn, String filter, PageArg pageArg) throws LDAPException {
		return searchContext(context, dn, filter, null, SearchControls.SUBTREE_SCOPE, pageArg);
	}

	/**
	 * Definition:查询某节点下所有层数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤器
	 * @param returnedAtts 返回的参数
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextSub(DirContext context, String dn, String filter, String[] returnedAtts, PageArg pageArg)
			throws LDAPException {
		return searchContext(context, dn, filter, returnedAtts, SearchControls.SUBTREE_SCOPE, pageArg);
	}

	/**
	 * Definition:查询某节点下所一层数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤条件
	 * @param returnedAtts 返回的参数数组
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextOne(DirContext context, String dn, String filter, String[] returnedAtts, PageArg pageArg)
			throws LDAPException {
		return searchContext(context, dn, filter, returnedAtts, SearchControls.ONELEVEL_SCOPE, pageArg);
	}

	/**
	 * Definition:查询某节点下所一层数据
	  @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤条件
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextOne(DirContext context, String dn, String filter, PageArg pageArg) throws LDAPException {
		return searchContext(context, dn, filter, null, SearchControls.ONELEVEL_SCOPE, pageArg);
	}

	/**
	 * Definition:查询某一指定节点属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter 过滤条件
	 * @param returnedAtts 返回属性
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextDn(DirContext context, String dn, String filter, String[] returnedAtts, PageArg pageArg)
			throws LDAPException {
		return searchContext(context, dn, filter, returnedAtts, SearchControls.OBJECT_SCOPE, pageArg);
	}

	/**
	 * Definition:查询某一指定节点属性
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param filter pageArg
	 * @param pageArg 分页参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static PageList searchContextDn(DirContext context, String dn, String filter, PageArg pageArg) throws LDAPException {
		return searchContext(context, dn, filter, null, SearchControls.OBJECT_SCOPE, pageArg);
	}
	
	/**
	 * Definition:查询节点数据
	 * @param context LDAP连接
	 * @param dn 节点DN
	 * @param returnedAtts 返回的属性数组
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static List searchContextSelf(DirContext context, String dn,
			String[] returnedAtts) throws LDAPException {
		return searchContext(context, dn, null, returnedAtts, SearchControls.OBJECT_SCOPE,null);
	}

	/**
	 * Definition:获取LDAP连接
	 * @param env LDAP连接参数
	 * @return
	 * @throws LDAPException
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	public static DirContext getDirContext(LDAPEnv env) throws LDAPException {
		Properties mEnv = new Properties();
		mEnv.put(Context.AUTHORITATIVE, "true");
		if (env.isPool()) {
			mEnv.put("com.sun.jndi.ldap.connect.pool", "true");
		}
		mEnv.put(Context.INITIAL_CONTEXT_FACTORY, env.getFactory());
		mEnv.put(Context.PROVIDER_URL, env.getUrl());
		mEnv.put("com.sun.jndi.ldap.connect.timeout", env.getTimeOut());
		mEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		if (env != null && "ssl".equals(env.getSecurityProtocol())) {
			mEnv.put(Context.SECURITY_PROTOCOL, env.getSecurityProtocol());
			System.setProperty("javax.net.ssl.trustStore", env.getSslTrustStore());
		}
		mEnv.put(Context.SECURITY_PRINCIPAL, env.getAdminUID());
		mEnv.put(Context.SECURITY_CREDENTIALS, env.getAdminPWD());
		mEnv.put(Context.REFERRAL, "follow");
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(mEnv);
		} catch (NamingException ex) {
			close(ctx);
			throw new LDAPException("open ldap connection exception ", ex);
		}
		return ctx;

	}

	/**
	 * Definition:关闭LDAP连接
	 * @param dirContext LDAP连接
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	public static void close(DirContext dirContext) {
		try {
			if (dirContext != null) 
				dirContext.close();
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Definition:判断属性是否为加解密属性
	 * @param attName
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年5月22日
	 */
	@SuppressWarnings("unused")
	private static boolean isCryptAtt(String attName) {
		return cryptPropList.contains(attName);
	}

	/**
	 * Definition:读取属性后对属性值进行特殊处理
	 * @param attName
	 * @param attValue
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	private static String readAttAfter(String attName, String attValue) {
		return attValue;
	}

	/**
	 * Definition:写入属性前对属性值进行特殊处理
	 * @param attName
	 * @param attValue
	 * @return
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	private static String writeAttBefore(String attName, String attValue) {
		return attValue;
	}
	
	/**
	 * Definition:根据javax.naming.directory.DirContext对象获得netscape.ldap.LDAPConnection
	 * @param context
	 * @return
	 * @throws NamingException
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	private static LDAPConnection getConnection(DirContext context) throws NamingException {
		if (context == null)
			return null;
		Hashtable ht = context.getEnvironment();
		String url = (String) ht.get(Context.PROVIDER_URL);
		Matcher matcher = Pattern.compile("ldap://(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)").matcher(url);
		if (matcher.find()) {
			String ip = matcher.group(1);
			String port = matcher.group(2);
			try {
				connPool = new ConnectionPool(1, 1, ip, Integer.parseInt(port));
				LDAPConnection conn = connPool.getConnection();
				conn.authenticate((String)ht.get(Context.SECURITY_PRINCIPAL), (String)ht.get(Context.SECURITY_CREDENTIALS));
				return conn;
			} catch (netscape.ldap.LDAPException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Definition:关闭LDAP连接
	 * @param con
	 * @Author: LanChao
	 * @Created date: 2015年5月23日
	 */
	private static void close(LDAPConnection con) {
		if (connPool != null)
			connPool.close(con);
		connPool.destroy();
	}

	
	public static void main(String[] args) {
		try {
			LDAPEnv env = new LDAPEnv();
			env.setUrl("ldap://192.168.1.212:12345/");
			env.setAdminUID("cn=Directory Manager");
			env.setAdminPWD("12345678");
			String baseDn = "dc=boco,dc=cmcc,dc=com";
			String filter = "iamUserIsAdmin=0";
			DirContext con = getDirContext(env);
			PageArg pageArg = PageUtils.getPageArg("0", "10");
			PageList pl = searchContextOne(con, "ou=users," + baseDn, filter, pageArg);
			System.out.println(pl.getTotalRow());
			System.out.println(pl.getTotalPage());
			System.out.println(pl.size());
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}
	
}
