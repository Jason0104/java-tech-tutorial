package com.java.tech.prototype;

/**
 * created by Jason on 2020/2/23
 */
public class Square extends Shape {

    public Square() {
        type = "Square";
    }

    @Override
    void draw() {
        System.out.println("Square Draw");
    }
}
