package com.java.tech.strategy;

import com.java.tech.strategy.request.SmsRequest;
import com.java.tech.strategy.response.SmsResponse;

/**
 * created by Jason on 2020/2/24
 *
 * 实现短信发送消息的业务
 */
public class SmsSendServiceImpl implements CommonSendService<SmsRequest, SmsResponse> {
    @Override
    public SmsResponse sendMessage(SmsRequest request) {
        SmsResponse response = new SmsResponse();
        response.setReturnCode("S00");
        response.setMsg(request.getRequestBody());
        return response;
    }
}
