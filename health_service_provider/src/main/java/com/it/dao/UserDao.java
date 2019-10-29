package com.it.dao;

import com.it.pojo.User;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
public interface UserDao {
    //根据用户名查询User基本信息
    User findByUsername(String username);
}
