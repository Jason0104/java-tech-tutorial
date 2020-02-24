package com.java.tech.factory.abstractFactory;

/**
 * created by Jason on 2020/2/24
 * 抽象工厂不需要用户传入参数,需要什么去工厂类拿什么
 */
public interface CarFactory {

    BMWCar getBMW();

    BensCar getBens();
}
