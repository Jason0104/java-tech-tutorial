package com.java.tech.strategy.response;

import java.io.Serializable;

/**
 * created by Jason on 2020/2/24
 */
public class BaseResponse implements Serializable {

    private String returnCode;
    private Object msg;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "returnCode='" + returnCode + '\'' +
                ", msg=" + msg;
    }
}
