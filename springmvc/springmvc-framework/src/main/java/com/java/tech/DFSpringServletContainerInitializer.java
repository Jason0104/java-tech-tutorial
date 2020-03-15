package com.java.tech;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * created by Jason on 2020/3/14
 */
@HandlesTypes(WebApplicationInitializer.class)
public class DFSpringServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> clazz, ServletContext ctx) throws ServletException {
        for (Class<?> clazzInfo : clazz) {
            try {
                //使用反射技术来执行onStartup方法
                Object obj = clazzInfo.newInstance();
                Method method = clazzInfo.getMethod("onStartup", ServletContext.class);
                method.invoke(obj, ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
