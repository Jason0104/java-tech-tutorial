package com.java.tech.proxy.staticProxy;

/**
 * created by Jason on 2020/2/24
 */
public class Render implements House {
    @Override
    public void render() {
        System.out.println("您好,我是租客,我想找房子");
    }
}
