package com.java.tech.template.service;

import com.java.tech.template.request.MemberQueryRequest;
import com.java.tech.template.response.MemberQueryResponse;

/**
 * created by Jason on 2020/2/24
 */
public interface MemberQuery {

    MemberQueryResponse queryMember(MemberQueryRequest request);
}
