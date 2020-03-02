package com.java.tech.mybatis.factory;

import com.java.tech.mybatis.executor.DFExecutor;
import com.java.tech.mybatis.session.DFConfiguration;

/**
 * created by Jason on 2020/3/2
 * 定义抽象工厂
 */
public interface ExecutorFactory {

    DFExecutor getSimpleExecutor(DFConfiguration configuration);

    DFExecutor getCachingExecutor(DFConfiguration configuration, DFExecutor executor);
}
