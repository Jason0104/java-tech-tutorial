package com.java.tech.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * created by Jason on 2020/3/4
 * 1.记录某个方法调用需要有日志,记录调用的参数和结果
 * 2.当方法调用抛出异常时,有特殊处理,比如打印异常日志和报警
 */
@Component
@Aspect
public class LogAopAspect {

    private Logger log = LoggerFactory.getLogger(LogAopAspect.class);

    //定义pointcut,within切点标志符匹配类LogService下所有joinPoint
    @Pointcut("within(LogService)")
    public void pointCut() {
    }

    //定义前置通知,方法调用之前执行,打印调用的方法名和调用的参数
    @Before("pointCut()")
    public void logMethodInvokeParameter(JoinPoint joinPoint) {
        log.info("before method {} invoke,param:{}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    //定义后置通知 方法调用成功后打印出方法名
    @AfterReturning(pointcut = "pointCut()", returning = "result")
    public void logMethodInvokeResult(JoinPoint joinPoint, Object result) {
        log.info("after method {} invoke,result:", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    //这个advice会在指定的joinpoint抛出异常时执行,打印异常的信息
    @AfterThrowing(pointcut = "pointCut()", throwing = "exception")
    public void logInvokeException(JoinPoint joinPoint, Exception exception) {
        log.info("method {} invoke exception:{}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }


}
