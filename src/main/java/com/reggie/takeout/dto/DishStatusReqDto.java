package com.reggie.takeout.dto;

import com.reggie.takeout.po.DishFlavorPo;
import com.reggie.takeout.po.DishPo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  10:07
 * @Description 菜品停售
 */
@Data
public class DishStatusReqDto implements Serializable {

    private  String status;

    private  Long ids[];

}
