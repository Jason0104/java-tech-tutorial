package com.java.tech.factory.abstractFactory.service;

/**
 * created by Jason on 2020/2/24
 */
public class DefaultServiceFactory implements ServiceFactory {
    @Override
    public UserService getUserService() {
        return new UserServiceImpl();
    }

    @Override
    public MemberService getMemberService() {
        return new MemberServiceImpl();
    }
}
