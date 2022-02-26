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
     * 业务类型(1-采购入库 2-完工入库 3-销退入库 4-其它入库 5-销售出库 6-生产出库 7-采退出库 8-其它出库)(必填)
     */
    @ApiModelProperty(name="businessType" ,value="业务类型(1-采购入库 2-完工入库 3-销退入库 4-其它入库 5-销售出库 6-生产出库 7-采退出库 8-其它出库)(必填)")
    @Excel(name = "业务类型(1-采购入库 2-完工入库 3-销退入库 4-其它入库 5-销售出库 6-生产出库 7-采退出库 8-其它出库)(必填)", height = 20, width = 30)
    private Integer businessType;

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
