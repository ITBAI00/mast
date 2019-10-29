package com.it.service;

import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.Permission;
import com.it.pojo.Role;

import java.util.List;

public interface RoleService {
    //角色分页查询
    PageResult findPage(QueryPageBean queryPageBean);

    //新增角色
    void add(Role role, Integer[] permissionIds);

    //查询所有权限
    List<Permission> findAll();

    //根据角色id回显数据
    Role findById(Integer id);

    //根据角色id查询所哟权限id
    List<Integer> findPermissionIdByOrleId(Integer id);

    //编辑
    void edit(Role role, Integer[] permissionIds);

    //删除
    void delete(Integer id);
}
