package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.OrdersPo;

/**
 * @Author gett
 * @Date 2022/4/28  17:59
 * @Description 订单
 */

public interface OrdersService extends IService<OrdersPo> {

    /**
     * @Description 下单
     * @Author gett
     */
    void submit(OrdersSubmitReqDto ordersSubmitReqDto) throws Exception;

    /**
     * @Description 分页查询
     * @Author gett
     */
    OrdersUserPageRspDto pages(OrdersUserPageReqDto ordersUserPageReqDto) throws Exception;


    /**
     * @Description 分页查询
     * @Author gett
     */
    Page<OrdersPageRspDto> pagess(OrdersPageReqDto ordersPageReqDto) throws Exception;


    /**
     * @Description 修改状态
     * @Author gett
     */
    void updateSave(OrdersUpdateReqDto ordersUpdateReqDto)  throws Exception;

    /**
     * @Description 再来一单
     * @Author gett
     */
    void again(OrdersAgainReqDto ordersAgainReqDto)  throws Exception;
}
