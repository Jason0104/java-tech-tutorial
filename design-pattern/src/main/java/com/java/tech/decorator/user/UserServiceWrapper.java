package com.java.tech.decorator.user;

import com.java.tech.factory.abstractFactory.service.UserService;

/**
 * created by Jason on 2020/2/24
 * <p>
 * 在查询用户之前检查用户额度信息
 */
public abstract class UserServiceWrapper implements UserService {

    protected UserService userService;
}
