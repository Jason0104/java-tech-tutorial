package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.Member;
import com.java.tech.model.Order;
import com.java.tech.model.request.MemberRequest;
import com.java.tech.model.response.MemberResponse;
import com.java.tech.service.MemberService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * created by Jason on 2020/3/5
 */
public class MemberServiceTest extends AbstractSpringContextTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void testQueryMember() {
        MemberRequest request = MemberRequest.builder()
                .memberId("00190")
                .memberName("jason")
                .address("beijing")
                .build();
        MemberResponse response = memberService.queryMember(request);
        System.out.println(response);
    }

}
