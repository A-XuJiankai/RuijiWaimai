package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.CategoryPo;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/14  15:49
 * @Description 分类管理
 */

public interface CategoryService extends IService<CategoryPo> {

    /**
     * @Description 新增分类
     * @Author gett
     */
    void saves(CategorySaveReqDto categorySaveReqDto) throws Exception;

    /**
     * @Description 分类信息分页查询
     * @Author gett
     */
    CategoryPageRspDto pages(CategoryPageReqDto categoryPageReqDto) throws Exception;

    /**
     * @Description 删除分类--根据id删除
     * @Author gett
     */
    void deletes(CategoryDeleteReqDto categoryDeleteReqDto) throws Exception;

    /**
     * @Description 修改分类
     * @Author gett
     */
    void updates(CategoryUpdateReqDto categoryUpdateReqDto) throws Exception;

    /**
     * @Description 列表查询
     * @Author gett
     */
    List<CategoryListRspDto> lists(CategoryListReqDto categoryListReqDto) throws Exception;



}
