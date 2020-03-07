package com.java.tech.impl;

import com.java.tech.dao.UserDao;
import com.java.tech.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * created by Jason on 2020/3/7
 */
@Service
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO `cu_user` (`username`, `address`, `mobile`, `isDeleted`) VALUES(?,?,?,?)";
        this.jdbcTemplate.update(sql, user.getUsername(), user.getAddress(), user.getMobile(), user.getIsDeleted());
    }
}
