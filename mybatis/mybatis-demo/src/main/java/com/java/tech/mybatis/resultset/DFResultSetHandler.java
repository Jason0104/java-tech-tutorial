package com.java.tech.mybatis.resultset;

import com.java.tech.mybatis.session.DFConfiguration;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * created by Jason on 2020/3/1
 */
public class DFResultSetHandler {

    private final DFConfiguration configuration;

    public DFResultSetHandler(DFConfiguration configuration) {
        this.configuration = configuration;
    }

    public <E> E handleResultSets(ResultSet resultSet, Class<?> type) throws Exception {
        Object obj = new DefaultObjectFactory().create(type);
        if (resultSet.next()) {
            int i = 0;
            //用到了反射
            for (Field field : obj.getClass().getDeclaredFields()) {
                setValue(obj, field, resultSet, i);
            }

        }
        return (E) obj;
    }

    private void setValue(Object obj, Field field, ResultSet resultSet, int i) throws Exception {
        Method method = obj.getClass().getMethod("set" + upperCapital(field.getName()), field.getType());
        method.invoke(obj, getResult(field, resultSet));
    }

    private Object getResult(Field field, ResultSet resultSet) throws Exception {
        Class<?> type = field.getType();
        if (Integer.class == type) {
            return resultSet.getInt(field.getName());
        } else if (String.class == type) {
            return resultSet.getString(field.getName());
        }
        return resultSet.getString(field.getName());
    }

    private String upperCapital(String name) {
        String head = name.substring(0, 1);
        String tail = name.substring(1);
        return head.toUpperCase() + tail;
    }
}
