package com.java.tech.serviceTest;

import com.java.tech.AbstractSpringContextTest;
import com.java.tech.model.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/7
 */
public class OrderTest extends AbstractSpringContextTest {

    @Autowired
    private Order order;

    @Test
    public void testOrder() {
        String result = order.getOrderId() + ":" + order.getOrderName();
        System.out.println(result);
    }
}
