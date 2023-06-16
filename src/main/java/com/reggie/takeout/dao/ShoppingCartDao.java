package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.ShoppingCartPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/28  9:24
 * @Description 购物车
 */
@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCartPo> {

}
