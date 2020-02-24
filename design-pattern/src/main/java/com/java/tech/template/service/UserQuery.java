package com.java.tech.template.service;

import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface UserQuery {

    UserQueryResponse queryUser(UserQueryRequest request);
}
