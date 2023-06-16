package com.reggie.takeout.dto;

import com.reggie.takeout.po.DishPo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/24  11:54
 * @Description 菜品信息分页查询
 */
@Data
public class DishPageRspDto extends DishPo implements Serializable{

    private static final long serialVersionUID = 1L;

    private List<DishFlavorSaveReqDto> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
