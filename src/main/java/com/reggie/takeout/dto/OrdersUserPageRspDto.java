package com.reggie.takeout.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author gett
 * @Date 2022/4/28  20:46
 * @Description 分页查询
 */
@Data
public class OrdersUserPageRspDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Page page;

}
