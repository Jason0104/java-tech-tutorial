package com.java.tech.mybatis.session.defaults;

import com.java.tech.mybatis.binding.DFMapperProxy;
import com.java.tech.mybatis.binding.DFMapperRegistry;
import com.java.tech.mybatis.executor.DFExecutor;
import com.java.tech.mybatis.session.DFConfiguration;
import com.java.tech.mybatis.session.DFSqlSession;

import java.lang.reflect.Proxy;

/**
 * created by Jason on 2020/3/2
 */
public class DefaultsSqlSession implements DFSqlSession {

    private final DFConfiguration configuration;
    private final DFExecutor executor;

    public DefaultsSqlSession(DFConfiguration configuration, DFExecutor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(DFMapperRegistry.DFMapperStatement statement, Object parameter) {
        return (T) executor.query(statement, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        //通过反射技术实现
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new DFMapperProxy<T>(this, type));
    }

    @Override
    public DFConfiguration getConfiguration() {
        return configuration;
    }
}
