package com.java.tech.template.impl;

import com.java.tech.template.ServiceCallBack;
import com.java.tech.template.ServiceTemplate;
import com.java.tech.template.request.MemberQueryRequest;
import com.java.tech.template.response.MemberQueryResponse;
import com.java.tech.template.service.MemberQuery;

/**
 * created by Jason on 2020/2/24
 */
public class MemberQueryImpl implements MemberQuery {
    @Override
    public MemberQueryResponse queryMember(MemberQueryRequest request) {
        return ServiceTemplate.execute(request, new ServiceCallBack<MemberQueryRequest, MemberQueryResponse>() {
            @Override
            public void checkParameter(MemberQueryRequest request) {

            }

            @Override
            public MemberQueryResponse process(MemberQueryRequest request) {
                return null;
            }

            @Override
            public MemberQueryResponse fillFailedResult(MemberQueryRequest request, String message) {
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });
    }
}
