package com.java.tech.template;

import com.java.tech.strategy.request.BaseRequest;
import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 *
 * 将所有的业务服务行为抽象为接口
 */
public interface ServiceCallBack<T extends BaseRequest,R extends BaseResponse> {

    //1.参数检查
    void checkParameter(T request);

    //2.业务处理
    R process(T request);

    //3.失败业务处理
    R fillFailedResult(T request,String message);

    //4.afterProcess
    void afterProcess();
}
