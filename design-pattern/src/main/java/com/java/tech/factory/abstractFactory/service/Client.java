package com.java.tech.factory.abstractFactory.service;

import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

import java.util.UUID;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
        //根据需要去工厂类获取
        ServiceFactory serviceFactory = new DefaultServiceFactory();
        UserService userService = serviceFactory.getUserService();

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setRequestId(UUID.randomUUID().toString());
        UserQueryResponse response = userService.queryUser(userQueryRequest);
        System.out.println(response.getReturnCode() + ":" + response.getUserType());

    }
}
