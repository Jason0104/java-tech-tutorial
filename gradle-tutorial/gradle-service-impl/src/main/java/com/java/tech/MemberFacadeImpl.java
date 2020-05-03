package com.java.tech;

import com.java.tech.service.MemberFacade;

/**
 * created by Jason on 2020/5/1
 */
public class MemberFacadeImpl implements MemberFacade {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
