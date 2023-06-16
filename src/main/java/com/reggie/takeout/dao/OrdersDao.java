package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.OrdersPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/28  17:59
 * @Description 订单
 */
@Mapper
public interface OrdersDao extends BaseMapper<OrdersPo> {

}
