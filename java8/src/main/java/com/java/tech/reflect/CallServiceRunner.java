package com.java.tech.reflect;

import com.java.tech.reflect.annotation.CallService;

/**
 * created by Jason on 2020/3/15
 */
public class CallServiceRunner {

    public static void main(String[] args) throws Exception {
        //打印出带有@CallSerivce注解类的值
        Class<?> clazz = Class.forName("com.java.tech.reflect.HelloService");
        CallService values = clazz.getAnnotation(CallService.class);
        System.out.println(values.value());
    }
}
