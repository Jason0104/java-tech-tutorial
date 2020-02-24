package com.java.tech.observer;

/**
 * created by Jason on 2020/2/24
 */
public interface Task {

    void add(TaskObserver observer);

    void remove(TaskObserver observer);

    void notifyMessage();
}
