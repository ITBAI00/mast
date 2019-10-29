package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.dao.MemberDao;
import com.it.dao.OrderDao;
import com.it.service.ReportService;
import com.it.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Kim
 * @Date: 2019/10/26
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception{
        Map<String,Object> map = new HashMap<>();
        //获取当天日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获取本周一的日期
        String thisWeekMonday  = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //获取当天新增会员
        Integer todayNewMember = memberDao.findMemberCountByDate(today);
        //获取总会员人数
        Integer totalMember = memberDao.findMemberTotalCount();
        //获取本周新增会员人数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);
        //获取本月新增会员人数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //获取当天预约人数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //获取当天到诊人数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
        //获取本周预约人数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
        //获取本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
        //获取本月预约人数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
        //获取本月到诊人数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //获取热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        map.put("reportDate", today);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrderNumber);
        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        map.put("hotSetmeal", hotSetmeal);

        return map;
    }
}
