package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.SetmealDishPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/25  15:35
 * @Description 套餐菜品关系
 */
@Mapper
public interface SetmealDishDao extends BaseMapper<SetmealDishPo> {

}
