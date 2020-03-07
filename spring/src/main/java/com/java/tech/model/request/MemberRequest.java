package com.java.tech.model.request;

import lombok.Builder;
import lombok.Data;


/**
 * created by Jason on 2020/3/5
 */
@Data
@Builder
public class MemberRequest extends BaseRequest {

    private String memberId;
    private String memberName;
    private String address;
}
