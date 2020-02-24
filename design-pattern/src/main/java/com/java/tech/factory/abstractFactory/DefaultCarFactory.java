package com.java.tech.factory.abstractFactory;

/**
 * created by Jason on 2020/2/24
 */
public class DefaultCarFactory implements CarFactory {
    @Override
    public BMWCar getBMW() {
        return new BMWCarImpl();
    }

    @Override
    public BensCar getBens() {
        return new BensCarImpl();
    }
}
