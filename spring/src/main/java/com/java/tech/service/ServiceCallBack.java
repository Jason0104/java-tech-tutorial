package com.java.tech.service;

/**
 * created by Jason on 2020/3/5
 * 把所有的业务模型进行抽象成几步
 */
public interface ServiceCallBack<T, R> {

    //check parameter
    void checkParameter(T request);

    //process
    R process(T request) throws Exception;

    //fill failed result
    R fillFailedResult(T request, String message);

    //after process
    void afterProcess();


}
