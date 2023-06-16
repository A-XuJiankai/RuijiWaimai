package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.AddressBookPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/26  15:41
 * @Description 地址簿
 */
@Mapper
public interface AddressBookDao extends BaseMapper<AddressBookPo> {

}
