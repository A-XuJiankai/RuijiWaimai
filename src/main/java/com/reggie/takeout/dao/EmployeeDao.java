package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.EmployeePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/12  17:35
 * @Description 员工表
 */
@Mapper
public interface EmployeeDao extends BaseMapper<EmployeePo> {

}
