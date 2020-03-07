package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.Member;
import com.java.tech.service.HelloService;
import com.java.tech.service.MemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/4
 */
public class MonitorTest extends AbstractSpringContextTest {

    @Autowired
    private HelloService helloService;

    @Autowired
    private MemberService memberService;

    @Test
    public void testMonitor() {
        String result = helloService.sayHello("Jason");
        System.out.println(result);
    }

}
