package com.java.tech.jdbc;

import com.java.tech.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * created by Jason on 2020/3/5
 */
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("id");
        String username = rs.getString("username");
        String address = rs.getString("address");
        String mobile = rs.getString("mobile");

        User user = User.builder().id(userId).username(username).address(address).mobile(mobile).build();
        return user;
    }
}
