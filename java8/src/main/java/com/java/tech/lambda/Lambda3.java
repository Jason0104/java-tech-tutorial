package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 * Java8 允许使用关键字"::"获取静态方法和构造方法
 */
public class Lambda3 {
    public static void main(String[] args) {
        Lambda2<String,String> methodReference = StringUtils::startWith;
        String result = methodReference.converter("Java");
        System.out.println(result);
    }
}
