package com.it.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.pojo.Permission;
import com.it.pojo.Role;
import com.it.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    //远程调用
    @Reference
    private UserService userService;

    /**
     * 框架根据username，查询到username,password,和所有的权限
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //从页面获取的用户名,进行远程调用,查询用户
        User user = userService.findByUsername(username);
        //进行判断
        if(user == null){
            return null;//永远登录不成功
        }
        //存储用户的所有权限
        List<GrantedAuthority> list = new ArrayList<>();
        //动态为当前用户授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //遍历角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //遍历权限集合，为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //将用户信息返回给框架：通过框架提供的User
        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);

        return securityUser;
    }
}
