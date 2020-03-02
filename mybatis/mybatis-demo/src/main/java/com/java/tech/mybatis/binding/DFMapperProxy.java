package com.java.tech.mybatis.binding;

import com.java.tech.mybatis.session.DFSqlSession;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * created by Jason on 2020/3/2
 * <p>
 * JDK动态代理实现
 */
@Log4j
public class DFMapperProxy<T> implements InvocationHandler, Serializable {

    private final DFSqlSession sqlSession;
    private final Class<T> mapperInterface;

    public DFMapperProxy(DFSqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //拿到sql
        DFMapperRegistry.DFMapperStatement statement = sqlSession.getConfiguration()
                .getMapperRegistry()
                .get(method.getDeclaringClass().getName() + "." + method.getName());

        if (null != statement) {
            log.info(String.format("SQL [%s],parameter [%s]", statement.getSql(), args[0]));
            return sqlSession.selectOne(statement, String.valueOf(args[0]));
        }
        return method.invoke(proxy, args);
    }
}
