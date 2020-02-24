package com.java.tech.singleton;

/**
 * created by Jason on 2020/2/23
 * 静态内部类
 */
public class StaticInner {

    private StaticInner() {
    }

    //定义内部类
    private static class LazyHolder {
        public final static StaticInner instance = new StaticInner();
    }

    public static StaticInner getInstance(){
        return LazyHolder.instance;
    }
}
