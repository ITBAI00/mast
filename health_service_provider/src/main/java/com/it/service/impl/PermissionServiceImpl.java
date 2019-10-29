package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.dao.PermissionDao;
import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.Permission;
import com.it.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@RestController
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    //权限分页
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取查询参数
        Integer currentPage = queryPageBean.getCurrentPage(); //页码
        Integer pageSize = queryPageBean.getPageSize(); //每页显示记录数
        String queryString = queryPageBean.getQueryString(); //查询条件

        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);

        //调用dao进行条件查询
        Page<Permission> page = permissionDao.selectByCondition(queryString);
        //获取总记录数
        long total = page.getTotal();
        //获取当前页数据
        List<Permission> rows = page.getResult();

        return new PageResult(total,rows);
    }

    //新增权限
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    //编辑权限
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    //根据id删除权限
    public void delete(Integer id) {
        //查询当前权限项是否和角色关联
        long count = permissionDao.findCountByPermissionId(id);
        if (count > 0){
            //当前权限被引用，不能删除
            throw new RuntimeException("当前权限被引用，不能删除");

        }
        permissionDao.delete(id);
    }

    //查询所有
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
