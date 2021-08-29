package com.java.tech.impl;


import com.java.tech.service.MemberFacade;
import org.springframework.stereotype.Service;

/**
 * created by Jason on 2020/5/1
 */
@Service
public class MemberFacadeImpl implements MemberFacade {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
