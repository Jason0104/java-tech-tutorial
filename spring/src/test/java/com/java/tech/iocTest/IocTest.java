package com.java.tech.iocTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.aware.CloudEngine;
import com.java.tech.model.Merchant;
import com.java.tech.model.Operator;
import com.java.tech.service.CloudEngineService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * created by Jason on 2020/3/11
 */
public class IocTest extends AbstractSpringContextTest {

    ClassPathXmlApplicationContext container = null;

    @Before
    public void init() {
        container = new ClassPathXmlApplicationContext("classpath*:config/appcontext-*.xml");
    }

    @Test
    public void testInit() {
        CloudEngineService engineService = (CloudEngineService) container.getBean("cloudEngine");
        engineService.init();
    }

    @Test
    public void testBeanName() {
        Merchant merchant = (Merchant) container.getBean("merchant");
        Operator operator = (Operator) container.getBean("operator");
        System.out.println(merchant);
        System.out.println(operator);
    }

}
