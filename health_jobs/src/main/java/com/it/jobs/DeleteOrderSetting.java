package com.it.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.service.OrderSettingService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeleteOrderSetting {
   @Reference
   private OrderSettingService orderSettingService;

 public void deleteOrderSetting (){
      //获取当前日期
      Calendar calendar = Calendar.getInstance();
      Date time = calendar.getTime();
      String date = new SimpleDateFormat("yyyy-MM-dd").format(time);
      //调用service方法
      orderSettingService.deleteOrderSetting(date);
      System.out.println("1");
  }
}
