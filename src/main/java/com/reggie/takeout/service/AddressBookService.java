package com.reggie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.takeout.dto.*;
import com.reggie.takeout.po.AddressBookPo;

import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/26  15:42
 * @Description 地址簿
 */

public interface AddressBookService extends IService<AddressBookPo> {

    /**
     * @Description 新增地址簿
     * @Author gett
     */
    void saves(AddressBookSaveReqDto addressBookSaveReqDto) throws Exception;

    /**
     * @Description 设置默认地址
     * @Author gett
     * @return
     */
    AddressBookDefaultRspDto setDefault(AddressBookDefaultReqDto addressBookDefaultReqDto) throws Exception;

    /**
     * @Description 根据id查询地址
     * @Author gett
     */
    AddressBookIdRspDto getid(AddressBookIdReqDto addressBookIdReqDto)  throws Exception;

    /**
     * @Description 查询默认地址
     * @Author gett
     */
    AddressBookDefaultRspDto getDefault()  throws Exception;

    /**
     * @Description 查询指定用户的全部地址
     * @Author gett
     */
    List<AddressListRspDto> lists( ) throws Exception;

    /**
     * @Description 修改地址簿
     * @Author gett
     */
    void updates(AddressBookUpdateReqDto addressBookUpdateReqDto) throws Exception;

    /**
     * @Description 删除地址簿
     * @Author gett
     */
    void deletes(AddressBookDeleteReqDto addressBookDeleteReqDto) throws Exception;
}
