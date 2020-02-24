package com.java.tech.proxy.staticProxy;

import com.java.tech.proxy.dynamicProxy.DynamicProxy;

/**
 * created by Jason on 2020/2/24
 */
public class Client {

    public static void main(String[] args) {
        //租客来租房 直接找到来中介
//        Houser fangdong = new Houser();
//
//        House houseRender = new HouseAgent(fangdong);
//        houseRender.render();

          //构建房东
          House house = (House) new DynamicProxy().getInstance(new Houser());
          house.render();

          //构建租客
          House render =(House) new DynamicProxy().getInstance(new Render());
          render.render();
    }
}
