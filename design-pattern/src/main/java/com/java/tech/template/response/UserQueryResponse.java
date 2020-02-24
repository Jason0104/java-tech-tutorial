package com.java.tech.template.response;

import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 */
public class UserQueryResponse extends BaseResponse {

    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "userType='" + userType + '\'' +"code=";
    }
}
