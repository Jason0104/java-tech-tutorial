package com.java.tech.model;


import lombok.Data;

import java.io.Serializable;

/**
 * created by Jason on 2020/3/7
 */
@Data
public class Order implements Serializable {
    private String orderId;
    private String orderName;
}
