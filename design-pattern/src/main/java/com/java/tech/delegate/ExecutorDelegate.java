package com.java.tech.delegate;

/**
 * created by Jason on 2020/2/23
 */
public class ExecutorDelegate implements Executor<String,String> {

    private final Executor<String,String> executorDelegate;

    public ExecutorDelegate(Executor executorDelegate) {
        this.executorDelegate = executorDelegate;
    }

    @Override
    public String query(String request) {
        return executorDelegate.query(request);
    }
}
