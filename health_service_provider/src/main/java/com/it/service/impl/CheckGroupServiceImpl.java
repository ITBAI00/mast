package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.dao.CheckGroupDao;
import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.CheckGroup;
import com.it.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 增加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    //新增检查组，同时需要让检查组管理检查项
    public void add(CheckGroup checkGroup, Integer[] checkitemIds){
        //新增检查组
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的多对多的关联关系，操作t_checkgroup和t_checkgroup_checkitem
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
    }

    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    /**
     * 根据检查组id查询选中的检查项
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckgroupId(Integer id) {
        List<Integer> checkitemIds = checkGroupDao.findCheckItemIdsByCheckgroupId(id);
        return checkitemIds;
    }

    /**
     * 编辑检查组信息，同时关联检查项
     * 注意：编辑这一步操作，必须被同一个事物所管理，要么同时成功，要么同时失败
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组基本信息，操作检查组t_checkgroup表
        checkGroupDao.edit(checkGroup);
        //清空原有检查组和检查项之间的关系，操作中间关系表
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //重新建立当前检查组和检查项之间的关系
        this.setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    /**
     * 根据id删除检查组
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //删除检查组和检查项的关系
        checkGroupDao.deleteAssociation(id);
        //删除检查组
        checkGroupDao.delete(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroupList = checkGroupDao.findAll();
        return checkGroupList;
    }
}
