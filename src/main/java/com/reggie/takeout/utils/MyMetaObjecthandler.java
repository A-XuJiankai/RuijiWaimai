package com.reggie.takeout.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author gett
 * @Date 2022/4/14  14:36
 * @Description 自定义元数据对象处理器---Mybatis Plus提供的公共字段自动填充功能
 *              统一对公共字段进行处理，避免了重复代码
 */
@Slf4j
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {

    public static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);


    /**
     * @Description 插入状态。自动填充
     * @Author gett
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        metaObject.setValue("createTime",sdf.format(new Date()));
        metaObject.setValue("updateTime",sdf.format(new Date()));
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * @Description 更新状态。自动填充
     * @Author gett
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");

        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);

        metaObject.setValue("updateTime",sdf.format(new Date()));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
