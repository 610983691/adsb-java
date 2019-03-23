package com.coulee.aicw.foundations.utils.mybatis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.dao.IBaseDao;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.service.AbstractBaseService;
import com.coulee.aicw.foundations.service.IBaseService;
import com.coulee.aicw.foundations.utils.page.PageArg;

/**
 * Description: MyBatis Generator 代码生成自定义插件<br>
 * Create Date: 2018年10月23日<br>
 * Copyright (C) 2018 Coulee All Right Reserved.<br>
 * @author oblivion
 * @version 1.0
 */
public class MyGeneratorPlugin extends PluginAdapter {
	
	// 是否生成service、controller
	private boolean generateServiceAndController;
	// 生成位置
	private String targetProject;
	// service接口包路径
	private String targetServicePackage;
	// service接口实现类包路径
	private String targetServiceImplPackage;
	// controller包路径
	private String targetControllerPackage;
	// entity文件路径
	private String entityFullPackage = "";
	// dao文件路径
	private String daoFullPackage = "";

	@Override
	public boolean validate(List<String> warnings) {
		generateServiceAndController = "true".equals(properties.getProperty("generateServiceAndController"));
		targetProject = properties.getProperty("targetProject");
		targetServicePackage = properties.getProperty("targetServicePackage");
		targetServiceImplPackage = properties.getProperty("targetServiceImplPackage");
		targetControllerPackage = properties.getProperty("targetControllerPackage");
		return true;
	}

	/**
	 * DTO实体类加入serialVersionUID属性
	 */
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		Field field = new Field();
		field.setFinal(true);
		field.setInitializationString("1L");
		field.setName("serialVersionUID");
		field.setStatic(true);
		field.setType(new FullyQualifiedJavaType("long"));
		field.setVisibility(JavaVisibility.PRIVATE);
		context.getCommentGenerator().addFieldComment(field, introspectedTable);
		topLevelClass.addField(field);
		topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModel;");
		topLevelClass.addJavaDocLine("import io.swagger.annotations.ApiModelProperty;\r\n");
		String entityName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
		topLevelClass.addJavaDocLine("@ApiModel(value =\"" + entityName + "\")");
		return true;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽DAO中没用的方法
	 */
	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽MapperXML文件中没用的配置
	 */
	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽MapperXML文件中没用的配置
	 */
	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 屏蔽MapperXML文件中没用的配置
	 */
	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	/**
	 * 修改MapperXML文件中方法名 加入findByParams方法配置
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement rootEle = document.getRootElement();
		this.modifyMethodName(rootEle);
		this.addFindByParamsMethod(rootEle, introspectedTable);
		this.addCountByParamsMethod(rootEle, introspectedTable);
		this.addFindByIdsMethod(rootEle, introspectedTable);
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}

	/**
	 * Description: 加入按ID集合查询方法<br>
	 * Created date: 2018年3月7日
	 * 
	 * @param rootEle
	 * @param introspectedTable
	 * @author oblivion
	 */
	private void addFindByIdsMethod(XmlElement rootEle, IntrospectedTable introspectedTable) {
		XmlElement selEle = new XmlElement("select");
		// 构造select标签的属性部分
		selEle.addAttribute(new Attribute("id", "findByIds"));
		selEle.addAttribute(new Attribute("parameterType", "java.util.List"));
		if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
			selEle.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId()));
		} else {
			selEle.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
		}
		// 构造SQL查询部分
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		selEle.addElement(new TextElement(sb.toString()));
		XmlElement baseColEle = new XmlElement("include");
		baseColEle.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
		selEle.addElement(baseColEle);
		if (introspectedTable.hasBLOBColumns()) {
			selEle.addElement(new TextElement(","));
			XmlElement blobColEle = new XmlElement("include");
			blobColEle.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId()));
			selEle.addElement(blobColEle);
		}
		sb.setLength(0);
		sb.append("from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		sb.append(" where ");
		IntrospectedColumn primaryKeyColumn = introspectedTable.getPrimaryKeyColumns().get(0);
		sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumn));
		sb.append(" in ");
		selEle.addElement(new TextElement(sb.toString()));
		// 构造foreach标签
		XmlElement foreachEle = new XmlElement("foreach");
		foreachEle.addAttribute(new Attribute("collection", "ids"));
		foreachEle.addAttribute(new Attribute("item", "item"));
		foreachEle.addAttribute(new Attribute("index", "index"));
		foreachEle.addAttribute(new Attribute("open", "("));
		foreachEle.addAttribute(new Attribute("close", ")"));
		foreachEle.addAttribute(new Attribute("separator", ","));
		foreachEle.addElement(new TextElement("#{item}"));
		selEle.addElement(foreachEle);
		rootEle.addElement(selEle);
	}

	/**
	 * Description: 加入按条件查询条数方法<br>
	 * Created date: 2018年3月6日
	 * 
	 * @param rootEle
	 * @param introspectedTable
	 * @author oblivion
	 */
	private void addCountByParamsMethod(XmlElement rootEle, IntrospectedTable introspectedTable) {
		XmlElement selEle = new XmlElement("select");
		// 构造select标签的属性部分
		selEle.addAttribute(new Attribute("id", "countByParams"));
		selEle.addAttribute(new Attribute("parameterType", "java.util.Map"));
		selEle.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		// 构造SQL查询部分
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		selEle.addElement(new TextElement(sb.toString()));
		// 构造SQL查询条件部分
		XmlElement whereEle = new XmlElement("where");
		XmlElement isNOtNullParamsEle = new XmlElement("if");
		isNOtNullParamsEle.addAttribute(new Attribute("test", "params != null"));
		whereEle.addElement(isNOtNullParamsEle);
		selEle.addElement(whereEle);
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			this.makeWhereCondition(sb, introspectedColumn, isNOtNullParamsEle);
		}
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			this.makeWhereCondition(sb, introspectedColumn, isNOtNullParamsEle);
		}
		rootEle.addElement(selEle);
	}

	/**
	 * Definition:加入条件查询的方法
	 * 
	 * @param rootEle
	 * @param introspectedTable
	 * @Author: oblivion
	 * @Created date: 2016年1月18日
	 */
	private void addFindByParamsMethod(XmlElement rootEle, IntrospectedTable introspectedTable) {
		XmlElement selEle = new XmlElement("select");
		// 构造select标签的属性部分
		selEle.addAttribute(new Attribute("id", "findByParams"));
		selEle.addAttribute(new Attribute("parameterType", "java.util.Map"));
		if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
			selEle.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId()));
		} else {
			selEle.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
		}
		// 构造SQL查询部分
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		selEle.addElement(new TextElement(sb.toString()));
		XmlElement baseColEle = new XmlElement("include");
		baseColEle.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
		selEle.addElement(baseColEle);
		if (introspectedTable.hasBLOBColumns()) {
			selEle.addElement(new TextElement(","));
			XmlElement blobColEle = new XmlElement("include");
			blobColEle.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId()));
			selEle.addElement(blobColEle);
		}
		sb.setLength(0);
		sb.append("from ");
		sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		selEle.addElement(new TextElement(sb.toString()));
		// 构造SQL查询条件部分
		XmlElement whereEle = new XmlElement("where");
		XmlElement isNOtNullParamsEle = new XmlElement("if");
		isNOtNullParamsEle.addAttribute(new Attribute("test", "params != null"));
		whereEle.addElement(isNOtNullParamsEle);
		selEle.addElement(whereEle);
		for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
			this.makeWhereCondition(sb, introspectedColumn, isNOtNullParamsEle);
		}
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			this.makeWhereCondition(sb, introspectedColumn, isNOtNullParamsEle);
		}
		sb.setLength(0);
		sb.append("order by ");
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)).append(", ");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("desc");
		selEle.addElement(new TextElement(sb.toString()));
		rootEle.addElement(selEle);
	}

	/**
	 * Definition:构造where中具体条件部分
	 * 
	 * @param sb
	 * @param introspectedColumn
	 * @param ele
	 * @Author: oblivion
	 * @Created date: 2016年1月19日
	 */
	private void makeWhereCondition(StringBuilder sb, IntrospectedColumn introspectedColumn, XmlElement ele) {
		XmlElement isNotNullElement = new XmlElement("if");
		sb.setLength(0);
		sb.append("params.").append(introspectedColumn.getJavaProperty());
		sb.append(" != null");
		if ("String".equals(introspectedColumn.getFullyQualifiedJavaType().getShortName())) {
			sb.append(" and params.");
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" !=''");
		}
		isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
		sb.setLength(0);
		sb.append("and ");
		sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
		sb.append(" = ");
		sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "params."));
		isNotNullElement.addElement(new TextElement(sb.toString()));
		ele.addElement(isNotNullElement);
	}

	/**
	 * Definition:修改配置文件内已有的方法名称
	 * 
	 * @param rootEle
	 * @Author: oblivion
	 * @Created date: 2016年1月18日
	 */
	private void modifyMethodName(XmlElement rootEle) {
		List<Element> eles = rootEle.getElements();
		for (Element ele : eles) {
			XmlElement xmlEle = (XmlElement) ele;
			String name = xmlEle.getName();
			if ("insert".equals(name)) {
				this.replaceEleAttVal(xmlEle, "id", "add");
			} else if ("update".equals(name)) {
				this.replaceEleAttVal(xmlEle, "id", "update");
			} else if ("select".equals(name)) {
				this.replaceEleAttVal(xmlEle, "id", "findById");
			} else if ("delete".equals(name)) {
				this.replaceEleAttVal(xmlEle, "id", "delete");
			}
		}
	}

	/**
	 * Definition:将已有的属性值替换为新的属性值
	 * 
	 * @param xmlEle
	 *            xml节点
	 * @param attName
	 *            属性
	 * @param newAttVal
	 *            新值
	 * @Author: oblivion
	 * @Created date: 2016年1月18日
	 */
	private void replaceEleAttVal(XmlElement xmlEle, String attName, String newAttVal) {
		List<Attribute> atts = xmlEle.getAttributes();
		if (atts == null || atts.isEmpty()) {
			return;
		}
		for (int i = 0; i < atts.size(); i++) {
			Attribute att = atts.get(i);
			if (attName.equals(att.getName())) {
				atts.remove(i);
				atts.add(i, new Attribute(attName, newAttVal));
			}
		}
	}

	/**
	 * 生成controller、service类
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		if (!generateServiceAndController) {
			//如果未配置生成controller和service，则直接生成dao相关文件
			return null;
		}
		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
		for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
			//取出dao和entity的包路径
			FullyQualifiedJavaType baseModelJavaType = javaFile.getCompilationUnit().getType();
			String fullName = baseModelJavaType.getFullyQualifiedNameWithoutTypeParameters();
			if (fullName.endsWith("Entity")) {
				this.entityFullPackage = fullName;
			} else if (fullName.endsWith("Mapper")) {
				this.daoFullPackage = fullName;
			}
		}
		JavaFormatter javaFormatter = context.getJavaFormatter();
		TopLevelClass controller = generateController(introspectedTable);
		Interface iservice = generateService(introspectedTable);
		TopLevelClass serviceImpl = generateServiceImpl(introspectedTable);
		// Controller实现类
		GeneratedJavaFile mapperJavafileController = new GeneratedJavaFile(controller, targetProject, "UTF-8",
				javaFormatter);
		mapperJavaFiles.add(mapperJavafileController);

		// Service接口
		GeneratedJavaFile mapperJavafileServiceInteface = new GeneratedJavaFile(iservice, targetProject, "UTF-8",
				javaFormatter);
		mapperJavaFiles.add(mapperJavafileServiceInteface);

		// Service实现类
		GeneratedJavaFile mapperJavafileServiceClass = new GeneratedJavaFile(serviceImpl, targetProject, "UTF-8",
				javaFormatter);
		mapperJavaFiles.add(mapperJavafileServiceClass);
		return mapperJavaFiles;
	}

	/**
	 * Description: 生成Controller
	 *
	 * @return
	 * @author LiuJianli
	 * @date 2018/7/5
	 */
	private TopLevelClass generateController(IntrospectedTable table) {

		String domainObjectName = table.getFullyQualifiedTable().getDomainObjectName();

		String service = targetControllerPackage + "." + domainObjectName + "Controller";

		TopLevelClass clazz = new TopLevelClass(new FullyQualifiedJavaType(service));

		clazz.addImportedType(new FullyQualifiedJavaType(service));

		clazz.addImportedType(
				new FullyQualifiedJavaType(BaseController.class.getName()));
		clazz.addImportedType(new FullyQualifiedJavaType(PageEntity.class.getName()));
		clazz.addImportedType(new FullyQualifiedJavaType(PageArg.class.getName()));
		clazz.addImportedType(new FullyQualifiedJavaType(Message.class.getName()));

		clazz.addImportedType(new FullyQualifiedJavaType(entityFullPackage));
		clazz.addImportedType(new FullyQualifiedJavaType(targetServicePackage + ".I" + domainObjectName + "Service"));

		clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.Logger"));

		clazz.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.Api"));
		clazz.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiOperation"));

		clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.LoggerFactory"));

		clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
		clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated"));

		clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.*"));

		clazz.addImportedType(new FullyQualifiedJavaType("java.util.List"));

		clazz.addAnnotation("@RestController");
		clazz.addAnnotation("@RequestMapping({\"/" + domainObjectName.toLowerCase() + "\"})");
		clazz.addAnnotation("@Api(tags = \"" + domainObjectName + " API\")");

		clazz.setVisibility(JavaVisibility.PUBLIC);
		clazz.setSuperClass(new FullyQualifiedJavaType("BaseController"));

		// 增加 logger 属性
		Field logger = new Field();
		logger.setName("logger");
		logger.setType(new FullyQualifiedJavaType("Logger"));
		logger.setVisibility(JavaVisibility.PRIVATE);
		logger.setInitializationString("LoggerFactory.getLogger(this.getClass())");
		clazz.addField(logger);

		String serviceImpl = domainObjectName.toLowerCase() + "Service";
		// 增加 接口 属性
		Field field = new Field();
		field.addAnnotation("@Autowired");
		field.setName(serviceImpl);
		field.setType(new FullyQualifiedJavaType("I" + domainObjectName + "Service"));
		field.setVisibility(JavaVisibility.PRIVATE);
		clazz.addField(field);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd");
		String dateTime = sdf.format(new Date());

		// 查询方法
		Method queryMethod = new Method();
		queryMethod.setVisibility(JavaVisibility.PUBLIC);

		queryMethod.addJavaDocLine("/**");
		queryMethod.addJavaDocLine(" * Description: 列表查询 <br>");
		queryMethod.addJavaDocLine(" * Created date: " + dateTime);
		queryMethod.addJavaDocLine(" * @param entity " + domainObjectName + " 对象 ");
		queryMethod.addJavaDocLine(" * @return: PageEntity 分页对象 ");
		queryMethod.addJavaDocLine(" * @author Administrator ");
		queryMethod.addJavaDocLine(" */");
		queryMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 列表查询\", notes = \"" + domainObjectName + " 列表查询\")");
		queryMethod.addAnnotation("@RequestMapping(value = \"/query\", method = RequestMethod.POST)");

		queryMethod.setName("query");

		queryMethod.setReturnType(new FullyQualifiedJavaType("PageEntity<" + domainObjectName + ">"));
		queryMethod
				.addParameter(new Parameter(new FullyQualifiedJavaType("@RequestBody " + domainObjectName), "entity"));

		queryMethod.addBodyLine("PageArg pageArg = this.getPageArg(entity);");
		queryMethod.addBodyLine(
				"return new PageEntity<" + domainObjectName + ">(" + serviceImpl + ".findByEntity(entity, pageArg));");
		clazz.addMethod(queryMethod);

		// 查询所有方法
		Method findallMethod = new Method();
		findallMethod.setVisibility(JavaVisibility.PUBLIC);

		findallMethod.addJavaDocLine("/**");
		findallMethod.addJavaDocLine(" * Description: 查询所有 <br>");
		findallMethod.addJavaDocLine(" * Created date: " + dateTime);
		findallMethod.addJavaDocLine(" * @return: " + domainObjectName + "  集合");
		findallMethod.addJavaDocLine(" * @author Administrator ");
		findallMethod.addJavaDocLine(" */");
		findallMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 查询所有 \", notes = \"" + domainObjectName + " 查询所有\")");
		findallMethod.addAnnotation("@RequestMapping(value = \"/list/all\", method = RequestMethod.POST)");

		findallMethod.setName("findAll");

		findallMethod.setReturnType(new FullyQualifiedJavaType("List<" + domainObjectName + ">"));

		findallMethod.addBodyLine("return " + serviceImpl + ".findByEntity(null,null);");
		clazz.addMethod(findallMethod);

		// 详细查询方法
		Method findMethod = new Method();
		findMethod.setVisibility(JavaVisibility.PUBLIC);

		findMethod.addJavaDocLine("/**");
		findMethod.addJavaDocLine(" * Description: 详细查询 <br>");
		findMethod.addJavaDocLine(" * Created date: " + dateTime);
		findMethod.addJavaDocLine(" * @param " + domainObjectName + " Id ");
		findMethod.addJavaDocLine(" * @return: " + domainObjectName + " 对象 ");
		findMethod.addJavaDocLine(" * @author Administrator ");
		findMethod.addJavaDocLine(" */");
		findMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 详细查询\", notes = \"" + domainObjectName + " 详细查询\")");
		findMethod.addAnnotation("@RequestMapping(value = \"/findById/{id}\", method = RequestMethod.POST)");

		findMethod.setName("findById");

		findMethod.setReturnType(new FullyQualifiedJavaType(domainObjectName));
		findMethod.addParameter(new Parameter(new FullyQualifiedJavaType("@PathVariable(\"id\") Integer "), "id"));

		findMethod.addBodyLine("return " + serviceImpl + ".findById(id);");
		clazz.addMethod(findMethod);

		// 新增方法
		Method addMethod = new Method();
		addMethod.setVisibility(JavaVisibility.PUBLIC);

		addMethod.addJavaDocLine("/**");
		addMethod.addJavaDocLine(" * Description: 新增 <br>");
		addMethod.addJavaDocLine(" * Created date: " + dateTime);
		addMethod.addJavaDocLine(" * @param " + domainObjectName + " 对象 ");
		addMethod.addJavaDocLine(" * @return: Message  对象");
		addMethod.addJavaDocLine(" * @author Administrator ");
		addMethod.addJavaDocLine(" */");
		addMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 新增 \", notes = \"" + domainObjectName + " 新增\")");
		addMethod.addAnnotation("@RequestMapping(value = \"/add\", method = RequestMethod.POST)");

		addMethod.setName("add");

		addMethod.setReturnType(new FullyQualifiedJavaType("Message"));
		addMethod.addParameter(
				new Parameter(new FullyQualifiedJavaType("@RequestBody " + domainObjectName), "entity"));

		addMethod.addBodyLine("return " + serviceImpl + ".add(entity);");
		clazz.addMethod(addMethod);

		// 修改方法
		Method updateMethod = new Method();
		updateMethod.setVisibility(JavaVisibility.PUBLIC);

		updateMethod.addJavaDocLine("/**");
		updateMethod.addJavaDocLine(" * Description: 修改 <br>");
		updateMethod.addJavaDocLine(" * Created date: " + dateTime);
		updateMethod.addJavaDocLine(" * @param " + domainObjectName + " 对象 ");
		updateMethod.addJavaDocLine(" * @return: Message  对象");
		updateMethod.addJavaDocLine(" * @author Administrator ");
		updateMethod.addJavaDocLine(" */");
		updateMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 修改 \", notes = \"" + domainObjectName + " 修改\")");
		updateMethod.addAnnotation("@RequestMapping(value = \"/update\", method = RequestMethod.POST)");

		updateMethod.setName("update");

		updateMethod.setReturnType(new FullyQualifiedJavaType("Message"));
		updateMethod.addParameter(
				new Parameter(new FullyQualifiedJavaType("@RequestBody " + domainObjectName), "entity"));

		updateMethod.addBodyLine("return " + serviceImpl + ".update(entity);");
		clazz.addMethod(updateMethod);

		// 删除方法
		Method deleteMethod = new Method();
		deleteMethod.setVisibility(JavaVisibility.PUBLIC);

		deleteMethod.addJavaDocLine("/**");
		deleteMethod.addJavaDocLine(" * Description: 删除 <br>");
		deleteMethod.addJavaDocLine(" * Created date: " + dateTime);
		deleteMethod.addJavaDocLine(" * @param  " + domainObjectName + " id ");
		deleteMethod.addJavaDocLine(" * @return: Message  对象");
		deleteMethod.addJavaDocLine(" * @author Administrator ");
		deleteMethod.addJavaDocLine(" */");
		deleteMethod.addAnnotation(
				"@ApiOperation(value = \"" + domainObjectName + " 删除 \", notes = \"" + domainObjectName + " 删除\")");
		deleteMethod.addAnnotation("@RequestMapping(value = \"/delete\", method = RequestMethod.POST)");

		deleteMethod.setName("delete");

		deleteMethod.setReturnType(new FullyQualifiedJavaType("Message"));
		deleteMethod.addParameter(new Parameter(new FullyQualifiedJavaType("@RequestBody Integer"), "id"));

		deleteMethod.addBodyLine("return " + serviceImpl + ".delete(id);");
		clazz.addMethod(deleteMethod);

		return clazz;
	}

	/**
	 * Description: 生成Service接口
	 *
	 * @return
	 * @author LiuJianli
	 * @date 2018/7/5
	 */
	private Interface generateService(IntrospectedTable table) {

		String domainObjectName = table.getFullyQualifiedTable().getDomainObjectName();

		// 生成 Service 名称
		String service = targetServicePackage + ".I" + domainObjectName + "Service";
		// 构造 Service 文件
		Interface interfaze = new Interface(new FullyQualifiedJavaType(service));
		// 设置作用域
		interfaze.setVisibility(JavaVisibility.PUBLIC);
		interfaze
				.addImportedType(new FullyQualifiedJavaType(IBaseService.class.getName()));

		interfaze.addSuperInterface(new FullyQualifiedJavaType("IBaseService"));

		return interfaze;
	}

	/**
	 * Description: 生成ServiceImpl
	 *
	 * @return
	 * @author LiuJianli
	 * @date 2018/7/5
	 */
	private TopLevelClass generateServiceImpl(IntrospectedTable table) {

		String domainObjectName = table.getFullyQualifiedTable().getDomainObjectName();

		String service = targetServicePackage + ".I" + domainObjectName + "Service";
		String serviceImpl = targetServiceImplPackage + "." + domainObjectName + "ServiceImpl";

		TopLevelClass clazz = new TopLevelClass(new FullyQualifiedJavaType(serviceImpl));

		clazz.addImportedType(new FullyQualifiedJavaType(service));
		clazz.addImportedType(
				new FullyQualifiedJavaType(AbstractBaseService.class.getName()));
		clazz.addImportedType(new FullyQualifiedJavaType(IBaseDao.class.getName()));

		clazz.addImportedType(new FullyQualifiedJavaType(daoFullPackage));

		clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.Logger"));
		clazz.addImportedType(new FullyQualifiedJavaType("org.slf4j.LoggerFactory"));
		clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));

		clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));

		clazz.addAnnotation("@Service");
		clazz.setVisibility(JavaVisibility.PUBLIC);
		clazz.setSuperClass(new FullyQualifiedJavaType("AbstractBaseService"));
		clazz.addSuperInterface(new FullyQualifiedJavaType(service));

		// logger
		Field logger = new Field();
		logger.setName("logger");
		logger.setType(new FullyQualifiedJavaType("Logger"));
		logger.setVisibility(JavaVisibility.PRIVATE);
		logger.setInitializationString("LoggerFactory.getLogger(this.getClass())");
		clazz.addField(logger);

		String dao = domainObjectName.toLowerCase() + "Dao";
		// 增加字段
		Field field = new Field();
		field.addAnnotation("@Autowired");
		field.setName(dao);
		field.setType(new FullyQualifiedJavaType(domainObjectName + "Mapper"));
		field.setVisibility(JavaVisibility.PRIVATE);
		clazz.addField(field);

		Method method = new Method();
		method.addAnnotation("@Override");
		method.setReturnType(new FullyQualifiedJavaType("IBaseDao"));
		method.setVisibility(JavaVisibility.PROTECTED);
		method.setName("getBaseDao");

		method.addBodyLine("return " + dao + ";");
		clazz.addMethod(method);
		return clazz;
	}
}
