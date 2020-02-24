package com.java.tech.proxy.staticProxy;

/**
 * created by Jason on 2020/2/24
 */
public class HouseAgent implements House {

    private Houser houser;

    public HouseAgent(Houser houser) {
        this.houser = houser;
    }

    @Override
    public void render() {
        houser.render();
    }
}
