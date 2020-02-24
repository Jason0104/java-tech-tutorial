package com.java.tech.factory.methodFactory;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
        //创建宝马车
        CarFactory carFactory = new BMWFactory();

        //根据工厂类获取宝马车对象
        ICar bmwCar = carFactory.createCar();
        bmwCar.running();

    }
}
