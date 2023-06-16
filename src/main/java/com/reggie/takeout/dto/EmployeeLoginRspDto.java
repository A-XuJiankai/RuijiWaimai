package com.reggie.takeout.dto;

import com.reggie.takeout.po.EmployeePo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/13  8:28
 * @Description 员工登录
 */
@Data
public class EmployeeLoginRspDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;

    private Boolean flag;

    private EmployeePo employeePo;

}
