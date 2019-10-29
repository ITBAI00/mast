package com.it.dao;

import com.github.pagehelper.Page;
import com.it.pojo.CheckItem;

import java.util.List;

/**
 * dao接口
 */
public interface CheckItemDao {
    public void add(CheckItem checkItem);
    public Page<CheckItem> selectByCondition(String queryString);

    public long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);
    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
