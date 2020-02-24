package com.java.tech.strategy;

import com.java.tech.strategy.request.EmailRequest;
import com.java.tech.strategy.response.EmailResponse;

/**
 * created by Jason on 2020/2/24
 * 实现邮箱发送消息的业务
 */
public class EmailSendServiceImpl implements CommonSendService<EmailRequest, EmailResponse>{
    @Override
    public EmailResponse sendMessage(EmailRequest request) {
        EmailResponse response = new EmailResponse();
        response.setReturnCode("M00");
        response.setMsg(request.getRequestBody());
        return response;
    }
}
