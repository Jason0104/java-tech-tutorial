package com.java.tech.delegate;

/**
 * created by Jason on 2020/2/23
 */
public class BatchExecutor extends BaseExecutor {
    @Override
    public String query(String request) {
        return "Batch Executor is executing:" +request;
    }
}
