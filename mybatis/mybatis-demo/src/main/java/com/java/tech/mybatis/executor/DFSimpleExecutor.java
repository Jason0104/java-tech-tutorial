package com.java.tech.mybatis.executor;

import com.java.tech.mybatis.binding.DFMapperRegistry;
import com.java.tech.mybatis.session.DFConfiguration;
import com.java.tech.mybatis.statement.DFStatementHandler;


/**
 * created by Jason on 2020/3/1
 */
public class DFSimpleExecutor implements DFExecutor {

    private DFConfiguration configuration;

    public DFSimpleExecutor(DFConfiguration configuration) {
        this.configuration = configuration;
    }

    public DFConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(DFConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter) {
        //真正用于操作数据库
        //这里要使用StatementHandler
        DFStatementHandler statementHandler = new DFStatementHandler(configuration);
        return (T) statementHandler.query(mapperStatement, parameter);
    }
}
