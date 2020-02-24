package com.java.tech.adapter.service;

import com.java.tech.adapter.request.LoginRequest;
import com.java.tech.adapter.response.LoginResponse;

/**
 * created by Jason on 2020/2/24
 *
 * 登录接口
 */
public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);
}
