package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.service.EchoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/3
 */
public class EchoServiceTest extends AbstractSpringContextTest {

    @Autowired
    private EchoService echoService;

    @Test
    public void testEchoService() {
        String result = echoService.echo("Jason");
        System.out.println(result);
    }
}
