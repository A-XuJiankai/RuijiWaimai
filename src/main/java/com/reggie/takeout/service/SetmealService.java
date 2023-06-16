package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.SetmealPo;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/14  16:50
 * @Description 套餐管理
 */

public interface SetmealService extends IService<SetmealPo> {

    /**
     * @Description 新增套餐
     * @Author gett
     */
    void saves(SetmealSaveReqDto setmealSaveReqDto) throws Exception;

    /**
     * @Description 套餐分页
     * @Author gett
     */
    Page<SetmealPageRspDto> pages(SetmealPageReqDto setmealPageReqDto) throws Exception;

    /**
     * @Description 修改套餐-回显信息
     * @Author gett
     */
    SetmealUpdatesRspDto updates(SetmealUpdatesReqDto setmealUpdatesReqDto) throws Exception;

    /**
     * @Description 修改套餐-保存
     * @Author gett
     */
    void updateSave(SetmealUpdateSaveReqDto setmealUpdateSaveReqDto) throws Exception;

    /**
     * @Description 删除套餐--根据id删除
     * @Author gett
     */
    void deletes(SetmealDeleteReqDto setmealDeleteReqDto) throws Exception;

    /**
     * @Description 菜品停售
     * @Author gett
     */
    void status(SetmealStatusReqDto setmealStatusReqDto)  throws Exception;

    /**
     * @Description 根据条件查询套餐数据
     * @Author gett
     */
    List<SetmealListRspDto> lists(SetmealListReqDto setmealListReqDto)  throws Exception;
}
