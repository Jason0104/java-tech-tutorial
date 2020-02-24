package com.java.tech.prototype;

/**
 * created by Jason on 2020/2/23
 */
public class Circle extends Shape {

    public Circle() {
        type = "Circle";
    }

    @Override
    void draw() {
        System.out.println("Circle Draw");
    }
}
