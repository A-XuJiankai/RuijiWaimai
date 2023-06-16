package com.reggie.takeout.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/14  8:40
 * @Description 员工信息分页查询
 */
@Data
public class EmployeePageRspDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Page page;

}
