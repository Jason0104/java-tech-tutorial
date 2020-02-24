package com.java.tech.adapter;

import com.java.tech.adapter.impl.LoginServiceImpl;
import com.java.tech.adapter.impl.WechatLoginAdapter;
import com.java.tech.adapter.impl.WeiboLoginAdapter;
import com.java.tech.adapter.request.LoginRequest;
import com.java.tech.adapter.response.LoginResponse;
import com.java.tech.adapter.service.LoginService;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
        LoginService loginService = new LoginServiceImpl();

        LoginRequest request = new LoginRequest();
        request.setUsername("Peter");
        LoginResponse response = loginService.login(request);
        System.out.println(response);

        //WechatLoginAdapter相当于转接头
        LoginService wechatLogin = new WechatLoginAdapter();
        LoginResponse wechatResponse = wechatLogin.login(request);
        System.out.println(wechatResponse);

        //使用微博登录
        LoginService weiboLogin = new WeiboLoginAdapter();
        LoginResponse weiboResponse =  weiboLogin.login(request);
        System.out.println(weiboResponse);
    }
}
