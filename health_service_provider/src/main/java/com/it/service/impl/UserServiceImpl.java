package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.it.dao.PermissionDao;
import com.it.dao.RoleDao;
import com.it.dao.UserDao;
import com.it.pojo.Permission;
import com.it.pojo.Role;
import com.it.pojo.User;
import com.it.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if(user == null){
            return null;
        }
        //获取用户id
        Integer userId = user.getId();
        //查询用户所具有的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查询权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            //角色关联权限
            role.setPermissions(permissions);
        }
        user.setRoles(roles);//让用户关联角色
        return user;
    }
}
