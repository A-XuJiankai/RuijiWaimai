package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/24  10:26
 * @Description 保存菜品信息，同时保存对应的口味数据
 */
@Data
public class DishFlavorSaveReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //菜品id
    private Long dishId;

    //口味名称
    private String name;

    //口味数据list
    private String value;

    private String createTime;

    private String updateTime;

    private Long createUser;

    private Long updateUser;

    //是否删除
    private Integer isDeleted;

}
