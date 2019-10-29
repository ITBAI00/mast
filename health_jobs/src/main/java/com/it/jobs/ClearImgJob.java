package com.it.jobs;

import com.it.constant.RedisConstant;
import com.it.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自动以job，实现清理垃圾图片
 */

public class ClearImgJob {
     //注入jedisPool
    @Autowired
    private JedisPool jedisPool;

    // 清理垃圾图片
    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            int count = 0;
            for (String picName : set) {
                //删除七牛云上存储的垃圾图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //删除redis集合中的垃圾图片的名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);

                System.out.println("删除了：" + (++count) + "次");
            }
        }

    }
}
