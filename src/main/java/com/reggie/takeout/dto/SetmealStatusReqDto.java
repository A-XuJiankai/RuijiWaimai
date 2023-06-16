package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/25  18:59
 * @Description 菜品停售
 */
@Data
public class SetmealStatusReqDto implements Serializable {

    private  String status;

    private  Long ids[];

}
