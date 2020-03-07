package com.java.tech.impl;

import com.java.tech.model.Member;
import com.java.tech.model.request.MemberRequest;
import com.java.tech.model.response.MemberResponse;
import com.java.tech.service.MemberService;
import com.java.tech.service.ServiceCallBack;
import com.java.tech.service.ServiceTemplate;
import org.springframework.stereotype.Service;


/**
 * created by Jason on 2020/3/3
 */
@Service
public class MemberServiceImpl implements MemberService {

    private ServiceTemplate serviceTemplate;

    public void setServiceTemplate(ServiceTemplate serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }


    @Override
    public MemberResponse queryMember(MemberRequest querier) {
        return ServiceTemplate.execute(querier, new ServiceCallBack<MemberRequest, MemberResponse>() {
            @Override
            public void checkParameter(MemberRequest request) {
                System.out.println("check parameter");
            }

            @Override
            public MemberResponse process(MemberRequest request) {
                Member member = Member.builder()
                        .memberId(request.getMemberId())
                        .address(request.getAddress())
                        .memberName(request.getMemberName())
                        .build();
                MemberResponse response = MemberResponse.builder().member(member).build();
                return response;
            }

            @Override
            public MemberResponse fillFailedResult(MemberRequest request, String message) {
                return null;
            }

            @Override
            public void afterProcess() {

            }
        });
    }
}
