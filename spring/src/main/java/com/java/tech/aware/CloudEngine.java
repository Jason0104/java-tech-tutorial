package com.java.tech.aware;

import com.java.tech.service.CloudEngineService;
import org.springframework.beans.factory.InitializingBean;

/**
 * created by Jason on 2020/3/12
 */
public class CloudEngine implements InitializingBean, CloudEngineService {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Bean开始初始化");
    }


    @Override
    public void init() {
        System.out.println("初始化方法");
    }
}
