package com.java.tech.reflect;

/**
 * created by Jason on 2020/3/15
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
