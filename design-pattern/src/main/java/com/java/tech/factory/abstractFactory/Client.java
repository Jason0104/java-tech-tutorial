package com.java.tech.factory.abstractFactory;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
        CarFactory factory = new DefaultCarFactory();
        BensCar bensCar =  factory.getBens();
        bensCar.createBens();
    }
}
