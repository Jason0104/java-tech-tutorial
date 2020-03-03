package com.java.tech.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Jason on 2020/3/3
 */
@Data
@Builder
public class Member implements Serializable {

    private String memberId;
    private String memberName;
    private String address;
}
