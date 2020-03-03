package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.service.EchoService;
import com.java.tech.service.HelloService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * created by Jason on 2020/3/3
 */
public class AopTest extends AbstractSpringContextTest {

    @Autowired
    private HelloService helloService;

    @Autowired
    private EchoService echoService;

    @Test
    public void testAop() {
//        String message = helloService.sayHello("Jason");
//        System.out.println(message);

        String result = echoService.echo("Peter");
        System.out.println(result);
    }
}
