package com.java.tech.adapter.service;

import com.java.tech.adapter.request.WechatLoginRequest;
import com.java.tech.adapter.response.WechatResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface WechatLogin {

    WechatResponse loginWechat(WechatLoginRequest wechatLoginRequest);
}
