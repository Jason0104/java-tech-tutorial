package com.java.tech.impl;

import com.java.tech.aop.MyLog;
import com.java.tech.service.EchoService;
import org.springframework.stereotype.Service;

/**
 * created by Jason on 2020/3/2
 */
@Service
public class EchoServiceImpl implements EchoService {
    @MyLog(value = "info")
    @Override
    public String echo(String name) {
        return "Hello " + name;
    }
}
