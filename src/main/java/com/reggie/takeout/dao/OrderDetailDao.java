package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.OrderDetailPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/28  18:03
 * @Description 订单明细
 */
@Mapper
public interface OrderDetailDao extends BaseMapper<OrderDetailPo> {

}
