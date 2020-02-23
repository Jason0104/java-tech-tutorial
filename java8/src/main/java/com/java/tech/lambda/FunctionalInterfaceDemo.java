package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        Lambda2<String,Integer> convert =(req ->  Integer.valueOf(req));
        Integer integer = convert.converter("123");
        System.out.println(integer);

        //方法引用使用方式
        Lambda2<String,Integer> from = Integer::valueOf;
        Integer methodReference = from.converter("456");
        System.out.println(methodReference);
    }
}
