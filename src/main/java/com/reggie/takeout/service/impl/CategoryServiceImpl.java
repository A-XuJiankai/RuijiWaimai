package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.CategoryDao;
import com.reggie.takeout.dao.DishDao;
import com.reggie.takeout.dao.SetmealDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.CategoryPo;
import com.reggie.takeout.po.DishPo;
import com.reggie.takeout.po.SetmealPo;
import com.reggie.takeout.service.CategoryService;
import com.reggie.takeout.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author gett
 * @Date 2022/4/14  15:50
 * @Description 分类管理
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryPo> implements CategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private DishDao dishDao;

    @Resource
    private SetmealDao setmealDao;


    /**
     * @Description 新增分类
     * @Author gett
     */
    @Override
    public void saves(CategorySaveReqDto categorySaveReqDto) throws Exception {

        logger.info("新增分类入参{}", JSONObject.toJSONString(categorySaveReqDto));

        try {

            CategoryPo categoryPo = new CategoryPo();
            BeanUtils.copyProperties(categorySaveReqDto,categoryPo);
            categoryPo.setIsDeleted(1);
            categoryDao.insert(categoryPo);

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
     * @Description 分类信息分页查询
     * @Author gett
     */
    @Override
    public CategoryPageRspDto pages(CategoryPageReqDto categoryPageReqDto) throws Exception {
        logger.info("分类信息分页查询入参{}",JSONObject.toJSONString(categoryPageReqDto));

        try {
            CategoryPageRspDto categoryPageRspDto = new CategoryPageRspDto();

            Page<CategoryPo> page = new Page<>(categoryPageReqDto.getPage(),categoryPageReqDto.getPageSize());

            LambdaQueryWrapper<CategoryPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(CategoryPo::getSort);
            wrapper.eq(CategoryPo::getIsDeleted,1);
            Page<CategoryPo> categoryPoPage = categoryDao.selectPage(page, wrapper);

            categoryPageRspDto.setPage(categoryPoPage);

            logger.info("分类信息分页查询出参{}",JSONObject.toJSONString(categoryPageRspDto));
            return categoryPageRspDto;

        }catch (Exception e){
            logger.info("分类信息分页查询异常{}",e);
            throw new BusinessException("分类信息分页查询异常");
        }

    }

    /**
     * @Description 删除分类--根据id删除
     * @Author gett
     */
    @Override
    public void deletes(CategoryDeleteReqDto categoryDeleteReqDto) throws Exception {
        logger.info("删除分类入参{}",JSONObject.toJSONString(categoryDeleteReqDto));

        try {

            //- 根据当前分类的ID，查询该分类下是否存在菜品dish(菜品表)，如果存在，则提示错误信息
            LambdaQueryWrapper<DishPo> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(DishPo::getCategoryId,categoryDeleteReqDto.getId());
            dishWrapper.eq(DishPo::getIsDeleted,1);
            int count1= dishDao.selectCount(dishWrapper);
            if (count1>0){
                throw new BusinessException("当前分类下关联了菜品，不能删除");
            }

            //- 根据当前分类的ID，查询该分类下是否存在套餐setmeal(套餐表)，如果存在，则提示错误信息
            LambdaQueryWrapper<SetmealPo> setmealWrapper = new LambdaQueryWrapper<>();
            setmealWrapper.eq(SetmealPo::getCategoryId,categoryDeleteReqDto.getId());
            setmealWrapper.eq(SetmealPo::getIsDeleted,1);

            int count2 = setmealDao.selectCount(setmealWrapper);
            if (count2>0){
                throw new BusinessException("当前分类下关联了套餐，不能删除");
            }

            //-删除分类
            CategoryPo categoryPo = new CategoryPo();
            categoryPo.setId(categoryDeleteReqDto.getId());
            categoryPo.setIsDeleted(0);
            categoryDao.updateById(categoryPo);

        }catch (Exception e){
            logger.info("删除分类异常{}",e);
            throw new BusinessException("删除分类异常");
        }


    }

    /**
     * @Description 修改分类
     * @Author gett
     */
    @Override
    public void updates(CategoryUpdateReqDto categoryUpdateReqDto) throws Exception{

        logger.info("修改分类入参{}",JSONObject.toJSONString(categoryUpdateReqDto));

        try {
            CategoryPo categoryPo = new CategoryPo();
            BeanUtils.copyProperties(categoryUpdateReqDto,categoryPo);
            categoryPo.setIsDeleted(1);
            categoryDao.updateById(categoryPo);

        }catch (Exception e){
            logger.info("修改分类异常{}",e);
            throw new BusinessException("修改分类异常");
        }
    }

    /**
     * @Description 列表查询
     * @Author gett
     */
    @Override
    public List<CategoryListRspDto> lists(CategoryListReqDto categoryListReqDto) throws Exception {
        logger.info("列表查询入参{}",JSONObject.toJSONString(categoryListReqDto));
        List<CategoryListRspDto> dtoList = new ArrayList<>();

        try {
            LambdaQueryWrapper<CategoryPo> wrapper = new LambdaQueryWrapper<>();

            wrapper.eq(categoryListReqDto.getType()!=null,CategoryPo::getType,categoryListReqDto.getType());
            wrapper.eq(CategoryPo::getIsDeleted,1);

            List<CategoryPo> categoryPos = categoryDao.selectList(wrapper);
            if (!ObjectUtils.isEmpty(categoryPos)){
                categoryPos.stream().forEach(categoryPo -> {
                    CategoryListRspDto categoryListRspDto = new CategoryListRspDto();
                    BeanUtils.copyProperties(categoryPo,categoryListRspDto);
                    dtoList.add(categoryListRspDto);
                });
            }



        }catch (Exception e){
            logger.info("列表查询异常{}",e);
            throw new BusinessException("列表查询异常");
        }

        return dtoList;
    }
}
