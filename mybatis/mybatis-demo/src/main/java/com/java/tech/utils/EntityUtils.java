package com.java.tech.utils;

import com.java.tech.entity.BaseEntity;
import com.java.tech.mapper.BaseMapper;

import java.util.Date;

/**
 * created by Jason on 2020/2/28
 */
public abstract class EntityUtils {

    public static <T extends BaseEntity> void init(T t) {
        Date date = new Date();
        t.setCreateTime(date);
        t.setUpdateTime(date);
    }

    public static <T extends BaseEntity> void update(T t) {
        Date date = new Date();
        t.setUpdateTime(date);
    }

    public static <T extends BaseEntity> void save(BaseMapper<T> baseMapper, T entity) {
        if (entity.getId() == 0 || entity.getId() == -1) {
            init(entity);
            baseMapper.create(entity);
        } else {
            update(entity);
            baseMapper.update(entity);
        }
    }
}
