package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.SetmealService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.Setmeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  15:39
 * @Description 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static Logger logger = LoggerFactory.getLogger(SetmealController.class);

    @Autowired
    private SetmealService setmealService;


    /**
     * @Description 新增套餐
     * @Author gett
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @RequestMapping(method = RequestMethod.POST)
    public R<String> save(@RequestBody Setmeal setmeal){
        logger.info("新增套餐入参{}", JSONObject.toJSONString(setmeal));
        try {

            SetmealSaveReqDto setmealSaveReqDto = new SetmealSaveReqDto();
            BeanUtils.copyProperties(setmeal,setmealSaveReqDto);

            setmealService.saves(setmealSaveReqDto);

            return R.success("新增套餐成功");
        }catch (Exception e) {
            logger.info("新增套餐异常{}", e);
            return R.error("新增套餐异常");
        }
    }


    /**
     * @Description 套餐分页
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/page")
    public R<Page> page(int page, int pageSize, String name) {

        logger.info("套餐分页分页入参{}-{}-{}", page, pageSize, name);

        try {

            SetmealPageReqDto setmealPageReqDto = new SetmealPageReqDto();
            setmealPageReqDto.setPage(page);
            setmealPageReqDto.setPageSize(pageSize);
            setmealPageReqDto.setName(name);

            Page<SetmealPageRspDto> pages = setmealService.pages(setmealPageReqDto);


            return R.success(pages);

        } catch (Exception e) {
            logger.info("菜品信息分页查询异常{}", e);
            return R.error("菜品信息分页查询异常");
        }
    }

    /**
     * @Description 修改套餐-回显信息
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public R<SetmealUpdatesRspDto> updates(@PathVariable Long id) {
        logger.info("修改套餐-回显信息入参{}", id);

        try {

            SetmealUpdatesReqDto setmealUpdatesReqDto = new SetmealUpdatesReqDto();
            setmealUpdatesReqDto.setId(id);
            SetmealUpdatesRspDto setmealUpdatesRspDto = setmealService.updates(setmealUpdatesReqDto);

            return R.success(setmealUpdatesRspDto);
        } catch (Exception e) {
            logger.info("修改套餐-回显信息异常{}", e);
            return R.error("修改套餐-回显信息异常");
        }
    }


    /**
     * @Description 修改套餐-保存
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> updateSave(@RequestBody Setmeal setmeal) {
        logger.info("修改菜品-保存入参{}", JSONObject.toJSONString(setmeal));

        try {
            SetmealUpdateSaveReqDto setmealUpdateSaveReqDto = new SetmealUpdateSaveReqDto();
            BeanUtils.copyProperties(setmeal,setmealUpdateSaveReqDto);

            setmealService.updateSave(setmealUpdateSaveReqDto);

            return R.success("修改套餐-保存成功");
        } catch (Exception e) {
            logger.info("修改套餐-保存异常{}", e);
            return R.error("修改套餐-保存异常");
        }
    }


    /**
     * @Description 删除套餐--根据id删除
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(Long ids[]) {

        logger.info("删除套餐入参{}", ids);

        try {
            SetmealDeleteReqDto setmealDeleteReqDto = new SetmealDeleteReqDto();
            setmealDeleteReqDto.setId(ids);

            setmealService.deletes(setmealDeleteReqDto);

            return R.success("删除套餐成功");
        } catch (Exception e) {
            logger.info("删除套餐异常{}", e);
            return R.error("删除套餐异常");
        }


    }



    /**
     * @Description 菜品停售
     * @Author gett
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @RequestMapping(method = RequestMethod.POST,value = "/status/{status}")
    public R<String> status(@PathVariable String status,Long ids[]){

        logger.info("菜品停售入参{}-{}", status,ids);

        try {
            SetmealStatusReqDto setmealStatusReqDto = new SetmealStatusReqDto();
            setmealStatusReqDto.setStatus(status);
            setmealStatusReqDto.setIds(ids);
            setmealService.status(setmealStatusReqDto);

            return R.success("菜品停售成功");

        }catch (Exception e) {
            logger.info("菜品停售异常{}", e);
            return R.error("菜品停售异常");
        }

    }

    /**
     * @Description   根据条件查询套餐数据
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<SetmealListRspDto>> list(Setmeal setmeal){

        logger.info("根据条件查询套餐数据入参{}",JSONObject.toJSONString(setmeal));

        try {

            SetmealListReqDto setmealListReqDto = new SetmealListReqDto();
            BeanUtils.copyProperties(setmeal,setmealListReqDto);
            List<SetmealListRspDto> list = setmealService.lists(setmealListReqDto);
            return R.success(list);

        }catch (Exception e) {
            logger.info("根据条件查询套餐数据异常{}", e);
            return R.error("根据条件查询套餐数据异常");
        }

    }

}
