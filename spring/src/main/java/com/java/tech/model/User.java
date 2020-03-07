package com.java.tech.model;

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
public class User {

    private int id;
    private String username;
    private String address;
    private String mobile;
    private int isDeleted;
}
