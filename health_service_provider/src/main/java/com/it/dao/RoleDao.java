package com.it.dao;

import com.github.pagehelper.Page;
import com.it.pojo.Permission;
import com.it.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
public interface RoleDao {
    //根据用户id查询该用户拥有的角色
    Set<Role> findByUserId(Integer userId);

    //角色查询
    Page<Role> selectByCondition(String queryString);

    //添加角色和权限的关联
    void setRoleandPermission(Map<String, Integer> map);

    //新增角色
    void add(Role role);

    //查询所有权限
    List<Permission> findAll();

    //根据角色id回显数据
    Role findById(Integer id);

    //根据角色id查询权限id
    List<Integer> findPermissionIdByOrleId(Integer id);

    //删除关联
    void deleteAssociation(Integer id);

    //修改
    void edit(Role role);

    //删除
    void delete(Integer id);
}
