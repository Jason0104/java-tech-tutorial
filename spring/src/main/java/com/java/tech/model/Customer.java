package com.java.tech.model;

import lombok.Data;

import java.io.Serializable;

/**
 * created by Jason on 2020/3/3
 */
@Data
public class Customer implements Serializable {

    private String name;
    private String address;
    private String mobile;
}
