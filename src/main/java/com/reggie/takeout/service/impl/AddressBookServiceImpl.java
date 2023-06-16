package com.reggie.takeout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.takeout.dao.AddressBookDao;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.AddressBookPo;
import com.reggie.takeout.service.AddressBookService;
import com.reggie.takeout.utils.BaseContext;
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
 * @Date 2022/4/26  15:45
 * @Description 地址簿
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBookPo> implements AddressBookService {

    private static Logger logger = LoggerFactory.getLogger(AddressBookServiceImpl.class);

    @Resource
    private AddressBookDao addressBookDao;

    /**
     * @Description 新增地址簿
     * @Author gett
     */
    @Override
    public void saves(AddressBookSaveReqDto addressBookSaveReqDto) throws Exception {
        logger.info("新增地址簿入参{}", JSONObject.toJSONString(addressBookSaveReqDto));

        try {
            AddressBookPo addressBookPo = new AddressBookPo();
            BeanUtils.copyProperties(addressBookSaveReqDto,addressBookPo);
            addressBookPo.setUserId(BaseContext.getCurrentId());
            addressBookPo.setIsDeleted(1);
            addressBookDao.insert(addressBookPo);

        }catch (Exception e){
            logger.error("新增地址簿异常{}", e);
            throw new BusinessException("新增地址簿异常");
        }

    }

    /**
     * @Description 设置默认地址
     * @Author gett
     */
    @Override
    public AddressBookDefaultRspDto setDefault(AddressBookDefaultReqDto addressBookDefaultReqDto) throws Exception {

        logger.info("设置默认地址入参{}", JSONObject.toJSONString(addressBookDefaultReqDto));

        try {
            AddressBookDefaultRspDto addressBookDefaultRspDto = new AddressBookDefaultRspDto();

            AddressBookPo addressBook = new AddressBookPo();
            addressBook.setIsDefault(0);

            LambdaQueryWrapper<AddressBookPo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBookPo::getUserId,BaseContext.getCurrentId());
            queryWrapper.eq(AddressBookPo::getIsDeleted,1);
            addressBookDao.update(addressBook,queryWrapper);

            addressBook.setIsDefault(1);
            addressBook.setId(addressBookDefaultReqDto.getId());
            addressBookDao.updateById(addressBook);

            LambdaQueryWrapper<AddressBookPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AddressBookPo::getUserId,BaseContext.getCurrentId());
            wrapper.eq(AddressBookPo::getId,addressBookDefaultReqDto.getId());
            wrapper.eq(AddressBookPo::getIsDeleted,1);
            AddressBookPo addressBookPo = addressBookDao.selectOne(wrapper);
            BeanUtils.copyProperties(addressBookPo,addressBookDefaultRspDto);

            return addressBookDefaultRspDto;
        }catch (Exception e){
            logger.error("新增地址簿异常{}", e);
            throw new BusinessException("新增地址簿异常");
        }
    }

    /**
     * @Description 根据id查询地址
     * @Author gett
     */
    @Override
    public AddressBookIdRspDto getid(AddressBookIdReqDto addressBookIdReqDto) throws Exception {

        logger.info("根据id查询地址入参{}", JSONObject.toJSONString(addressBookIdReqDto));

        try {
            AddressBookIdRspDto addressBookIdRspDto = new AddressBookIdRspDto();
            LambdaQueryWrapper<AddressBookPo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AddressBookPo::getId,addressBookIdReqDto.getId());
            wrapper.eq(AddressBookPo::getIsDeleted,1);

            AddressBookPo addressBookPo = addressBookDao.selectOne(wrapper);

            BeanUtils.copyProperties(addressBookPo,addressBookIdRspDto);

            return addressBookIdRspDto;
        }catch (Exception e){
            logger.error("根据id查询地址异常{}", e);
            throw new BusinessException("根据id查询地址异常");
        }

    }

    /**
     * @Description 查询默认地址
     * @Author gett
     */
    @Override
    public AddressBookDefaultRspDto getDefault() throws Exception {
        try {

            AddressBookDefaultRspDto addressBookDefaultRspDto = new AddressBookDefaultRspDto();
            LambdaQueryWrapper<AddressBookPo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBookPo::getUserId, BaseContext.getCurrentId());
            queryWrapper.eq(AddressBookPo::getIsDefault, 1);
            queryWrapper.eq(AddressBookPo::getIsDeleted, 1);

            AddressBookPo addressBookPo = addressBookDao.selectOne(queryWrapper);
            BeanUtils.copyProperties(addressBookPo,addressBookDefaultRspDto);
            return addressBookDefaultRspDto;

        }catch (Exception e){
            logger.error("查询默认地址异常{}", e);
            throw new BusinessException("查询默认地址异常");
        }

    }

    /**
     * @Description 查询指定用户的全部地址
     * @Author gett
     */
    @Override
    public List<AddressListRspDto> lists( ) throws Exception{

        //logger.info("查询指定用户的全部地址入参{}", JSONObject.toJSONString(addressListReqDto));

        try {

            List<AddressListRspDto> list = new ArrayList<>();

            LambdaQueryWrapper<AddressBookPo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(null != BaseContext.getCurrentId(), AddressBookPo::getUserId, BaseContext.getCurrentId());
            queryWrapper.orderByDesc(AddressBookPo::getUpdateTime);
            queryWrapper.eq(AddressBookPo::getIsDeleted,1);

            List<AddressBookPo> addressBookPos = addressBookDao.selectList(queryWrapper);
           if (!ObjectUtils.isEmpty(addressBookPos)){
               addressBookPos.stream().forEach(addressBookPo -> {
                   AddressListRspDto addressListRspDto = new AddressListRspDto();
                   BeanUtils.copyProperties(addressBookPo,addressListRspDto);
                   list.add(addressListRspDto);
               });
           }

            return list;
        }catch (Exception e){
            logger.error("查询指定用户的全部地址异常{}", e);
            throw new BusinessException("查询指定用户的全部地址异常");
        }

    }

    /**
     * @Description 修改地址簿
     * @Author gett
     */
    @Override
    public void updates(AddressBookUpdateReqDto addressBookUpdateReqDto) throws Exception {
        logger.info("修改地址簿入参{}", JSONObject.toJSONString(addressBookUpdateReqDto));

        try {

            AddressBookPo addressBookPo = new AddressBookPo();
            BeanUtils.copyProperties(addressBookUpdateReqDto,addressBookPo);

            addressBookDao.updateById(addressBookPo);

        }catch (Exception e){
            logger.error("修改地址簿异常{}", e);
            throw new BusinessException("修改地址簿异常");
        }
    }

    /**
     * @Description 删除地址簿
     * @Author gett
     */
    @Override
    public void deletes(AddressBookDeleteReqDto addressBookDeleteReqDto) throws Exception {
        logger.info("删除地址簿入参{}", JSONObject.toJSONString(addressBookDeleteReqDto));

        try {

            AddressBookPo addressBookPo = new AddressBookPo();
            addressBookPo.setId(addressBookDeleteReqDto.getIds());
            addressBookPo.setIsDeleted(0);
            addressBookDao.updateById(addressBookPo);

        }catch (Exception e){
            logger.error("删除地址簿异常{}", e);
            throw new BusinessException("删除地址簿异常");
        }
    }
}
