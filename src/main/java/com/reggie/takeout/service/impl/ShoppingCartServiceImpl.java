package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.ShoppingCartDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.ShoppingCartPo;
import com.reggie.takeout.service.ShoppingCartService;
import com.reggie.takeout.utils.BaseContext;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/28  9:25
 * @Description 购物车
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCartPo> implements ShoppingCartService {

    private static Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    public static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);


    @Resource
    private ShoppingCartDao shoppingCartDao;

    /**
     * @Description 加入购物车
     * @Author gett
     */
    @Override
    public ShoppingCartAddRspDto add(ShoppingCartAddReqDto shoppingCartAddReqDto) throws Exception {
        logger.info("加入购物车入参{}", JSONObject.toJSONString(shoppingCartAddReqDto));

        try {

            ShoppingCartAddRspDto shoppingCartAddRspDto = new ShoppingCartAddRspDto();

            //A. 获取当前登录用户，为购物车对象赋值

            //B. 根据当前登录用户ID 及 本次添加的菜品ID/套餐ID，查询购物车数据是否存在
            shoppingCartAddReqDto.getDishId();
            LambdaQueryWrapper<ShoppingCartPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShoppingCartPo::getUserId,shoppingCartAddReqDto.getUserId());
            wrapper.eq(shoppingCartAddReqDto.getDishId()!=null,ShoppingCartPo::getDishId,shoppingCartAddReqDto.getDishId());
            wrapper.eq(shoppingCartAddReqDto.getSetmealId()!=null,ShoppingCartPo::getSetmealId,shoppingCartAddReqDto.getSetmealId());

            ShoppingCartPo shoppingCartPo = shoppingCartDao.selectOne(wrapper);

            if (ObjectUtils.isEmpty(shoppingCartPo)) {
                //如果不存在，则添加到购物车，数量默认就是1
                ShoppingCartPo po = new ShoppingCartPo();
                BeanUtils.copyProperties(shoppingCartAddReqDto,po);
                po.setNumber(1);
                po.setIsDeleted(1);
                po.setUpdateTime(sdf.format(new Date()));
                po.setCreateTime(sdf.format(new Date()));
                shoppingCartDao.insert(po);
                BeanUtils.copyProperties(po,shoppingCartAddRspDto);
            }else {
                //存在，判断口味是否相同
                if (shoppingCartPo.getDishFlavor().equals(shoppingCartAddReqDto.getDishFlavor())){
                    ShoppingCartPo po = new ShoppingCartPo();
                    BeanUtils.copyProperties(shoppingCartAddReqDto,po);
                    po.setNumber(1);
                    po.setIsDeleted(1);
                    po.setUpdateTime(sdf.format(new Date()));
                    po.setCreateTime(sdf.format(new Date()));
                    shoppingCartDao.insert(po);
                    BeanUtils.copyProperties(po,shoppingCartAddRspDto);
                }else {
                    Integer number = shoppingCartPo.getNumber();
                    shoppingCartPo.setNumber(number+1);
                    shoppingCartPo.setIsDeleted(1);
                    shoppingCartPo.setUpdateTime(sdf.format(new Date()));
                    shoppingCartDao.updateById(shoppingCartPo);
                    BeanUtils.copyProperties(shoppingCartPo,shoppingCartAddRspDto);
                }


            }
            return shoppingCartAddRspDto;

        }catch (Exception e){
            logger.error("加入购物车异常{}", e);
            throw new BusinessException("加入购物车异常");
        }

    }

    /**
     * @Description 查看购物车
     * @Author gett
     */
    @Override
    public List<ShoppingCartListRspDto> lists() throws Exception {

        try {
            ArrayList<ShoppingCartListRspDto> list = new ArrayList<>();

            LambdaQueryWrapper<ShoppingCartPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShoppingCartPo::getUserId, BaseContext.getCurrentId());
            wrapper.eq(ShoppingCartPo::getIsDeleted, 1);
            wrapper.gt(ShoppingCartPo::getNumber,0);
            wrapper.orderByAsc(ShoppingCartPo::getCreateTime);

            List<ShoppingCartPo> shoppingCartPos = shoppingCartDao.selectList(wrapper);

            if (shoppingCartPos.size()>0&&shoppingCartPos!=null){
                shoppingCartPos.stream().forEach(shoppingCartPo -> {
                    ShoppingCartListRspDto shoppingCartListRspDto = new ShoppingCartListRspDto();
                    BeanUtils.copyProperties(shoppingCartPo,shoppingCartListRspDto);
                    list.add(shoppingCartListRspDto);

                });
            }

            return list;

        }catch (Exception e){
            logger.error("加入购物车异常{}", e);
            throw new BusinessException("加入购物车异常");
        }
    }

    /**
     * @Description 清空购物车
     * @Author gett
     */
    @Override
    public void deletes() throws Exception {
        try {

            ShoppingCartPo shoppingCartPo = new ShoppingCartPo();
            shoppingCartPo.setIsDeleted(0);
            shoppingCartPo.setUserId(BaseContext.getCurrentId());
            shoppingCartPo.setUpdateTime(sdf.format(new Date()));
            shoppingCartDao.update(shoppingCartPo,null);

        }catch (Exception e){
            logger.error("清空购物车异常{}", e);
            throw new BusinessException("清空购物车异常");
        }
    }

    /**
     * @Description 减少购物车数量
     * @Author gett
     */
    @Override
    public ShoppingCartSubRspDto sub(ShoppingCartSubReqDto shoppingCartSubReqDto) throws Exception {
        logger.info("减少购物车数量入参{}", JSONObject.toJSONString(shoppingCartSubReqDto));

        try {
            ShoppingCartSubRspDto shoppingCartSubRspDto = new ShoppingCartSubRspDto();

            LambdaQueryWrapper<ShoppingCartPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(shoppingCartSubReqDto.getDishId()!=null,ShoppingCartPo::getDishId,shoppingCartSubReqDto.getDishId());
            wrapper.eq(shoppingCartSubReqDto.getSetmealId()!=null,ShoppingCartPo::getSetmealId,shoppingCartSubReqDto.getSetmealId());
            wrapper.eq(ShoppingCartPo::getIsDeleted,1);

            ShoppingCartPo shoppingCartPo = shoppingCartDao.selectOne(wrapper);
            Integer number = shoppingCartPo.getNumber();

            if(number>0){
                shoppingCartPo.setNumber(number-1);
                shoppingCartDao.updateById(shoppingCartPo);
                BeanUtils.copyProperties(shoppingCartPo,shoppingCartSubRspDto);
            }



            return shoppingCartSubRspDto;

        }catch (Exception e){
            logger.error("减少购物车数量异常{}", e);
            throw new BusinessException("减少购物车数量异常");
        }

    }
}
