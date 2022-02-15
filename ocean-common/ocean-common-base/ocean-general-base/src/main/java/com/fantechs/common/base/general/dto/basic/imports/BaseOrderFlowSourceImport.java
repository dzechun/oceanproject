package com.fantechs.common.base.general.dto.basic.imports;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseOrderFlowSourceImport implements Serializable {

    /**
     * 作业模块(1-入库作业 2-库内作业 3-出库作业)(必填)
     */
    @ApiModelProperty(name="operationModule" ,value="作业模块(1-入库作业 2-库内作业 3-出库作业)(必填)")
    @Excel(name = "作业模块(1-入库作业 2-库内作业 3-出库作业)(必填)", height = 20, width = 30)
    private Integer operationModule;

    /**
     * 单据节点编码(必填)
     */
    @ApiModelProperty(name="orderTypeCode" ,value="单据节点编码(必填)")
    @Excel(name = "单据节点编码(必填)", height = 20, width = 30)
    private String orderTypeCode;

    /**
     * 来源单据类型编码
     */
    @ApiModelProperty(name="sourceOrderTypeCode" ,value="来源单据类型编码")
    @Excel(name = "来源单据类型编码", height = 20, width = 30)
    private String sourceOrderTypeCode;

    /**
     * 下推单据类型编码(必填)
     */
    @ApiModelProperty(name="nextOrderTypeCode" ,value="下推单据类型编码(必填)")
    @Excel(name = "下推单据类型编码(必填)", height = 20, width = 30)
    private String nextOrderTypeCode;
}
