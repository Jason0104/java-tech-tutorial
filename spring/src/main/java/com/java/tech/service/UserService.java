package com.java.tech.service;

import com.java.tech.model.User;

import java.util.List;

/**
 * created by Jason on 2020/3/5
 */
public interface UserService {

    User queryUser(String sql);

    List<User> queryList(String param);
}
