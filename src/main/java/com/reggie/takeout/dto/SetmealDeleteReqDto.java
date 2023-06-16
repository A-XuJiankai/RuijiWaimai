package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/25  18:57
 * @Description 删除套餐--根据id删除
 */
@Data
public class SetmealDeleteReqDto implements Serializable {

    private Long id[];

}
