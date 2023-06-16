package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.DishPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/14  16:49
 * @Description  菜品表
 */
@Mapper
public interface DishDao extends BaseMapper<DishPo> {

}
