package com.java.tech.mybatis.binding;

import com.java.tech.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * created by Jason on 2020/3/1
 */
public class DFMapperRegistry {

    private static final Map<String, DFMapperStatement> methodMapping = new HashMap<>();

    public DFMapperRegistry() {
        //这里需要手动模拟sql
        methodMapping.putIfAbsent("com.java.tech.mapper.UserMapper.queryByMobile", new DFMapperStatement("select * from cu_user where mobile=%s", User.class));
    }

    /**
     * DFMapperStatement用于保存映射器节点的信息
     *
     * @param <T>
     */
    public class DFMapperStatement<T> {
        private String sql;
        private Class<T> type;

        public DFMapperStatement(String sql, Class<T> type) {
            this.sql = sql;
            this.type = type;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Class<T> getType() {
            return type;
        }

        public void setType(Class<T> type) {
            this.type = type;
        }
    }

    public DFMapperStatement get(String namespace) {
        return methodMapping.get(namespace);
    }
}
