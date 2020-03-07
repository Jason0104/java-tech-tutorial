package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.User;
import com.java.tech.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * created by Jason on 2020/3/5
 */
public class UserServiceTest extends AbstractSpringContextTest {

    @Autowired
    private UserService userService;

    @Test
    public void testQueryUser() {
        String sql = "select * from cu_user where id=1";
        User user = userService.queryUser(sql);
        System.out.println(user);
    }

    @Test
    public void testQueryForList() {
        String sql = "select * from cu_user";
        List<User> users = userService.queryList(sql);
        System.out.println(users);
    }
}


