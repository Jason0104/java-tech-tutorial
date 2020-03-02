package com.java.tech.mybatis.executor;

import com.java.tech.mybatis.binding.DFMapperRegistry;
import com.java.tech.mybatis.session.DFConfiguration;
import com.java.tech.mybatis.statement.DFStatementHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * created by Jason on 2020/3/1
 * <p>
 * 这里采用了委派设计模式
 */
public class DFCachingExecutor implements DFExecutor {

    private DFExecutor delegate;
    private DFConfiguration configuration;

    //缓存查询结果
    private Map<String, Object> localCache = new HashMap<>();


    public DFCachingExecutor(DFConfiguration configuration, DFExecutor executor) {
        this.configuration = configuration;
        this.delegate = executor;
    }

    public DFCachingExecutor(DFExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter) {
        Object resultObj = localCache.get(mapperStatement.getSql());
        //如果缓存有结果 则返回缓存内容
        if (resultObj != null) {
            return (T) resultObj;
        }
        //没有则查询
        //这里使用了委派模式
        resultObj = delegate.query(mapperStatement, parameter);
        localCache.putIfAbsent(mapperStatement.getSql(), resultObj);
        return (T) resultObj;
    }
}
