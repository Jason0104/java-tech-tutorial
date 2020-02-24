package com.java.tech.adapter.response;

import java.io.Serializable;

/**
 * created by Jason on 2020/2/24
 */
public class LoginResponse implements Serializable {

    private String message;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
