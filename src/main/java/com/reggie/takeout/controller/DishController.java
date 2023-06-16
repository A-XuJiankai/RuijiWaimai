package com.reggie.takeout.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.DishService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.Dish;
import com.reggie.takeout.vo.DishVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/24  8:28
 * @Description 菜品表
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    private static Logger logger = LoggerFactory.getLogger(DishController.class);

    @Autowired
    private DishService dishService;

    /**
     * @Description 保存菜品信息，同时保存对应的口味数据
     * @Author gett
     */

    @RequestMapping(method = RequestMethod.POST)
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> save(@RequestBody Dish dish) {
        logger.info("保存菜品信息入参{}", JSONObject.toJSONString(dish));
        try {
            DishSaveReqDto reqDto = new DishSaveReqDto();
            reqDto.setCategoryName(dish.getCategoryName());
            reqDto.setCopies(dish.getCopies());
            reqDto.setCategoryId(dish.getCategoryId());
            reqDto.setPrice(dish.getPrice());
            reqDto.setCode(dish.getCode());
            reqDto.setImage(dish.getImage());
            reqDto.setDescription(dish.getDescription());
            reqDto.setStatus(dish.getStatus());
            reqDto.setSort(dish.getSort());
            reqDto.setName(dish.getName());

            List<DishFlavorSaveReqDto> dishFlavorSaveReqDtos = new ArrayList<>();
            dish.getFlavors().stream().forEach(dishFlavor -> {
                DishFlavorSaveReqDto dishFlavorSaveReqDto = new DishFlavorSaveReqDto();
                BeanUtils.copyProperties(dishFlavor, dishFlavorSaveReqDto);
                dishFlavorSaveReqDtos.add(dishFlavorSaveReqDto);
            });
            reqDto.setFlavors(dishFlavorSaveReqDtos);

            dishService.saves(reqDto);

            return R.success("保存菜品信息成功");

        } catch (Exception e) {
            logger.info("保存菜品信息异常{}", e);
            return R.error("保存菜品信息异常");
        }

    }

    /**
     * @Description 菜品信息分页查询
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET, value = "/page")
    public R<Page> page(int page, int pageSize, String name) {

        logger.info("菜品信息分页入参{}-{}-{}", page, pageSize, name);

        try {

            DishPageReqDto dishPageReqDto = new DishPageReqDto();
            dishPageReqDto.setPage(page);
            dishPageReqDto.setPageSize(pageSize);
            dishPageReqDto.setName(name);

            Page<DishPageRspDto> pages = dishService.pages(dishPageReqDto);


            return R.success(pages);

        } catch (Exception e) {
            logger.info("菜品信息分页查询异常{}", e);
            return R.error("菜品信息分页查询异常");
        }
    }


    /**
     * @Description 修改菜品-回显信息
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public R<DishUpdateRspDto> updates(@PathVariable Long id) {
        logger.info("修改菜品入参{}", id);

        try {

            DishUpdateReqDto dishUpdateReqDto = new DishUpdateReqDto();
            dishUpdateReqDto.setId(id);
            DishUpdateRspDto dishUpdateRspDto = dishService.updates(dishUpdateReqDto);

            return R.success(dishUpdateRspDto);
        } catch (Exception e) {
            logger.info("修改菜品异常{}", e);
            return R.error("修改菜品异常");
        }
    }

    /**
     * @Description 修改菜品-保存
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> updateSave(@RequestBody Dish dish) {
        logger.info("修改菜品-保存入参{}", JSONObject.toJSONString(dish));

        try {
            DishUpdateSaveReqDto dishUpdateSaveReqDto = new DishUpdateSaveReqDto();
            BeanUtils.copyProperties(dish,dishUpdateSaveReqDto);

            dishService.updateSave(dishUpdateSaveReqDto);

            return R.success("修改菜品-保存成功");
        } catch (Exception e) {
            logger.info("修改菜品-保存异常{}", e);
            return R.error("修改菜品-保存异常");
        }
    }


    /**
     * @Description 删除菜品--根据id删除
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> delete(Long ids[]) {

        logger.info("删除分类入参{}", ids);

        try {
            DishDeleteReqDto dishDeleteReqDto = new DishDeleteReqDto();
            dishDeleteReqDto.setId(ids);

            dishService.deletes(dishDeleteReqDto);

            return R.success("删除成功");
        } catch (Exception e) {
            logger.info("删除分类异常{}", e);
            return R.error("删除分类异常");
        }


    }



    /**
     * @Description 菜品停售
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST,value = "/status/{status}")
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> status(@PathVariable String status,Long ids[]){

        logger.info("菜品停售入参{}-{}", status,ids);

        try {
            DishStatusReqDto dishStatusReqDto = new DishStatusReqDto();
            dishStatusReqDto.setStatus(status);
            dishStatusReqDto.setIds(ids);
            dishService.status(dishStatusReqDto);

            return R.success("菜品停售成功");

        }catch (Exception e) {
            logger.info("菜品停售异常{}", e);
            return R.error("菜品停售异常");
        }

    }

    /**
     * @Description 列表查询
     * @Author gett
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @Cacheable(value = "dishCache",key = "#dishVo.categoryId+'_'+#dishVo.status")
    public R<List<Dish>> list(DishVo dishVo){
        logger.info("列表查询入参{}",JSONObject.toJSONString(dishVo));
        try {

            DishListReqDto dishListReqDto = new DishListReqDto();
            BeanUtils.copyProperties(dishVo,dishListReqDto);

            //List<DishListRspDto> dishes=dishService.lists(dishListReqDto);
            List<Dish> dishes=dishService.listss(dishListReqDto);

            return R.success(dishes);

        }catch (Exception e) {
            logger.info("列表查询异常{}", e);
            return R.error("列表查询异常");
        }

    }
}
