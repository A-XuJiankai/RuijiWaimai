package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/14  11:36
 * @Description 编辑员工信息---根据id查询员工信息
 */

@Data
public class EmployeeByIdReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
}
