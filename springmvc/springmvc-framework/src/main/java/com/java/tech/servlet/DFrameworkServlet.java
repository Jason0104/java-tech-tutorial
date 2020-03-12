package com.java.tech.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * created by Jason on 2020/3/12
 */
public abstract class DFrameworkServlet extends DFHttpServletBean {

    @Override
    protected void initServletBean() {
        onRefresh();
    }

    protected abstract void onRefresh();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    protected abstract void doService(HttpServletRequest request, HttpServletResponse response);
}
