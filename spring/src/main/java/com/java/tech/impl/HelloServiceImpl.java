package com.java.tech.impl;

import com.java.tech.service.HelloService;
import org.springframework.stereotype.Service;


public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String message) {
        return "nihao " + message;
    }
}