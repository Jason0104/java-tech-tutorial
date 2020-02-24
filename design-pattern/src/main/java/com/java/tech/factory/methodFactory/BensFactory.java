package com.java.tech.factory.methodFactory;

/**
 * created by Jason on 2020/2/24
 */
public class BensFactory implements CarFactory {

    @Override
    public ICar createCar() {
        return new BensCar();
    }
}
