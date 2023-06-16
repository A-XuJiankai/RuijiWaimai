package com.reggie.takeout.service.impl;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.*;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.*;
import com.reggie.takeout.service.OrdersService;
import com.reggie.takeout.service.ShoppingCartService;
import com.reggie.takeout.utils.BaseContext;
import com.reggie.takeout.utils.BusinessException;
import com.reggie.takeout.utils.DateUtil;
import com.reggie.takeout.utils.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author gett
 * @Date 2022/4/28  18:00
 * @Description 订单
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, OrdersPo> implements OrdersService {

    private static Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    public static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);

    @Resource
    private OrdersDao ordersDao;

    @Resource
    private OrderDetailDao orderDetailDao;

    @Resource
    private ShoppingCartDao shoppingCartDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AddressBookDao addressBookDao;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @Description 下单
     * @Author gett
     */
    @Override
    @Transactional
    public void submit(OrdersSubmitReqDto ordersSubmitReqDto) throws Exception {
        logger.info("下单入参{}", JSONObject.toJSONString(ordersSubmitReqDto));
        //A. 获得当前用户id, 查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCartPo> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCartPo::getIsDeleted,1);
        shoppingCartWrapper.eq(ShoppingCartPo::getUserId,userId);
        shoppingCartWrapper.gt(ShoppingCartPo::getNumber,0);

        List<ShoppingCartPo> shoppingCartPos = shoppingCartDao.selectList(shoppingCartWrapper);

        if (shoppingCartPos.size()<0&& shoppingCartPos==null){
            throw new BusinessException("购物车为空，不能下单");
        }


        //B. 根据当前登录用户id, 查询用户数据
        UserPo userPo = userDao.selectById(userId);


        //C. 根据地址ID, 查询地址数据
        Long addressBookId = ordersSubmitReqDto.getAddressBookId();
        AddressBookPo addressBookPo = addressBookDao.selectById(addressBookId);

        if ( addressBookPo==null){
            throw new BusinessException("地址信息为空，不能下单");
        }

        String orderId = IdUtil.getId();
        Long aLong = Long.parseLong(orderId);
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetailPo> orderDetails = shoppingCartPos.stream().map(shoppingCartPo -> {
            OrderDetailPo orderDetailPo = new OrderDetailPo();
            orderDetailPo.setName(shoppingCartPo.getName());
            orderDetailPo.setOrderId(aLong);
            orderDetailPo.setDishId(shoppingCartPo.getDishId());
            orderDetailPo.setSetmealId(shoppingCartPo.getSetmealId());
            orderDetailPo.setDishFlavor(shoppingCartPo.getDishFlavor());
            orderDetailPo.setNumber(shoppingCartPo.getNumber());
            orderDetailPo.setAmount(shoppingCartPo.getAmount());
            orderDetailPo.setImage(shoppingCartPo.getImage());
            amount.addAndGet(shoppingCartPo.getAmount().multiply(new BigDecimal(shoppingCartPo.getNumber())).intValue());
            return orderDetailPo;
        }).collect(Collectors.toList());

        //D. 组装订单明细数据, 批量保存订单明细

        OrdersPo ordersPo = new OrdersPo();
        BeanUtils.copyProperties(ordersSubmitReqDto,ordersPo);
        ordersPo.setStatus(2);
        ordersPo.setUserId(userId);
        ordersPo.setOrderTime(sdf.format(new Date()));
        ordersPo.setCheckoutTime(sdf.format(new Date()));
        ordersPo.setAmount(new BigDecimal(amount.get()));
        ordersPo.setUserName(userPo.getName());
        ordersPo.setPhone(addressBookPo.getPhone());
        ordersPo.setAddress((addressBookPo.getProvinceName() == null ? "" : addressBookPo.getProvinceName())
                + (addressBookPo.getCityName() == null ? "" : addressBookPo.getCityName())
                + (addressBookPo.getDistrictName() == null ? "" : addressBookPo.getDistrictName())
                + (addressBookPo.getDetail() == null ? "" : addressBookPo.getDetail()));
        ordersPo.setConsignee(addressBookPo.getConsignee());
        ordersPo.setNumber(orderId);
        //E. 组装订单数据, 批量保存订单数据
        ordersDao.insert(ordersPo);
        if (orderDetails.size()>0 && orderDetails!=null){
            orderDetails.stream().forEach(po->{

                orderDetailDao.insert(po);

            });
        }

        //F. 删除当前用户的购物车列表数据
        shoppingCartPos.stream().forEach(shoppingCartPo -> {
            shoppingCartPo.setIsDeleted(0);
            shoppingCartDao.updateById(shoppingCartPo);
        });


    }

    /**
     * @Description 分页查询
     * @Author gett
     */
    @Override
    public OrdersUserPageRspDto pages(OrdersUserPageReqDto ordersUserPageReqDto) throws Exception {
        logger.info("分类信息分页查询入参{}",JSONObject.toJSONString(ordersUserPageReqDto));

        try {
            OrdersUserPageRspDto ordersUserPageRspDto = new OrdersUserPageRspDto();

            Page<OrdersPo> page = new Page<>(ordersUserPageReqDto.getPage(), ordersUserPageReqDto.getPageSize());

            LambdaQueryWrapper<OrdersPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrdersPo::getUserId,BaseContext.getCurrentId());
            Page<OrdersPo> ordersPoPage = ordersDao.selectPage(page, wrapper);

            ordersUserPageRspDto.setPage(ordersPoPage);

            logger.info("分类信息分页查询出参{}",JSONObject.toJSONString(ordersUserPageRspDto));
            return ordersUserPageRspDto;

        }catch (Exception e){
            logger.info("分类信息分页查询异常{}",e);
            throw new BusinessException("分类信息分页查询异常");
        }
    }

    /**
     * @Description 分页查询
     * @Author gett
     */
    @Override
    public Page<OrdersPageRspDto>  pagess(OrdersPageReqDto ordersPageReqDto) throws Exception {
        logger.info("分类信息分页查询入参{}",JSONObject.toJSONString(ordersPageReqDto));
        try {
            Page<OrdersPo> ordersPoPage = new Page<>(ordersPageReqDto.getPage(), ordersPageReqDto.getPageSize());
            Page<OrdersPageRspDto> saveReqDtoPage = new Page<>();

            LambdaQueryWrapper<OrdersPo> ordersPoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ordersPoLambdaQueryWrapper.like(ordersPageReqDto.getNumber()!=null,OrdersPo::getNumber,ordersPageReqDto.getNumber());
            ordersPoLambdaQueryWrapper.orderByDesc(OrdersPo::getOrderTime);
            ordersPoLambdaQueryWrapper.between(ordersPageReqDto.getBeginTime()!=null &&ordersPageReqDto.getEndTime()!=null,
                    OrdersPo::getOrderTime,ordersPageReqDto.getBeginTime(),ordersPageReqDto.getEndTime());
            ordersPoPage= ordersDao.selectPage(ordersPoPage, ordersPoLambdaQueryWrapper);

            BeanUtils.copyProperties(ordersPoPage,saveReqDtoPage);

            return saveReqDtoPage;

        }catch (Exception e){
            logger.info("分类信息分页查询异常{}",e);
            throw new BusinessException("分类信息分页查询异常");
        }
    }

    /**
     * @Description 修改状态
     * @Author gett
     */
    @Override
    public void updateSave(OrdersUpdateReqDto ordersUpdateReqDto) throws Exception {
        logger.info("修改状态入参{}", JSONObject.toJSONString(ordersUpdateReqDto));

        try {
            OrdersPo ordersPo = new OrdersPo();
            BeanUtils.copyProperties(ordersUpdateReqDto,ordersPo);
            ordersDao.updateById(ordersPo);

        }catch (Exception e){
            logger.info("修改状态异常{}",e);
            throw new BusinessException("修改状态异常");
        }

    }

    /**
     * @Descriptio 再来一单
     * @Author gett
     */
    @Override
    public void again(OrdersAgainReqDto ordersAgainReqDto) throws Exception {
        logger.info("再来一单入参{}", JSONObject.toJSONString(ordersAgainReqDto));

        try {

            OrdersPo ordersPo = ordersDao.selectById(ordersAgainReqDto.getId());

            LambdaQueryWrapper<OrderDetailPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetailPo::getOrderId,ordersPo.getNumber());
            List<OrderDetailPo> orderDetailPos = orderDetailDao.selectList(wrapper);

            orderDetailPos.stream().forEach(orderDetailPo -> {
                ShoppingCartAddReqDto shoppingCartAddReqDto = new ShoppingCartAddReqDto();
                shoppingCartAddReqDto.setAmount(orderDetailPo.getAmount());
                shoppingCartAddReqDto.setImage(orderDetailPo.getImage());
                shoppingCartAddReqDto.setName(orderDetailPo.getName());
                shoppingCartAddReqDto.setUserId(ordersPo.getUserId());
                if (orderDetailPo.getDishId()!=null){
                    shoppingCartAddReqDto.setDishId(orderDetailPo.getDishId());
                }
                if (orderDetailPo.getSetmealId()!=null){
                    shoppingCartAddReqDto.setSetmealId(orderDetailPo.getSetmealId());
                }
                shoppingCartAddReqDto.setDishFlavor(orderDetailPo.getDishFlavor());
                shoppingCartAddReqDto.setNumber(orderDetailPo.getNumber());
                shoppingCartAddReqDto.setCreateTime(sdf.format(new Date()));
                shoppingCartAddReqDto.setUpdateTime(sdf.format(new Date()));
                shoppingCartAddReqDto.setIsDeleted(0);

                try {
                    shoppingCartService.add(shoppingCartAddReqDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }catch (Exception e){
            logger.info("再来一单异常{}",e);
            throw new BusinessException("再来一单异常");
        }

    }


}
