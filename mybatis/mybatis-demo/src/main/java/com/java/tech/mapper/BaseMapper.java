package com.java.tech.mapper;

import com.java.tech.entity.BaseEntity;

/**
 * created by Jason on 2020/2/28
 */
public interface BaseMapper<T extends BaseEntity> {

    void create(T entity);

    T findById(int id);

    void update(T entity);

    void delete(int id);

}
