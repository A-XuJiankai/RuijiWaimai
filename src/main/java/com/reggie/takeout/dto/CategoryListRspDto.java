package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/24  9:59
 * @Description 列表查询
 */
@Data
public class CategoryListRspDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    //分类名称
    private String name;

    //顺序
    private Integer sort;

    private String createTime;

    private String updateTime;

    private Long createUser;

    private Long updateUser;

    //是否删除
    private Integer isDeleted;

}
