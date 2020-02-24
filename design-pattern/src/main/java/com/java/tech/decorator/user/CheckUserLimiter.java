package com.java.tech.decorator.user;

import com.java.tech.factory.abstractFactory.service.UserService;
import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public class CheckUserLimiter extends UserServiceWrapper {
    public CheckUserLimiter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserQueryResponse queryUser(UserQueryRequest request) {
        //查询用户之前进行查询用户额度
        checkUserLimiter(request);
        return userService.queryUser(request);
    }

    private void checkUserLimiter(UserQueryRequest request) {
        System.out.println("检查用户额度信息,用户名=" + request.getUserId());
    }
}
