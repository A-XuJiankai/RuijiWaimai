package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.SetmealPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/14  16:48
 * @Description 套餐表
 */
@Mapper
public interface SetmealDao extends BaseMapper<SetmealPo> {

}
