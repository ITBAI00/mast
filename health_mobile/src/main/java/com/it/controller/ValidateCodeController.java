package com.it.controller;

import com.aliyuncs.exceptions.ClientException;
import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.entity.Result;
import com.it.utils.SMSUtils;
import com.it.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //用户在线体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //使用工具类，产生随机的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        if(telephone != null && telephone.length() > 0){
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, validateCode.toString());
            } catch (ClientException e) {
                //验证码发送失败
                e.printStackTrace();
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //将验证码保存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 120*60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户快速登录发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //使用工具类，产生随机的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        if(telephone != null && telephone.length() > 0){
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, validateCode.toString());
            } catch (ClientException e) {
                //验证码发送失败
                e.printStackTrace();
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //将验证码保存到redis
        //todo 验证码未开启存储
        //jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5*60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
