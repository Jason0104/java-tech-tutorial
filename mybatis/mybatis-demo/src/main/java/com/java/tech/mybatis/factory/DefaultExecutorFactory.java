package com.java.tech.mybatis.factory;

import com.java.tech.mybatis.executor.DFCachingExecutor;
import com.java.tech.mybatis.executor.DFExecutor;
import com.java.tech.mybatis.executor.DFSimpleExecutor;
import com.java.tech.mybatis.session.DFConfiguration;

/**
 * created by Jason on 2020/3/2
 */
public class DefaultExecutorFactory implements ExecutorFactory {
    @Override
    public DFExecutor getSimpleExecutor(DFConfiguration configuration) {
        return new DFSimpleExecutor(configuration);
    }

    @Override
    public DFExecutor getCachingExecutor(DFConfiguration configuration, DFExecutor executor) {
        return new DFCachingExecutor(configuration, executor);
    }
}
