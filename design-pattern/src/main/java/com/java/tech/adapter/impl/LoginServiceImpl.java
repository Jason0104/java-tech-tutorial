package com.java.tech.adapter.impl;

import com.java.tech.adapter.request.LoginRequest;
import com.java.tech.adapter.response.LoginResponse;
import com.java.tech.adapter.service.LoginService;

/**
 * created by Jason on 2020/2/24
 */
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();
        response.setMessage("使用用户名和密码登录系统,用户名=" + loginRequest.getUsername());
        response.setCode("200");
        return response;
    }
}
