package com.java.tech.template.impl;

import com.java.tech.template.ServiceCallBack;
import com.java.tech.template.ServiceTemplate;
import com.java.tech.template.request.UserQueryRequest;
import com.java.tech.template.response.UserQueryResponse;
import com.java.tech.template.service.UserQuery;
/**
 * created by Jason on 2020/2/24
 */
public class UserQueryImpl implements UserQuery {
    @Override
    public UserQueryResponse queryUser(UserQueryRequest request) {
        return ServiceTemplate.execute(request, new ServiceCallBack<UserQueryRequest, UserQueryResponse>() {
            @Override
            public void checkParameter(UserQueryRequest request) {
                System.out.println("check parameter:" + request.getUserId());
            }

            @Override
            public UserQueryResponse process(UserQueryRequest request) {
                UserQueryResponse response = new UserQueryResponse();
                response.setMsg(request.getRequestBody());
                response.setReturnCode("200");
                response.setUserType(request.getChannelType());
                return response;
            }

            @Override
            public UserQueryResponse fillFailedResult(UserQueryRequest request, String message) {
                return null;
            }

            @Override
            public void afterProcess() {
                System.out.println("User Query Processing afterProcess");
            }
        });
    }
}
