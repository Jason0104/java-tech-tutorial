package com.java.tech.web;

import com.java.tech.servlet.DFDispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * created by Jason on 2020/3/14
 */
public abstract class AbstractWebApplicationInitializer implements DFWebApplicationInitializer {

    public void onStartup(ServletContext context) {
        ServletRegistration.Dynamic registration = context.addServlet("dfDispatcherServlet", new DFDispatcherServlet());
        registration.addMapping("/");
    }
}
