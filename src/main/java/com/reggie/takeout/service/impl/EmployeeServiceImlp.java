package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.EmployeeDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.po.EmployeePo;
import com.reggie.takeout.service.EmployeeService;
import com.reggie.takeout.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * @Author gett
 * @Date 2022/4/12  18:44
 * @Description 后台系统登录功能  员工管理
 */
@Service
public class EmployeeServiceImlp extends ServiceImpl<EmployeeDao, EmployeePo> implements EmployeeService{

    private static Logger logger = LoggerFactory.getLogger(EmployeeServiceImlp.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);

    @Resource
    private EmployeeDao employeeDao;

    /**
     * @Description 员工登录
     * @Author gett
     * @param employee
     */
    @Override
    public EmployeeLoginRspDto login(EmployeeLoginReqDto employee) throws  Exception {

        logger.info("员工登录入参{}", JSONObject.toJSONString(employee));
        EmployeeLoginRspDto employeeLoginRspDto = new EmployeeLoginRspDto();

        try {

            //①. 将页面提交的密码password进行md5加密处理, 得到加密后的字符串
            String password = employee.getPassword();
            password = DigestUtils.md5DigestAsHex(password.getBytes());

            //②. 根据页面提交的用户名username查询数据库中员工数据信息
            LambdaQueryWrapper<EmployeePo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EmployeePo::getUsername,employee.getUsername());
            //用户名是唯一索引
            EmployeePo employeePo = employeeDao.selectOne(wrapper);

            //③. 如果没有查询到, 则返回登录失败结果
            if (ObjectUtils.isEmpty(employeePo)&&employeePo==null){
                employeeLoginRspDto.setMsg("登陆失败");
                employeeLoginRspDto.setFlag(false);
                return employeeLoginRspDto;
            }

            //④. 密码比对，如果不一致, 则返回登录失败结果
            if (!password.equals(employeePo.getPassword())){
                employeeLoginRspDto.setMsg("登陆失败");
                employeeLoginRspDto.setFlag(false);
                return employeeLoginRspDto;
            }

            //⑤. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
            if (employeePo.getStatus()==0){
                employeeLoginRspDto.setMsg("账号已禁用");
                employeeLoginRspDto.setFlag(false);
                return employeeLoginRspDto;
            }

            //⑥. 登录成功，将员工id存入Session, 并返回登录成功结果
            employeeLoginRspDto.setFlag(true);
            employeeLoginRspDto.setEmployeePo(employeePo);
            employeeLoginRspDto.setMsg("登陆成功");
            logger.info("员工登录出参{}", JSONObject.toJSONString(employeeLoginRspDto));


        }catch (Exception e){
            logger.error("登陆异常{}", e);
            throw new BusinessException("登陆异常");
        }
        return employeeLoginRspDto;
    }

    /**
     * @Description 新增员工
     * @Author gett
     */
    @Override
    public void save(EmployeeSaveReqDto employee) throws Exception {

        logger.info("新增员工入参{}",JSONObject.toJSONString(employee));

        try {
            EmployeePo employeePo = new EmployeePo();

            BeanUtils.copyProperties(employee,employeePo);
            //员工设置初始默认密码 123456， 并对密码进行MD5加密。
            employeePo.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            //employeePo.setCreateTime(sdf.format(new Date()));
            //employeePo.setUpdateTime(sdf.format(new Date()));

            employeeDao.insert(employeePo);

        }catch (Exception e){
            if (e.getMessage().contains("Duplicate entry")){
                String[] split = e.getMessage().split(" ");
                logger.info("{}", e.getMessage());
                String msg = split[9] + "已存在";
                throw new BusinessException(msg);
            }
        }
    }

    /**
     * @Description 员工信息分页查询
     * @Author gett
     * @return
     */
    @Override
    public EmployeePageRspDto pages(EmployeePageReqDto employeepageReqDto) throws  Exception{
        logger.info("员工信息分页查询入参{}", JSONObject.toJSONString(employeepageReqDto));

        try {

            //构造分页构造器
            Page<EmployeePo> page = new Page<>(employeepageReqDto.getPage(),employeepageReqDto.getPageSize());


            LambdaQueryWrapper<EmployeePo> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(EmployeePo::getUpdateTime);
            if (StringUtils.isNotEmpty(employeepageReqDto.getName())){
                wrapper.like(EmployeePo::getName,employeepageReqDto.getName() );
            }


            Page<EmployeePo> employeePoPage = employeeDao.selectPage(page, wrapper);

            EmployeePageRspDto employeepageRspDto = new EmployeePageRspDto();
            employeepageRspDto.setPage(employeePoPage);
            logger.info("员工信息分页查询出参{}", JSONObject.toJSONString(employeepageRspDto));

            return employeepageRspDto;

        }catch (Exception e){
            logger.error("员工信息分页查询异常{}：", e);
            throw new BusinessException("员工信息分页查询异常");
        }

    }

    /**
     * @Description 启用/禁用员工账号 || 编辑员工信息
     * @Author gett
     */
    @Override
    public void updates(EmployeeUpdateReqDto employeeUpdateReqDto) throws  Exception {
        logger.info("员工信息修改入参{}",JSONObject.toJSONString(employeeUpdateReqDto));
        try {

            long id = Thread.currentThread().getId();
            logger.info("线程id为：{}",id);

            EmployeePo employeePo = new EmployeePo();
            BeanUtils.copyProperties(employeeUpdateReqDto,employeePo);
            //employeePo.setUpdateTime(sdf.format(new Date()));
            employeeDao.updateById(employeePo);

        }catch (Exception e){
            logger.error("员工信息修改异常{}", e);
            throw new BusinessException("员工信息修改异常");
        }

    }

    /**
     * @Description 编辑员工信息---根据id查询员工信息
     * @Author gett
     */
    @Override
    public EmployeeByIdRspDto getByBId(EmployeeByIdReqDto reqDto) throws Exception {

        logger.info("根据id查询员工信息入參{}",JSONObject.toJSONString(reqDto));

        try {

            EmployeeByIdRspDto employeeByIdRspDto = new EmployeeByIdRspDto();

            EmployeePo employeePo = employeeDao.selectById(reqDto.getId());
            BeanUtils.copyProperties(employeePo,employeeByIdRspDto);

            if (ObjectUtils.isEmpty(employeePo) && reqDto!=null){
                throw new BusinessException("该用户信息不存在");
            }

            return employeeByIdRspDto;

        }catch (Exception e){
            logger.error("根据id查询员工信息异常{}", e);
            throw new BusinessException("根据id查询员工信息异常");
        }

    }


}
