package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.constant.MessageConstant;
import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.entity.Result;
import com.it.pojo.Permission;
import com.it.pojo.Role;
import com.it.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Reference
    private RoleService roleService;

    /**
     * 角色分页查询
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = roleService.findPage(queryPageBean);
        return pageResult;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds){
        try {
            roleService.add(role,permissionIds);
        } catch (Exception e) {
            //新增成功
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        //新增失败
        return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
    }

    /**
     * 查询所有权限
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {

            List<Permission> list = roleService.findAll();
            //查询成功,前端需要list集合数据,所以成功并返回给前端页面
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,list);

        } catch (Exception e) {
            e.printStackTrace();
            //查询失败
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    /**
     * 根据id查询回显数据
     * @param id
     * @return
     */
    @RequestMapping("/fingById")
    public Result findById(Integer id){

        Role roleId = roleService.findById(id);
        if (roleId !=null){
            //查询成功
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,roleId);
        }
        return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
    }

    /**
     * 根据角色id查询对应的所有权限项id
     * @param id
     * @return
     */
    @RequestMapping("/findPermissionIdByOrleId")
    public Result findPermissionIdByOrleId(Integer id){
        try {
            List<Integer> permissionId = roleService.findPermissionIdByOrleId(id);

            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,permissionId);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 编辑
     * @param role
     * @param permissionIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds){
        try {

            roleService.edit(role,permissionIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ROLE_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }
}
