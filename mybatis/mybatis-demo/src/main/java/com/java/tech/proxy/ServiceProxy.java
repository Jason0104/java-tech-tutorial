package com.java.tech.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * created by Jason on 2020/2/29
 */
public class ServiceProxy implements InvocationHandler {
    private Object target;

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------我是JDK动态代理------------");
        Object result;
        System.out.println("我准备说hello");
        result = method.invoke(target, args);
        System.out.println("我已经说过了");
        return result;
    }
}
