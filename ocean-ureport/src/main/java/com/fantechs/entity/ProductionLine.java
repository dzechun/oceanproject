package com.fantechs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 产线智能看板实体
 *
 */
@Data
public class ProductionLine {

    @ApiModelProperty(name="productLineTop",value = "当日数据")
    private ProductLineTop productLineTop;

    @ApiModelProperty(name="productLineLeft",value = "工单数据")
    private ProductLineLeft productLineLeft;

    @ApiModelProperty(name="productLineRights",value = "节拍数据")
    private List<ProductLineRight> productLineRights;

}
