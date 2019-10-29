package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.dao.OrderSettingDao;
import com.it.pojo.OrderSetting;
import com.it.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> list) {
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(countByOrderDate > 0){
                    //已经进行了预约设置
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //没有进行预约设置，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {//date：yyyy-MM
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //调用Dao，根据日期范围查询预约设置数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("date",orderSetting.getOrderDate().getDate());//获取日期数字（几号）
                map1.put("number", orderSetting.getNumber());
                map1.put("reservations", orderSetting.getReservations());
                result.add(map1);
            }
        }
        return result;
    }

    //根据日期设置可预约人数
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count > 0){
            //已经进行预约设置，修改预约人数设置
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前没有进行预约设置
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public void deleteOrderSetting(String date) {
        orderSettingDao.deleteOrderSetting(date);
    }
}
