package com.java.tech.adapter.impl;

import com.java.tech.adapter.request.WechatLoginRequest;
import com.java.tech.adapter.response.WechatResponse;
import com.java.tech.adapter.service.WechatLogin;

/**
 * created by Jason on 2020/2/24
 *
 * 单独实现wechat登录的业务
 */
public class WechatLoginServiceImpl implements WechatLogin {
    @Override
    public WechatResponse loginWechat(WechatLoginRequest wechatLoginRequest) {
        WechatResponse response = new WechatResponse();
        response.setCode("Wechat");
        response.setMessage("使用微信登录系统,用户名="+wechatLoginRequest.getUsername());
        return response;
    }
}
