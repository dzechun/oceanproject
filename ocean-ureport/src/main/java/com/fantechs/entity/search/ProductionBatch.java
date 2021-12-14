package com.fantechs.entity.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次达成率实体
 *
 */
@Data
public class ProductionBatch {

    /**
     * 日期
     */
    @ApiModelProperty(name="Udate",value = "日期")
    private String Udate;

    /**
     * 达成率
     */
    @ApiModelProperty(name="Unumber",value = "达成率")
    private String Unumber;



}
