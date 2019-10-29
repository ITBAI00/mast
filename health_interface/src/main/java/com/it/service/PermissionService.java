package com.it.service;

import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.Permission;

import java.util.List;

public interface PermissionService {

    //权限控制分页接口
    PageResult pageQuery(QueryPageBean queryPageBean);

    //新增权限
    void add(Permission permission);

    //编辑权限
    void edit(Permission permission);

    //根据id删除权限
    void delete(Integer id);

    //查询所权限
    List<Permission> findAll();
}
