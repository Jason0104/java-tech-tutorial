package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public class DefaultMethodForInterface {
    public static void main(String[] args) {
        MethodIForInterface methodIForInterface = new MethodIForInterface() {
            @Override
            public String sayHello(String name) {
                return "Jason " + name;
            }
        };

        String result = methodIForInterface.sayHello("Chen");
        System.out.println(result);
    }
}
