package com.java.tech.proxy.staticProxy;

/**
 * created by Jason on 2020/2/24
 */
public class Houser implements House {
    @Override
    public void render() {
        System.out.println("您好,我是房东,房子目前委托给中介");
    }
}
