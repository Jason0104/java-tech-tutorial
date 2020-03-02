package com.java.tech.proxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * created by Jason on 2020/2/29
 */
public class CglibServiceProxy implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("--------我是CGLIB动态代理--------");
        Object object;
        System.out.println("我准备说hello");
        object = methodProxy.invokeSuper(o, objects);
        System.out.println("我已经说过了");
        return object;
    }
}
