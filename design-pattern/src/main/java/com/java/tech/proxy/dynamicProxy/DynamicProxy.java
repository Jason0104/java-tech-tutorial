package com.java.tech.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by Jason on 2020/2/24
 */
public class DynamicProxy implements InvocationHandler {

    //被代理的对象
    private Object target;

    public Object getInstance(Object obj){
        this.target = obj;
        Class<?> clazz = obj.getClass();

        //字节码重组
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }
}
