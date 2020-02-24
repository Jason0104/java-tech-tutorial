package com.java.tech.template.response;

import com.java.tech.strategy.response.BaseResponse;

/**
 * created by Jason on 2020/2/24
 */
public class MemberQueryResponse extends BaseResponse {

    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
