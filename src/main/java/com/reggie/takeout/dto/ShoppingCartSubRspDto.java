package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author gett
 * @Date 2022/4/28  9:34
 * @Description 减少购物车数量
 */
@Data
public class ShoppingCartSubRspDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //用户id
    private Long userId;

    //菜品id
    private Long dishId;

    //套餐id
    private Long setmealId;

    //口味
    private String dishFlavor;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;

    private String createTime;

    private String updateTime;

    //是否删除
    private Integer isDeleted;
}
