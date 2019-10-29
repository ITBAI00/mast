package com.it.controller;


import com.it.constant.MessageConstant;
import com.it.entity.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关操作
 * @Author: Kim
 * @Date: 2019/10/25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //获得当前登录用户的用户名
    @PreAuthorize("hasAuthority('USER_QUERY')")
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if(user != null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
