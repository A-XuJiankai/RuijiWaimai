package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.OrdersService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.Orders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author gett
 * @Date 2022/4/28  18:01
 * @Description 订单
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    private static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService ordersService;

    /**
     * @Description 下单
     * @Author gett
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public R<String> submit(@RequestBody Orders orders) {

        logger.info("下单入参{}", JSONObject.toJSONString(orders));

        try {

            OrdersSubmitReqDto ordersSubmitReqDto = new OrdersSubmitReqDto();
            BeanUtils.copyProperties(orders,ordersSubmitReqDto);

            ordersService.submit(ordersSubmitReqDto);

           return R.success("下单成功");

        }catch (Exception e){
            logger.info("下单异常",e);
            return R.error("下单异常");
        }


    }


    /**
     * @Description 分页查询
     * @Author gett
     */
    @RequestMapping(value = "/userPage",method = RequestMethod.GET)
    public R<Page> userPage(int page, int pageSize){
        logger.info("分页查询入参{}--{}",pageSize,page);
        try{

            OrdersUserPageReqDto ordersUserPageReqDto = new OrdersUserPageReqDto();
            ordersUserPageReqDto.setPage(page);
            ordersUserPageReqDto.setPageSize(pageSize);

            OrdersUserPageRspDto ordersUserPageRspDto =ordersService.pages(ordersUserPageReqDto);
            logger.info("分页查询出参{}",JSONObject.toJSONString(ordersUserPageRspDto));

            return  R.success(ordersUserPageRspDto.getPage());

        }catch (Exception e){
            logger.info("分页查询异常{}",e);
            return R.error("分页查询异常");
        }
    }

   /**
     * @Description 分页查询
     * @Author gett
     */
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public R<Page> page(int page, int pageSize,String number,String beginTime,String endTime){
        logger.info("分页查询入参{}--{}-{}-{}-{}",pageSize,page,number,beginTime,endTime);
        try{

            OrdersPageReqDto ordersPageReqDto = new OrdersPageReqDto();
            ordersPageReqDto.setNumber(number);
            ordersPageReqDto.setBeginTime(beginTime);
            ordersPageReqDto.setEndTime(endTime);

            ordersPageReqDto.setPage(page);
            ordersPageReqDto.setPageSize(pageSize);


            Page<OrdersPageRspDto> pagess = ordersService.pagess(ordersPageReqDto);
            logger.info("分页查询出参{}",JSONObject.toJSONString(pagess));

            return  R.success(pagess);

        }catch (Exception e){
            logger.info("分页查询异常{}",e);
            return R.error("分页查询异常");
        }
    }

    /**
     * @Description 修改状态
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R<String> updateSave(@RequestBody Orders orders) {
        logger.info("修改状态入参{}", JSONObject.toJSONString(orders));

        try {
            OrdersUpdateReqDto ordersUpdateReqDto = new OrdersUpdateReqDto();
            BeanUtils.copyProperties(orders,ordersUpdateReqDto);

            ordersService.updateSave(ordersUpdateReqDto);

            return R.success("修改状态成功");
        } catch (Exception e) {
            logger.info("修改状态异常{}", e);
            return R.error("修改状态异常");
        }
    }

    /**
     * @Description 再来一单
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST,value = "/again")
    public R<String> again(@RequestBody Orders orders) {
        logger.info("再来一单入参{}", JSONObject.toJSONString(orders));

        try {
            OrdersAgainReqDto ordersAgainReqDto = new OrdersAgainReqDto();
            BeanUtils.copyProperties(orders,ordersAgainReqDto);

            ordersService.again(ordersAgainReqDto);

            return R.success("再来一单成功");
        } catch (Exception e) {
            logger.info("再来一单异常{}", e);
            return R.error("再来一单异常");
        }
    }


}
