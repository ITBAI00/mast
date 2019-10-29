package com.it.service;

import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    //添加
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(int id);

    List<Setmeal> getAllSetmeal();

    List<Map<String, Object>> findSetmealCount();

}
