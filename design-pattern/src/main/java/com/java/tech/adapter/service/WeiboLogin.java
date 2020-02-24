package com.java.tech.adapter.service;

import com.java.tech.adapter.request.WeiboRequest;
import com.java.tech.adapter.response.WeiboResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface WeiboLogin {

    WeiboResponse loginWeibo(WeiboRequest weiboRequest);
}
