package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 * 定义函数式接口
 */
@FunctionalInterface
public interface Lambda2<T, R> {
    R converter(T req);

}
