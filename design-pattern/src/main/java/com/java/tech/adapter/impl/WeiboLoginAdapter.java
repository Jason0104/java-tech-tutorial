package com.java.tech.adapter.impl;

import com.java.tech.adapter.request.LoginRequest;
import com.java.tech.adapter.request.WeiboRequest;
import com.java.tech.adapter.response.LoginResponse;
import com.java.tech.adapter.service.LoginService;

/**
 * created by Jason on 2020/2/24
 */
public class WeiboLoginAdapter extends WeiboLoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        WeiboRequest request = new WeiboRequest();
        request.setUsername(loginRequest.getUsername());
        return loginWeibo(request);
    }
}
