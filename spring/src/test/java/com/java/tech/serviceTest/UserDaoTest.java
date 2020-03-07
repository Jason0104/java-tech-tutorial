package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.dao.UserDao;
import com.java.tech.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/7
 */
public class UserDaoTest extends AbstractSpringContextTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void addUser() {
        User user = User.builder().username("tonny").address("wuhan").mobile("15216602980").isDeleted(0).build();
        userDao.addUser(user);
    }
}
