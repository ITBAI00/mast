package com.it.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.constant.MessageConstant;
import com.it.entity.Result;
import com.it.pojo.Setmeal;
import com.it.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预约套餐
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    /**
     * 预约套餐信息缓存
     * @author timy
     */
    @Reference
    private SetmealService setmealService;

    //获取所有预约套餐
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal(){
        try {
            List<Setmeal> list = setmealService.getAllSetmeal();

            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据id查询套餐
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
