package com.it.dao;

import com.github.pagehelper.Page;
import com.it.pojo.CheckGroup;
import com.it.pojo.CheckItem;
import com.it.pojo.Setmeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetmealDao {
    //新建套餐
    void add(Setmeal setmeal);
    //新建套餐和检查组的关系
    void setSetmealAndCheckGroup(HashMap<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(int id);

    List<Integer> findCheckgroupIdsBySetmealId(int id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void delete(int id);

    List<Setmeal> getAllSetmeal();

    List<CheckGroup> findCheckGroupsBySetmealId(Integer id);

    List<CheckItem> findCheckItemsByCheckGroupId(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
