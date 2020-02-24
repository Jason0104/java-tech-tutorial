package com.java.tech.delegate;

/**
 * created by Jason on 2020/2/23
 */
public interface Executor<T,R> {

    R query(T request);
}
