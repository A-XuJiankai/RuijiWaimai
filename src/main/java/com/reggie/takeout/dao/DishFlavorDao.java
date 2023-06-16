package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.DishFlavorPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/24  8:54
 * @Description 菜品口味
 */
@Mapper
public interface DishFlavorDao extends BaseMapper<DishFlavorPo> {

}
