package com.java.tech.adapter.request;

import java.io.Serializable;

/**
 * created by Jason on 2020/2/24
 */
public class LoginRequest implements Serializable {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
