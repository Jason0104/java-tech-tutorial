package com.java.tech.singleton;

/**
 * created by Jason on 2020/2/23
 * 饿汉模式
 */
public class Hungry {

    private static final Hungry instance = new Hungry();

    private Hungry(){

    }

    public static Hungry getInstance(){
        return instance;
    }
}
