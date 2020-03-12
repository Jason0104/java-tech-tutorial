package com.java.tech.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * created by Jason on 2020/3/12
 */
public abstract class DFHttpServletBean extends HttpServlet {

    @Override
    public void init() throws ServletException {
        initServletBean();
    }

    //定义一个抽象方法 这里使用模版方法模式
    protected abstract void initServletBean();
}
