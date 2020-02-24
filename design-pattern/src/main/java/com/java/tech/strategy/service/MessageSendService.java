package com.java.tech.strategy.service;

import com.java.tech.strategy.ChannelType;
import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface MessageSendService{

    BaseResponse sendMessage(ChannelType channelType);
}
