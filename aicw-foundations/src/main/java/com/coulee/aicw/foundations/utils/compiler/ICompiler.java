package com.coulee.aicw.foundations.utils.compiler;

/**
 * Description:动态编译Java源码
 * Copyright (C) 2015 Coulee All Right Reserved.
 * Author：在alibaba的william.liangf基础上修改
 * Create Date: 2015年12月22日
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public interface ICompiler {

	/**
	 * Definition:编译java源码
	 * @param code java源码
	 * @return 编译后的实例对象
	 * @Author: LanChao
	 * @Created date: 2015年12月22日
	 */
	public Class<?> compile(String code);

}
