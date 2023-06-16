package com.reggie.takeout.dto;

import com.reggie.takeout.po.SetmealDishPo;
import com.reggie.takeout.po.SetmealPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  16:45
 * @Description 根据条件查询套餐数据
 */
@Data
public class SetmealListReqDto extends SetmealPo implements Serializable {

    private List<SetmealDishPo> setmealDishes;//套餐关联的菜品集合

    private String categoryName;//分类名称
}
