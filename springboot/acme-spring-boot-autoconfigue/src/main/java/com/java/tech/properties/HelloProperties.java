package com.java.tech.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * created by Jason on 2020/6/11
 */

@ConfigurationProperties(prefix = "acme")
public class HelloProperties {

    private String name;
    private int age;
    private String address;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
