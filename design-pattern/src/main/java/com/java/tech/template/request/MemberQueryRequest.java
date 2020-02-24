package com.java.tech.template.request;

import com.java.tech.strategy.request.BaseRequest;

/**
 * created by Jason on 2020/2/24
 */
public class MemberQueryRequest extends BaseRequest {

    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
