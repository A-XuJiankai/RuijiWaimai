package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/14  11:36
 * @Description 编辑员工信息---根据id查询员工信息
 */

@Data
public class EmployeeByIdRspDto implements Serializable {

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
