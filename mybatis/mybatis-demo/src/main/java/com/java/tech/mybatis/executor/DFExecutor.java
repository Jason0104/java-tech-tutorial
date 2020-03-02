package com.java.tech.mybatis.executor;

import com.java.tech.mybatis.binding.DFMapperRegistry;

/**
 * created by Jason on 2020/3/1
 */
public interface DFExecutor {

    <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter);
}
