package com.it.dao;

import com.github.pagehelper.Page;
import com.it.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup
     */
    public void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项的关系
     * @param map
     */
    public void setCheckGroupAndCheckItem(Map map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckgroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    void delete(Integer id);

    List<CheckGroup> findAll();

}
