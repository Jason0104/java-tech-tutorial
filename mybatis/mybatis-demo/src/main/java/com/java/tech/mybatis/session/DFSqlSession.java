package com.java.tech.mybatis.session;

import com.java.tech.mybatis.binding.DFMapperRegistry;

/**
 * created by Jason on 2020/3/2
 */
public interface DFSqlSession {

    <T> T selectOne(DFMapperRegistry.DFMapperStatement statement, Object parameter);

    <T> T getMapper(Class<T> type);

    DFConfiguration getConfiguration();
}
