package com.reggie.takeout.vo;

import com.reggie.takeout.po.DishPo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/24  10:16
 * @Description 保存菜品信息，同时保存对应的口味数据
 */
@Data
public class Dish extends DishPo implements Serializable {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
