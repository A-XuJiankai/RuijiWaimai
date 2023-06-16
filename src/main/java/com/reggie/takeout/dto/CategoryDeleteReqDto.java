package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/14  16:22
 * @Description  删除分类
 */
@Data
public class CategoryDeleteReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

}
