package com.java.tech.user;

import com.java.tech.AbstractTest;
import com.java.tech.entity.User;
import com.java.tech.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * created by Jason on 2020/2/28
 */
public class UserTest extends AbstractTest {

    private Reader reader;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private UserMapper userMapper;

    @Before
    public void init() {
        //1.加载mybatis-config配置文件
        try {
            reader = Resources.getResourceAsReader("config/mybatis-config.xml");
            //2.构建sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            //3.构建SqlSession
            sqlSession = sqlSessionFactory.openSession();
            userMapper = sqlSession.getMapper(UserMapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            User user = new User();
            user.setUsername("test");
            user.setAddress("wuhan");
            user.setMobile("18601705888");

            userMapper.create(user);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindById() {
        User user = userMapper.findById(5);
        System.out.println(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(4);
        user.setUsername("tony");
        user.setAddress("wuhan");
        user.setMobile("18801790199");
        userMapper.update(user);
        sqlSession.commit();
    }

    @Test
    public void testDeleteUser() {
        userMapper.delete(2);
        sqlSession.commit();
    }

    @Test
    public void testQueryByMobile() {
        User user = userMapper.queryByMobile("18801790190");
        System.out.println(user);
    }

    @Test
    public void testQueryUserByName() {
        List<User> userList = userMapper.queryUserByName("tom");
        System.out.println(userList);
    }
}
