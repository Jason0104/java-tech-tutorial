package com.java.tech.serviceTest;

import com.java.tech.service.HelloService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * created by Jason on 2020/3/3
 */
public class HelloServiceTest {

    @Test
    public void testHelloService() {
        //把xml中定义的bean注册到IOC容器中
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:config/appcontext-service.xml");
        //从容器中获取bean
        HelloService helloService = (HelloService) applicationContext.getBean("helloService");
        String message = helloService.sayHello("Jason");
        System.out.println(message);
    }
}
