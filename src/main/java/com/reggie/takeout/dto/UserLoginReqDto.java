package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/26  15:02
 * @Description
 */
@Data
public class UserLoginReqDto implements Serializable {

    private String phone;
    private String code;

}
