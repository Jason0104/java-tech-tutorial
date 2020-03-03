package com.java.tech.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * created by Jason on 2020/3/3
 */
@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.java.tech.aop.MyLog)")
    public void annotationPointCut() {
        System.out.println("annotationPointCut");
    }

    @After("annotationPointCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        System.out.println("注解日志等级:" + myLog.value());
    }

    @Before("execution(* com.java.tech.impl..*.*(..))")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("方法名称:" + method.getName());
    }
}
