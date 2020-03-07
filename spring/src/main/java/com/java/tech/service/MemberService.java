package com.java.tech.service;

import com.java.tech.model.request.MemberRequest;
import com.java.tech.model.response.MemberResponse;

/**
 * created by Jason on 2020/3/3
 */
public interface MemberService {

    MemberResponse queryMember(MemberRequest querier);
}
