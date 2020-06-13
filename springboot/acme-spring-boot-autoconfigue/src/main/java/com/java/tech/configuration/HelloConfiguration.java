package com.java.tech.configuration;

import com.java.tech.properties.HelloProperties;
import com.java.tech.service.HelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Jason on 2020/6/11
 */
@Configuration
@EnableConfigurationProperties(HelloProperties.class)
public class HelloConfiguration {

    private final HelloProperties properties;

    public HelloConfiguration(HelloProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HelloService getHelloService() {
        return new HelloService(properties.getName(), properties.getAge(), properties.getAddress());
    }
}
