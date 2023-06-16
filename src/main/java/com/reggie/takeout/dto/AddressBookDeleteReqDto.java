package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/27  17:41
 * @Description 删除地址簿
 */
@Data
public class AddressBookDeleteReqDto implements Serializable {

    private Long ids;

}
