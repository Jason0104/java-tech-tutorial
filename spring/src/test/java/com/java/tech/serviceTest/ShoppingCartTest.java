package com.java.tech.serviceTest;

import com.java.tech.model.ShoppingCart;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * created by Jason on 2020/3/3
 */
public class ShoppingCartTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:config/appcontext-service.xml");
        ShoppingCart shoppingCart = (ShoppingCart) applicationContext.getBean("shoppingCart");
        List<String> fruits = shoppingCart.getFruits();

        System.out.println("获得list的值为:" + fruits);
        System.out.println("获得string的值为:" + shoppingCart.getName());
        System.out.println("获得map的值为:" + shoppingCart.getBasicInfo());
        System.out.println("获得vip的信息为:" + shoppingCart.getBasicInfo().get("vip"));
    }
}
