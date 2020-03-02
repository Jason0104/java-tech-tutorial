package com.java.tech.mapper;

import com.java.tech.entity.Student;

import java.util.List;

/**
 * created by Jason on 2020/2/28
 */
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> queryAll();
}
