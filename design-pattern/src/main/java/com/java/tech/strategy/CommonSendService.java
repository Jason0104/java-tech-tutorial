package com.java.tech.strategy;

import com.java.tech.strategy.request.BaseRequest;
import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 *
 * 发送消息可以有多种方式比如短信/email
 *
 * 策略类 定义多种固定算法实现
 */
public interface CommonSendService<T extends BaseRequest, R extends BaseResponse> {

    R sendMessage(T request);
}
