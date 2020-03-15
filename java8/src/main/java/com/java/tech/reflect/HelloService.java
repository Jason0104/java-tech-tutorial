package com.java.tech.reflect;

import com.java.tech.reflect.annotation.CallService;

/**
 * created by Jason on 2020/3/15
 */
@CallService(value = "hello")
public interface HelloService {

    String sayHello(String name);
}
