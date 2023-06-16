package com.reggie.takeout.dto;

import com.reggie.takeout.po.DishPo;
import com.reggie.takeout.vo.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author gett
 * @Date 2022/4/25  9:20
 * @Description  修改菜品-保存
 */
@Data
public class DishUpdateSaveReqDto extends DishPo implements Serializable {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
