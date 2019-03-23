package com.coulee.aicw.foundations.utils.compiler.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coulee.aicw.foundations.utils.compiler.ICompiler;
import com.coulee.aicw.foundations.utils.compiler.utils.ClassHelper;
import com.coulee.aicw.foundations.utils.compiler.utils.ClassUtils;

;

/**
 * Description:动态编译抽象类
 * Copyright (C) 2015 Coulee All Right Reserved.
 * Author：在alibaba的william.liangf基础上修改
 * Create Date: 2015年12月22日
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version 1.0
 */
public abstract class AbstractCompiler implements ICompiler {
    
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");
    
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");
    
    public Class<?> compile(String code) {
        code = code.trim();
        Matcher matcher = PACKAGE_PATTERN.matcher(code);
        String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        matcher = CLASS_PATTERN.matcher(code);
        String cls;
        if (matcher.find()) {
            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }
        String className = pkg != null && pkg.length() > 0 ? pkg + "." + cls : cls;
        try {
            return Class.forName(className, true, ClassHelper.getCallerClassLoader(getClass()));
        } catch (ClassNotFoundException e) {
            if (! code.endsWith("}")) {
                throw new IllegalStateException("The java code not endsWith \"}\", code: \n" + code + "\n");
            }
            try {
                return doCompile(className, code);
            } catch (RuntimeException t) {
                throw t;
            } catch (Throwable t) {
                throw new IllegalStateException("Failed to compile class, cause: " + t.getMessage() + ", class: " + className + ", code: \n" + code + "\n, stack: " + ClassUtils.toString(t));
            }
        }
    }
    
    protected abstract Class<?> doCompile(String name, String source) throws Throwable;

}
