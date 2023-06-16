package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.UserPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/26  8:56
 * @Description 用户信息
 */
@Mapper
public interface UserDao extends BaseMapper<UserPo> {

}
