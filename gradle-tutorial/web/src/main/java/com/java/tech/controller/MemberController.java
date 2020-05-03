package com.java.tech.controller;

import com.java.tech.MemberFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by Jason on 2020/5/1
 */
@Controller
public class MemberController {

    @Autowired
    private MemberFacadeImpl memberFacade;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String sayHello() {
        memberFacade.sayHello("Jason");
        return "hello";
    }
}
