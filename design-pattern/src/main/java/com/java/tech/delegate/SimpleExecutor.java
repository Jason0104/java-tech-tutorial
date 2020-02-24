package com.java.tech.delegate;


/**
 * created by Jason on 2020/2/23
 */
public class SimpleExecutor extends BaseExecutor {
    @Override
    public String query(String request) {
        return "Simple Executor is executing:" + request;
    }
}
