package com.java.tech.decorator;

/**
 * created by Jason on 2020/2/24
 */
public class ChineseCook implements Cook {
    @Override
    public void cookDinner() {
        System.out.println("开始做饭");
    }
}
