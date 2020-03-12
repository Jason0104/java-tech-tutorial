package com.java.tech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * created by Jason on 2020/3/9
 */
@Controller
public class EchoController {

    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String echo() {
        return "echo";
    }
}
