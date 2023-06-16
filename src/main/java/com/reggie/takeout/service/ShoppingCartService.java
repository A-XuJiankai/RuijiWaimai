package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.ShoppingCartPo;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/28  9:25
 * @Description 购物车
 */

public interface ShoppingCartService  extends IService<ShoppingCartPo> {

    /**
     * @Description 加入购物车
     * @Author gett
     */
    ShoppingCartAddRspDto add(ShoppingCartAddReqDto shoppingCartAddReqDto) throws Exception;

    /**
     * @Description 查看购物车
     * @Author gett
     */
    List<ShoppingCartListRspDto> lists() throws Exception;

    /**
     * @Description 清空购物车
     * @Author gett
     */
    void deletes() throws Exception;

    /**
     * @Description 减少购物车数量
     * @Author gett
     */
    ShoppingCartSubRspDto sub(ShoppingCartSubReqDto shoppingCartSubReqDto) throws Exception;
}
