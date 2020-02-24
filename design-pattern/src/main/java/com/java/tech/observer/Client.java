package com.java.tech.observer;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {

        Manager manager = new Manager();
        new WorkA(manager, "张三");
        new WorkB(manager, "李四");

        manager.assignTask("盖房子");
    }
}
