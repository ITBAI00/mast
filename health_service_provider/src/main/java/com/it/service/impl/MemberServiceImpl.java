package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.it.dao.MemberDao;
import com.it.pojo.Member;
import com.it.service.MemberService;
import com.it.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Kim
 * @Date: 2019/10/23
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    //注入接口
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据电话查找
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberDao.findByTelephone(telephone);
        return member;
    }

    /**
     * 添加会员
     * @param member
     */
    @Override
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    /**
     * 根据月份查询当时会员的总人数。
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            //给月份拼接当月最后一天
            month = month + "-31";//2019-08-31
            Integer count = memberDao.findMemberCountBeforeDate(month);
            list.add(count);
        }
        return list;
    }

    /**
     * 获取当天新增会员
     * @param today
     * @return
     */
    @Override
    public Integer findMemberCountByDate(String today) {
        return memberDao.findMemberCountByDate(today);
    }

    /**
     * 获取会员的总人数
     * @return
     */
    @Override
    public Integer findMemberTotalCount() {
        return memberDao.findMemberTotalCount();
    }

    /**
     * 获取本周新增会员人数
     * @param thisWeekMonday
     * @return
     */
    @Override
    public Integer findMemberCountAfterDate(String thisWeekMonday) {
        return memberDao.findMemberCountAfterDate(thisWeekMonday);
    }

    @Override
    public List<Map<String, Object>> findSex() {
        return  memberDao.findSex();
    }

    @Override
    public List<Map<String, Object>> findAge() {
        return memberDao.findAge();
    }
}
