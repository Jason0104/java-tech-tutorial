package com.java.tech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * created by Jason on 2020/3/8
 */
@Controller
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return "hello";
    }

    @RequestMapping(value = "/sayHi", method = RequestMethod.GET)
    public String sayHi() {
        return "hi";
    }
}
