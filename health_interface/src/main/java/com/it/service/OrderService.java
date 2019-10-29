package com.it.service;

import com.it.entity.Result;

import java.util.Map;

/**
 * 体验预约服务接口
 */
public interface OrderService {
    /**
     * 预约添加
     * @param map
     * @return
     * @throws Exception
     */
    Result order(Map map) throws Exception;

    /**
     * 根据预约id进行查询
     * @param id
     * @return
     */
    Map findById(Integer id) throws Exception;

}
