package com.java.tech.prototype;

/**
 * created by Jason on 2020/2/23
 */
public class Rectangle extends Shape {

    public Rectangle() {
        type = "Rectangle";
    }

    @Override
    void draw() {
        System.out.println("Rectangle Draw");
    }
}
