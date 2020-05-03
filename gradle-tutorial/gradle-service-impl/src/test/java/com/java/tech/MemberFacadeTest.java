package com.java.tech;

import com.java.tech.service.MemberFacade;
import org.junit.Test;

/**
 * created by Jason on 2020/5/1
 */
public class MemberFacadeTest {

    @Test
    public void testQueryMember() {
        MemberFacade memberFacade = new MemberFacadeImpl();
        String result = memberFacade.sayHello("Jason");
        System.out.println(result);
    }
}
