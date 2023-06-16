package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.ShoppingCartService;
import com.reggie.takeout.utils.BaseContext;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/28  9:27
 * @Description 购物车
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private static Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * @Description 加入购物车
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST,value = "/add")
    public R<ShoppingCartAddRspDto> add(@RequestBody ShoppingCart shoppingCart) {
        logger.info("加入购物车入参{}", JSONObject.toJSONString(shoppingCart));

        try {
            ShoppingCartAddReqDto shoppingCartAddReqDto = new ShoppingCartAddReqDto();
            BeanUtils.copyProperties(shoppingCart,shoppingCartAddReqDto);
            shoppingCartAddReqDto.setUserId(BaseContext.getCurrentId());
            ShoppingCartAddRspDto shoppingCartAddRspDto=shoppingCartService.add(shoppingCartAddReqDto);

            return R.success(shoppingCartAddRspDto);
        }catch (Exception e) {
            logger.info("加入购物车异常{}", e);
            return R.error("加入购物车异常");
        }
    }

    /**
     * @Description 减少购物车数量
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST,value = "/sub")
    public R<ShoppingCartSubRspDto> sub(@RequestBody ShoppingCart shoppingCart) {
        logger.info("减少购物车数量入参{}", JSONObject.toJSONString(shoppingCart));

        try {
            ShoppingCartSubReqDto shoppingCartSubReqDto = new ShoppingCartSubReqDto();
            BeanUtils.copyProperties(shoppingCart,shoppingCartSubReqDto);
            shoppingCartSubReqDto.setUserId(BaseContext.getCurrentId());
            ShoppingCartSubRspDto shoppingCartAddRspDto =shoppingCartService.sub(shoppingCartSubReqDto);

            return R.success(shoppingCartAddRspDto);
        }catch (Exception e) {
            logger.info("减少购物车数量异常{}", e);
            return R.error("减少购物车数量异常");
        }
    }

    /**
     * @Description 查看购物车
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/list")
    public R<List<ShoppingCartListRspDto>> list() {

        try {

            List<ShoppingCartListRspDto> list=shoppingCartService.lists();

            return R.success(list);
        }catch (Exception e) {
            logger.info("查看购物车异常{}", e);
            return R.error("查看购物车异常");
        }
    }

    /**
     * @Description 清空购物车
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/clean")
    public R<String> clean() {

        try {

            shoppingCartService.deletes();

            return R.success("清空购物车成功");
        }catch (Exception e) {
            logger.info("清空购物车异常{}", e);
            return R.error("清空购物车异常");
        }
    }
}
