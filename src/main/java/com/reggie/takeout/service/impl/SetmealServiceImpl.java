package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.SetmealDao;
import com.reggie.takeout.dao.SetmealDishDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.*;
import com.reggie.takeout.service.SetmealService;
import com.reggie.takeout.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/14  16:52
 * @Description  套餐管理
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, SetmealPo> implements SetmealService {

    private static Logger logger = LoggerFactory.getLogger(SetmealServiceImpl.class);

    @Resource
    private SetmealDao setmealDao;

    @Resource
    private SetmealDishDao setmealDishDao;

    /**
     * @Description 新增套餐
     * @Author gett
     */
    @Override
    @Transactional
    public void saves(SetmealSaveReqDto setmealSaveReqDto) throws Exception {
        logger.info("新增套餐入参{}", JSONObject.toJSONString(setmealSaveReqDto));

        //保存套餐的基本信息
        SetmealPo setmealPo = new SetmealPo();
        BeanUtils.copyProperties(setmealSaveReqDto,setmealPo);
        setmealPo.setIsDeleted(1);
        setmealDao.insert(setmealPo);

        if (!ObjectUtils.isEmpty(setmealSaveReqDto.getSetmealDishes())){
            setmealSaveReqDto.getSetmealDishes().stream().forEach(setmealDishPo -> {
                setmealDishPo.setSetmealId(setmealPo.getId());
                setmealDishPo.setIsDeleted(1);
                setmealDishDao.insert(setmealDishPo);
            });
        }

    }

    /**
     * @Description 套餐分页
     * @Author gett
     */
    @Override
    public Page<SetmealPageRspDto> pages(SetmealPageReqDto setmealPageReqDto) throws Exception {
        logger.info("套餐分页分页查询入参{}", JSONObject.toJSONString(setmealPageReqDto));

        try {

            Page<SetmealPo> setmealPoPage = new Page<>(setmealPageReqDto.getPage(), setmealPageReqDto.getPageSize());
            Page<SetmealPageRspDto> saveReqDtoPage = new Page<>();
            ArrayList<SetmealPageRspDto> dtos = new ArrayList<>();

            //菜单表
            LambdaQueryWrapper<SetmealPo> setmealPoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealPoLambdaQueryWrapper.like(setmealPageReqDto.getName()!=null,SetmealPo::getName,setmealPageReqDto.getName());
            setmealPoLambdaQueryWrapper.orderByDesc(SetmealPo::getUpdateTime);
            setmealPoLambdaQueryWrapper.eq(SetmealPo::getIsDeleted,1);
            setmealPoPage= setmealDao.selectPage(setmealPoPage, setmealPoLambdaQueryWrapper);

            BeanUtils.copyProperties(setmealPoPage,saveReqDtoPage,"records");

            logger.info("00000{}",JSONObject.toJSONString(saveReqDtoPage));
            logger.info("00000{}",JSONObject.toJSONString(setmealPoPage));

            List<SetmealPo> records = setmealPoPage.getRecords();
            records.stream().forEach(setmealPo -> {
                SetmealPageRspDto setmealPageRspDto = new SetmealPageRspDto();
                BeanUtils.copyProperties(setmealPo,setmealPageRspDto);

                LambdaQueryWrapper<SetmealPo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealPo::getId,setmealPo.getCategoryId());
                queryWrapper.eq(SetmealPo::getIsDeleted,1);
                SetmealPo po = setmealDao.selectOne(queryWrapper);

                if (!ObjectUtils.isEmpty(po)){
                    setmealPageRspDto.setCategoryName(po.getName());
                }
                dtos.add(setmealPageRspDto);
            });

            saveReqDtoPage.setRecords(dtos);
            return saveReqDtoPage;

        }catch (Exception e){
            logger.error("套餐分页查询异常{}", e);
            throw new BusinessException("套餐分页查询异常");
        }


    }

    /**
     * @Description 修改套餐-回显信息
     * @Author gett
     */
    @Override
    public SetmealUpdatesRspDto updates(SetmealUpdatesReqDto setmealUpdatesReqDto) throws Exception {
        logger.info("修改套餐-回显信息入参{}", JSONObject.toJSONString(setmealUpdatesReqDto));
        SetmealUpdatesRspDto setmealUpdatesRspDto = new SetmealUpdatesRspDto();

        try {

            //根据菜品id查询信息以及对应的口味
            LambdaQueryWrapper<SetmealPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealPo::getId, setmealUpdatesReqDto.getId());
            wrapper.eq(SetmealPo::getIsDeleted,1);
            SetmealPo setmealPo = setmealDao.selectOne(wrapper);

            BeanUtils.copyProperties(setmealPo,setmealUpdatesRspDto);

            if (!ObjectUtils.isEmpty(setmealPo)){
                LambdaQueryWrapper<SetmealDishPo> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDishPo::getSetmealId, setmealPo.getId());
                queryWrapper.eq(SetmealDishPo::getIsDeleted,1);
                List<SetmealDishPo> setmealDishPos = setmealDishDao.selectList(queryWrapper);

                setmealUpdatesRspDto.setSetmealDishes(setmealDishPos);

            }


        }catch (Exception e){
            logger.error("修改套餐-回显信息异常{}", e);
            throw new BusinessException("修改套餐-回显信息异常");
        }


        return setmealUpdatesRspDto;
    }

    /**
     * @Description  修改套餐-保存
     * @Author gett
     */
    @Override
    @Transactional
    public void updateSave(SetmealUpdateSaveReqDto setmealUpdateSaveReqDto) throws Exception {
        logger.info("修改菜品-保存入参{}",JSONObject.toJSONString(setmealUpdateSaveReqDto));

        //更新dish表
        SetmealPo setmealPo = new SetmealPo();
        BeanUtils.copyProperties(setmealUpdateSaveReqDto,setmealPo);
        setmealPo.setIsDeleted(1);
        setmealDao.updateById(setmealPo);

        //清除当前口味信息
        LambdaQueryWrapper<SetmealDishPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDishPo::getSetmealId,setmealUpdateSaveReqDto.getId());
        setmealDishDao.delete(wrapper);

        setmealUpdateSaveReqDto.getSetmealDishes().stream().forEach(dishFlavor -> {
            SetmealDishPo setmealDishPo = new SetmealDishPo();
            BeanUtils.copyProperties(dishFlavor,setmealDishPo);
            setmealDishPo.setIsDeleted(1);
            setmealDishPo.setSetmealId(setmealUpdateSaveReqDto.getId());
            setmealDishDao.insert(setmealDishPo);
        });

    }

    /**
     * @Description 删除套餐--根据id删除
     * @Author gett
     */
    @Override
    @Transactional
    public void deletes(SetmealDeleteReqDto setmealDeleteReqDto) throws Exception {
        logger.info("删除套餐入参{}",JSONObject.toJSONString(setmealDeleteReqDto));

        Long[] ids = setmealDeleteReqDto.getId();
        for (int i = 0; i < ids.length; i++) {

            LambdaQueryWrapper<SetmealPo> setmealWrapper = new LambdaQueryWrapper<>();
            setmealWrapper.eq(SetmealPo::getId,ids[i]);
            setmealWrapper.eq(SetmealPo::getIsDeleted,1);
            setmealWrapper.eq(SetmealPo::getStatus,1);
            SetmealPo one = setmealDao.selectOne(setmealWrapper);

            if (!ObjectUtils.isEmpty(one)){
                //- 根据当前菜品的ID，查询该菜品下是否存在菜品口味
                LambdaQueryWrapper<SetmealDishPo> setmealDishWrapper = new LambdaQueryWrapper<>();
                setmealDishWrapper.eq(SetmealDishPo::getSetmealId,ids[i]);
                setmealDishWrapper.eq(SetmealDishPo::getIsDeleted,1);
                List<SetmealDishPo> setmealDishPos = setmealDishDao.selectList(setmealDishWrapper);

                if (setmealDishPos.size()>0){
                    setmealDishPos.stream().forEach(setmealDishPo -> {
                        SetmealDishPo po = new SetmealDishPo();
                        po.setIsDeleted(0);
                        po.setId(setmealDishPo.getId());
                        setmealDishDao.updateById(po);
                    });


                    SetmealPo setmealPo = new SetmealPo();
                    setmealPo.setId(ids[i]);
                    setmealPo.setIsDeleted(0);
                    setmealDao.updateById(setmealPo);
                }

            }else {
                throw new BusinessException("套餐正在售卖中，不能删除");
            }
        }
    }

    /**
     * @Description 套餐停售
     * @Author gett
     */
    @Override
    public void status(SetmealStatusReqDto setmealStatusReqDto) throws Exception {
        logger.info("套餐停售入参{}",JSONObject.toJSONString(setmealStatusReqDto));
        try {

            if (setmealStatusReqDto.getIds()!=null){
                Long[] ids = setmealStatusReqDto.getIds();
                for (int i = 0; i < ids.length; i++) {
                    LambdaQueryWrapper<SetmealPo> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SetmealPo::getId,ids[i]);
                    wrapper.eq(SetmealPo::getIsDeleted,1);
                    SetmealPo setmealPo = setmealDao.selectOne(wrapper);
                    if (!ObjectUtils.isEmpty(setmealPo)){
                        String status = setmealStatusReqDto.getStatus();
                        int anInt = Integer.parseInt(status);
                        setmealPo.setStatus(anInt);
                        setmealDao.updateById(setmealPo);
                    }
                }
            }


        }catch (Exception e){
            logger.error("套餐停售异常{}", e);
            throw new BusinessException("套餐停售异常");
        }

    }

    /**
     * @Description 根据条件查询套餐数据
     * @Author gett
     */
    @Override
    public List<SetmealListRspDto> lists(SetmealListReqDto setmealListReqDto) throws Exception {
        logger.info("根据条件查询套餐数据入参{}",JSONObject.toJSONString(setmealListReqDto));
        ArrayList<SetmealListRspDto> list = new ArrayList<>();

        try {

            LambdaQueryWrapper<SetmealPo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(setmealListReqDto.getCategoryId() != null,SetmealPo::getCategoryId,setmealListReqDto.getCategoryId());
            queryWrapper.eq(setmealListReqDto.getStatus() != null,SetmealPo::getStatus,setmealListReqDto.getStatus());
            queryWrapper.eq(SetmealPo::getIsDeleted ,1);
            queryWrapper.orderByDesc(SetmealPo::getUpdateTime);

            List<SetmealPo> setmealPos = setmealDao.selectList(queryWrapper);

            if (setmealPos.size()>0 && setmealPos!=null){
                setmealPos.stream().forEach(setmealPo -> {
                    SetmealListRspDto setmealListRspDto = new SetmealListRspDto();
                    BeanUtils.copyProperties(setmealPo,setmealListRspDto);
                    list.add(setmealListRspDto);
                });
            }

        }catch (Exception e){
            logger.error("根据条件查询套餐数据异常{}", e);
            throw new BusinessException("根据条件查询套餐数据异常");
        }
        return list;
    }
}
