package com.java.tech.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * created by Jason on 2020/3/3
 */
@Data
public class ShoppingCart {

    private List<String> fruits;
    private String name;
    private Map<String, Customer> basicInfo;

}
