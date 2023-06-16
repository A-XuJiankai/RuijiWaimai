package com.reggie.takeout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.takeout.po.CategoryPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author gett
 * @Date 2022/4/14  15:51
 * @Description 分类管理
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryPo> {

}
