package com.java.tech.factory.abstractFactory;

/**
 * created by Jason on 2020/2/24
 */
public class BMWCarImpl implements BMWCar {
    @Override
    public void createBMW() {
        System.out.println("创建BMW Car");
    }
}
