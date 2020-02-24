package com.java.tech.observer;

/**
 * created by Jason on 2020/2/24
 */
public class WorkA implements TaskObserver {
    private Manager manager;
    private String name;

    public WorkA(Manager manager, String name) {
        this.manager = manager;
        this.name = name;
        manager.add(this);
    }

    @Override
    public void update(String message) {
        System.out.println(name + "已收到" + message + "任务");
    }
}
