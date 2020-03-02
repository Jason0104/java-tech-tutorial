package com.java.tech.mapper;

import com.java.tech.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Jason on 2020/2/28
 */
public interface UserMapper extends BaseMapper<User> {

    User queryByMobile(@Param("mobile") String mobile);

    List<User> queryUserByName(@Param("username") String username);

    List<User> queryUserList(@Param("userlist") List<String> userlist);

}
