package com.java.tech.factory.simple;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {

        BMW bmw = (BMW) SimpleCarFactory.getCar("BMW");
        System.out.println(bmw);
    }
}
