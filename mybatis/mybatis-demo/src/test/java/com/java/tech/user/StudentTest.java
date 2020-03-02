package com.java.tech.user;

import com.java.tech.entity.Student;
import com.java.tech.mapper.StudentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;

/**
 * created by Jason on 2020/2/28
 */
public class StudentTest {

    private Logger log = LoggerFactory.getLogger(StudentTest.class);
    private Reader reader;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private StudentMapper studentMapper;

    @Before
    public void init() {
        //1.加载mybatis-config配置文件
        try {
            reader = Resources.getResourceAsReader("config/mybatis-config.xml");
            //2.构建sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            //3.构建SqlSession
            sqlSession = sqlSessionFactory.openSession();
            studentMapper = sqlSession.getMapper(StudentMapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        Student student = new Student();
        student.setName("chen1");
        student.setAddress("shanghai");
        Date date = new Date();
        student.setCreateTime(date);
        student.setUpdateTime(date);
        studentMapper.create(student);
        sqlSession.commit();
    }

    @Test
    public void testFindById() {
        Student student = studentMapper.findById(6);
        System.out.println(student);
    }

    @Test
    public void testQueryAll() {
        List<Student> studentList = studentMapper.queryAll();
        System.out.println(studentList);
    }
}
