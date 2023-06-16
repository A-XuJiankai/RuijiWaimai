package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/25  8:32
 * @Description 删除菜品--根据id删除
 */
@Data
public class DishDeleteReqDto implements Serializable {

    private Long id[];

}
