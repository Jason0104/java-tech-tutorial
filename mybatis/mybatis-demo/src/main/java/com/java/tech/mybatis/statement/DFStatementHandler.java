package com.java.tech.mybatis.statement;

import com.java.tech.mybatis.binding.DFMapperRegistry;
import com.java.tech.mybatis.resultset.DFResultSetHandler;
import com.java.tech.mybatis.session.DFConfiguration;
import org.apache.ibatis.executor.result.DefaultResultHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * created by Jason on 2020/3/1
 */
public class DFStatementHandler {

    private final DFConfiguration configuration;
    private final DFResultSetHandler resultHandler;

    public DFStatementHandler(DFConfiguration configuration) {
        this.configuration = configuration;
        this.resultHandler = new DFResultSetHandler(configuration);
    }

    public <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter) {
        //建立数据库连接
        try {
            Connection connection = getConnection();
            PreparedStatement pst = connection.prepareStatement(String.format(mapperStatement.getSql(), parameter));
            pst.execute();
            //由resultHandler处理返回结果
            return (T) resultHandler.handleResultSets(pst.getResultSet(), mapperStatement.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //这个地方直接hard code
    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?allowMultiQueries=true", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
