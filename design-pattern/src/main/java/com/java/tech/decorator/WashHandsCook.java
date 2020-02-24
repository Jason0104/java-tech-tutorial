package com.java.tech.decorator;

/**
 * created by Jason on 2020/2/24
 */
public class WashHandsCook extends CookWrapper {
    public WashHandsCook(Cook cook) {
        this.cook = cook;
    }

    @Override
    public void cookDinner() {
        System.out.println("先洗手");
        cook.cookDinner();
    }
}
