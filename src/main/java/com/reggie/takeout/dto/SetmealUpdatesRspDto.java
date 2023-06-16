package com.reggie.takeout.dto;

import com.reggie.takeout.po.SetmealDishPo;
import com.reggie.takeout.po.SetmealPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  17:45
 * @Description 修改套餐-回显信息
 */
@Data
public class SetmealUpdatesRspDto extends SetmealPo implements Serializable {

    private List<SetmealDishPo> setmealDishes;//套餐关联的菜品集合

    private String categoryName;//分类名称

}
