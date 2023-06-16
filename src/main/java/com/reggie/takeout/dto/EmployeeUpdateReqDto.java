package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author gett
 * @Date 2022/4/14  10:33
 * @Description 启用/禁用员工账号 || 编辑员工信息
 */
@Data
public class EmployeeUpdateReqDto implements Serializable {

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
