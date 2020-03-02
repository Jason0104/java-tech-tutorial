package com.java.tech.proxy;

/**
 * created by Jason on 2020/2/29
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void echo(String name) {
        System.out.println("Hello " + name);
    }
}
