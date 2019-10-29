package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.it.constant.MessageConstant;
import com.it.constant.RedisMessageConstant;
import com.it.entity.Result;
import com.it.pojo.Order;
import com.it.service.OrderService;
import com.it.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 处理体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    //在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从redis中获取保存的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //将用户输入的验证码和redis中保存的验证码进行比对
        String validateCode = (String) map.get("validateCode");
        if(validateCode != null && validateCode.equals(codeInRedis)){
            Result result = null;
            //验证码比对成功，调用服务完成预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.ORDER_FAIL);
            }

            if(result.isFlag()){
                //如果预约成功,给用户发送预约成功短信
                try {
                    //去设置orderDate模板(String) map.get("orderDate") ----  ValidateCodeUtils.generateValidateCode4String(6)

                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                    return new Result(false, MessageConstant.ORDER_FAIL);
                }
            }
            return result;
        }else{
            //验证码比对失败，返回结果给页面。
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 根据id查询预约相关信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
