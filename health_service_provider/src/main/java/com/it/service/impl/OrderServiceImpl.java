package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.constant.MessageConstant;
import com.it.dao.MemberDao;
import com.it.dao.OrderDao;
import com.it.dao.OrderSettingDao;
import com.it.entity.Result;
import com.it.pojo.Member;
import com.it.pojo.Order;
import com.it.pojo.OrderSetting;
import com.it.service.OrderService;
import com.it.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务实现类
 *
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;//注入预约设置
    @Autowired
    private MemberDao memberDao;//注入会员dao接口
    @Autowired
    private OrderDao orderDao;//注入预约dao接口

    /**
     *添加体检预约
     * @param map 预约表单数据
     * @return 预约结果
     */
    @Override
    public Result order(Map map) throws Exception {
        //检查当天是否进行了预约设置
        String orderDate = (String) map.get("orderDate");//预约日期
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约满
        int reservations = orderSetting.getReservations();//当天已预约人数
        int number = orderSetting.getNumber();//当天可预约人数
        if(reservations >= number){
            //已预约满
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //校验当前用户是否为会员,根据手机号来判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复注册
        if(member != null){
            Integer memberId = member.getId();//会员id
            int setmealId = Integer.parseInt((String) map.get("setmealId"));//预约套餐id
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经预约成功，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //可以预约，当前预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //保存预约信息到预约表
        Order order = new Order(member.getId(), date, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 根据预约id查询预约相关信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
