package com.java.tech.factory.abstractFactory.service;

import com.java.tech.template.request.MemberQueryRequest;
import com.java.tech.template.response.MemberQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public class MemberServiceImpl implements MemberService {
    @Override
    public MemberQueryResponse queryMemberInfo(MemberQueryRequest request) {
        MemberQueryResponse response = new MemberQueryResponse();
        response.setReturnCode("200");
        response.setMemberId("member");
        response.setMsg(request.getRequestId());
        return response;
    }
}
