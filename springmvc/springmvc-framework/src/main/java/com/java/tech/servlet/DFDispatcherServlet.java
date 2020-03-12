package com.java.tech.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Jason on 2020/3/12
 */
public class DFDispatcherServlet extends DFrameworkServlet {
    @Override
    protected void onRefresh() {
        initStrategies();
    }

    private void initStrategies() {
        initHandlerMappings();
    }

    private void initHandlerMappings() {
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) {

    }
}
