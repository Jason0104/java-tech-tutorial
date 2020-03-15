package com.java.tech.servlet;

import com.java.tech.web.method.DFHandlerMethod;
import com.java.tech.web.view.DFModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * created by Jason on 2020/3/14
 */
public class DFHandlerExecutionChain {

    private DFHandlerMethod handlerMethod;

    public DFHandlerExecutionChain(DFHandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public DFModelAndView handler() throws InvocationTargetException, IllegalAccessException {
        Method method = handlerMethod.getMethod();
        Object obj = handlerMethod.getBean();

        Object viewName = method.invoke(obj, null);
        DFModelAndView modelAndView = new DFModelAndView((String) viewName);
        return modelAndView;
    }
}
