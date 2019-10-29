package com.it.dao;

import com.github.pagehelper.Page;
import com.it.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * @Author: Kim
 * @Date: 2019/10/25
 */
public interface PermissionDao {
    //根据角色id查询具有的权限
    Set<Permission> findByRoleId(Integer roleId);

    //权限控制分页
    Page<Permission> selectByCondition(String queryString);

    //新增权限
    void add(Permission permission);

    //编辑权限
    void edit(Permission permission);

    //删除前先检查是否与角色关联
    long findCountByPermissionId(Integer id);

    //删除角色
    void delete(Integer id);

    //查询所有
    List<Permission> findAll();
}
