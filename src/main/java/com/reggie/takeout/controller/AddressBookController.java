package com.reggie.takeout.controller;

import com.alibaba.fastjson.JSONObject;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.service.AddressBookService;
import com.reggie.takeout.utils.R;
import com.reggie.takeout.vo.AddressBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/26  15:47
 * @Description 地址簿
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    private static Logger logger = LoggerFactory.getLogger(AddressBookController.class);

    @Autowired
    private AddressBookService addressBookService;

    /**
     * @Description 新增地址簿
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.POST)
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        logger.info("新增地址簿入参{}", JSONObject.toJSONString(addressBook));

        try {
            AddressBookSaveReqDto addressBookSaveReqDto = new AddressBookSaveReqDto();
            BeanUtils.copyProperties(addressBook,addressBookSaveReqDto);
            addressBookService.saves(addressBookSaveReqDto);
            return R.success(addressBook);
        }catch (Exception e) {
            logger.info("新增地址簿异常{}", e);
            return R.error("新增地址簿异常");
        }
    }

    /**
     * @Description 设置默认地址
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT,value = "/default")
    public R<AddressBookDefaultRspDto> setDefault(@RequestBody AddressBook addressBook) {
        logger.info("设置默认地址入参{}", JSONObject.toJSONString(addressBook));

        try {

            AddressBookDefaultReqDto addressBookDefaultReqDto = new AddressBookDefaultReqDto();
            BeanUtils.copyProperties(addressBook,addressBookDefaultReqDto);
            AddressBookDefaultRspDto addressBookDefaultRspDto=addressBookService.setDefault(addressBookDefaultReqDto);
            if (ObjectUtils.isEmpty(addressBookDefaultRspDto)){
                return R.error("无默认地址");
            }
            return R.success(addressBookDefaultRspDto);
        }catch (Exception e) {
            logger.info("设置默认地址异常{}", e);
            return R.error("设置默认地址异常");
        }
    }

    /**
     * @Description  根据id查询地址
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public R<AddressBookIdRspDto> get(@PathVariable Long id) {
        logger.info("根据id查询地址入参{}", id);

        try {
            AddressBookIdReqDto addressBookIdReqDto = new AddressBookIdReqDto();
            addressBookIdReqDto.setId(id);

            AddressBookIdRspDto addressBookIdRspDto=addressBookService.getid(addressBookIdReqDto);

            if (ObjectUtils.isEmpty(addressBookIdRspDto)) {
                return R.error("没有找到该对象");
            }
            return R.success(addressBookIdRspDto);

        }catch (Exception e) {
            logger.info("根据id查询地址异常{}", e);
            return R.error("根据id查询地址异常");
        }

    }

    /**
     * @Description 查询默认地址
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/default")
    public R<AddressBookDefaultRspDto> getDefault() {

        try {

            AddressBookDefaultRspDto addressBookDefaultRspDto=addressBookService.getDefault();

            if (ObjectUtils.isEmpty(addressBookDefaultRspDto)) {
                return R.error("没有找到该对象");
            }
            return R.success(addressBookDefaultRspDto);

        }catch (Exception e) {
            logger.info("查询默认地址异常{}", e);
            return R.error("查询默认地址异常");
        }


    }

    /**
     * @Description 查询指定用户的全部地址
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.GET,value = "/list")
    public R<List<AddressListRspDto>> list() {

        try {

            List<AddressListRspDto> lists= addressBookService.lists();

            return R.success(lists);

        }catch (Exception e) {
            logger.info("查询默认地址异常{}", e);
            return R.error("查询默认地址异常");
        }

    }

    /**
     * @Description 修改地址簿
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.PUT)
    public R<String> updates(@RequestBody AddressBook addressBook) {
        logger.info("修改地址簿入参{}", JSONObject.toJSONString(addressBook));

        try {
            AddressBookUpdateReqDto  addressBookUpdateReqDto = new AddressBookUpdateReqDto();
            BeanUtils.copyProperties(addressBook,addressBookUpdateReqDto);
            addressBookService.updates(addressBookUpdateReqDto);
            return R.success("修改地址簿成功");
        }catch (Exception e) {
            logger.info("修改地址簿异常{}", e);
            return R.error("修改地址簿异常");
        }
    }

    /**
     * @Description 删除地址簿
     * @Author gett
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public R<String> delete(Long ids) {
        logger.info("删除地址簿入参{}", ids);

        try {
            AddressBookDeleteReqDto  addressBookDeleteReqDto = new AddressBookDeleteReqDto();
            addressBookDeleteReqDto.setIds(ids);

            addressBookService.deletes(addressBookDeleteReqDto);
            return R.success("删除地址簿成功");
        }catch (Exception e) {
            logger.info("删除地址簿异常{}", e);
            return R.error("删除地址簿异常");
        }
    }
}
