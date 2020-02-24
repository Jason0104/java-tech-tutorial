package com.java.tech.strategy;

import com.java.tech.strategy.request.BaseRequest;
import com.java.tech.strategy.request.EmailRequest;
import com.java.tech.strategy.request.SmsRequest;
import com.java.tech.strategy.response.BaseResponse;
import com.java.tech.strategy.service.MessageSendService;
import com.java.tech.strategy.service.MessageSendServiceImpl;

import java.util.UUID;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    //测试代码
    public static void main(String[] args) {
        BaseRequest smsRequest = new SmsRequest();
        smsRequest.setRequestBody(UUID.randomUUID().toString());
        MessageSendService messageSendService = new MessageSendServiceImpl(smsRequest);

        //用户选择用短信发送
        BaseResponse response = messageSendService.sendMessage(ChannelType.SMS);
        System.out.println(response);
    }
}
