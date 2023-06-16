package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/26  16:41
 * @Description 根据id查询地址
 */
@Data
public class AddressBookIdReqDto implements Serializable {

    private Long id;

}
