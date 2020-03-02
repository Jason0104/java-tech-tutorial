package com.java.tech.user;

import com.java.tech.AbstractTest;
import com.java.tech.entity.Hobby;
import com.java.tech.mapper.HobbyMapper;
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
import java.util.Arrays;

/**
 * created by Jason on 2020/2/29
 */
public class HobbyTest extends AbstractTest {

    private Logger log = LoggerFactory.getLogger(HobbyTest.class);
    private Reader reader;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private HobbyMapper hobbyMapper;

    @Before
    public void init() {
        //1.加载mybatis-config配置文件
        try {
            reader = Resources.getResourceAsReader("config/mybatis-config.xml");
            //2.构建sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            //3.构建SqlSession
            sqlSession = sqlSessionFactory.openSession();
            hobbyMapper = sqlSession.getMapper(HobbyMapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        Hobby hobby = new Hobby();
        hobby.setHobbys(Arrays.asList("football", "basketball", "walk"));
        hobbyMapper.create(hobby);
        sqlSession.commit();
    }

    @Test
    public void testQueryAll() {
        Hobby hobby = hobbyMapper.findById(2);
        System.out.println(hobby);
    }
}
