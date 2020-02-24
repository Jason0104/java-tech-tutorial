package com.java.tech.adapter.impl;

import com.java.tech.adapter.request.LoginRequest;
import com.java.tech.adapter.request.WechatLoginRequest;
import com.java.tech.adapter.response.LoginResponse;
import com.java.tech.adapter.service.LoginService;

/**
 * created by Jason on 2020/2/24
 *
 * wechat适配器
 */
public class WechatLoginAdapter extends WechatLoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        WechatLoginRequest request = new WechatLoginRequest();
        request.setUsername(loginRequest.getUsername());
        return loginWechat(request);
    }
}
