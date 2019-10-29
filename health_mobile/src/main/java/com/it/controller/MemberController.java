package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.entity.Result;
import com.it.pojo.Member;
import com.it.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Kim
 * @Date: 2019/10/23
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    //注册服务
    @Reference
    private MemberService memberService;

    //注入redis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 会员登录
     *
     * @param map
     * @return
     */
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //根据电话，获取redis中存储对应的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //比较两个验证码
        if (codeInRedis != null && !codeInRedis.equals(validateCode)) {
            //验证码不一致
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        } else {
            //验证码一致
            //判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {
                //自动注册会员
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //成功登陆
            //写入cookie
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            //路径
            //todo  这里忘了1
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//保存时间30天
            response.addCookie(cookie);//添加到响应
            //保存会员到redis
            //todo 这里忘了2
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
    }
}
