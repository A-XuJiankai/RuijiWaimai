package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.CategoryService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/14  15:47
 * @Description 分类管理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * @Description 新增分类
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST)
    public R<String> save(@RequestBody Category category){
        logger.info("新增分类入参{}", JSONObject.toJSONString(category));
        try {
            CategorySaveReqDto categorySaveReqDto = new CategorySaveReqDto();
            BeanUtils.copyProperties(category,categorySaveReqDto);
            categoryService.saves(categorySaveReqDto);

            return R.success("新增分类成功");

        }catch (Exception e){
            logger.info("新增分类异常{}",e);
            return R.error("新增分类异常");
        }

    }

    /**
     * @Description 分类信息分页查询
     * @Author gett
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public R<Page> page(int page,int pageSize){
        logger.info("分类信息分页查询入参{}--{}",pageSize,page);
        try{

            CategoryPageReqDto categoryPageReqDto = new CategoryPageReqDto();
            categoryPageReqDto.setPage(page);
            categoryPageReqDto.setPageSize(pageSize);

            CategoryPageRspDto categoryPageRspDto=categoryService.pages(categoryPageReqDto);
            logger.info("分类信息分页查询出参{}",JSONObject.toJSONString(categoryPageRspDto));

            return  R.success(categoryPageRspDto.getPage());

        }catch (Exception e){
            logger.info("分类信息分页查询异常{}",e);
            return R.error("分类信息分页查询异常");
        }
    }

    /**
     * @Description 删除分类--根据id删除
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public R<String> delete(@RequestParam("ids") Long ids){

        logger.info("删除分类入参{}",ids);

        try {
            CategoryDeleteReqDto categoryDeleteReqDto = new CategoryDeleteReqDto();
            categoryDeleteReqDto.setId(ids);

            categoryService.deletes(categoryDeleteReqDto);

            return R.success("删除成功");
        }catch (Exception e){
            logger.info("删除分类异常{}",e);
            return R.error("删除分类异常");
        }


    }

    /**
     * @Description 修改分类
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R<String> update(@RequestBody Category category){

        logger.info("修改分类入参{}",JSONObject.toJSONString(category));

        try {
            CategoryUpdateReqDto categoryUpdateReqDto = new CategoryUpdateReqDto();
            BeanUtils.copyProperties(category,categoryUpdateReqDto);
            categoryService.updates(categoryUpdateReqDto);

            return R.success("修改分类成功");
        }catch (Exception e){
            logger.info("修改分类异常{}",e);
            return R.error("修改分类异常");
        }

    }

    /**
     * @Description 列表查询
     * @Author gett
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R<List<CategoryListRspDto>> list(Category category){

        logger.info("列表查询入参{}",JSONObject.toJSONString(category));

        try {
            CategoryListReqDto categoryListReqDto = new CategoryListReqDto();
            BeanUtils.copyProperties(category,categoryListReqDto);
            List<CategoryListRspDto> lists=categoryService.lists(categoryListReqDto);

            logger.info("列表查询出参{}",lists.toString());

            return R.success(lists);

        }catch (Exception e){
            logger.info("列表查询异常{}",e);
            return R.error("列表查询异常");
        }


    }

}
