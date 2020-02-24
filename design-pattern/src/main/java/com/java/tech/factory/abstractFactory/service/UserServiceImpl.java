package com.java.tech.factory.abstractFactory.service;

import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public class UserServiceImpl implements UserService {
    @Override
    public UserQueryResponse queryUser(UserQueryRequest request) {
        UserQueryResponse response = new UserQueryResponse();
        response.setReturnCode("200");
        response.setUserType("User");
        response.setMsg(request.getRequestId());
        return response;
    }
}
