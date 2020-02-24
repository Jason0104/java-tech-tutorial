package com.java.tech.template.request;

import com.java.tech.strategy.request.BaseRequest;

/**
 * created by Jason on 2020/2/24
 */
public class UserQueryRequest extends BaseRequest {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
