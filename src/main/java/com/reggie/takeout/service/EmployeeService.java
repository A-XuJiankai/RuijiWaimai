package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.EmployeePo;

/**
 * @Author gett
 * @Date 2022/4/12  18:42
 * @Description 后台系统登录功能  员工管理
 */

public interface EmployeeService extends IService<EmployeePo> {

    /**
     * @Description 员工登录
     * @Author gett
     * @param employeeLoginReqDto
     */
    EmployeeLoginRspDto login(EmployeeLoginReqDto employeeLoginReqDto) throws  Exception;

    /**
     * @Description 新增员工
     * @Author gett
     * @param employeeSaveReqDto
     */
    void save(EmployeeSaveReqDto employeeSaveReqDto) throws  Exception;

    /**
     * @Description 员工信息分页查询
     * @Author gett
     * @return
     */
    EmployeePageRspDto pages(EmployeePageReqDto employeepageReqDto) throws  Exception;

    /**
     * @Description 启用/禁用员工账号 || 编辑员工信息
     * @Author gett
     */
    void updates(EmployeeUpdateReqDto employeeUpdateReqDto) throws  Exception;

    /**
     * @Description 编辑员工信息---根据id查询员工信息
     * @Author gett
     */
    EmployeeByIdRspDto getByBId(EmployeeByIdReqDto reqDto) throws  Exception;


}
