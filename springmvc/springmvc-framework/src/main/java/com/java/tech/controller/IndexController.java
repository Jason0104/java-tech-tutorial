package com.java.tech.controller;

import com.java.tech.annotation.DFController;
import com.java.tech.annotation.DFRequestMapping;

/**
 * created by Jason on 2020/3/14
 */
@DFController
public class IndexController {

    @DFRequestMapping(value = "/index")
    public String index() {
        return "index";
    }
}
