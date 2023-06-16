package com.reggie.takeout.config;

import com.reggie.takeout.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author gett
 * @Date 2022/4/13  17:19
 * @Description 全局异常处理器,针对添加员工用户名唯一
 *
 *   Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'zhangsan' for key 'idx_username'
 *
 *              异常处理方法逻辑:
 *                  指定捕获的异常类型为 SQLIntegrityConstraintViolationException
 *                  解析异常的提示信息, 获取出是那个值违背了唯一约束
 *                  组装错误信息并返回
 */
//@ControllerAdvice : 指定拦截那些类型的控制器;
//@ResponseBody: 将方法的返回值 R 对象转换为json格式的数据, 响应给页面;
//上述使用的两个注解, 也可以合并成为一个注解 @RestControllerAdvice

//属性annotations指定拦截哪一类的Controller方法
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class  GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
