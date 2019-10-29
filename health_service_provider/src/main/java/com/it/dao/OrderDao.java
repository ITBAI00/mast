package com.it.dao;

import com.it.pojo.Order;
import com.it.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface OrderDao {
    //通过条件查找预约信息查找
    List<Order> findByCondition(Order order);
    //添加预约信息
    void add(Order order);

    Map findById4Detail(Integer id);

    //获取当天新增预约人数
    Integer findOrderCountByDate(String today);

    //获取当天到诊人数
    Integer findVisitsCountByDate(String today);

    //根据日期统计预约数，统计指定日期之后的预约数
    Integer findOrderCountAfterDate(String date);

    //根据日期统计到诊数，统计指定日期之后的到诊数
    Integer findVisitsCountAfterDate(String date);

    //获取热门套餐
    List<Map<String, Object>> findHotSetmeal();
}
