package com.reggie.takeout.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.po.OrderDetailPo;
import com.reggie.takeout.po.OrdersPo;
import com.reggie.takeout.po.SetmealDishPo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/28  20:46
 * @Description 分页查询
 */
@Data
public class OrdersPageRspDto implements Serializable{

    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
    private Integer status;

    //下单用户id
    private Long userId;

    //地址id
    private Long addressBookId;

    //下单时间
    private String orderTime;

    //结账时间
    private String checkoutTime;

    //支付方式 1微信，2支付宝
    private Integer payMethod;

    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    //用户名
    private String userName;

    //手机号
    private String phone;

    //地址
    private String address;

    //收货人
    private String consignee;

}
