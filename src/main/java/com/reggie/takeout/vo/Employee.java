package com.reggie.takeout.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/12  19:18
 * @Description 员工表
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    private String createTime;

    private String updateTime;

    private Long createUser;

    private Long updateUser;

}
