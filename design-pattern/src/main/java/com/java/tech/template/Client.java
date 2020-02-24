package com.java.tech.template;

import com.java.tech.template.impl.UserQueryImpl;
import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.service.UserQuery;

import java.util.UUID;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {

        UserQuery userQuery = new UserQueryImpl();
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserId("Peter");
        userQueryRequest.setRequestBody(UUID.randomUUID().toString());
        userQueryRequest.setChannelType("User");
        userQuery.queryUser(userQueryRequest);
    }
}
