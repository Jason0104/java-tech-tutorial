package com.java.tech.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Jason on 2020/2/28
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseEntity {

    private String name;
    private String address;
}
