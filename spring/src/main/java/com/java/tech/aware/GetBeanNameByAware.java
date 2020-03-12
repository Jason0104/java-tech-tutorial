package com.java.tech.aware;

import org.springframework.beans.factory.BeanNameAware;

/**
 * created by Jason on 2020/3/11
 */
public class GetBeanNameByAware implements BeanNameAware {
    @Override
    public void setBeanName(String name) {
        System.out.println("从容器中获得Bean实例的名称是:" + name);
    }
}
