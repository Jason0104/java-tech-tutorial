package com.java.tech.adapter.impl;

import com.java.tech.adapter.request.WeiboRequest;
import com.java.tech.adapter.response.WeiboResponse;
import com.java.tech.adapter.service.WeiboLogin;

/**
 * created by Jason on 2020/2/24
 * 单独实现weibo登录的业务
 */
public class WeiboLoginServiceImpl implements WeiboLogin {
    @Override
    public WeiboResponse loginWeibo(WeiboRequest weiboRequest) {
        WeiboResponse response = new WeiboResponse();
        response.setCode("weibo");
        response.setMessage("使用微博登录系统,用户名="+ weiboRequest.getUsername());
        return response;
    }
}
