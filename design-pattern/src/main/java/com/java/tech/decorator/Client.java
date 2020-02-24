package com.java.tech.decorator;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
//      Cook cook = new ChineseCook();
//      cook.cookDinner();

        Cook cook1 = new WashHandsCook(new ChineseCook());
        cook1.cookDinner();
    }
}
