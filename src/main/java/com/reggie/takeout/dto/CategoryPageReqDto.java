package com.reggie.takeout.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/14  16:22
 * @Description  分类信息分页查询
 */
@Data
public class CategoryPageReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int page;

    private int pageSize;

}
