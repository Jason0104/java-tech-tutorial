package com.java.tech.factory.simple;

/**
 * created by Jason on 2020/2/24
 *
 * 简单工厂非常不灵活 需要动态的修改传入参数 耦合性很强
 */
public class SimpleCarFactory {

    public static Object getCar(String carName){
        if("BMW".equals(carName)){
            return new BMW();
        }else if("Bens".equals(carName)){
            return new Bens();
        }else {
            return new Audi();
        }
    }
}
