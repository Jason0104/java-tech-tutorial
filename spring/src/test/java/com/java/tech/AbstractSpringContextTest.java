package com.java.tech;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by Jason on 2020/3/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:config/appcontext-*.xml"})
public abstract class AbstractSpringContextTest {
}
