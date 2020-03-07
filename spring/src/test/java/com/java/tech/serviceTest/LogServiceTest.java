package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.aop.LogService;
import com.java.tech.service.HelloService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/4
 */

public class LogServiceTest extends AbstractSpringContextTest {

    @Autowired
    private LogService logService;

    @Autowired
    private HelloService helloService;

    @Test
    public void testLog() throws Exception {
        logService.log("hello");
//        logService.getExceptionMethod();
//        String result = helloService.sayHello("Jason");
//        System.out.println(result);
    }
}
