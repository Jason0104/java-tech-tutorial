package com.java.tech.prototype;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Jason on 2020/2/23
 */
public class ShapeFactory {

    private static Map<String,Shape> shapeMap = new ConcurrentHashMap<String,Shape>();

    public static Shape getShape(String shapeId){
        return shapeMap.get(shapeId);
    }

    public static Map<String,Shape> getCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(), rectangle);

        return shapeMap;
    }

    //测试代码
    public static void main(String[] args) {
        shapeMap = getCache();
        Shape shape = getShape("2");
        System.out.println(shape.getType());
    }

}
