package com.java.tech.decorator.user;

import com.java.tech.factory.abstractFactory.service.UserService;
import com.java.tech.factory.abstractFactory.service.UserServiceImpl;
import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

import java.util.UUID;

/**
 * created by Jason on 2020/2/24
 */
public class Bootstrap {

    public static void main(String[] args) {
        UserService userService = new CheckUserLimiter(new UserServiceImpl());
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("Peter");
        request.setRequestId(UUID.randomUUID().toString());
        UserQueryResponse userQueryResponse = userService.queryUser(request);
        System.out.println(userQueryResponse);
    }
}
