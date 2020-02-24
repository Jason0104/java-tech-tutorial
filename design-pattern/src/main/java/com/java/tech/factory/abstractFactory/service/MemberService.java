package com.java.tech.factory.abstractFactory.service;

import com.java.tech.template.request.MemberQueryRequest;
import com.java.tech.template.response.MemberQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface MemberService {

    MemberQueryResponse queryMemberInfo(MemberQueryRequest request);
}
