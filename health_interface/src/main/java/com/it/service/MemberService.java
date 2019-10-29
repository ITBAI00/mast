package com.it.service;

import com.it.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author: Kim
 * @Date: 2019/10/23
 */

public interface MemberService {
    //通过电话查找会员
    Member findByTelephone(String telephone);

    //注册会员
    void add(Member member);

    //通过月份查询对应的会员数量
    List<Integer> findMemberCountByMonth(List<String> months);

    //获取当天新增会员
    Integer findMemberCountByDate(String today);

    //获取所有的会员人数
    Integer findMemberTotalCount();

    //获取本周新增会员
    Integer findMemberCountAfterDate(String thisWeekMonday);

    List<Map<String, Object>> findSex();

    List<Map<String, Object>> findAge();

}
