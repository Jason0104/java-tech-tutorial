package com.java.tech.strategy.service;

import com.java.tech.strategy.ChannelType;
import com.java.tech.strategy.request.BaseRequest;
import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 */
public class MessageSendServiceImpl implements MessageSendService {
    private BaseRequest baseRequest;

    public MessageSendServiceImpl(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }

    @Override
    public BaseResponse sendMessage(ChannelType channelType) {
        return channelType.getCommonSendService().sendMessage(baseRequest);
    }
}
