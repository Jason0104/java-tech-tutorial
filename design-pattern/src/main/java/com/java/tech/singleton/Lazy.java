package com.java.tech.singleton;

/**
 * created by Jason on 2020/2/23
 * 懒汉模式
 */
public class Lazy {
    private static Lazy instance = null;

    private Lazy() {

    }

    public static Lazy getInstance() {
        if (instance == null) {
            instance = new Lazy();
        }
        return instance;
    }
}
