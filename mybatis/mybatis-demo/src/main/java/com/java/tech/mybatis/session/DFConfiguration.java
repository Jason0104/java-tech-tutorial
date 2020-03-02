package com.java.tech.mybatis.session;

import com.java.tech.mybatis.binding.DFMapperRegistry;

/**
 * created by Jason on 2020/3/1
 */
public class DFConfiguration {

    private String basePackage;
    private final DFMapperRegistry mapperRegistry = new DFMapperRegistry();

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public DFMapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}
