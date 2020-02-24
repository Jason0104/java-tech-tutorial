package com.java.tech.factory.abstractFactory.service;

/**
 * created by Jason on 2020/2/24
 */
public interface ServiceFactory {

    UserService getUserService();

    MemberService getMemberService();
}
