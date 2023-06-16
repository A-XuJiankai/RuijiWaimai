package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/25  16:44
 * @Description 套餐分页
 */
@Data
public class SetmealPageReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

    private String name;

}
