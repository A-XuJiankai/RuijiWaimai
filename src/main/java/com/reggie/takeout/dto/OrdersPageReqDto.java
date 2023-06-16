package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/28  20:45
 * @Description 分页查询
 */
@Data
public class OrdersPageReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

    private String number;

    private String beginTime;

    private String endTime;

}
