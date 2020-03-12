package com.java.tech.web.method;

import java.lang.reflect.Method;

/**
 * created by Jason on 2020/3/12
 */
public class DFHandlerMethod {

    private final Object bean;
    private final Method method;

    public DFHandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
