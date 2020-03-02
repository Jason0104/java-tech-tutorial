package com.java.tech;

import com.java.tech.entity.Role;
import com.java.tech.mapper.RoleMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by Jason on 2020/3/1
 */
public class RoleDaoTest extends AbstractSpringContextTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testInsert() {
        Role role = new Role();
        role.setName("test");
        roleMapper.create(role);
    }

    @Test
    public void testFindById() {
        Role role = roleMapper.findById(2);
        System.out.println(role);
    }
}
