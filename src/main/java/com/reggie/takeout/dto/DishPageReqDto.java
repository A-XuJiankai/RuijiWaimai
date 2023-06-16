package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/24  11:52
 * @Description 菜品信息分页查询
 */
@Data
public class DishPageReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

    private String name;


}
