package com.java.tech;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * created by Jason on 2020/4/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreApplication.class)
public abstract class AbstractSpringContext {

    protected abstract void setChild();
}
