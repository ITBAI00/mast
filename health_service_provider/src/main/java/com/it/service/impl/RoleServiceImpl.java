package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.dao.RoleDao;
import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.Permission;
import com.it.pojo.Role;
import com.it.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    //角色分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage(); //当前页码
        Integer pageSize = queryPageBean.getPageSize(); //每页显示的记录数
        String queryString = queryPageBean.getQueryString(); //查询条件

        //分页助手
        PageHelper.startPage(currentPage,pageSize);

        //调用dao进行条件查询
        Page<Role> page = roleDao.selectByCondition(queryString);

        //获取总记录数
        long total = page.getTotal();

        //获取当前页数据
        List<Role> rows = page.getResult();

        return new PageResult(total,rows);
    }

    //设置检查组和检查项关联  方法抽取
    public void setRoleandPermission(Integer roleId,Integer[] PermissionIds){

        if (PermissionIds != null && PermissionIds.length > 0){
            for (Integer checkitemId : PermissionIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("role_id",roleId);
                map.put("permission_id",checkitemId);
                roleDao.setRoleandPermission(map);
            }
        }
    }

    //新增角色
    public void add(Role role, Integer[] permissionIds) {
        roleDao.add(role);
        //调用方法setCheckGroupandCheckItem
        this.setRoleandPermission(role.getId(),permissionIds);
    }

    //查询所有权限
    public List<Permission> findAll() {
        return roleDao.findAll();
    }

    //根据角色id回显数据
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    //根据角色id查询所有权限id
    public List<Integer> findPermissionIdByOrleId(Integer id) {
        return roleDao.findPermissionIdByOrleId(id);
    }

    //编辑角色，同时需要更新和权限的关联关系
    public void edit(Role role, Integer[] permissionIds) {
        //修改角色基本信息,操作角色t_role表
        roleDao.edit(role);

        //清理当前角色关联的检查项,操作中间关系表t_role_permission表
        roleDao.deleteAssociation(role.getId());

        //重新建立当前检查组和检查项的关联关系
        this.setRoleandPermission(role.getId(),permissionIds);
    }

    //删除角色
    public void delete(Integer id) {
        //清理角色和权限的关联
        roleDao.deleteAssociation(id);

        //根据id删除t_role表
        roleDao.delete(id);
    }
}
