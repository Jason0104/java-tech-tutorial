package com.java.tech.reflect;

import java.lang.reflect.Method;

/**
 * created by Jason on 2020/3/15
 */
public class Bootstrap {

    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        String result = helloService.sayHello("Peter");
//        System.out.println(result);

        try {
            Class<?> clazz = Class.forName("com.java.tech.reflect.HelloServiceImpl");
            Method method = clazz.getMethod("sayHello", String.class);
            Object result = method.invoke(clazz.newInstance(), "Jason");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
