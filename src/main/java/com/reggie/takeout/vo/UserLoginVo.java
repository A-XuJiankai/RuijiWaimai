package com.reggie.takeout.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/26  15:01
 * @Description 登录
 */
@Data
public class UserLoginVo implements Serializable {

    private String phone;
    private String code;

}
