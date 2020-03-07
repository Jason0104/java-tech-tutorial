package com.java.tech.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Jason on 2020/3/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseModel {

    private String sender;
    private String receiver;
    private Double amount;
}
