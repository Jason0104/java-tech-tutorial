package com.java.tech.impl;

import com.java.tech.model.Member;
import com.java.tech.service.MemberService;

import java.util.UUID;

/**
 * created by Jason on 2020/3/3
 */
public class MemberServiceImpl implements MemberService {
    @Override
    public Member queryMember() {
        Member member = Member.builder().memberId(UUID.randomUUID().toString()).memberName("Jason").address("shanghai").build();
        return member;
    }
}
