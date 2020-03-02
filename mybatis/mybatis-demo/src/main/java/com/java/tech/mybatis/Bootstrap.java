package com.java.tech.mybatis;

import com.java.tech.entity.User;
import com.java.tech.mapper.UserMapper;
import com.java.tech.mybatis.executor.DFExecutor;
import com.java.tech.mybatis.executor.DFSimpleExecutor;
import com.java.tech.mybatis.factory.DefaultExecutorFactory;
import com.java.tech.mybatis.factory.ExecutorFactory;
import com.java.tech.mybatis.session.DFConfiguration;
import com.java.tech.mybatis.session.DFSqlSession;
import com.java.tech.mybatis.session.defaults.DefaultsSqlSession;

/**
 * created by Jason on 2020/3/2
 */
public class Bootstrap {

    public static void main(String[] args) {

        //构建Configuration
        DFConfiguration configuration = new DFConfiguration();
        configuration.setBasePackage("com.java.tech.mapper");

        //构建ExecutorFactory工厂
        ExecutorFactory factory = new DefaultExecutorFactory();

        //获得Executor
        DFExecutor executor = factory.getCachingExecutor(configuration, new DFSimpleExecutor(configuration));

        //构建SqlSession
        DFSqlSession sqlSession = new DefaultsSqlSession(configuration, executor);

        //开始sql
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryByMobile("18801790199");
        System.out.println(user);

    }
}
