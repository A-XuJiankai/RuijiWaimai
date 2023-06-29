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
 * @Author yaqi-zhou
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
     * @Author yaqi-zhou
     */
    @RequestMapping(method = RequestMethod.POST,value = "/add")
*post请求方式提交，"/add"是接口路径
    public R<ShoppingCartAddRspDto> add(@RequestBody ShoppingCart shoppingCart) {
*通过@RequestBody注解将前端传来的ShoppingCart对象转换为Java对象
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
*创建ShoppingCartAddReqDto对象,使用BeanUtils.copyProperties()方法将前端传来的ShoppingCart对象的属性值赋给它
*设置ShoppingCart的userId属性为当前用户的id（调用BaseContext.getCurrentId()获取），调用shoppingCartService的add()方法，进行购物车操作
*如果加入购物车操作成功，返回成功提示信息和ShoppingCartAddRspDto对象
*如果加入购物车操作失败，返回失败提示信息
*如果加入购物车操作异常，记录异常日志并返回错误信息
    /**
     * @Description 减少购物车数量
     * @Author yaqi-zhou
     */
    @RequestMapping(method = RequestMethod.POST,value = "/sub")
*POST请求方式提交，接口路径为"/sub"
    public R<ShoppingCartSubRspDto> sub(@RequestBody ShoppingCart shoppingCart) {
*通过@RequestBody注解将前端传来的ShoppingCart对象转换为Java对象    
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
*创建ShoppingCartSubReqDto对象，使用BeanUtils.copyProperties()方法将前端传来的ShoppingCart对象的属性值赋给它
*设置ShoppingCart的userId属性为当前用户的id（调用BaseContext.getCurrentId()获取），调用shoppingCartService的sub()方法，进行购物车数量减少操作
*如果购物车数量减少操作成功，返回成功提示信息和ShoppingCartSubRspDto对象
*如果购物车数量减少操作失败，返回失败提示信息
*如果购物车数量减少操作异常，记录异常日志并返回错误信息
    /**
     * @Description 查看购物车
     * @Author yaqi-zhou
     */
    @RequestMapping(method = RequestMethod.GET,value = "/list")
*通过GET请求方式提交，接口路径为"/list"
    public R<List<ShoppingCartListRspDto>> list() {

        try {

            List<ShoppingCartListRspDto> list=shoppingCartService.lists();

            return R.success(list);
        }catch (Exception e) {
            logger.info("查看购物车异常{}", e);
            return R.error("查看购物车异常");
        }
    }
*直接调用shoppingCartService的lists()方法，返回包含ShoppingCartListRspDto对象的列表
*如果查询成功，返回成功提示信息和ShoppingCartListRspDto对象
*如果查询失败，返回失败提示信息
*如果查询异常，记录异常日志并返回错误信息

    /**
     * @Description 清空购物车
     * @Author yaqi-zhou
     */
    @RequestMapping(method = RequestMethod.DELETE,value = "/clean")
*通过DELETE请求方式提交，接口路径为"/clean"
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
*直接调用shoppingCartService的deletes()方法进行购物车清空操作
*如果清空操作成功，返回成功提示信息
*如果清空操作失败，返回失败提示信息
*如果清空操作异常，记录异常日志并返回错误信息
