package com.java.tech.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * created by Jason on 2020/3/4
 */
@Component
@Aspect
public class MonitorMethodExecutionAspect {

    private Logger log = LoggerFactory.getLogger(MonitorMethodExecutionAspect.class);

    //匹配类下的所有方法
    @Pointcut("within(com.java.tech.impl.*)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object monitorMethodExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        //开始计时
        stopWatch.start();

        //正在用于方法执行
        Object obj = joinPoint.proceed();

        //计时结束
        stopWatch.stop();

        reportMonitor(joinPoint.getSignature().toShortString(), stopWatch.getTotalTimeMillis());
        return obj;
    }

    private void reportMonitor(String methodName, long totalTimeMillis) {
        log.info("method {} invoked,cost time:{}ms", methodName, totalTimeMillis);
    }

}
