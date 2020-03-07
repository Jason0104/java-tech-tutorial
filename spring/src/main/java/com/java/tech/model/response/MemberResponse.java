package com.java.tech.model.response;

import com.java.tech.model.Member;
import lombok.Builder;
import lombok.Data;

/**
 * created by Jason on 2020/3/5
 */
@Data
@Builder
public class MemberResponse extends BaseResult {

    private Member member;
}
