package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.entity.Result;
import com.it.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    /**
     * 按性别分类
     * @return
     */
    @RequestMapping("/findSex")
    public Result findSex(){
        List<Map<String,Object>> sexList=memberService.findSex();
        Map<String,Object> map = new HashMap<>();
        List<String> list = getList(sexList);
        map.put("sexName",list);
        map.put("sexCount",sexList);
        return new Result(true,"查询男女比例成功",map);
    }

    /**
     * 按年龄分类
     * @return
     */
    @RequestMapping("/findAge")
    public Result findAge(){

        List<Map<String,Object>> sexList=memberService.findAge();
        Map<String,Object> map = new HashMap<>();
        List<String> list = getList(sexList);
        map.put("ageGroup",list);
        map.put("ageCount",sexList);
        return new Result(true,"查询男女比例成功",map);
    }

    /**
     * 提取出相同代码,组成方法
     * @param list
     * @return
     */
    public List<String> getList(List<Map<String,Object>>list){
        ArrayList<String> strings = new ArrayList<>();
        for (Map<String, Object> map1 : list) {
            String name = (String) map1.get("name");
            strings.add(name);
        }
        return strings;
    }
}
