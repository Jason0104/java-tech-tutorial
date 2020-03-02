package com.java.tech.proxy;

import java.lang.reflect.Method;

/**
 * created by Jason on 2020/2/29
 */
public class ReflectServiceTest {

    public static void main(String[] args) throws Exception {
        //通过反射创建ReflectService对象
        Object service = Class.forName("com.java.tech.proxy.ReflectService").newInstance();
        //获取服务方法
        Method method = service.getClass().getMethod("sayHello", String.class);
        //反射调用
        method.invoke(service, "Jason");
    }
}
