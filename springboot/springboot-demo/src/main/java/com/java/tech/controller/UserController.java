package com.java.tech.controller;

import com.java.tech.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by Jason on 2020/6/11
 */
@RestController
public class UserController {

    @Autowired
    private HelloService helloService;

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getUserInfo() {
        return helloService.getUseInfo();
    }
}
