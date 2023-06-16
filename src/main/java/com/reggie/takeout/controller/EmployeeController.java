package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.vo.Employee;
import com.reggie.takeout.po.EmployeePo;
import com.reggie.takeout.service.EmployeeService;
import com.reggie.takeout.utils.DateUtil;
import com.reggie.takeout.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @Author gett
 * @Date 2022/4/12  18:52
 * @Description 后台系统登录功能  员工管理
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);

    @Autowired
    private EmployeeService employeeService;

    /**
     * @Description 员工登录
     * 登录成功，request将信息存入session。
     * @Author gett
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R<EmployeePo> login(HttpServletRequest request, @RequestBody Employee employee) {

        logger.info("员工登录入参{}", JSONObject.toJSONString(employee));

        EmployeeLoginRspDto login=null;

        try {
            EmployeeLoginReqDto employeeLoginReqDto = new EmployeeLoginReqDto();
            BeanUtils.copyProperties(employee,employeeLoginReqDto);
            login = employeeService.login(employeeLoginReqDto);

            if (!login.getFlag()&&login.getFlag()!=null){
                return R.error(login.getMsg());
            }

            //登录成功，将员工id存入Session, 并返回登录成功结果
            request.getSession().setAttribute("employee",login.getEmployeePo().getId());

            logger.info("员工登录出参{}", JSONObject.toJSONString(login));

        }catch (Exception e){
            logger.info("员工登录异常",e);
            return R.error("员工登录异常");
        }

        return R.success(login.getEmployeePo());
    }

    /**
     * @Description 员工退出
     * @Author gett
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public R<String> logout(HttpServletRequest request) {

        try {

            //①. 清理Session中的用户id
            request.getSession().removeAttribute("employee");

            //②. 返回结果
            return R.success("退出成功");

        }catch (Exception e){
            logger.info("员工退出异常",e);
            return R.error("员工退出异常");
        }

    }

    /**
     * @Description 新增员工
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST)
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee) {
        logger.info("新增员工入参{}",JSONObject.toJSONString(employee));

        try {

            //Long empId = (Long) request.getSession().getAttribute("employee");
            //employee.setCreateUser(empId);
            //employee.setUpdateUser(empId);
            EmployeeSaveReqDto employeeSaveReqDto = new EmployeeSaveReqDto();
            BeanUtils.copyProperties(employee,employeeSaveReqDto);
            employeeService.save(employeeSaveReqDto);

            return R.success("新增员工成功");

        }catch (Exception e){
            logger.info("新增员工异常{}",e);
            return R.error(e.getMessage());
        }

    }

    /**
     * @Description 员工信息分页查询
     * @Author gett
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public R<Page> pages(int page, int pageSize , String name) {
        logger.info("员工信息分页查询入参{}-{}-{}",page,pageSize,name);
        try {

            EmployeePageReqDto employeepageReqDto = new EmployeePageReqDto();
            employeepageReqDto.setPage(page);
            employeepageReqDto.setPageSize(pageSize);
            employeepageReqDto.setName(name);

            EmployeePageRspDto rspDto = employeeService.pages(employeepageReqDto);
            logger.info("员工信息分页查询出参{}",JSONObject.toJSONString(rspDto));

            return R.success(rspDto.getPage());

        }catch(Exception e){
            logger.info("员工信息分页查询异常{}",e);
            return R.error("员工信息分页查询异常");
        }


    }

    /**
     * @Description 启用/禁用员工账号 || 编辑员工信息
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        logger.info("员工信息修改入参{}",JSONObject.toJSONString(employee));

        try {
            //js在对长度较长的长整型数据进行处理时，会损失精度
            //为了防止丢失精度
            //Long empId = (Long)request.getSession().getAttribute("employee");

            EmployeeUpdateReqDto employeeUpdateReqDto = new EmployeeUpdateReqDto();
            BeanUtils.copyProperties(employee,employeeUpdateReqDto);
            //employeeUpdateReqDto.setUpdateUser(empId);
            employeeService.updates(employeeUpdateReqDto);
            return  R.success("员工信息修改成功");
        }catch(Exception e){
            logger.info("员工信息修改异常{}",e);
            return R.error("员工信息修改异常");
        }
    }


    /**
     * @Description 编辑员工信息---根据id查询员工信息
     * @Author gett
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public R<EmployeeByIdRspDto> getById(@PathVariable Long id){
        logger.info("根据id查询员工信息入參{}",id);
        try {

            EmployeeByIdReqDto reqDto = new EmployeeByIdReqDto();
            reqDto.setId(id);
            EmployeeByIdRspDto rspDto = employeeService.getByBId(reqDto);

            return R.success(rspDto);

        }catch(Exception e){
            logger.info("根据id查询员工信息异常{}",e);
            return R.error("根据id查询员工信息异常");
        }
    }

}
