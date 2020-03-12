package com.java.tech.iocTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.aware.SpringContainer;
import com.java.tech.impl.HelloServiceImpl;
import com.java.tech.model.Account;
import com.java.tech.model.request.AccountRequest;
import com.java.tech.service.AccountService;
import com.java.tech.service.EchoService;
import com.java.tech.service.HelloService;
import org.junit.Test;

/**
 * created by Jason on 2020/3/11
 */
public class SpringContainerTest extends AbstractSpringContextTest {

    @Test
    public void testGetBeanName() {
        AccountService accountService = (AccountService) SpringContainer.getBean("accountService");
        accountService.create(buildAccountParam());
    }

    @Test
    public void testGetBeanByClassName() {
        EchoService echoService = SpringContainer.getBean("echoService", EchoService.class);
        String result = echoService.echo("Peter");
        System.out.println(result);
    }

    @Test
    public void testGetBean() {
        HelloService helloService = SpringContainer.getBean(HelloServiceImpl.class);
        String message = helloService.sayHello("lucy");
        System.out.println(message);
    }

    private AccountRequest buildAccountParam() {
        return AccountRequest.builder().account(Account.builder().sender("lucy").receiver("lili").amount(2000.00).build()).build();
    }
}
