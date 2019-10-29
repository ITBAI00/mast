package com.it.service;

import com.it.pojo.User;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
public interface UserService {
    //根据用户名查找用户数据
    User findByUsername(String username);
}
