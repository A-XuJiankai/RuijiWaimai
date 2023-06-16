package com.reggie.takeout.dto;

import com.reggie.takeout.po.SetmealDishPo;
import com.reggie.takeout.po.SetmealPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  16:10
 * @Description 新增套餐
 */
@Data
public class SetmealSaveReqDto extends SetmealPo implements Serializable {

    private List<SetmealDishPo> setmealDishes;//套餐关联的菜品集合

    private String categoryName;//分类名称

}
