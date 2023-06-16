package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.DishPo;
import com.reggie.takeout.vo.Dish;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/14  16:50
 * @Description 菜品管理
 */

public interface DishService extends IService<DishPo> {

    /**
     * @Description 保存菜品信息，同时保存对应的口味数据
     * @Author gett
     */
    void saves(DishSaveReqDto reqDto) throws Exception;

    /**
     * @Description 菜品信息分页查询
     * @Author gett
     */
    Page<DishPageRspDto> pages(DishPageReqDto dishPageReqDto)  throws Exception;

    /**
     * @Description 修改菜品-回显信息
     * @Author gett
     * @return
     */
    DishUpdateRspDto updates(DishUpdateReqDto dishUpdateReqDto)  throws Exception;

    /**
     * @Description 删除菜品--根据id删除
     * @Author gett
     */
    void deletes(DishDeleteReqDto dishDeleteReqDto)  throws Exception;

    /**
     * @Description 修改菜品-保存
     * @Author gett
     */
    void updateSave(DishUpdateSaveReqDto dishUpdateSaveReqDto) throws Exception;

    /**
     * @Description 菜品停售
     * @Author gett
     */
    void status(DishStatusReqDto dishStatusReqDto) throws Exception;

    /**
     * @Description 列表查询
     * @Author gett
     */
    List<DishListRspDto> lists(DishListReqDto dishListReqDto) throws Exception;

    /**
     * @Description 列表查询改进
     * @Author gett
     */
    List<Dish> listss(DishListReqDto dishListReqDto) throws Exception;
}
