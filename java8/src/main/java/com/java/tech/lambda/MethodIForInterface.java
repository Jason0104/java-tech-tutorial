package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public interface MethodIForInterface {
    String sayHello(String name);

    default String handleMessage(String message) {
        return message;
    }
}
