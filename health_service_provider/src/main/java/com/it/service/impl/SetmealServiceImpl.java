package com.it.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.constant.RedisConstant;
import com.it.dao.SetmealDao;
import com.it.entity.PageResult;
import com.it.entity.QueryPageBean;
import com.it.pojo.CheckGroup;
import com.it.pojo.CheckItem;
import com.it.pojo.Setmeal;
import com.it.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 新增套餐，并让套餐绑定对应的检查组
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐,并且还要获取，添加后的数据库id
        setmealDao.add(setmeal);
        //套餐绑定对应的检查组
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId, checkgroupIds);
        try {
            //将图片名称保存在Redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //设定套餐和检查组的关系
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds){
        if(checkgroupIds != null && checkgroupIds.length >0) {
            for (Integer checkgroupId : checkgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", setmealId);
                map.put("checkgroup_id", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

    /**
     * 分页查找
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //mybatis提供的分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> setmealPage = setmealDao.findByCondition(queryString);
        return new PageResult(setmealPage.getTotal(), setmealPage.getResult());
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        Setmeal setmeal = setmealDao.findById(id);
        //根据查询到的setmeal，获取它的id去查询对应的检查组
        if(setmeal != null){
            //查询套餐对应的检查组
            List<CheckGroup> checkGroupList = setmealDao.findCheckGroupsBySetmealId(setmeal.getId());
            //根据检查组查询对应的检查项
            if(checkGroupList != null && checkGroupList.size() > 0){
                for (CheckGroup checkGroup : checkGroupList) {
                    List<CheckItem> checkItemList = setmealDao.findCheckItemsByCheckGroupId(checkGroup.getId());
                    checkGroup.setCheckItems(checkItemList);
                }
            }
            setmeal.setCheckGroups(checkGroupList);
        }
        return setmeal;
    }

    /**
     * 查询套餐和检查组关系表，复选框
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        List<Integer> setmealIds = setmealDao.findCheckgroupIdsBySetmealId(id);
        return setmealIds;
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.设置套餐基本信息
        setmealDao.edit(setmeal);
        //2.清除关系表：套餐和检查组的关系
        setmealDao.deleteAssociation(setmeal.getId());
        //3.重新建立联系套餐和检查组的关联关系
        this.setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        try {
            //将图片名称保存在Redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据id删除套餐
     * @param id
     */
    @Override
    public void delete(int id) {
        setmealDao.delete(id);
    }

    //获得所有预约套餐

    /**
     * 设置缓存
     * @author t
     * @return
     */
    @Override
    public List<Setmeal> getAllSetmeal() {
        String json;
        List<Setmeal>setmeals = null;
        try {
            //现在redis中查询
            json = jedisPool.getResource().get("json");
            //将json数据转换为数组格式
            List<Setmeal> list = JSON.parseArray(json, Setmeal.class);
            if (list!=null){
                //缓存中有数据
                return list;
            }
            //缓存中没有数据,去数据库查询
            setmeals = setmealDao.getAllSetmeal();
            //将json数据转换为字符串
            json = JSON.toJSON(setmeals).toString();
            //将数据保存到redis
            jedisPool.getResource().set("json",json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return setmeals;

        //List<Setmeal> list = setmealDao.getAllSetmeal();
        //return list;
    }

    /**
     * 套餐预约占比-->查询套餐数量
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
