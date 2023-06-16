package com.reggie.takeout.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.CategoryDao;
import com.reggie.takeout.dao.DishDao;
import com.reggie.takeout.dao.DishFlavorDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.CategoryPo;
import com.reggie.takeout.po.DishFlavorPo;
import com.reggie.takeout.po.DishPo;
import com.reggie.takeout.service.DishService;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.vo.Dish;
import com.reggie.takeout.vo.DishFlavor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @Author gett
 * @Date 2022/4/14  16:51
 * @Description
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishDao, DishPo> implements DishService {

    private static Logger logger = LoggerFactory.getLogger(DishServiceImpl.class);

    @Resource
    private DishDao dishDao;

    @Resource
    private DishFlavorDao dishFlavorDao;

    @Resource
    private CategoryDao categoryDao;

    /**
     * @Description 保存菜品信息，同时保存对应的口味数据
     * @Author gett
     */
    @Transactional
    @Override
    public void saves(DishSaveReqDto reqDto) throws Exception {
        logger.info("保存菜品信息入参{}", JSONObject.toJSONString(reqDto));

            reqDto.setIsDeleted(1);
            dishDao.insert(reqDto);


        reqDto.getFlavors().stream().forEach(dishFlavorSaveReqDto -> {
                Long id = reqDto.getId();//菜品id
                DishFlavorPo dishFlavorPo = new DishFlavorPo();
                dishFlavorPo.setDishId(id);
                dishFlavorPo.setName(dishFlavorSaveReqDto.getName());
                dishFlavorPo.setValue(dishFlavorSaveReqDto.getValue());
                dishFlavorPo.setIsDeleted(1);
                dishFlavorDao.insert(dishFlavorPo);
            });

    }

    /**
     * @Description 菜品信息分页查询
     * @Author gett
     */
    @Override
    public Page<DishPageRspDto> pages(DishPageReqDto dishPageReqDto) throws Exception {

        logger.info("菜品信息分页查询入参{}", JSONObject.toJSONString(dishPageReqDto));

        try {

            Page<DishPo> dishPoPage = new Page<>(dishPageReqDto.getPage(), dishPageReqDto.getPageSize());
            Page<DishPageRspDto> saveReqDtoPage = new Page<>();
            ArrayList<DishPageRspDto> dtos = new ArrayList<>();

            //菜单表
            LambdaQueryWrapper<DishPo> dishPoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishPoLambdaQueryWrapper.like(dishPageReqDto.getName()!=null,DishPo::getName,dishPageReqDto.getName());
            dishPoLambdaQueryWrapper.orderByDesc(DishPo::getUpdateTime);
            dishPoLambdaQueryWrapper.eq(DishPo::getIsDeleted,1);
            dishPoPage= dishDao.selectPage(dishPoPage, dishPoLambdaQueryWrapper);

            BeanUtils.copyProperties(dishPoPage,saveReqDtoPage,"records");

            logger.info("00000{}",JSONObject.toJSONString(saveReqDtoPage));
            logger.info("00000{}",JSONObject.toJSONString(dishPoPage));

            List<DishPo> records = dishPoPage.getRecords();
            records.stream().forEach(dishSaveReqDto -> {
                DishPageRspDto dishPageRspDto = new DishPageRspDto();
                BeanUtils.copyProperties(dishSaveReqDto,dishPageRspDto);
                LambdaQueryWrapper<CategoryPo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CategoryPo::getId,dishSaveReqDto.getCategoryId());
                queryWrapper.eq(CategoryPo::getIsDeleted,1);
                CategoryPo categoryPo = categoryDao.selectOne(queryWrapper);

                if (!ObjectUtils.isEmpty(categoryPo)){
                    dishPageRspDto.setCategoryName(categoryPo.getName());
                }
                dtos.add(dishPageRspDto);
            });

            saveReqDtoPage.setRecords(dtos);
            return saveReqDtoPage;

        }catch (Exception e){
            logger.error("菜品信息分页查询异常{}", e);
            throw new BusinessException("菜品信息分页查询异常");
        }



    }

    /**
     * @Description 修改菜品-回显信息
     * @Author gett
     * @return
     */
    @Override
    public DishUpdateRspDto updates(DishUpdateReqDto dishUpdateReqDto) throws Exception {

        logger.info("修改菜品-回显信息入参{}", JSONObject.toJSONString(dishUpdateReqDto));
        DishUpdateRspDto dishUpdateRspDto = new DishUpdateRspDto();

        try {

            //根据菜品id查询信息以及对应的口味
            LambdaQueryWrapper<DishPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishPo::getId, dishUpdateReqDto.getId());
            wrapper.eq(DishPo::getIsDeleted,1);
            DishPo dishPo = dishDao.selectOne(wrapper);

            BeanUtils.copyProperties(dishPo,dishUpdateRspDto);

            if (!ObjectUtils.isEmpty(dishPo)){
                LambdaQueryWrapper<DishFlavorPo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DishFlavorPo::getDishId, dishPo.getId());
                queryWrapper.eq(DishFlavorPo::getIsDeleted,1);
                List<DishFlavorPo> dishFlavorPos = dishFlavorDao.selectList(queryWrapper);

                dishUpdateRspDto.setFlavors(dishFlavorPos);

            }


        }catch (Exception e){
            logger.error("修改菜品-回显信息异常{}", e);
            throw new BusinessException("修改菜品-回显信息异常");
        }


        return dishUpdateRspDto;
    }

    /**
     * @Description 删除菜品--根据id删除
     * @Author gett
     */
    @Transactional
    @Override
    public void deletes(DishDeleteReqDto dishDeleteReqDto)  throws Exception{
        logger.info("删除菜品入参{}",JSONObject.toJSONString(dishDeleteReqDto));

        Long[] ids = dishDeleteReqDto.getId();
        for (int i = 0; i < ids.length; i++) {
            LambdaQueryWrapper<DishPo> dishPoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishPoLambdaQueryWrapper.eq(DishPo::getId,ids[i]);
            dishPoLambdaQueryWrapper.eq(DishPo::getIsDeleted,1);
            dishPoLambdaQueryWrapper.eq(DishPo::getStatus,1);

            DishPo one = dishDao.selectOne(dishPoLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(one)){
                throw new BusinessException("套餐正在售卖中，不能删除");
            }

            //- 根据当前菜品的ID，查询该菜品下是否存在菜品口味
            LambdaQueryWrapper<DishFlavorPo> dishFlavorWrapper = new LambdaQueryWrapper<>();
            dishFlavorWrapper.eq(DishFlavorPo::getDishId,ids[i]);
            dishFlavorWrapper.eq(DishFlavorPo::getIsDeleted,1);
            List<DishFlavorPo> dishFlavorPos = dishFlavorDao.selectList(dishFlavorWrapper);

            if (dishFlavorPos.size()>0){
                dishFlavorPos.stream().forEach(dishFlavorPo -> {
                    DishFlavorPo po = new DishFlavorPo();
                    po.setIsDeleted(0);
                    po.setId(dishFlavorPo.getId());
                    dishFlavorDao.updateById(po);
                });


                DishPo dishPo = new DishPo();
                dishPo.setId(ids[i]);
                dishPo.setIsDeleted(0);
                dishDao.updateById(dishPo);
            }
        }

    }

    /**
     * @Description 修改菜品-保存
     * @Author gett
     */
    @Transactional
    @Override
    public void updateSave(DishUpdateSaveReqDto dishUpdateSaveReqDto) throws Exception {
        logger.info("修改菜品-保存入参{}",JSONObject.toJSONString(dishUpdateSaveReqDto));

        //更新dish表
        DishPo dishPo = new DishPo();
        BeanUtils.copyProperties(dishUpdateSaveReqDto,dishPo);
        dishPo.setIsDeleted(1);
        dishDao.updateById(dishPo);

        //清除当前口味信息
        LambdaQueryWrapper<DishFlavorPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavorPo::getDishId,dishUpdateSaveReqDto.getId());
        dishFlavorDao.delete(wrapper);

        dishUpdateSaveReqDto.getFlavors().stream().forEach(dishFlavor -> {
            DishFlavorPo dishFlavorPo = new DishFlavorPo();
            BeanUtils.copyProperties(dishFlavor,dishFlavorPo);
            dishFlavorPo.setIsDeleted(1);
            dishFlavorPo.setDishId(dishUpdateSaveReqDto.getId());

            dishFlavorDao.insert(dishFlavorPo);
        });

    }

    /**
     * @Description 菜品停售
     * @Author gett
     */
    @Override
    public void status(DishStatusReqDto dishStatusReqDto) throws Exception {
        logger.info("菜品停售入参{}",JSONObject.toJSONString(dishStatusReqDto));
        try {

            if (dishStatusReqDto.getIds()!=null){
                Long[] ids = dishStatusReqDto.getIds();
                for (int i = 0; i < ids.length; i++) {
                    LambdaQueryWrapper<DishPo> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(DishPo::getId,ids[i]);
                    wrapper.eq(DishPo::getIsDeleted,1);
                    DishPo dishPo = dishDao.selectOne(wrapper);
                    if (!ObjectUtils.isEmpty(dishPo)){
                        String status = dishStatusReqDto.getStatus();
                        int anInt = Integer.parseInt(status);
                        dishPo.setStatus(anInt);
                        dishDao.updateById(dishPo);
                    }
                }
            }


        }catch (Exception e){
            logger.error("菜品停售异常{}", e);
            throw new BusinessException("菜品停售异常");
        }

    }

    /**
     * @Description 列表查询
     * @Author gett
     */
    @Override
    public List<DishListRspDto> lists(DishListReqDto dishListReqDto) throws Exception {
        logger.info("列表查询入参{}",JSONObject.toJSONString(dishListReqDto));

        List<DishListRspDto> list = new ArrayList<>();
        try {
            LambdaQueryWrapper<DishPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(dishListReqDto.getCategoryId()!=null,DishPo::getCategoryId,dishListReqDto.getCategoryId());
            wrapper.eq(DishPo::getIsDeleted,1);
            wrapper.eq(DishPo::getStatus,1);
            wrapper.orderByDesc(DishPo::getUpdateTime);

            List<DishPo> dishPos = dishDao.selectList(wrapper);
            if(!ObjectUtils.isEmpty(dishPos)){
                dishPos.stream().forEach(dishPo -> {
                    DishListRspDto dishListRspDto = new DishListRspDto();
                    BeanUtils.copyProperties(dishPo,dishListRspDto);
                    list.add(dishListRspDto);
                });
            }


            return list;

        }catch (Exception e){
            logger.error("列表查询异常{}", e);
            throw new BusinessException("列表查询异常");
        }

    }

    /**
     * @Description 列表查询改进
     * @Author gett
     */
    @Override
    public List<Dish> listss(DishListReqDto dishListReqDto) throws Exception{
        logger.info("列表查询入参{}",JSONObject.toJSONString(dishListReqDto));

        List<Dish> list = new ArrayList<Dish>();

        try {
            LambdaQueryWrapper<DishPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(dishListReqDto.getCategoryId()!=null,DishPo::getCategoryId,dishListReqDto.getCategoryId());
            wrapper.eq(DishPo::getIsDeleted,1);
            wrapper.eq(DishPo::getStatus,1);
            wrapper.eq(DishPo::getIsDeleted,1);
            wrapper.orderByDesc(DishPo::getUpdateTime);

            List<DishPo> dishPos = dishDao.selectList(wrapper);

            if(dishPos.size()>0 && dishPos!=null){

                dishPos.stream().forEach(dishPo -> {
                    Dish dish= new Dish();
                    BeanUtils.copyProperties(dishPo,dish);

                    CategoryPo categoryPo = categoryDao.selectById(dishPo.getCategoryId());

                    if (!ObjectUtils.isEmpty(categoryPo)){
                        dish.setCategoryName(categoryPo.getName());
                    }

                    List<DishFlavor> dishFlavors = new ArrayList<>();

                    //查询口味
                    LambdaQueryWrapper<DishFlavorPo> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(DishFlavorPo::getIsDeleted,1);
                    queryWrapper.eq(DishFlavorPo::getDishId,dishPo.getId());

                    List<DishFlavorPo> dishFlavorPos = dishFlavorDao.selectList(queryWrapper);

                    if (dishFlavorPos.size()>0 && dishFlavorPos!=null){
                        dishFlavorPos.stream().forEach(dishFlavorPo -> {
                            DishFlavor dishFlavor = new DishFlavor();
                            BeanUtils.copyProperties(dishFlavorPo,dishFlavor);
                            dishFlavors.add(dishFlavor);
                        });

                    }
                    dish.setFlavors(dishFlavors);
                    list.add(dish);
                });



            }

            return list;

        }catch (Exception e){
            logger.error("列表查询异常{}", e);
            throw new BusinessException("列表查询异常");
        }

    }

}
