package com.java.tech.delegate;

/**
 * created by Jason on 2020/2/23
 */
public class Client {

    public static void main(String[] args) {
        Executor executor = new BatchExecutor();
        ExecutorDelegate delegate = new ExecutorDelegate(executor);
        String result = delegate.query("writing code");
        System.out.println(result);
    }
}
