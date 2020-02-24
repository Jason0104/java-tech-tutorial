package com.java.tech.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Jason on 2020/2/24
 */
public class Manager implements Task {

    private List<TaskObserver> observerList = new ArrayList<>();
    private String message;

    @Override
    public void add(TaskObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void remove(TaskObserver observer) {
        int index = observerList.indexOf(observer);
        if (index > 0) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyMessage() {
        for (int i = 0; i < observerList.size(); i++) {
            TaskObserver observer = observerList.get(i);
            observer.update(message);
        }
    }

    public void assignTask(String taskName) {
        this.message = taskName;
        System.out.println("接到任务:" + taskName);
        this.notifyMessage();
    }
}
