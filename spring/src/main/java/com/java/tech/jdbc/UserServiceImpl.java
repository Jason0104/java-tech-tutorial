package com.java.tech.jdbc;

import com.java.tech.model.User;
import com.java.tech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * created by Jason on 2020/3/5
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate userTemplate;

    @Override
    public User queryUser(String sql) {
        return userTemplate.queryForObject(sql, new UserRowMapper());
    }

    @Override
    public List<User> queryList(String param) {
        return userTemplate.query(param, new UserRowMapper());
    }
}
