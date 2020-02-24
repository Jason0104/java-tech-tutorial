package com.java.tech.factory.abstractFactory.service;

import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface UserService {

    UserQueryResponse queryUser(UserQueryRequest request);
}
